package iss.work.addressbook.tests;

import iss.work.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.getContactHelper().initContactCreationForm();
    app.getContactHelper().fillContactForm(new ContactData("testname", "testlastname", "111", "test@test.com"));
    app.getContactHelper().submitContactForm();
    app.getContactHelper().returnToHomePage();
  }

}
