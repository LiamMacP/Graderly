package apps.liamm.graderly.utilities;

public class FormValidation {

    public static boolean isValidEmail(String email) {
        return (!email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
