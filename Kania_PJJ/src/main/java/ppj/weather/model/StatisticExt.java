package ppj.weather.model;

import ppj.weather.model.Intervals;

import java.util.Calendar;
import java.util.Date;

public class StatisticExt {

    public static Intervals getWeatherStatisticsIntervalByInput(String input) {
        if(input == null) {
            return Intervals.DAY;
        }

        input = input.toUpperCase();

        switch (input) {
            case "WEEK":
                return Intervals.WEEK;
            case "TWO_WEEKS":
                return Intervals.TWO_WEEKS;

                default:
                    return Intervals.DAY;
        }
    }

    public static Date getDateRangeFromNowByInterval(Intervals interval) {
        Calendar calendar = Calendar.getInstance();

        switch (interval) {
            case WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case TWO_WEEKS:
                calendar.add(Calendar.DAY_OF_YEAR, -14);
                break;

                default:
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    break;
        }

        return calendar.getTime();
    }

}
