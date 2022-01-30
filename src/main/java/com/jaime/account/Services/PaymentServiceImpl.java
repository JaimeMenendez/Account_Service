package com.jaime.account.Services;

import com.jaime.account.DTOs.GetPaymentDTO;
import com.jaime.account.DTOs.PaymentDTO;
import com.jaime.account.Errors.*;
import com.jaime.account.Models.Payment;
import com.jaime.account.Repository.PaymentRepository;
import com.jaime.account.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    final PaymentRepository paymentRepository;
    final UserRepository userService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    public void insertPayments(List<PaymentDTO> payments) {
        if (payments.stream().distinct().count() < payments.size()) {
            throw new WrongPaymentException();
        }
        payments.forEach(payment -> {
            boolean exist = paymentRepository.existsPaymentByPeriodAndUser_Email(payment.getPeriodDate(), payment.getEmployee());
            if (exist) {
                throw new WrongPaymentException();
            }
        });

        payments.forEach(payment -> {
            paymentRepository.insertPayments(payment.getPeriodDate(), payment.getSalary(), payment.getEmployee());
        });
    }

    public void updatePayment(PaymentDTO payment) {
        var count = paymentRepository.updatePayment(payment.getEmployee(), payment.getPeriodDate(), payment.getSalary());
        if (count <= 0) {
            throw new WrongPaymentException();
        }
    }

    public List<GetPaymentDTO> selectAllPayments(String email) {
        List<Payment> payments = paymentRepository.findPaymentsByUser_EmailOrderByPeriodDesc(email);
        return payments.stream().map(payment -> {
            GetPaymentDTO dto = new GetPaymentDTO();
            dto.setPeriod(payment.getPeriod());
            dto.setSalary(payment.getSalary());
            dto.setName(payment.getUser().getName());
            dto.setLastname(payment.getUser().getLastname());

            return dto;
        }).collect(Collectors.toList());
    }

    public GetPaymentDTO selectPeriodPayment(String email, Date period) {
        Payment payment = paymentRepository
                .findPaymentByPeriodAndUser_Email(period, email)
                .stream().findFirst().get();

        GetPaymentDTO dto = new GetPaymentDTO();
        dto.setPeriod(payment.getPeriod());
        dto.setSalary(payment.getSalary());
        dto.setName(payment.getUser().getName());
        dto.setLastname(payment.getUser().getLastname());

        return dto;
    }

}
