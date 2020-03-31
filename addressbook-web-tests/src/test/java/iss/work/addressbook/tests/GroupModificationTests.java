package iss.work.addressbook.tests;

import iss.work.addressbook.model.GroupData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;

public class GroupModificationTests extends TestBase {
    @Test
    public void testGroupModification() throws Exception {
        app.getNavigationHelper().gotoGroupPage();

        if (!app.getGroupHelper().isThereAGroup()) {
            app.getGroupHelper().createGroup(new GroupData("test1", "test2", "test3"));
        }
        List<GroupData> before = app.getGroupHelper().getGroups();
        GroupData group = new GroupData(before.get(0).getId(), "test1", "test2", "test3");
        app.getGroupHelper().selectGroup(0);
        app.getGroupHelper().initGroupModification();
        app.getGroupHelper().fillGroupForm(group);
        app.getGroupHelper().submitGroupModification();
        app.getGroupHelper().returnToGroupPage();
        List<GroupData> after = app.getGroupHelper().getGroups();
        Assert.assertEquals(after.size(), before.size());

        after.remove(0);
        after.add(group);
        Assert.assertEquals(new HashSet<GroupData>(after), new HashSet<GroupData>(before));
    }
}
