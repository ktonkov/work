package iss.work.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iss.work.addressbook.model.GroupData;
import iss.work.addressbook.model.Groups;
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

public class GroupModificationTests extends TestBase {
    private Groups before;

    @DataProvider
    public Iterator<Object[]> validGroupsJson() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")));
            String line = reader.readLine();
            String json = "";
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            reader.close();
            Gson gson = new Gson();
            List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>(){}.getType());
            return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        } catch (IOException e) {
            return validGroups();
        }
    }

    private Iterator<Object[]> validGroups() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[]{new GroupData().withName("test 1").withHeader("header 1").withFooter("footer 1")});
        list.add(new Object[]{new GroupData().withName("test 2").withHeader("header 2").withFooter("footer 2")});
        list.add(new Object[]{new GroupData().withName("test 3").withHeader("header 3").withFooter("footer 3")});
        return list.iterator();
    }

    @BeforeMethod
    public void prepare() {
        app.goTo().groupPage();
        if (app.groups().getAll().size() == 0) {
            before = app.groups().create(new GroupData().withName("test")).getAll();
        } else {
            before = app.groups().getAll();
        }
    }

    @Test(dataProvider = "validGroupsJson")
    public void testGroupModification(GroupData group) throws Exception {
        GroupData groupToModify = before.iterator().next();
        Groups after = app.groups().modify(groupToModify.getId(), group).getAll();
        assertThat(after.size(), equalTo(before.size()));
        assertThat(after, equalTo(before.without(groupToModify).withAdded(group)));
    }
}
