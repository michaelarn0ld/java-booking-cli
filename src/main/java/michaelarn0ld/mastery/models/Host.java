package michaelarn0ld.mastery.models;

import java.math.BigDecimal;

public class Host extends Client {

    /*
    FIELDS
     */
    private String id;
    private String streetAddress;
    private String city;
    private String postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

    /*
    CONSTRUCTORS
     */
    public Host(){}

    public Host(String id) {
        this.id = id;
    }

    /*
    GETTERS & SETTERS
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }
}
