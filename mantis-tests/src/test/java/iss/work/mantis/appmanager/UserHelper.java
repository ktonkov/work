package iss.work.mantis.appmanager;

import iss.work.mantis.model.UserData;
import iss.work.mantis.model.Users;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHelper extends HelperBase {
    private Users userCache = null;

    public UserHelper(ApplicationManager app) {
        super(app);
    }

    public Users getAll() {
        if (userCache != null) {
            return new Users(userCache);
        }
        userCache = new Users();
        Pattern pattern = Pattern.compile("(\\d+)");
        for (WebElement row : driver.findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            UserData users = new UserData()
                    .withId(Integer.parseInt(pattern.matcher(cells.get(0).getAttribute("href")).group(0)))
                    .withUsername(cells.get(0).getText())
                    .withEmail(cells.get(2).getText());
            userCache.add(users);
        }
        return new Users(userCache);
    }

    public void initUserModificationById(int id) {
        click(By.xpath("//a[@href='manage_user_edit_page.php?user_id=" + id + "']"));
    }

    public void changeUserPassword(UserData user) {
        initUserModificationById(user.getId());
        click(By.cssSelector("input[value='Reset Password']"));
    }

}
