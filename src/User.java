public class User {
    private String username;
    private String password;

    private static final String EMAIL_REGEX =
            "[a-zA-Z0-9._+%-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}";

    private static final String PASSWORD_REGEX =
            "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])[a-zA-Z0-9!@#$%^&*()]{8,12}";

    public User(String username, String password) {
        String emailError = validateEmail(username);
        if (emailError != null) {
            throw new IllegalArgumentException(emailError);
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            throw new IllegalArgumentException(passwordError);
        }

        this.username = username;
        this.password = password;
    }

    private static String validateEmail(String email) {
        if (email.length() > 50) {
            return "Username is too long, try something shorter";
        }

        if (!email.matches(EMAIL_REGEX)) {
            return "Please enter a valid Email as username";
        }

        return null;
    }

    private static String validatePassword(String password) {
        if (password.length() < 8) {
            return "Your password is too short, add more characters";
        }

        if (password.length() > 12) {
            return "Your password is too long, try a shorter one";
        }

        if (!password.matches(PASSWORD_REGEX)) {
            return "Please enter a valid password";
        }

        return null;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return username + " " + password;
    }
}