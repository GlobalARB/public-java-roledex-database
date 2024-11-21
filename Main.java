import java.util.*;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Main {
    private static Scanner scanner = new Scanner(System.in); 
    private static ContactsRecord CR = new ContactsRecord();
    private static final String RETURN_MAIN_MENU = "To return to the main menu, type 'main'";
    private static final String MAIN ="main";
    private static final String ROLEDEX = "Roledex.csv";
    private static final String YES = "Yes"; 
    private static final String NO = "No"; 
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String COMPANY = "company";
    private static final String TAG = "tag";
    private static final String NA = "UNKNOWN";
    private static final Set<String> fields = Set.of(COUNTRY,CITY,COMPANY,TAG);
        
    public static void main(String[] args) { 
        boolean save = true;
        boolean changed = false;
        //Skip user input if passed with command-line arg 
        if (args.length == 1) {
            String readFile = args[0];
            ContactsRecord RCR = ReadFromFile.readContacts(readFile);
            CR.addContactsRecord(RCR); 
            scanner.close();
        }
        ContactsRecord MCR = ReadFromFile.readContacts(ROLEDEX);
        CR.addContactsRecord(MCR); 
        mainLoop:
        while (true) { // Loop until EOF is reached
            displayMenu();
            String input = scanner.nextLine().trim(); // Read input
            System.out.println("");
            if (input.equalsIgnoreCase("exit")) {
                save = exitMenu();
                System.out.println("Exititing Program");
                break; // Exit the loop if user inputs 'exit'
            }
            int choice = -1;
            try {
                choice = Integer.parseInt(input); // Convert input to integer
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'exit'.");
                continue;
            }
            switch (choice) {
                case (1):
                    handleSearchByName();
                    break;
                case (2):
                    handleSearchByParam(COUNTRY);
                    break;
                case (3):
                    handleSearchByParam(CITY);
                    break;
                case (4): 
                    handleSearchByParam(COMPANY);
                    break;
                case (5):
                    handleSearchByParam(TAG);
                    break;
                case (6):
                    handleAddContact();
                    changed = true;
                    break;
                case (7):
                    System.out.println("You have decided to import from file.");
                    System.out.println("Write the exact name of the file in the directory, including .csv or .txt: ");
                    String rf = scanner.nextLine().strip();
                    ContactsRecord RFCR = ReadFromFile.readContacts(rf);
                    CR.addContactsRecord(RFCR);
                    changed = true;
                    break;
                case(8):
                    displayOptions();
                    break;
                case (9):
                    handleDoubleSearch();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }            
        }
    scanner.close(); // Close the scanner      
    //Update main Roledex with new Contacts 
    if (save & changed) {
        AppendToFile.appendContacts(ROLEDEX, CR.getContacts());
        }
    }
    private static void displayOptions() {
        System.out.println("Available countries: " + CR.mapByCountries.keySet()); // Debugging ou
        System.out.println("");
        System.out.println("Available cities: " + CR.mapByCities.keySet()); // Debugging output
        System.out.println("");
        System.out.println("Available companies: " + CR.mapByCompanies.keySet()); // Debugging ou
        System.out.println("");
        System.out.println("Available tags: " + CR.mapByTags.keySet()); // Debugging output
        System.out.println("");
    }
    private static String readInput() {
            String input = scanner.nextLine().strip().toLowerCase();
            if (MAIN.equalsIgnoreCase(input)) {
                System.out.println("");
                return "main";
            } else {
                return input;
            }
    }
    
    private static void displayMenu() {
        System.out.println("Welcome to the Personal Roledex of Alexander Ryssdal-Banoun");
        System.out.println("You have ten options:");
        System.out.println("    1. Seach by Name");
        System.out.println("    2. Seach by Country");
        System.out.println("    3. Seach by City");
        System.out.println("    4. Seach by Company");
        System.out.println("    5. Seach by Tags");
        System.out.println("    6. Add Contact");
        System.out.println("    7. Import contacts from file");
        System.out.println("    8. List of all possible countries, cities, tags, & companies"); 
        System.out.println("    9. Double search");
        System.out.println("    exit to exit");
        System.out.printf("Decide by number or exit: ");
    }
    private static boolean exitMenu() {
        int attempts = 0;
        System.out.println("Befor exiting, do you want to save the changes you've made to Rolodex?");
        while (attempts < 3) {
            System.out.println("Type yes or no: ");
            String ans = scanner.nextLine().strip();
            if (YES.equalsIgnoreCase(ans)){
                return true;
            } else if (NO.equalsIgnoreCase(ans)) {
                return false;
            } else {
                System.out.println("Invalid choice. Please try again.");
                attempts++;
            }
        }
        System.out.println("Too many invalid attempts. Exiting without saving.");
        return false;
    }
   private static void handleDoubleSearch(){
        System.out.println("You have decided to double search");
        System.out.printf("Type yes for full profile list, default is names only: ");
        String ans = scanner.nextLine().strip(); // Read the next line of input
        while (true){
            System.out.println("Input the first field of search, followed by the query, separated by comma, ex {country,ita}:");
            String xline = readInput(); 
            if (xline.equalsIgnoreCase(MAIN)) {return;}
            String[] xparts = xline.split(",");
            if (!fields.contains(xparts[0])){
                System.out.println("Invalid input.");
                continue;
            }
            System.out.println("Input the second field of search, followed by the query, separated by comma, ex {tag,mist}:");
            String yline = readInput(); 
            if (yline.equalsIgnoreCase(MAIN)) {return;}
            String[] yparts = yline.split(",");
            if (!fields.contains(yparts[0])){
                System.out.println("Invalid input.");
                continue;
            }
            List<Contact> list = CR.doubleSearch(xparts[0],xparts[1],yparts[0],yparts[1]);
            if (list.isEmpty()) {
                System.out.println("No matches.");
            } else {
                printList(list, ans); 
            }
        }        
   }
   
   private static void handleSearchByName(){
        System.out.println("You have decided to search by name");
        System.out.println(RETURN_MAIN_MENU);
        while (true){
            System.out.printf("Input full name: ");
            String name = readInput();
            if (name.equalsIgnoreCase(MAIN)) {return;}
            Contact person = CR.findContact(name);
            if (person!= null) {
                System.out.printf("Contact found: ");
                System.out.println(person.toDisplayString());
            }   
            else {
            System.out.println("Contact not found.");
            }    
        }
    }

    private static void handleSearchByParam(String param){
        System.out.println("You have decided to search by " + param + ".");
        System.out.printf("Type yes for full profile list, default is names only: ");
        String ans = readInput();
        if (ans.equalsIgnoreCase(MAIN)) {return;}
        System.out.println(RETURN_MAIN_MENU);
        while (true){
            System.out.printf("Input full name of %s: ", param);
            String instance = readInput();
            if (instance.equalsIgnoreCase(MAIN)) {return;}
            List<Contact> list = CR.search(param, instance);
            if (list.isEmpty()) {
                System.out.println("No contacts found in " + instance);
            } else {
                printList(list, ans); 
            }
        }
    }
    private static void printList(List<Contact> list, String ans){
        if (ans.equalsIgnoreCase(YES)) {
            System.out.println("Profiles of matches:"); 
            System.out.println("");
            for (Contact c : list) {
                System.out.println(c.toDisplayString());
            }
        } else {
            System.out.println("Names of matches:");
            System.out.println("");
            for (Contact c : list) {
                System.out.println(c.getName());
            }
        }
    }

    private static String getInputOrDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().strip();
        if (MAIN.equalsIgnoreCase(input)) {
            return null; // User wants to return to the main menu
        }
        return input.isEmpty() ? defaultValue : input;
    }

    private static void handleAddContact() {
        System.out.println("You have decided to add a contact. If any field, except name, is unknown hit enter.");
        while (true){
            String name = getInputOrDefault("Name: ", null);
            if (name == null || CR.findContact(name) != null) {
                if (name != null) {
                    System.out.println("Contact with this name already exists.");
                }
                return;
            }
            String phoneNumber = getInputOrDefault("Phone number: ", Contact.NA);
            if (phoneNumber == null) { return; }
            String email = getInputOrDefault("Email: ", Contact.NA);
            if (email == null) { return; }
            System.out.println("The fields countries, cities, companies and tags, can take multiple parameters, separate with semicolon");
            String countries = getInputOrDefault("Country: ", Contact.NA);
            if (countries == null) { return; }
            String cities = getInputOrDefault("Cities: ", Contact.NA);
            if (cities == null) { return; }
            String companies = getInputOrDefault("Companies: ", Contact.NA);
            if (companies == null) { return; }
            String tags = getInputOrDefault("Tags: ", Contact.NA);
            if (tags == null) { return; }
            String bio = getInputOrDefault("Bio: ", Contact.NA);
            if (bio == null) { return; }
            CR.addContact(Contact.createContactWithStrings(name, phoneNumber, email, countries, cities, companies, tags, bio)); 
            System.out.printf("New contact %s added.\n", name); 
        }
    }
}
