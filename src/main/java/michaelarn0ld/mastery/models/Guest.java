package michaelarn0ld.mastery.models;

public class Guest extends Client {

    /*
    FIELDS
     */
    private int id;
    private String firstName;

    /*
    CONSTRUCTORS
     */
    public Guest(){}

    public Guest(int id) {
        this.id = id;
    }

    /*
    GETTERS & SETTERS
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}