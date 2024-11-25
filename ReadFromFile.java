import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromFile {
    public static ContactsRecord readContacts(String filename) {
        ContactsRecord contactsRecord = new ContactsRecord();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Contact contact = ContactsRecord.parseContact(line);
                if (contact != null){
                    contactsRecord.addUniqueContact(contact);
                }
            }
        } catch (IOException e) {
            System.out.println("Rolodex is empty: " + e.getMessage());
        }
        return contactsRecord;
    }

}
