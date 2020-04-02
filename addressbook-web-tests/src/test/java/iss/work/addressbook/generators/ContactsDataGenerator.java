package iss.work.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.GroupData;
import iss.work.addressbook.tests.ContactCreationTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactsDataGenerator {
    Logger logger = LoggerFactory.getLogger(ContactsDataGenerator.class);

    @Parameter(names = "-c", description = "Contacts count")
    public int count;
    @Parameter(names = "-f", description = "Target file")
    public String file;

    public static void main(String[] args) throws IOException {
        ContactsDataGenerator generator = new ContactsDataGenerator();
        JCommander jCommander = new JCommander(generator);
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        List<ContactData> contacts = generateContacts(count);
        switch (file.substring(file.lastIndexOf("."))) {
            case ".csv":
                saveAsCsv(contacts, new File(file));
                break;
            case ".xml":
                saveAsXml(contacts, new File(file));
                break;
            case ".json":
                saveAsJson(contacts, new File(file));
                break;
            default:
                logger.error("Unrecognized format " + file.substring(file.lastIndexOf(".")));
                break;
        }
    }

    private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(contacts);
        Writer writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    private void saveAsXml(List<ContactData> contacts, File file) throws IOException {
        XStream xStream = new XStream();
        xStream.processAnnotations(GroupData.class);
        String xml = xStream.toXML(contacts);
        Writer writer = new FileWriter(file);
        writer.write(xml);
        writer.close();
    }

    private void saveAsCsv(List<ContactData> contacts, File file) throws IOException {
        Writer writer = new FileWriter(file);
        for (ContactData contact : contacts) {
            writer.write(String.format("%s;%s;%s\n", contact.getFirstName(), contact.getLastName()));
        }
        writer.close();
    }

    private List<ContactData> generateContacts(int count) {
        ArrayList<ContactData> contacts = new ArrayList<ContactData>();
        for(int i = 0; i < count; i++) {
            ContactData contact = new ContactData().withFirstName(String.format("firstname %s", i))
                    .withLastName(String.format("lastname %s", i));
            contacts.add(contact);
        }
        return contacts;
    }
}
