package model;

public class Customer {
    private int    customerId;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public Customer() {}

    public Customer(int customerId, String fullName, String email, String phone, String address) {
        this.customerId = customerId;
        this.fullName   = fullName;
        this.email      = email;
        this.phone      = phone;
        this.address    = address;
    }

    public int    getCustomerId() { return customerId; }
    public String getFullName()   { return fullName; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }
    public String getAddress()    { return address; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setFullName(String fullName)  { this.fullName = fullName; }
    public void setEmail(String email)        { this.email = email; }
    public void setPhone(String phone)        { this.phone = phone; }
    public void setAddress(String address)    { this.address = address; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %s", customerId, fullName, email, phone);
    }
}
