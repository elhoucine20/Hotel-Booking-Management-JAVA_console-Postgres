package controller;

import model.Reservation;
import model.User;
import repository.impl.RoomRepository;
import service.ReservationService;
import util.InputUtils;
import util.Menus;

import java.util.List;
import java.util.Scanner;

public class ReservationController {

    ReservationService reservationService = new ReservationService();
    public void createReservationController(Scanner scanner, User user) {

        try {
            String roomNumber = InputUtils.lireString(scanner, "Saisir roomNumber : ");
            int numberOfGuests = InputUtils.lireInt(scanner, "Saisir Number OfGuests : ");
            String dateDebut = InputUtils.lireString(scanner, "Saisir la date de checkIn (YYYY,MM,DD) : ");
            String dateFin = InputUtils.lireString(scanner, "Saisir la date de checkOut (YYYY,MM,DD) : ");

            if (reservationService.createReservationService(user,roomNumber,numberOfGuests, dateDebut,dateFin)) {
                System.out.println("Reservation cree avec success !!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void allReservationsUserController(User user) {

        List<Reservation> reservations =
                reservationService.allReservationsUserService(user);
        System.out.println("========== MES RESERVATIONS ==========");
        if (reservations.isEmpty()) {
            System.out.println("Vous n'avez aucune reservation.");
            return;
        }
        for (Reservation reservation : reservations) {

            System.out.println("Code : " + reservation.getReservationCode()+ " | Check-in : " + reservation.getCheckIn()+ " | Check-out : " + reservation.getCheckOut()
                            + " | Guests : " + reservation.getNumberOfGuests()+ " | Nuits : " + reservation.getNumberOfNights()
                            + " | Total : " + reservation.getTotalPrice() + " | Status : " + reservation.getStatus()
            );
        }
    }

    public void cancelReservationController(Scanner scanner, User user) {
        try {
            String codeReservation = InputUtils.lireString(scanner, "Saisir le code de reservation : ");
            if (reservationService.cancelReservationService(codeReservation, user)) {
                System.out.println("Reservation annuler avec success !!");
            } else {
                System.out.println("Reservation introuvable ou elle ne vous appartient pas");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void updateReservationController(Scanner scanner, User user) {
        try {
            String codeReservation = InputUtils.lireString(scanner, "Saisir le code de reservation : ");
            String roomNumber = InputUtils.lireString(scanner, "Saisir nouvelle number de room : ");
            int numberOfGuests = InputUtils.lireInt(scanner, "Saisir nouvelle  number of guests : ");

            if (reservationService.updateReservationService(user, codeReservation, roomNumber, numberOfGuests)) {
                System.out.println("reservation updated avec success !!");
            } else {
                System.out.println("impossible de modifier la reservatioN");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
