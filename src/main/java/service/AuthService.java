package service;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import model.User;
import model.enums.RoleUser;
import repository.JdbcUserRepository;
import repository.impl.UserRepository;
import util.Menus;
import util.ValidationUtils;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthService() {
        this.userRepository = new JdbcUserRepository();
        this.passwordService = new PasswordService();
    }

    public Boolean registerService(Scanner scanner,String name, String email, RoleUser role, String password)throws Exception{
        try {
            email = email.toLowerCase();
            ValidationUtils.ValidateString(name);
            ValidationUtils.ValidateEmail(email);
            if (userRepository.existsByEmail(email)){
                throw new EmailAlreadyExistsException("email deja exist s'il vous plais verifie votre email");
            }
            ValidationUtils.ValidatePassword(password);

            role = RoleUser.client;
            UUID id = UUID.randomUUID();
            String salt = passwordService.generateSalt();
            String passwordHash = passwordService.hashPassword(password, salt);
            User user = new User(id, name, email, passwordHash, salt, role);
            userRepository.save(user);
            //Menus.menuApresLogin(scanner,user);

            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public User loginService(String email, String password) throws Exception {

        email = email.toLowerCase();
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isEmpty()){
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }
        User user = result.get();
        boolean passwordcorrect = passwordService.verifypassword(password,user.getPasswordHash(),user.getSalt());
        if (!passwordcorrect)
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        return user;
    }

/*

/*
    public void changePasswordService(User user,String NPassword){
        try {
            ValidationUtils.ValidatePassword(NPassword);
            if (userRepository.changePasswordRepository(user,NPassword))
                System.out.println("password changed avec success");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void verifierProfileService(User user, String name,String email,String phone){

        try {
            ValidationUtils.ValidateEmail(email);
            ValidationUtils.ValidatePhone(phone);
            if (ValidationUtils.ValidateString(name)){
                userRepository.verifierProfileRepository(user,name,email,phone);
                System.out.println("votre profile est verifier avec succes ");

            }}catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
*/
}
