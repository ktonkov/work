package iss.work.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import iss.work.addressbook.model.GroupData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {
    @DataProvider
    public Iterator<Object[]> validContactsJson() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")));
            String line = reader.readLine();
            String json = "";
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            reader.close();
            Gson gson = new Gson();
            List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>(){}.getType());
            return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        } catch (IOException e) {
            return validContacts();
        }
    }

    private Iterator<Object[]> validContacts() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[]{new ContactData().withFirstName("firstname 1").withLastName("lastname 1")});
        list.add(new Object[]{new ContactData().withFirstName("firstname 2").withLastName("lastname 2")});
        list.add(new Object[]{new ContactData().withFirstName("firstname 3").withLastName("lastname 3")});
        return list.iterator();
    }

    @Test(dataProvider = "validContactsJson")
    public void testContactCreation(ContactData contact) throws Exception {
        app.goTo().homePage();
        Contacts before = app.contacts().getAll();
        Contacts after = app.contacts().create(contact).getAll();
        contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt());
        assertThat(after.size(), equalTo(before.size() + 1));
        assertThat(after, equalTo(before.withAdded(contact)));
    }

}
