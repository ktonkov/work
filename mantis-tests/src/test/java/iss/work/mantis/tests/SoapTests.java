package iss.work.mantis.tests;

import biz.futureware.mantis.rpc.soap.client.MantisConnectLocator;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import biz.futureware.mantis.rpc.soap.client.ProjectData;
import iss.work.mantis.model.Issue;
import iss.work.mantis.model.Project;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Set;

public class SoapTests extends TestBase {

    @Test
    public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
        Set<Project> projects = app.soap().getProjects();
        System.out.println(projects.size());
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }

    @Test
    public void testCreateIssue() throws RemoteException, ServiceException, MalformedURLException {
        if (!isIssueOpen(1)) {
            Issue issue = new Issue().withSummary("TestIssue1").withDescription("testIssueDescriptor1")
                    .withProject(app.soap().getProjects().iterator().next());
            Issue created = app.soap().addIssue(issue);
            Assert.assertEquals(issue.getSummary(), created.getSummary());
        }
    }
}
