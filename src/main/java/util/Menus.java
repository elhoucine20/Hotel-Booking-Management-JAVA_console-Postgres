package util;

import controller.AuthController;
import controller.ReservationController;
import controller.RoomController;
import model.User;
import repository.JdbcRoomRepository;
import repository.impl.RoomRepository;
import service.ReservationService;
import service.RoomService;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Menus {

    static  AuthController  authController = new AuthController();
    static RoomController roomController = new RoomController();
    static ReservationController reservationController = new ReservationController();
    static RoomRepository roomRepository = new JdbcRoomRepository();
    static RoomService roomService = new RoomService();
    static ReservationService reservationService = new ReservationService();


    public static void menuClient(Scanner scan, User user) throws Exception {
        boolean isTrue = true;
        while (isTrue){
            //if (user!=null)
            System.out.println(" === Welcome : "+user.getFullName()+" === ");
            //else  Menus.menuAuth(scan);
            System.out.println("================================");

            System.out.println("1. Search available rooms");
            System.out.println("2. View all rooms");
            System.out.println("3. Create reservation");
            System.out.println("4. My reservations");
            System.out.println("5. Update reservation");
            System.out.println("6. Cancel reservation");
            System.out.println("7. Update profile");
            System.out.println("8. Change password");
            System.out.println("9. Logout");
            System.out.println("0. Exit");
            int choixBeforLogin = scan.nextInt();

            switch (choixBeforLogin){
                case 1:// roomController.serviceAffichierRoomsAvailable( roomRepository,roomService);
                     break;
                case 2:// roomController.serviceAffichierRooms( roomRepository,roomService);
                     break;
               // case 3: reservationController.createReservationController(scan,user, roomRepository,reservationService);break;
                //case 4: reservationController.reservationServiceAffichier(user,reservationService);break;
                //case 5: reservationController.updateReservationController(scan,reservationService);break;
                //case 6: reservationController.cancelReservationController(scan,user,reservationService);break;
              //  case 7: authController.verifierProfileController(scan,user);break;
                //case 8: authController.changePasswordController(scan,user);break;
                case 9: System.out.println("Logout"); isTrue = false; Menus.menuAuth(scan) ; break;
                case 0: System.out.println("Exit"); isTrue = false; break;
                default:
                    System.out.println("s'il vous plais saisir une choix correct!!");break;

            }

        }
    }

    public static void menuAuth(Scanner scan) throws Exception {
        int choix;
        boolean isTrue = true;
        do {
            System.out.println("========================");
            System.out.println("HOTEL BOOKING");
            System.out.println("========================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            choix = scan.nextInt();

            switch (choix){
                case 1: authController.registerController(scan);break;
                case 2: authController.loginController(scan);break;
                case 0: System.out.println("Bye Bye"); isTrue=false; break;
                default:
                    System.out.println("s'il vous plais saisir une choix correct!!");break;

            }
        }while (isTrue);
    }

/*
    public static void updateStatuRoomReservationDynamique(){
        // Background scheduler to process expired reservations automatically
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                reservationService.updateReservationRoomStatus();
            } catch (Exception e) {

            }
        }, 0, 23, TimeUnit.HOURS);

    }
*/


    public static void menuAdmin(Scanner scan, User user) throws Exception {
        boolean isTrue = true;
        while (isTrue){
            System.out.println(" === Welcome : "+user.getFullName()+" === ");
            System.out.println("==============================");

            System.out.println("1. Create Room");
            System.out.println("2. Update Room");
            System.out.println("4. All Rooms");
            System.out.println("5. Update Reservation");
            System.out.println("6. Update profile");
            System.out.println("7. Change password");
            System.out.println("8. Logout");
            System.out.println("0. Exit");
            int choixBeforLogin = scan.nextInt();

            switch (choixBeforLogin){
                case 1:
                    roomController.createRoomController(scan,user); break;
                case 2:  roomController.updateRoomController(scan,user); break;
                case 4: roomController.allRoomsController(); break;
                case 5:  System.out.println("update reservarion statu ");break;
                case 6:  System.out.println("update profile!");break;
                case 7:  System.out.println("change password");break;
                case 8: System.out.println("Logout"); isTrue = false; Menus.menuAuth(scan) ; break;
                case 0: System.out.println("Exit"); isTrue = false; break;
                default:
                    System.out.println("s'il vous plais saisir une choix correct!!");break;

            }

        }
    }

}
