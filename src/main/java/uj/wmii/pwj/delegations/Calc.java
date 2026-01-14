package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Calc {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");

    public BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        ZonedDateTime startDateTime = ZonedDateTime.parse(start, FORMATTER);
        ZonedDateTime endDateTime = ZonedDateTime.parse(end, FORMATTER);

        if (!startDateTime.isBefore(endDateTime)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long totalHours = ChronoUnit.HOURS.between(startDateTime, endDateTime);
        long totalMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

        long fullDays = totalHours / 24;
        long remainingHours = totalHours % 24;
        long remainingMinutes = totalMinutes % 60;

        double remainingTime = remainingHours + (remainingMinutes / 60.0);

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (fullDays > 0) {
            totalAmount = totalAmount.add(dailyRate.multiply(BigDecimal.valueOf(fullDays)));
        }

        if (remainingTime > 0) {
            BigDecimal partialDayAmount;

            if (remainingTime <= 8) {
                partialDayAmount = dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
            }
            else if (remainingTime <= 12) {
                partialDayAmount = dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            }
            else {
                partialDayAmount = dailyRate;
            }

            totalAmount = totalAmount.add(partialDayAmount);
        }

        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }
}