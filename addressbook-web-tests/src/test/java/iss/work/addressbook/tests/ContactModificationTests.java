package iss.work.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {
    private Contacts before;

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

    @BeforeMethod
    public void prepare() {
        app.goTo().homePage();
        if (app.contacts().getAll().size() == 0) {
            before = app.contacts().create(new ContactData().withFirstName("test")).getAll();
        } else {
            before = app.contacts().getAll();
        }
    }

    @Test(dataProvider = "validContactsJson")
    public void testContactModification(ContactData contact) throws Exception {
        ContactData contactToModify = before.iterator().next();
        Contacts after = app.contacts().modify(contactToModify, contact).getAll();
        assertThat(after.size(), equalTo(before.size()));
        assertThat(after, equalTo(before.without(contactToModify).withAdded(contact)));
    }
}