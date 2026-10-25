
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
}
