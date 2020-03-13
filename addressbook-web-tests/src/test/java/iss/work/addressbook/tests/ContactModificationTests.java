package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() throws Exception {
        app.getNavigationHelper().gotoHomePage();
        app.getContactHelper().modifyFirstContact();
        app.getContactHelper().fillContactForm(new ContactData("testname", "testlastname", "111", "test@test.com"));
        app.getContactHelper().submitContactModification();
        app.getContactHelper().returnToHomePage();
    }
}