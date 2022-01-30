package com.jaime.account.DTOs;

import com.jaime.account.Validation.PeriodValidation;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentDTO)) return false;
        PaymentDTO that = (PaymentDTO) o;
        return getEmployee().equals(that.getEmployee()) && getPeriod().equals(that.getPeriod());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Pattern(regexp = "[\\w.]+(@acme.com)$", message = "Email must be from acme.com domain")
    @Email(message = "You must enter a valid email")
    @NotBlank(message = "Email must not be null!")
    private String employee;

    @PeriodValidation
    private String period;

    @Min(value = 1, message = "Salary must be non negative!")
    private long salary;

    public Date getPeriodDate() {
        SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
        format.setLenient(false);
        Date javaDate = null;
        try {
            javaDate = new Date(format.parse(this.period).getTime());
        } catch (ParseException e) {
            System.out.println(period + " is Invalid Date format");
        }
        return javaDate;
    }
}
