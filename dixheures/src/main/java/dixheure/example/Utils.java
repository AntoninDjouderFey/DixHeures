package dixheure.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {


    /**
     * @param datestr
     * @return
     * @throws DateTimeParseException
     */
    public static LocalDate parseDate(String datestr) throws DateTimeParseException{

        LocalDate date = null;

        try {
            DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("yyyy MM dd");
                            date = LocalDate.parse(datestr.trim(), formatter);
            System.out.printf("%s%n", date);
        }
        catch (DateTimeParseException exc) {
            System.out.printf("%s is not parsable!%n", datestr);
            throw exc;      // Rethrow the exception.
        }


        return date;

        
    }




public static void main(String[] args) {
    String date = "2003 06 03";
    LocalDate parsedDate = Utils.parseDate(date);
    System.out.println(parsedDate);
}

}