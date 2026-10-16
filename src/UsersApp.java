//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class UsersApp{


    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner reader = new Scanner(new File("./src/users.txt"))) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.trim().split("\\s+");

                try {
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid input format");
                    }

                    User user = new User(parts[0], parts[1]);
                    users.add(user);

                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        }

        users.sort(Comparator.comparing(User::getUsername));

        for (User user : users) {
            System.out.println(user);
        }
    }

    User parseUser(String user){
        String[] x = user.trim().split("\\s+");
        if (x.length == 2){
            return new User(x[0],x[1]);
        }
        return null;
    }
    private static final String EMAIL_REGEX =
            "[a-zA-Z0-9._+%-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}";

    private static final String PASSWORD_REGEX =
            "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#@!+.,])[a-zA-Z0-9#@!+.,]{8,12}";

}
