import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AppendToFile {
    public static void appendContacts(String filename, List<Contact> contacts) {
        try (FileWriter writer = new FileWriter(filename, true)) { // true to append
            for (Contact contact : contacts) {
                writer.write(contact.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Or handle the exception as required
        }
    }
}

