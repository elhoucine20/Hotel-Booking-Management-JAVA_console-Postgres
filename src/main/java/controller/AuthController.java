package controller;

import model.User;
import exception.InvalidCredentialsException;
import model.enums.RoleUser;
import service.AuthService;
import util.InputUtils;
import util.Menus;

import java.util.Scanner;

public class AuthController {
   private final AuthService authService = new AuthService();

    public void registerController(Scanner scanner){
        try {
            String name = InputUtils.lireString(scanner,"Saisir votre nom : ");
            String email = InputUtils.lireString(scanner,"Saisir votre email : ");
            String password = InputUtils.lireString(scanner,"Saisir votre mot de pass : ");

            boolean isInscrire = authService.registerService(scanner,name,email, null,password);
            if (isInscrire)
             System.out.println("Inscription avec success !");
        }catch (Exception e){
            System.out.println(e.getMessage());

        }
    }

    public void loginController(Scanner scanner){
        try{
            String email = InputUtils.lireString(scanner,"Saisir votre email : ");
            String password = InputUtils.lireString(scanner,"Saisir votre mot de pass: ");
            User user = authService.loginService(email,password);
            if (user.getRole() == RoleUser.client){
                System.out.println("======== Dashboard Client =======");
                Menus.menuClient(scanner,user);
            }else if (user.getRole() == RoleUser.admin){
                System.out.println("======== Dashboard Admin =======");
                Menus.menuAdmin(scanner,user);
            }
        } catch (InvalidCredentialsException e) {
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
/*

    public void changePasswordController(Scanner scanner,User user){
        String Npassword = InputUtils.lireString(scanner,"saisir votre nouveau password : ");

        authService.changePasswordService(user,Npassword);
    }


    public void verifierProfileController(Scanner scanner,User user){
        String Name = InputUtils.lireString(scanner,"vrifier votre nom : ");
        String email = InputUtils.lireString(scanner,"verifier votre email : ");
        String phone = InputUtils.lireString(scanner,"verifier votre phone : ");
        authService.verifierProfileService(user,Name,email,phone);

    }

 */
}
