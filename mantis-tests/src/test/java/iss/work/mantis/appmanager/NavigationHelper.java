package iss.work.mantis.appmanager;

import iss.work.mantis.model.Users;
import org.openqa.selenium.By;

public class NavigationHelper extends HelperBase {
    public NavigationHelper(ApplicationManager app) {
        super(app);
    }

    public ApplicationManager managePage() {
        click(By.xpath("//*[@id=\"sidebar\"]/ul/li[6]"));
        return app;
    }

    public ApplicationManager manageUsersPage() {
        click(By.xpath("//*[@id=\"main-container\"]/div[2]/div[2]/div/ul/li[2]"));
        return app;
    }
}
