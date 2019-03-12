package apps.liamm.graderly.utilities;

public class FormValidation {

    public static boolean isValidEmail(String email) {
        return (!email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidMobileNumber(String number) {
        if (number.isEmpty()) {
            return false;
        }

        if (number.length() == 11) {
            return number.substring(0, 1).equals("07");
        } else if (number.length() == 12) {
            return number.substring(0, 2).equals("+44");
        }
        return false;
    }
}
