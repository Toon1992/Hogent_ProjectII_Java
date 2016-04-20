package domein;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by donovandesmedt on 13/04/16.
 */
public class HulpMethode {
    public static Date geefEersteDagVanDeWeek(LocalDate datum){
        switch (datum.getDayOfWeek())
        {
            case TUESDAY:
                datum = datum.minusDays(1);
                break;
            case WEDNESDAY:
                datum = datum.minusDays(2);
                break;
            case THURSDAY:
                datum = datum.minusDays(3);
                break;
            case FRIDAY:
                datum = datum.minusDays(4);
                break;
            case SATURDAY:
                datum = datum.minusDays(5);
                break;
            case SUNDAY:
                datum = datum.minusDays(6);
                break;
            default:
        }

        return convertLocalDateToDate(datum);
    }
    public static Date convertLocalDateToDate(LocalDate datum)
    {
        Instant instant = Instant.from(datum.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }
    public static int getWeekOfDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    public static Date getFirstDayOfWeek(int week){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }
}
