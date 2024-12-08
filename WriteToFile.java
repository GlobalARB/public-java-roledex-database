import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteToFile {
    public static void writeContacts(String filename, ContactsRecord contactsRecord) {
        try (FileWriter writer = new FileWriter(filename)) {
            List<Contact> contacts = contactsRecord.getContacts();
            for (Contact contact : contacts) {
                writer.write(contact.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Or handle the exception as required
        }
    }
}
