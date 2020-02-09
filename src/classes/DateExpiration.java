package classes;

import java.time.LocalDate;
import java.io.Serializable;

public class DateExpiration implements Serializable {
    private int year, month, day;

    public DateExpiration(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateExpiration(LocalDate localDate, int longevite) {
        localDate = localDate.plusDays(longevite);

        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
