import java.util.*;
import java.io.IOException;


// Step 2: Define the ContactsRecord class
public class ContactsRecord {
    public List<Contact> contacts;
    public Map<String, List<Contact>> mapByCountries;
    public Map<String, List<Contact>> mapByCities;
    public Map<String, List<Contact>> mapByCompanies;
    public Map<String, List<Contact>> mapByTags;

    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String COMPANY = "company";    
    private static final String TAG = "tag";    
    public static final String NA = "UNKNOWN";

    // Constructor
    public ContactsRecord() {
        this.contacts = new ArrayList<>();
        mapByCountries = new HashMap<>();
        mapByCities = new HashMap<>();
        mapByCompanies = new HashMap<>();
        mapByTags = new HashMap<>();
    }

    // Method to add a new contact
    public void addContact(Contact contact) {
    contacts.add(contact);
    Arrays.stream(contact.getCountriesAsString().toLowerCase().split(";"))
          .forEach(country -> mapByCountries.computeIfAbsent(country, k -> new ArrayList<>()).add(contact));
    Arrays.stream(contact.getCitiesAsString().toLowerCase().split(";"))
          .forEach(city -> mapByCities.computeIfAbsent(city, k -> new ArrayList<>()).add(contact));
    Arrays.stream(contact.getCompaniesAsString().toLowerCase().split(";"))
          .forEach(company -> mapByCompanies.computeIfAbsent(company, k -> new ArrayList<>()).add(contact));
    Arrays.stream(contact.getTagsAsString().toLowerCase().split(";"))
          .forEach(tag -> mapByTags.computeIfAbsent(tag, k -> new ArrayList<>()).add(contact));
    }


    // Method to add a unique contact
    public void addUniqueContact(Contact contact) {
        if (findContact(contact.getName())==null) {
            addContact(contact);
        } else {
            System.out.println("Contact with name " + contact.getName() + " already exists.");
        }
    }

    // Method to add all contacts form ContactsRecord to this ContactsRecord
    public void addContactsRecord(ContactsRecord CR) {
        for (Contact contact : CR.getContacts()){
            this.addContact(contact);
        }
    }

    // Method to remove a contact
    public void removeContact(String name) {
        boolean removed = contacts.removeIf(contact -> contact.getName().equals(name));
        if (removed) {
            // Also remove from all maps to maintain consistency
            mapByCountries.values().forEach(list -> list.removeIf(contact -> contact.getName().equals(name)));
            mapByCities.values().forEach(list -> list.removeIf(contact -> contact.getName().equals(name)));
            mapByCompanies.values().forEach(list -> list.removeIf(contact -> contact.getName().equals(name)));
            mapByTags.values().forEach(list -> list.removeIf(contact -> contact.getName().equals(name)));
        } else {
            System.out.println("Contact with name " + name + " not found.");
        }
    }

    // Method to find a contact by name
    public Contact findContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null; // or throw an exception if preferred
    }

    //Method to get all contacts from Country
    public List<Contact> searchCountry(String country) {
        return mapByCountries.getOrDefault(country.toLowerCase(), Collections.emptyList());
    }
 
    //Method to get all contacts from a City
    public List<Contact> searchCity(String city) {
        return mapByCities.getOrDefault(city.toLowerCase(), Collections.emptyList());
    }
 
    //Method to get all contacts from a Company 
    public List<Contact> searchCompany(String company) {
        return mapByCompanies.getOrDefault(company.toLowerCase(), Collections.emptyList());
    }
 
    //Method to get all contacts with a tag 
    public List<Contact> searchTag(String tag) {
        return mapByTags.getOrDefault(tag.toLowerCase(), Collections.emptyList());
    } 
    
    // Method to get a copy of the contacts list
    public List<Contact> getContacts() {
        return new ArrayList<>(contacts); // Return a copy of the contacts list
    }

    // Method to print all contacts in a list of contacts 
    public void printAll(){
        for (Contact contact : this.getContacts()) {
            System.out.println(contact.toString());
        }
    }
    //Generic search method
    public List<Contact> search(String field, String query) {
        query = query.toLowerCase();
        System.out.println("Searching for: " + query.toLowerCase()); // Debugging output
        System.out.println("");
        switch (field.toLowerCase()) {
            case COUNTRY:
                return mapByCountries.getOrDefault(query, new ArrayList<>());
            case CITY:
                return mapByCities.getOrDefault(query, new ArrayList<>());
            case COMPANY:
                return mapByCompanies.getOrDefault(query, new ArrayList<>());
            case TAG:
                return mapByTags.getOrDefault(query, new ArrayList<>());
            default:
                System.out.println("Unknown fields"); // Debugging output
                // Handle unknown field
                return new ArrayList<>();
        }
    }
    //Generic search method that returns a set
    public Set<Contact> setSearch(String field, String query) {
        query = query.toLowerCase();
        System.out.println("Searching for: " + query.toLowerCase()); // Debugging output
        System.out.println("");
        switch (field.toLowerCase()) {
            case COUNTRY:
                List<Contact> countryList = mapByCountries.getOrDefault(query, new ArrayList<>());
                return new HashSet<>(countryList);
            case CITY:
                List<Contact> cityList = mapByCities.getOrDefault(query, new ArrayList<>());
                return new HashSet<>(cityList);
            case COMPANY:
                List<Contact> companyList = mapByCompanies.getOrDefault(query, new ArrayList<>());
                return new HashSet<>(companyList);
            case TAG:
                List<Contact> tagList = mapByTags.getOrDefault(query, new ArrayList<>());
                return new HashSet<>(tagList);
            default:
                System.out.println("Unknown fields"); // Debugging output
                // Handle unknown field
                return new HashSet<>();
        }
    }
    // Method to intersect two sets of contacts
    private Set<Contact> intersect(Set<Contact> set1, Set<Contact> set2) {
        Set<Contact> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    //Searching for two paramters
    public List<Contact> doubleSearch(String xfield, String xquery, String yfield, String yquery) {
        Set<Contact> contactsInX = setSearch(xfield, xquery);
        Set<Contact> contactsInY = setSearch(yfield, yquery);
        List<Contact> matches = new ArrayList<>(intersect(contactsInX, contactsInY));
        return matches;
    }
    // Search method
    public static Contact parseContact(String line) {
        if (line == null || line.trim().isEmpty()) {
        // Handle the case where the line is empty or doesn't contain commas
        // You can throw an exception, return null, or create a default Contact object
            return null; // or handle this case as appropriate for your application
        }
        String[] fields = line.split(",\\s*");
        if (fields.length==0){
            return null;
        }
        //Parse fields
        String name = fields[0].trim();
        String phoneNumber = fields.length > 1 ? fields[1].trim() : Contact.NA;
        String email = fields.length > 2 ? fields[2].trim() : Contact.NA;
        Set<String> countries = fields.length > 3 ? new HashSet<>(Arrays.asList(fields[3].split(";"))) : new HashSet<>();
        Set<String> cities = fields.length > 4 ? new HashSet<>(Arrays.asList(fields[4].split(";"))) : new HashSet<>();
        Set<String> companies = fields.length > 5 ? new HashSet<>(Arrays.asList(fields[5].split(";"))) : new HashSet<>();
        Set<String> tags = fields.length > 6 ? new HashSet<>(Arrays.asList(fields[6].split(";"))) : new HashSet<>();
        String bio = fields.length > 7 ? fields[7].trim() : Contact.NA;
        return new Contact(name, phoneNumber, email, countries, cities, companies, tags, bio);
    }

}
