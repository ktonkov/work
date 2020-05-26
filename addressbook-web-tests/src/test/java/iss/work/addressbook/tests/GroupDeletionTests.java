package iss.work.addressbook.tests;

import iss.work.addressbook.model.GroupData;
import iss.work.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupDeletionTests extends TestBase {
    private Groups before;

    @BeforeMethod
    public void prepare() {
        app.goTo().groupPage();
        if (app.groups().getAll().size() == 0) {
            before = app.groups().create(new GroupData().withName("test")).getAll();
        } else {
            before = app.groups().getAll();
        }
    }

    @Test
    public void testGroupDeletion() throws Exception {
        GroupData groupToDelete = before.iterator().next();
        Groups after = app.groups().delete(groupToDelete).getAll();
        assertThat(after.size(), equalTo(before.size() - 1));
        assertThat(after, equalTo(before.without(groupToDelete)));
    }

}
