package controller;

import model.Reservation;
import model.User;
import model.enums.PaymentMethod;
import model.enums.ReservationStatus;
import repository.impl.RoomRepository;
import service.ReservationService;
import util.InputUtils;
import util.Menus;
import util.payment.PaymentCash;
import util.payment.PaymentPaypal;
import util.payment.PaymentStrategy;

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

            System.out.println("Choisir la methode de payement :");
            System.out.println("1. CASH");
            System.out.println("2. PAYPAL");
            int choix = InputUtils.lireInt(scanner, "Saisie votre choix : ");
            PaymentStrategy paymentMethod;
            if (choix == 1) {
                paymentMethod = new PaymentCash();
            } else if (choix == 2) {
                paymentMethod = new PaymentPaypal();
            } else {
                System.out.println("votre choix invalide !");
                return;
            }
            if (reservationService.createReservationService(user,roomNumber,numberOfGuests, dateDebut,dateFin,paymentMethod)) {
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


    public void updateReservationStatuController(Scanner scanner){

        try {
            String code = InputUtils.lireString(scanner,"saisie le code de reservation : ");
            ReservationStatus statu = null;
            do {
                System.out.println("choisir la nouvelle statu :");
                System.out.println("1- CONFIRMED");
                System.out.println("2- CANCELLED");
                System.out.println("3- COMPLETED");

                int choix = InputUtils.lireInt(scanner,"");

                switch (choix){
                    case 1: statu = ReservationStatus.CONFIRMED; break;
                    case 2: statu = ReservationStatus.CANCELLED; break;
                    case 3: statu = ReservationStatus.COMPLETED; break;
                    default:
                        System.out.println("Votre choix n'exist pas !!"); break;
                }
            }while (statu==null);
            if (reservationService.updateReservationStatuService(code,statu))
                System.out.println("Reservation statu updated whith success");
            else
                System.out.println("impossible de modifier le statu de cette reservation !!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void allReservationsController() {
        List<Reservation> reservations = reservationService.AllReservationService();
        System.out.println("========== TOUTES LES RESERVATIONS ==========");
        if (reservations.isEmpty()) {
            System.out.println("Aucune reservation trouver.");
            return;
        }
        for (Reservation reservation : reservations) {
            System.out.println("Code : " + reservation.getReservationCode() + " | User : " + reservation.getUserId()
                    + " | Room : " + reservation.getRoom_id() + " | Check-in : " + reservation.getCheckIn() + " | Check-out : "
                    + reservation.getCheckOut() + " | Guests : " + reservation.getNumberOfGuests() + " | Nuits : "
                    + reservation.getNumberOfNights() + " | Total : " + reservation.getTotalPrice()+ " | Status : " + reservation.getStatus());
        }
    }
}
