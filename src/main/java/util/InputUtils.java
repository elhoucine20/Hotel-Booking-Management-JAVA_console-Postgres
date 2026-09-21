package util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class InputUtils {

    public static double lireDouble(Scanner scanner , String label){
        double input=0;
        try {
            System.out.print(label+" ");
            input = scanner.nextDouble();
        }catch (NumberFormatException e){
            // System.out.println("s'il vous plais saisir votre choix ");
            InputUtils.lireDouble(scanner,label);
        }
        return input;
    }

    public static String lireString(Scanner scanner , String label){
        String input;
        System.out.print(label+" ");
        do {
            input = scanner.nextLine();
        }while (input.isEmpty());
        return input;
    }

    public static int lireInt(Scanner scanner , String label){
        int input=0;
        try {
            System.out.print(label+" ");
            input = scanner.nextInt();
        }catch (NumberFormatException e){
            // System.out.println("s'il vous plais saisir votre choix ");
            InputUtils.lireInt(scanner,label);
        }
        return input;
    }

    public static long lireLong(Scanner scanner , String label){
        long input = 0;
        try {
            System.out.print(label+" ");
            input = scanner.nextLong();
        }catch (NumberFormatException e){
            // System.out.println("s'il vous plais saisir votre choix ");
            InputUtils.lireLong(scanner,label);
        }
        return input;
    }


}
