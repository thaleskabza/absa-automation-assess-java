file://<WORKSPACE>/src/test/java/com/absa/pages/WebTablesPage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 288
uri: file://<WORKSPACE>/src/test/java/com/absa/pages/WebTablesPage.java
text:
```scala
package com.absa.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
i@@mport org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import com.absa.models.UserData;

public class WebTablesPage extends BasePage {

    private static final String PAGE_URL = "http://www.way2automation.com/angularjs-protractor/webtables/";

    // Form field locators
    private final By modal = By.cssSelector("div.modal");
    private final By firstNameField = By.name("FirstName");
    private final By lastNameField = By.name("LastName");
    private final By userNameField = By.name("UserName");
    private final By passwordField = By.name("Password");
    private final By companyAAARadio = By.xpath("//label[contains(text(),'Company AAA')]/input");
    private final By companyBBBRadio = By.xpath("//label[contains(text(),'Company BBB')]/input");
    private final By roleDropdown = By.name("RoleId"); // Changed to simple locator
    
    private final By emailField = By.name("Email");
    private final By cellPhoneField = By.name("Mobilephone");

    private final By saveButton = By.xpath("//button[contains(@class, 'btn-success') and text()='Save']");
    private final By closeButton = By.xpath("//button[contains(@class, 'btn-danger') and text()='Close']");

    // Table and button
    @FindBy(how = How.CSS, using = "table.smart-table.table-striped[table-title='Smart Table example']")
    private WebElement userListTable;

    @FindBy(how = How.XPATH, using = "//button[contains(@class, 'btn') and contains(@class, 'btn-link')]")
    private WebElement addUserButton;

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    // Navigate to page
    public WebTablesPage navigateTo() {
        driver.get(PAGE_URL);
        return this;
    }

    // Verify if user list table is visible
    public boolean isUserListTableDisplayed() {
        return userListTable.isDisplayed();
    }

    // Extract headers from the user list table
    public List<String> getHeaderList() {
        List<WebElement> headerSpans = userListTable.findElements(
            By.cssSelector("tr.smart-table-header-row th span.header-content")
        );
        return headerSpans.stream()
                          .map(e -> e.getText().trim())
                          .filter(text -> !text.isEmpty())
                          .collect(Collectors.toList());
    }

    // Click "Add User"
    public void clickAddUser() {
        addUserButton.click();
        sleep(1000); // Wait for modal to open
    }

    // Add user using provided data
    public void addUser(UserData user) {
        sleep(1000); // Give time for form to load

        setFieldValue(firstNameField, user.getFirstName());
        setFieldValue(lastNameField, user.getLastName());
        setFieldValue(userNameField, user.getUsername());
        setFieldValue(cellPhoneField, user.getMobilePhone());

        if (user.getPassword() != null) {
            setFieldValue(passwordField, user.getPassword());
        }

        if (user.getEmail() != null) {
            setFieldValue(emailField, user.getEmail());
        }

        selectCustomer(user.getCompany());
        selectRole(user.getRole());

        driver.findElement(saveButton).click();
        sleep(1000); // Give time for modal to close
    }

    private void setFieldValue(By locator, String value) {
        try {
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(value);
        } catch (NoSuchElementException e) {
            System.err.println("Element not found: " + locator);
        }
    }

    private void selectCustomer(String company) {
        By radioButton = By.xpath(String.format(
            "//label[contains(@class,'radio') and contains(text(), '%s')]/input", 
            company
        ));
        try {
            driver.findElement(radioButton).click();
        } catch (NoSuchElementException e) {
            System.err.println("Radio button not found for company: " + company);
        }
    }

    private void selectRole(String role) {
        try {
            Select roleSelect = new Select(driver.findElement(roleDropdown));
            roleSelect.selectByVisibleText(role);
        } catch (NoSuchElementException e) {
            System.err.println("Role option not found: " + role);
        }
    }

    public void clickSaveButton() {
        driver.findElement(saveButton).click();
        sleep(1000); // Wait for modal to close
    }

    public boolean isUserPresentInList(String value) {
        try {
            List<WebElement> rows = userListTable.findElements(By.cssSelector("tbody tr"));
            for (WebElement row : rows) {
                if (row.getText().contains(value)) {
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("User list table or rows not found.");
        }
        return false;
    }

    // Simple sleep utility
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator