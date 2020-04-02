package iss.work.addressbook.appmanager;

import iss.work.addressbook.model.ContactData;
import iss.work.addressbook.model.Contacts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver driver) {
        super(driver);
    }

    private Contacts contactsCache = null;

    public void submitContactForm() {
        click(By.xpath("(//input[@name='submit'])[2]"));
    }

    public void fillContactForm(
            ContactData contact,
            boolean construction
    ) {
        type(By.name("firstname"), contact.getFirstName());
        type(By.name("lastname"), contact.getLastName());
        type(By.name("home"), contact.getHomePhone());
        type(By.name("email"), contact.getEmail1());

        if (construction && contact.getGroup() != null) {
            new Select(driver.findElement(By.name("new_group"))).selectByVisibleText(contact.getGroup());
        } else if (!construction) {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void initContactCreationForm() {
        click(By.linkText("add new"));
    }

    public void selectContactById(int id) {
        click(By.id(String.valueOf(id)));
    }

    public void initContactModificationById(int id) {
        click(By.xpath("//a[@href='edit.php?id=" + id + "']"));
    }

    public void submitContactModification() {
        click(By.xpath("(//input[@name='update'])[2]"));
    }

    public void deleteSelectedContact() {
        click(By.cssSelector("input[value='Delete']"));
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    public ContactHelper create(ContactData contact) {
        initContactCreationForm();
        fillContactForm(contact, true);
        submitContactForm();
        contactsCache = null;
        returnToHomePage();
        return this;
    }

    public ContactHelper delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContact();
        contactsCache = null;
        acceptAlert();
        waitForHomePage();
        return this;
    }

    public ContactHelper modify(ContactData contactToModify, ContactData modifiedContact) {
        modifiedContact.withId(contactToModify.getId());
        initContactModificationById(contactToModify.getId());
        fillContactForm(modifiedContact, false);
        submitContactModification();
        contactsCache = null;
        returnToHomePage();
        return this;
    }

    public Contacts getAll() {
        if (contactsCache != null) {
            return new Contacts(contactsCache);
        }
        contactsCache = new Contacts();
        for (WebElement raw : driver.findElements(By.name("entry"))) {
            List<WebElement> cells = raw.findElements(By.tagName("td"));
            String allPhones = cells.get(5).getText();
            String allEmails = cells.get(4).getText();
            ContactData contact = new ContactData()
                    .withId(Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value")))
                    .withLastName(cells.get(1).getText())
                    .withFirstName(cells.get(2).getText())
                    .withAllPhones(allPhones)
                    .withAllEmails(allEmails);
            contactsCache.add(contact);
        }
        return new Contacts(contactsCache);
    }

    private void waitForHomePage() {
        while (!isElementPresent(By.id("maintable"))) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int size() {
        return driver.findElements(By.name("selected[]")).size();
    }

    public ContactData contactDataFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        ContactData result = new ContactData()
                .withFirstName(driver.findElement(By.name("firstname")).getAttribute("value"))
                .withLastName(driver.findElement(By.name("lastname")).getAttribute("value"))
                .withHomePhone(driver.findElement(By.name("home")).getAttribute("value"))
                .withMobilePhone(driver.findElement(By.name("mobile")).getAttribute("value"))
                .withWorkPhone(driver.findElement(By.name("work")).getAttribute("value"))
                .withEmail1(driver.findElement(By.name("email")).getAttribute("value"))
                .withEmail2(driver.findElement(By.name("email2")).getAttribute("value"))
                .withEmail3(driver.findElement(By.name("email3")).getAttribute("value"));
        driver.navigate().back();
        return result;
    }
}
