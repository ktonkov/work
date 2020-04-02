package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static iss.work.addressbook.tests.TestBase.app;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPhoneTests extends TestBase {
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
    public void checkPhone() {
        ContactData contact = before.iterator().next();
        ContactData contactDataFromEditForm = app.contacts().contactDataFromEditForm(contact);
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactDataFromEditForm)));
    }

    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
                .stream().filter((s) -> !s.equals(""))
                .map((s) -> s.replaceAll("\\s", "").replaceAll("[-()]", ""))
                .collect(Collectors.joining("\n"));
    }
}
