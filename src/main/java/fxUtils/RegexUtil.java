package fxUtils;

/**
 * Class for checking the correctness of the entered strings, based on regular expressions.
 */
public class RegexUtil {

    /**
     * @return true if selected city match to city regex, false otherwise.
     */
    public static boolean cityRegex(String city) {
        return city.matches("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]* *)+");
    }

    /**
     * @return true if selected distance match to distance regex, false otherwise.
     */
    public static boolean distanceRegex(String distance) {
        return distance.matches("([1-9][0-9]*)*(25|50|00)");
    }

    /**
     * @return true if selected personal id match to personal id regex, false otherwise.
     */
    public static boolean peselRegex(String pesel) {
        return pesel.matches("[0-9]{11}");
    }

    /**
     * @return true if selected name match to name regex, false otherwise.
     */
    public static boolean nameRegex(String name) {
        return name.matches("[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*");
    }

    /**
     * @return true if selected surname match to surname regex, false otherwise.
     */
    public static boolean surnameRegex(String surname) {
        return surname.matches("([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)(-[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*)?");
    }

    /**
     * @return true if selected record match to record regex, false otherwise.
     */
    public static boolean recordRegex(String record) {
        return record.matches("[0-9]{2}:[0-5][0-9]:[0-9]{2}");
    }
}
