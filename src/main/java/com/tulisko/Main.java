package com.tulisko;

import config.DatabaseInitializer;
import db.DatabaseConnection;
import model.User;
import repository.JdbcUserRepository;
import repository.impl.UserRepository;
import service.AuthService;
import service.PasswordService;
import util.Menus;

import java.sql.Connection;
import java.util.Optional;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws Exception {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Hello and welcome!"));
        DatabaseConnection.getInstance().getConnection();
        DatabaseInitializer.initialize();


        Scanner scanner = new Scanner(System.in);

        //Menus.updateStatuRoomReservationDynamique();
        Menus.menuAuth(scanner);
        //UserRepository userRepository = new JdbcUserRepository();

    }

}
