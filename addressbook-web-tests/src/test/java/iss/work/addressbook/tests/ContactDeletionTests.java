package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactDeletionTests extends TestBase {
    @Test
    public void testContactDeletion() throws Exception {
        app.getNavigationHelper().gotoHomePage();
        if (! app.getContactHelper().isThereAContact()) {
            app.getContactHelper().creatContact(new ContactData("testname", "testlastname", "111",
                "test@test.com", "test1"));
        }
        app.getContactHelper().modifyFirstContact();
        app.getContactHelper().deleteSelectedContact();
    }
}
