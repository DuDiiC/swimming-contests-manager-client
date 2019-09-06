package fxUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for checking the correctness of the entered strings, based on regular expressions.
 */
public class RegexUtil {

    public static boolean cityRegex(String city) {
        return city.matches("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]* *)+");
//        return regex("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]* *)+", city);
    }

    public static boolean distanceRegex(String distance) {
        return distance.matches("([1-9][0-9]*)*(25|50|00)");
//        return regex("([1-9][0-9]*)*(25|50|00)", distance);
    }

    public static boolean peselRegex(String pesel) {
        return pesel.matches("[0-9]{11}");
//        return regex("[0-9]{11}", pesel);
    }

    public static boolean nameRegex(String name) {
        return name.matches("[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*");
//        return regex("[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*", name);
    }

    public static boolean surnameRegex(String surname) {
        return surname.matches("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)(-[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)?");
//        return regex("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)(-[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)?", surname);
    }

    public static boolean recordRegex(String record) {
        return record.matches("[0-9]{2}:[0-5][0-9]:[0-9]{2}");
//        return regex("[0-9]{2}:[0-5][0-9]:[0-9]{2}", record);
    }

//    private static boolean regex(String p, String m) {
//        Pattern pattern = Pattern.compile(p);
//        Matcher matcher = pattern.matcher(m);
//        return matcher.matches();
//    }
}
