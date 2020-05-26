package iss.work.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import iss.work.mantis.model.Issue;
import iss.work.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {
    ApplicationManager app;

    public SoapHelper(ApplicationManager app) {
        this.app = app;
    }

    public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");
        return Arrays.asList(projects)
                .stream().map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName()))
                .collect(Collectors.toSet());
    }

    public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
        IssueData issueData = new IssueData();
        issueData.setSummary(issue.getSummary());
        issueData.setDescription(issue.getDescription());
        issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
        MantisConnectPortType mc = getMantisConnect();
        String[] categories = mc.mc_project_get_categories("administrator", "root",
                BigInteger.valueOf(issue.getProject().getId()));
        issueData.setCategory(categories[0]);
        BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
        IssueData issueDataCreated = mc.mc_issue_get("administrator", "root", issueId);
        return new Issue().withId(issueDataCreated.getId().intValue())
                .withSummary(issueDataCreated.getSummary())
                .withDescription(issueDataCreated.getDescription())
                .withProject(new Project().withId(issueDataCreated.getProject().getId().intValue())
                        .withName(issueDataCreated.getProject().getName()));
    }

    private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        return new MantisConnectLocator()
                .getMantisConnectPort(new URL(app.getProperty("web.soapUrl")));
    }

    public boolean isIssueOpen(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        IssueData issueData = mc.mc_issue_get("administrator", "root", BigInteger.valueOf(issueId));
        //String resolution = issueData.getResolution().getName();
        return issueData.getResolution().getName().equals("open");
    }
}
