package com.jaime.account.Repository;

import com.jaime.account.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Payment(USER_ID,PERIOD,SALARY) SELECT USER.ID, ?1 , ?2 FROM USER WHERE USER.EMAIL = ?3 ", nativeQuery = true)
    void insertPayments(Date period, long salary, String email);

    boolean existsPaymentByPeriodAndUser_Email(Date Period, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PAYMENT SET SALARY = ?3 WHERE USER_ID = (SELECT ID FROM USER WHERE USER.EMAIL = ?1) AND PERIOD = ?2", nativeQuery = true)
    int updatePayment(String email, Date period, long salary);

    List<Payment> findPaymentsByUser_EmailOrderByPeriodDesc(String email);

    List<Payment> findPaymentByPeriodAndUser_Email(Date period, String email);
}
