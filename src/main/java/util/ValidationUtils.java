package util;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.InvalidPassword;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ValidationUtils {

    public static void ValidateEmail(String email){
        if (email.isEmpty() || !email.contains("@gmail.com")){
            throw new InvalidCredentialsException("s'il vous plais saisir email correct !!");
        }
    }

    public static boolean ValidateString(String name){
        if (name.isEmpty() || name.length() <= 2){
            throw new InvalidCredentialsException("s'il vous plais saisir votre nom !!");
        }else return true;
    }
    /*
    public static boolean ValidateCodeReservation(String code){
        if (code.isEmpty() || code.length() <= 2){
            throw new InvalidCredentialsException("s'il vous plais saisir code de reservation !!");
        }else return true;
    }
    */

    public static void ValidatePhone(String phone){
        if (phone.isBlank() || phone.length() < 10){
            throw  new InvalidCredentialsException("s'il vous plais saisir votre phone !!");
        }
    }
    public static void ValidatePassword(String pass){
        if (pass.isBlank() || pass.length() < 8){
            throw new InvalidPassword("s'il vous plais saisir une password correct (au moins 8 characters)!!");
        }
    }


    public static void ValidateLesDates(LocalDate date1,LocalDate date2){
        if (date1.isBefore(LocalDate.now())){
            throw new DateTimeException("vous avais saisir une date deja passer '"+date1+"' !!");
        }
        if (date2.isBefore(LocalDate.now())){
            throw new DateTimeException("vous avais saisir une date deja passer '"+date2+"' !!");
        }
        if (date2.isBefore(date1) )
            throw new DateTimeException("slvp verifier vous dates (date checkIn '"+date1+"') et (date checkOut '"+date2+"') !!");
    }

    public static LocalDate parseDate(String date) {

        String regex = "^\\d{4},\\d{2},\\d{2}$";

        if (!date.matches(regex)) {
            throw new DateTimeException("la forma de cette date '"+date+"' invalid doit respecter cette form 'yyyy,MM,DD' ");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");
            return LocalDate.parse(date, formatter);

        } catch (DateTimeParseException e) {
            throw new  DateTimeException("la date '" + date + "' est invalide");
        }
    }

    public static boolean ValidateIsAfetrLimit(LocalDate date1){
        if (LocalDate.now().plusMonths(6).isBefore(date1)){
            throw new DateTimeException("impossible a reserver the room after 6 months !!!");
        }
        return true;
    }

    public static long ValidateDaysBetweenDates(LocalDate date1,LocalDate date2){
        long days;
        days = ChronoUnit.DAYS.between(date1,date2);
        if (days>30)
            throw new DateTimeException("impossible de reserver une chambre plus que 30 jour !!");
        return days;
    }


    public static boolean CheckPossiiliteDeReserver(LocalDate checkIn,LocalDate checkOut,LocalDate reservedIn,LocalDate reservetOut,Map<LocalDate,LocalDate> reservations){
        if (checkIn.equals(checkOut))
            throw new IllegalArgumentException("s'il vous plais saisir des dates differentes !!");

        if (checkIn.isBefore(reservetOut) && checkOut.isAfter(reservedIn)){
            System.out.println("this room does not available dans ces dates : ");
            for (Map.Entry<LocalDate,LocalDate> entry : reservations.entrySet()){
                System.out.println("de : "+entry.getKey()+", a : "+entry.getValue());
            }
            throw new DateTimeException("s'il vous plais doit reserver autre date que ne contient pas dans ces dates et merci !");
        }

        else return true;
    }

}
