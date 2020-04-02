package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactEmailTests extends TestBase {
    private Contacts before;

    @BeforeClass
    public void prepare() {
        app.goTo().homePage();
        if (app.contacts().getAll().size() == 0) {
            before = app.contacts().create(new ContactData().withFirstName("test")).getAll();
        } else {
            before = app.contacts().getAll();
        }
    }

    @Test
    public void checkEmail() {
        ContactData contact = before.iterator().next();
        ContactData contactDataFromEditForm = app.contacts().contactDataFromEditForm(contact);
        assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactDataFromEditForm)));
    }

    private String mergeEmails(ContactData contact) {
        return Arrays.asList(contact.getEmail1(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s) -> !s.equals(""))
                .collect(Collectors.joining("\n"));
    }
}
