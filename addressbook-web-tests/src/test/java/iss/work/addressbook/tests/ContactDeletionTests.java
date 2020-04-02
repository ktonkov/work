package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {
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
    public void testContactDeletion() throws Exception {
        ContactData contactToDelete = before.iterator().next();
        Contacts after = app.contacts().delete(contactToDelete).getAll();
        assertThat(after.size(), equalTo(before.size() - 1));
        assertThat(after, equalTo(before.without(contactToDelete)));
    }
}
