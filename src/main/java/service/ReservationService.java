package service;
import model.Reservation;
import model.Room;
import model.User;
import model.enums.ReservationStatus;
import model.enums.RoomStatus;
import repository.ReservationRepository;
import repository.impl.InMemoryReservationRepository;
import repository.impl.RoomRepository;
import util.ValidationUtils;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class ReservationService {

    InMemoryReservationRepository reservationRepository = new ReservationRepository();
    RoomRepository romRepository;
    private static int counter=1;

    public void myReservationsService(User user){
        reservationRepository.affichierReservationsUser(user);
    }

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

}
