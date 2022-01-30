package com.jaime.account.Services;

import com.jaime.account.DTOs.GetPaymentDTO;
import com.jaime.account.DTOs.PaymentDTO;
import com.jaime.account.Errors.WrongPaymentException;
import com.jaime.account.Models.Payment;
import com.jaime.account.Repository.PaymentRepository;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public interface PaymentService {
    void insertPayments(List<PaymentDTO> payments);

    void updatePayment(PaymentDTO payment);

    List<GetPaymentDTO> selectAllPayments(String email);

    GetPaymentDTO selectPeriodPayment(String email, Date period);

    default Date stringToDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        Date javaDate = null;
        try {
            javaDate = new Date(format.parse(date).getTime());
        } catch (ParseException e) {
            throw new WrongPaymentException();
        }
        return javaDate;
    }
}
