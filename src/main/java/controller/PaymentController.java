package controller;

import model.Payment;
import model.User;
import service.PaymentService;

import java.util.ArrayList;
import java.util.List;

public class PaymentController {
    PaymentService paymentService = new PaymentService();

    public void paymentsUserController(User user) {
        List<Payment> paymentsUser = paymentService.paymentsUserService(user);
        System.out.println("========== MES PAIEMENTS ==========");
        if (paymentsUser.isEmpty()) {
            System.out.println("aucun payment !!");
            return;
        }
        for (Payment payment : paymentsUser) {
            System.out.println(
                    "Reservation : " + payment.getReservation_id()
                            + " | Montant : " + payment.getAmount()
                            + " | Methode : " + payment.getPayment_method()
                            + " | Statu : " + payment.getPaymentStatus()
                            + " | Date : " + payment.getPayment_date()
            );
        }
    }}
