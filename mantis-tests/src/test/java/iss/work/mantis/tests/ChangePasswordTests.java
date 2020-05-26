package iss.work.mantis.tests;

import iss.work.mantis.model.MailMessage;
import iss.work.mantis.model.UserData;
import iss.work.mantis.model.Users;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;

import java.io.IOException;
import java.util.List;

public class ChangePasswordTests extends TestBase {
    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void testChangePassword() throws IOException {
        String password = "password";

        Users users = app.login(app.getProperty("web.adminLogin"), app.getProperty("web.adminPassword"))
                .goTo().managePage().goTo().manageUsersPage().getAll();
        UserData user = users
                .getNot(app.getProperty("web.adminLogin"));

        app.changeUserPassword(user);

        List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
        String confirmationLink = findConfirmationLink(mailMessages, user.getEmail());
        app.registration().finish(confirmationLink, password);
        Assert.assertTrue(app.getSession().login(user.getUsername(), password));

    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.getTo().equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.getText());
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
