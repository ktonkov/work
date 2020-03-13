package iss.work.addressbook.model;

public class ContactData {
    private final String firstName;
    private final String lastName;
    private final String homePhone;
    private final String email;

    public ContactData(
        String firstName,
        String lastName,
        String homePhone,
        String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.homePhone = homePhone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getEmail() {
        return email;
    }
}
