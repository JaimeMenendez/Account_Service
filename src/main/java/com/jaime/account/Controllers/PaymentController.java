package com.jaime.account.Controllers;

import com.jaime.account.DTOs.GetPaymentDTO;
import com.jaime.account.DTOs.PaymentDTO;
import com.jaime.account.Services.PaymentService;
import com.jaime.account.Validation.PeriodValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<?> addPayments(@Valid @RequestBody List<@Valid PaymentDTO> payments) {
        paymentService.insertPayments(payments);
        return new ResponseEntity<>(Map.of("status", "Added successfully!"), HttpStatus.OK);
    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<?> updatePayment(@Valid @RequestBody PaymentDTO payment) {
        System.out.println(payment);
        paymentService.updatePayment(payment);
        return new ResponseEntity<>(Map.of("status", "Updated successfully!"), HttpStatus.OK);
    }


    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getAllPayments(@RequestParam(name = "period", required = false) String period,
                                            @AuthenticationPrincipal UserDetails userLogged) {
        if (period == null) {
            List<GetPaymentDTO> payments = paymentService.selectAllPayments(userLogged.getUsername());
            return new ResponseEntity<>(payments, HttpStatus.OK);
        }
        Date periodDate = paymentService.stringToDate(period, "MM-yyyy");
        GetPaymentDTO payment = paymentService.selectPeriodPayment(userLogged.getUsername(), periodDate);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
