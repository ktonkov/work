package iss.work.addressbook.model;

public class ContactData {
    private final String firstName;
    private final String lastName;
    private final String homePhone;
    private final String email;
    private final String group;

    public ContactData(
            String firstName,
            String lastName,
            String homePhone,
            String email,
            String group
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.homePhone = homePhone;
        this.email = email;
        this.group = group;
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

    public String getGroup() {
        return group;
    }
}
