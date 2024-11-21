import java.util.*;

// Step 1: Define the Contact class
public class Contact {
    //Class fields
    private String name;
    private String phoneNumber;
    private String email;
    private Set<String> countries;
    private Set<String> cities;
    private Set<String> companies;
    private Set<String> tags;
    private String bio;
    public static final String NA = "UNKNOWN";
    public static final int FIELDS = 8;
    
    // Constructors
    // Main Constructor
    public Contact(String name, String phoneNumber, String email, Set<String> countries,
                   Set<String> cities, Set<String> companies, Set<String> tags, String bio) {
        this.name = name;
        this.phoneNumber = phoneNumber != null ? phoneNumber : NA ;
        this.email = email != null ? email : NA;
        this.countries = countries != null ? new HashSet<>(countries) : new HashSet<>();
        this.cities = cities != null ? new HashSet<>(cities) : new HashSet<>();
        this.companies = companies != null ? new HashSet<>(companies) : new HashSet<>();
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
        this.bio = bio != null ? bio : NA;
    }
     // Factory method for creating Contact with strings
    public static Contact createContactWithStrings(String name, String phoneNumber, String email,
                                                   String countriesStr, String citiesStr,
                                                   String companiesStr, String tagsStr, String bio) {
        return new Contact(name, phoneNumber, email, parseStringToSet(countriesStr),
                           parseStringToSet(citiesStr), parseStringToSet(companiesStr),
                           parseStringToSet(tagsStr), bio);
    }

    // Helper method to parse string to Set
    private static Set<String> parseStringToSet(String str) {
        return str != null && !str.isEmpty() ? new HashSet<>(Arrays.asList(str.split(";"))) : new HashSet<>();
    }

    // Overloaded constructor with fewer parameters
    public Contact(String name, String phoneNumber, String email, String countries) {
        createContactWithStrings(name, phoneNumber, email, countries, null, null, null, null);
    }

    public Contact(String name, String phoneNumber, String email) {
        this(name, phoneNumber, email,null, null, null, null, null);
    }

    public Contact(String name, String phoneNumber) {
        this(name, phoneNumber, null, null, null, null, null, null);
    }

    public Contact(String name) {
        this(name, null, null, null, null, null, null, null);
    }

// Getters
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    // Custom getters that returns a delimited string using semi-colon as the delimiter
    public String getCitiesAsString() {
        return String.join(";", cities);  
    }

    public String getCountriesAsString() {
        return String.join(";", countries);
    }
    
    public String getCompaniesAsString() {
        return String.join(";", companies);
    }

    public String getTagsAsString() {
        return String.join(";", tags);  
    }

    public String getBio() {
        return bio;
    }

// Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : NA;
    }
    public void setEmail(String email) {
        this.email = email != null ? email : NA;
    }

    // Custom setters that take a delimited string
    public void setCitiesFromString(String citiesStr) {
        this.cities = (citiesStr == null || citiesStr.isEmpty()) ? new HashSet<>() : new HashSet<>(Arrays.asList(citiesStr.split(";")));
    }

    public void setCountriesFromString(String countriesStr) {
        this.countries = (countriesStr == null || countriesStr.isEmpty()) ? new HashSet<>() : new HashSet<>(Arrays.asList(countriesStr.split(";")));
    }

    public void setCompaniesFromString(String companiesStr) {
        this.companies = (companiesStr == null || companiesStr.isEmpty()) ? new HashSet<>() : new HashSet<>(Arrays.asList(companiesStr.split(";")));
    }

    public void setTagsFromString(String tagsStr) {
        this.tags = (tagsStr == null || tagsStr.isEmpty()) ? new HashSet<>() : new HashSet<>(Arrays.asList(tagsStr.split(";")));
    }

    public void setBio(String bio) {
        this.bio = bio != null ? bio : NA;
    }

    // toString method
    @Override
    public String toString() {
        return this.getName() + "," + this.getPhoneNumber() + "," + this.getEmail() + "," + 
           this.getCountriesAsString() + "," + this.getCitiesAsString()
           + "," + this.getCompaniesAsString() + "," + 
           this.getTagsAsString() + "," + this.getBio();
    }
    
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        appendField(sb, this.getPhoneNumber());
        appendField(sb, this.getEmail());
        appendField(sb, this.getCountriesAsString());
        appendField(sb, this.getCitiesAsString());
        appendField(sb, this.getCompaniesAsString());
        appendField(sb, this.getTagsAsString());
        appendField(sb, this.getBio());
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing comma
        }
        return sb.toString();
    }

    private void appendField(StringBuilder sb, String field) {
        if (field != null && !field.isEmpty()) {
                sb.append(",").append(field);
            }
    }


    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Contact contact = (Contact) obj;
        return Objects.equals(name, contact.name); // Use name for equality check
    }

    // hashCode method combining name, email and phoneNumber
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    
    // Simple email validation method
    public boolean isValidEmail() {
        return this.email.contains("@");
    }
}
