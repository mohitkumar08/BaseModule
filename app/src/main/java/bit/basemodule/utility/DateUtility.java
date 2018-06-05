package bit.basemodule.utility;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {
    String SEPARATOR = "-";
    String DD_MM_YYYY = "dd-MM-yyyy";
    String dd_MM_YYYY_T_HH_MM_SS_Z = "dd-MM-yyyy'T'HH:mm:ssZ";
    String DD_MMM_YYYY = "dd MMM yyyy";
    String MMM_DD = "MMM dd ";

    public static String convertDateAnyFormat(String fromPattern, String toPattern, String dateValue) {
        try {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat fromPatternFormat = new SimpleDateFormat(fromPattern, Locale.US);
            fromPatternFormat.setTimeZone(timeZone);
            SimpleDateFormat toPatternFormat = new SimpleDateFormat(toPattern, Locale.US);
            toPatternFormat.setTimeZone(timeZone);
            return toPatternFormat.format(fromPatternFormat.parse(dateValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
