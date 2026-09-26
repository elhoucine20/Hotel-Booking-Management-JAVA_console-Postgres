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
import util.payment.PaymentStrategy;

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

    public boolean createReservationService(User user, String roomNumber, int numberOfGuests, String dateDebut, String dateFin, PaymentStrategy paymentMethod) throws SQLException {

        if (roomRepository.findByNumber(roomNumber).isEmpty()) {  // if exist room or not
            System.out.println("Cette room n'existe pas !!"); return false;
        }
        Room room = roomRepository.findByNumber(roomNumber).get();  // find room
        if (room.getStatus() == RoomStatus.MAINTENANCE) {  // check room status
            System.out.println("Cette room pas disponible !!"); return false;
        }
        if (numberOfGuests <= 0 || numberOfGuests > room.getCapacity()) {  // check number of guests
            System.out.println("number of guests  n'adapte  pas avec la capacity de room !!"); return false;
        }
        LocalDate checkIn = ValidationUtils.parseDate(dateDebut);   // validate date 1
        LocalDate checkOut = ValidationUtils.parseDate(dateFin);   //validate date 2
        try {
            ValidationUtils.ValidateLesDates(checkIn, checkOut);  // validate the dates logiquement
        } catch (DateTimeException e) {
            System.out.println(e.getMessage()); return false;
        }
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);  // number of nights
        Map<LocalDate, LocalDate> reservations = reservationRepository.findLesDatesReservationsByRoom(roomNumber); //  les dates de reservations de cette room
        if (!this.disponibleForReserver(reservations,checkIn,checkOut)) // disoinible for reserved on this time
            return false;
        LocalDate reservationDate = LocalDate.now();   // la date actuel de reservation
        BigDecimal totalPrice = this.calculerPrice(reservationDate,checkIn,checkOut,room.getPricePerNight());  // calculer total price

        UUID reservationId = UUID.randomUUID();   // generate les autres proprties
        UUID userId = user.getId();
        UUID roomId = room.getId();
        ReservationStatus status = ReservationStatus.CONFIRMED;  // statu de reservation by default confirmed
        String reservationCode = reservationRepository.generateReservationCode();  // generate code de reservation
        Reservation reservation = new Reservation(reservationId, reservationCode, userId, roomId, checkIn, checkOut, numberOfGuests, numberOfNights, totalPrice, status);

        PaymentStatus paymentStatus = paymentMethod.paye(totalPrice);

        PaymentMethod paymentMethod1 ;
        if(paymentStatus.equals(PaymentStatus.PENDING))
            paymentMethod1 =  PaymentMethod.cash;
        else
            paymentMethod1 = PaymentMethod.paypal;
        Payment payment = new Payment(UUID.randomUUID(), reservationId, totalPrice, paymentMethod1, paymentStatus, null);

        connection.setAutoCommit(false);  // debut de transaction
        try {
            if (!reservationRepository.saveReservationRepository(reservation)) {
                connection.rollback(); return false;
            }
            if (!paymentRepository.save(payment)) {
                connection.rollback(); return false;
            }
            Invoice invoice = invoiceService.createInvoiceService(payment);
            if (!invoiceRepository.save(invoice)) {
                connection.rollback(); return false;
            }
            connection.commit(); return true;
        } catch (SQLException e) {
            connection.rollback(); e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Reservation> allReservationsUserService(User user) {
        return reservationRepository.allReservationsUser(user);
    }

    public boolean cancelReservationService(String codeReservation, User user) {
        return reservationRepository.cancelReservationRepository(codeReservation, user);
    }

    public boolean updateReservationService(User user, String codeReservation, String roomNumber, int numberOfGuests) {

        //  verifier existe reservation
        Reservation reservation = reservationRepository.findReservationsByCode(codeReservation);
        if (reservation == null) {
            System.out.println("Cette reservation n'existe pas !!"); return false;
        }

        // verifier la reservation appartient a l'user
        if (!reservation.getUserId().equals(user.getId())) {
            System.out.println("Cette reservation inconnu !!"); return false;
        }
        // verifier la nouvelle room existe
        Optional<Room> roomOptional = roomRepository.findByNumber(roomNumber);
        if (roomOptional.isEmpty()) {
            System.out.println("cette rooom n'existe pas !!"); return false;
        }
        Room room = roomOptional.get();
        // verifier la capacity
        if (numberOfGuests <= 0 || numberOfGuests > room.getCapacity()) {
            System.out.println("nombre de quests depasser la capacity de room !!"); return false;
        }
        // calculer le nombre des nights
        long numberOfNights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        // calculer la nouvelle price
        //BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        BigDecimal totalPrice = this.calculerPrice(LocalDate.now(),reservation.getCheckIn(),reservation.getCheckOut(),room.getPricePerNight());

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

    private BigDecimal calculerPrice(LocalDate reservationDate, LocalDate checkIn, LocalDate checkOut, BigDecimal pricePerNight){
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDate currentDate = checkIn;
        while (currentDate.isBefore(checkOut)) {
            BigDecimal priceForNight = pricePerNight;
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

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }


    private boolean disponibleForReserver(Map<LocalDate, LocalDate> reservations,LocalDate checkIn,LocalDate checkOut) {
        for (Map.Entry<LocalDate, LocalDate> reservation : reservations.entrySet()) {
            LocalDate reservedIn = reservation.getKey();
            LocalDate reservedOut = reservation.getValue();
            if (checkIn.isBefore(reservedOut) && checkOut.isAfter(reservedIn)) {
                System.out.println("Cette room est deja reserver pour dans ce periode " + checkIn + " - " + checkOut + " !!");
                return false;
            }
        }
        return true;
    }
}
