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
        Pattern pattern = Pattern.compile("(\\d+$)");
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 0) {
                String href = cells.get(0).findElement(By.tagName("a")).getAttribute("href");
                Matcher matcher = pattern.matcher(href);
                boolean b = matcher.find();
                int i = matcher.groupCount();
                String id = matcher.group(0);
                UserData users = new UserData()
                        .withId(Integer.parseInt(id))
                        .withUsername(cells.get(0).getText())
                        .withEmail(cells.get(2).getText());
                userCache.add(users);
            }
        }
        return new Users(userCache);
    }

    public void initUserModificationById(int id) {
        click(By.xpath("//a[@href='manage_user_edit_page.php?user_id=" + id + "']"));
    }

    public void changeUserPassword(UserData user) {
        initUserModificationById(user.getId());
        //WebElement btnGroup = driver.findElement(By.className("btn-group"));
        //btnGroup.findElement(By.id("manage-user-reset_form")).click();
        click(By.id("manage-user-reset-form"));
    }

}
