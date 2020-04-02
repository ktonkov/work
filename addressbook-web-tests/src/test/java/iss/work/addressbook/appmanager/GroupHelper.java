package iss.work.addressbook.appmanager;

import iss.work.addressbook.model.GroupData;
import iss.work.addressbook.model.Groups;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GroupHelper extends HelperBase {

    public GroupHelper(WebDriver driver) {
        super(driver);
    }

    private Groups groupsCache = null;

    public void submitGroupCreation() {
        click(By.name("submit"));
    }

    public void fillGroupForm(GroupData groupData) {
        type(By.name("group_name"), groupData.getName());
        type(By.name("group_header"), groupData.getHeader());
        type(By.name("group_footer"), groupData.getFooter());
    }

    public void initGroupCreation() {
        click(By.name("new"));
    }

    public void returnToGroupPage() {
        click(By.linkText("group page"));
    }

    public void deleteSelectedGroups() {
        click(By.name("delete"));
    }

    public void selectGroup(int id) {
        click(By.cssSelector("input[value='" + id + "']"));
    }

    public void initGroupModification() {
        click(By.name("edit"));
    }

    public void submitGroupModification() {
        click(By.name("update"));
    }

    public GroupHelper create(GroupData group) {
        initGroupCreation();
        fillGroupForm(group);
        submitGroupCreation();
        groupsCache = null;
        returnToGroupPage();
        return this;
    }

    public GroupHelper modify(int id, GroupData group) {
        group.withId(id);
        selectGroup(id);
        initGroupModification();
        fillGroupForm(group);
        submitGroupModification();
        groupsCache = null;
        returnToGroupPage();
        return this;
    }

    public GroupHelper delete(GroupData group) {
        selectGroup(group.getId());
        deleteSelectedGroups();
        groupsCache = null;
        returnToGroupPage();
        return this;
    }

    public Groups getAll() {
        if (groupsCache != null) {
            return new Groups(groupsCache);
        }

        groupsCache = new Groups();
        for (WebElement element : driver.findElements(By.cssSelector("span.group"))) {
            GroupData group = new GroupData()
                    .withId(Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value")))
                    .withName(element.getText());
            groupsCache.add(group);
        }
        return new Groups(groupsCache);
    }

    public int size() {
        return driver.findElements(By.name("selected[]")).size();
    }
}
