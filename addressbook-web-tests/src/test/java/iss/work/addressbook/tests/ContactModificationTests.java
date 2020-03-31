package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() throws Exception {
        app.getNavigationHelper().gotoHomePage();
        if (!app.getContactHelper().isThereAContact()) {
            app.getContactHelper().creatContact(new ContactData("testname", "testlastname", "111",
                    "test@test.com", "test1"));
        }
        int before = app.getContactHelper().getContactCount();
        app.getContactHelper().modifyFirstContact();
        app.getContactHelper().fillContactForm(new ContactData("testname", "testlastname", "111",
                "test@test.com", null), false);
        app.getContactHelper().submitContactModification();
        app.getContactHelper().returnToHomePage();
        int after = app.getContactHelper().getContactCount();
        Assert.assertEquals(after, before);
    }
}