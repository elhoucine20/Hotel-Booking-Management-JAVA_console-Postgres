package service;

import db.DatabaseConnection;
import model.*;
import model.enums.PaymentMethod;
import model.enums.PaymentStatus;
import model.enums.ReservationStatus;
import model.enums.RoomStatus;
import repository.JdbcInvoiceRepository;
import repository.JdbcPaymentRepository;
import repository.JdbcReservationRepository;
import repository.JdbcRoomRepository;
import repository.impl.InvoiceRepository;
import repository.impl.PaymentRepository;
import repository.impl.ReservationRepository;
import repository.impl.RoomRepository;
import util.ValidationUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class ReservationService {
    private RoomRepository roomRepository = new JdbcRoomRepository();
    private ReservationRepository reservationRepository = new JdbcReservationRepository();
    private PaymentRepository paymentRepository = new JdbcPaymentRepository();
    private Connection connection = DatabaseConnection.getInstance().getConnection();
    private InvoiceRepository invoiceRepository = new JdbcInvoiceRepository();
    private InvoiceService invoiceService = new InvoiceService();

    public boolean createReservationService(User user, String roomNumber, int numberOfGuests, String dateDebut, String dateFin, PaymentMethod paymentMethod) throws SQLException {

        Optional<Room> roomOptionel = roomRepository.findByNumber(roomNumber);
        if (roomOptionel.isEmpty()) {
            System.out.println("Cette room n'existe pas !!");
            return false;
        }
        Room room = roomOptionel.get();
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            System.out.println("Cette room pas disponible !!");
            return false;
        }
        if (numberOfGuests <= 0 || numberOfGuests > room.getCapacity()) {
            System.out.println("number of guests  n'adapte  pas avec la capacity de room !!");
            return false;
        }
        LocalDate checkIn = ValidationUtils.parseDate(dateDebut);
        LocalDate checkOut = ValidationUtils.parseDate(dateFin);
        try {
            ValidationUtils.ValidateLesDates(checkIn, checkOut);
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        Map<LocalDate, LocalDate> reservations = reservationRepository.findLesDatesReservationsByRoom(roomNumber);
        for (Map.Entry<LocalDate, LocalDate> reservation : reservations.entrySet()) {
            LocalDate reservedIn = reservation.getKey();
            LocalDate reservedOut = reservation.getValue();
            if (checkIn.isBefore(reservedOut) && checkOut.isAfter(reservedIn)) {
                System.out.println("Cette room est deja reserver pour dans ce periode " + checkIn + " - " + checkOut + " !!");
                return false;
            }
        }
        LocalDate reservationDate = LocalDate.now();

        BigDecimal totalPrice = this.calculerPrice(reservationDate,checkIn,checkOut,room.getPricePerNight());

        UUID reservationId = UUID.randomUUID();
        UUID userId = user.getId();
        UUID roomId = room.getId();
        ReservationStatus status = ReservationStatus.CONFIRMED;
        String reservationCode = reservationRepository.generateReservationCode();
        Reservation reservation = new Reservation(reservationId, reservationCode, userId, roomId, checkIn, checkOut, numberOfGuests, numberOfNights, totalPrice, status);
        PaymentStatus paymentStatus = (paymentMethod == PaymentMethod.cash) ? PaymentStatus.PENDING : PaymentStatus.PAID;
        Payment payment = new Payment(UUID.randomUUID(), reservationId, totalPrice, paymentMethod, paymentStatus, null);

        connection.setAutoCommit(false);
        try {
            boolean reservationSaved = reservationRepository.saveReservationRepository(reservation);
            if (!reservationSaved) {
                connection.rollback();
                return false;
            }
            boolean paymentSaved = paymentRepository.save(payment);
            if (!paymentSaved) {
                connection.rollback();
                return false;
            }
            Invoice invoice = invoiceService.createInvoice(payment);
            boolean invoiceSaved = invoiceRepository.save(invoice);
            if (!invoiceSaved) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private BigDecimal calculerPrice(LocalDate reservationDate, LocalDate checkIn, LocalDate checkOut, BigDecimal priceForNight){
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDate currentDate = checkIn;
        while (currentDate.isBefore(checkOut)) {
            //BigDecimal priceForNight = pricePerNight;
            int month = currentDate.getMonthValue();
            if (month == 7 || month == 8) {
                priceForNight = priceForNight.multiply(new BigDecimal("1.30"));
            }else if (month == 11 || month == 12 || month == 1  || month == 2){
                priceForNight = priceForNight.multiply(new BigDecimal("0.85"));
            }

            DayOfWeek day = currentDate.getDayOfWeek();
            if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY){
                priceForNight = priceForNight.multiply(new BigDecimal("1.15"));
            }
            totalPrice = totalPrice.add(priceForNight);
            currentDate = currentDate.plusDays(1);
        }
        if (numberOfNights >= 14) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.85"));
        } else if (numberOfNights >= 7) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.90"));
        }
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(reservationDate, checkIn);
        if (daysBeforeCheckIn >= 30) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.95"));
        }else if (daysBeforeCheckIn <= 3) {
            totalPrice = totalPrice.multiply(new BigDecimal("1.10"));
        }

        System.out.println("Prix avant reductions : " + totalPrice);
        System.out.println("Nombre de nuits : " + numberOfNights);
        System.out.println("Jours avant check-in : " + daysBeforeCheckIn);

        return totalPrice;
    }

    private void checkDate(LocalDate checkIn, LocalDate checkOut){}

    public List<Reservation> allReservationsUserService(User user) {
        return reservationRepository.allReservationsUser(user);
    }

    public boolean cancelReservationService(String codeReservation, User user) {
        return reservationRepository.cancelReservationRepository(codeReservation, user);
    }

    public boolean updateReservationService(User user, String codeReservation, String roomNumber, int numberOfGuests) {

        //  verifier la reservation existe
        Reservation reservation = reservationRepository.findReservationsByCode(codeReservation);
        if (reservation == null) {
            System.out.println("Cette reservation n'existe pas !!");
            return false;
        }

        // verifier la reservation appartient a l'user
        if (!reservation.getUserId().equals(user.getId())) {
            System.out.println("Cette reservation inconnu !!");
            return false;
        }
        // verifier la nouvelle room existe
        Optional<Room> roomOptional = roomRepository.findByNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            System.out.println("cette rooom n'existe pas !!");
            return false;
        }
        Room room = roomOptional.get();
        // verifier la capacity
        if (numberOfGuests <= 0 || numberOfGuests > room.getCapacity()) {
            System.out.println("nombre de personnes depasser la capacity de room !!");
            return false;
        }

        // calculer le nombre des nights
        long numberOfNights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        // calculer la nouvelle price
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));

        // update reservation
        return reservationRepository.updateReservationRepository(codeReservation, roomNumber, numberOfGuests, totalPrice);
    }

    public boolean updateReservationStatuService(String reservationCode, ReservationStatus newStatu) {
        Reservation reservation = reservationRepository.findReservationsByCode(reservationCode);
        if (reservation == null) {
            System.out.println("cette reservation n'exist pas ");
            return false;
        }
        if (reservation.getStatus() == newStatu) {
            System.out.println("cette reservation a ete deja dans ce satau " + newStatu);
            return false;
        }
        return reservationRepository.updateReservationStatus(reservationCode, newStatu);
    }

    public List<Reservation> AllReservationService() {
        return reservationRepository.findAll();
    }



