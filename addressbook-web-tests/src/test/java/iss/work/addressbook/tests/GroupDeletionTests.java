package iss.work.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iss.work.addressbook.model.GroupData;
import iss.work.addressbook.model.Groups;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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

public class GroupDeletionTests extends TestBase {
    private Groups before;

    @BeforeClass
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
