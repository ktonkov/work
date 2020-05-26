package iss.work.mantis.tests;

import iss.work.mantis.appmanager.HttpSession;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class LoginTests extends TestBase {
    @Test
    public void testLogin() throws IOException {
        HttpSession session = app.getSession();
        assertTrue(session.login(app.getProperty("web.adminLogin"), app.getProperty("web.adminLogin")));
    }
}