/*
    public void createReservationService(RoomRepository roomRepository, User user, String roomNumber, int numberOfGuests, String dateDebut
            , String dateFin){
        romRepository = roomRepository;
        List<Reservation> reservations = new ArrayList<>();   // list de reservations filtrer par status confirmed  et auusi by room number
        Room room = roomRepository.getroomByNumber(roomNumber);
        try {
            // ValidationUtils.ValidateCodeReservation(reservationCode);  // validate code de reservation
            if (!roomNumber.isEmpty() && numberOfGuests > 0 && numberOfGuests < 6 && !dateDebut.isEmpty() && !dateFin.isEmpty()){    // validation des inputs
                //System.out.println("hi12");
                ReservationStatus reservationStatus;
                UUID id = UUID.randomUUID();
                UUID UserId = user.getId();
                BigDecimal totalPrice;
                LocalDateTime createdAt = LocalDateTime.now();
                long numberOfNighits;
                if (!room.getStatus().equals(RoomStatus.MAINTENANCE)){   // condition pour verifier statu de room

                    // validate number of guests     SINGLE  DOUBLE  SUITE
                    if (numberOfGuests <= room.getCapacity()){             // condition pour verifier numberGuests avec type de room
                        //System.out.println("hi1");
                        LocalDate checkIn = ValidationUtils.parseDate(dateDebut); // transform date to Localedate
                        LocalDate checkOut = ValidationUtils.parseDate(dateFin);

                        // validate les dates after or before
                        try {
                            ValidationUtils.ValidateLesDates(checkIn,checkOut);   // validate the dates  before or after else excetion
                        }catch (DateTimeException e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        ValidationUtils.ValidateIsAfetrLimit(checkIn);  // limit 6 months

                        // check if deja reserved in this date
                        reservations.addAll(reservationRepository.getReservationsByRoomNumber(roomNumber).values());
                        for (Reservation res:reservations){
                            LocalDate reservedIN = res.getCheckIn();
                            LocalDate reservedOut = res.getCheckOut();
                            ValidationUtils.CheckPossiiliteDeReserver(checkIn,checkOut,reservedIN,reservedOut,reservationRepository.getLesDatesReservationsByRoom(roomNumber));
                            //if (!ValidationUtils.CheckPossiiliteDeReserver(checkIn,checkOut,reservedIN,reservedOut,reservationRepository.getLesDatesReservationsByRoom(roomNumber))){
                            //}
                        }

                        numberOfNighits = ValidationUtils.ValidateDaysBetweenDates(checkIn,checkOut); // days between dates (nights) else exception
                        totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNighits));   // total price
                        reservationStatus = ReservationStatus.CONFIRMED;
                        String codeReservation = "R-"+checkIn+"-"+counter++;
                        Reservation reservation = new Reservation(id,codeReservation,UserId,roomNumber,checkIn,checkOut,
                                numberOfGuests,numberOfNighits,totalPrice,reservationStatus,createdAt);          // nes Reservation
                        // System.out.println("hi");
                        reservationRepository.saveReservationRepository(id,reservation);   // save in RepositoryReservation
                        //room.setStatus(RoomStatus.MAINTENANCE);
                        //System.out.println("hi4");
                    } else {
                        System.out.println("s'il vous plais reserver une champre matcher avec le nombre de votre gusts !!");
                    }
                }else {
                    System.out.println("this room pas dosponible !!");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void cancelReservationService(String code,User user){
        boolean cancelled = reservationRepository.cancelReservationRepository(code,user);
        if (cancelled)
            System.out.println("reservation cancelled avec succes ");
        else System.out.println("reservation introuvable !!");
    }

    public void updateReservationService(String code,String roomNumber, int numberOfGuests){
        Reservation reservation = reservationRepository.getReservationsByCode(code);
        //  if (ValidationUtils.ValidateCodeReservation(code) ){
        if (numberOfGuests<=0 || numberOfGuests >= 6)throw new IllegalArgumentException("numberOfGuests is impossible try again !!");

        if (reservation.getRoomNumber().equals(roomNumber) && reservation.getNumberOfNights() == numberOfGuests){
            throw new IllegalArgumentException("votre donnees deja exist !!");
        }else{
            BigDecimal totalPix ;
            Room room = romRepository.getroomByNumber(roomNumber);
            if (room.getCapacity() < numberOfGuests ) throw new IllegalArgumentException("impossible de change to this room !!");
            LocalDate checkOut = reservation.getCheckOut();
            LocalDate checkIn = reservation.getCheckIn();
            long days = ChronoUnit.DAYS.between(checkIn,checkOut);
            totalPix = room.getPricePerNight().multiply(BigDecimal.valueOf(days));
            //room.setStatus(RoomStatus.MAINTENANCE);
            reservationRepository.updateReservationRepository(code,roomNumber,numberOfGuests,totalPix);
        }
        //   }
    }

    public void updateReservationRoomStatus() {

        for (Reservation reservation : reservationRepository.getReservations().values()){
            if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)){
                if (reservation.getCheckOut().isBefore(LocalDate.now())) {
                    reservation.setStatus(ReservationStatus.COMPLETED);
                    Room room = romRepository.getroomByNumber(reservation.getRoomNumber());
                    room.setStatus(RoomStatus.AVAILABLE);
                }
            }
        }
    }


 */

}
