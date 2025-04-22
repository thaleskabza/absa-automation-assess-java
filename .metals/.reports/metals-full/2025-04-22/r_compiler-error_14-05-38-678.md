file://<WORKSPACE>/src/test/java/com/absa/stepdefinitions/steps.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/test/java/com/absa/stepdefinitions/steps.java
text:
```scala

package com.absa.stepdefinitions;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.absa.models.UserData;
import com.absa.pages.WebTablesPage;
import com.absa.utils.TestDataUtil;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class steps {
    private WebDriver driver;
    private WebTablesPage webTablesPage;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) throws Exception {
        this.scenario = scenario;
        String hubUrl = System.getenv("SELENIUM_HUB_URL");
        if (hubUrl == null || hubUrl.isEmpty()) {
            hubUrl = "http://host.docker.internal:4444/wd/hub";
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        driver = new RemoteWebDriver(new URL(hubUrl), capabilities);

        webTablesPage = new WebTablesPage(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName() + "_failure");
                scenario.attach(driver.getPageSource(), "text/html", "Page Source on Failure");
                scenario.log("Current URL: " + driver.getCurrentUrl());
            } catch (Exception e) {
                scenario.log("Failed to capture debug info: " + e.getMessage());
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }


        // Helper method to take screenshots for successful steps if needed
        private void takeScreenshot(String stepName) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                scenario.attach(screenshot, "image/png", stepName + "_" + timestamp);
            } catch (Exception e) {
                scenario.log("Failed to capture screenshot: " + e.getMessage());
            }
        }

    @Given("User navigate to {string}")
    public void i_navigate_to(String url) throws InterruptedException {
        driver.get(url);
        webTablesPage = new WebTablesPage(driver);
        takeScreenshot("After_Navigation");
        Thread.sleep(1000);
    }

    @Then("User should see the user list table with headers:")
    public void i_should_see_the_user_list_table_with_headers(DataTable dataTable) throws InterruptedException {
        Assert.assertTrue(webTablesPage.isUserListTableDisplayed(), "User list table is not displayed");
        List<String> expected = dataTable.asList();
        List<String> actual = webTablesPage.getHeaderList();
        Assert.assertEquals(actual, expected, "Table headers do not match expected");
    }

    @When("User click {string}")
    public void i_click(String buttonText) throws InterruptedException {
        if ("Add User".equalsIgnoreCase(buttonText)) {
            webTablesPage.clickAddUser();
            takeScreenshot("After_Add1");
            Thread.sleep(1000);
        } else {
            driver.findElement(By.xpath(String.format("//button[contains(text(),'%s')]", buttonText))).click();
            takeScreenshot("After_Add2");
            Thread.sleep(1000);

        }

    }

    @And("User add a user with data:")
    public void i_add_a_user_with_data(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            UserData user = new UserData();
            user.setFirstName(row.get("firstName"));
            user.setLastName(row.get("lastName"));
            String uniqueUsername = row.get("userName") + "_" + System.currentTimeMillis();
            user.setUsername(uniqueUsername);
            user.setPassword(row.get("password"));
            user.setCompany(row.get("customer"));
            user.setRole(row.get("role"));
            user.setEmail(row.get("email"));
            user.setMobilePhone(row.get("cellPhone"));

            webTablesPage.clickAddUser();
            webTablesPage.addUser(user);// uncommented this function call

            // Save for later verification
            TestDataUtil.setLatestUser(user);
            row.put("userName", uniqueUsername);
            webTablesPage.clickSaveButton();
        }
        scenario.log("Added users via DataTable: " + rows);
        takeScreenshot("After_Form_fill");
        Thread.sleep(1000);
    }

    @Then("User should see the user {string} in the user list with details:")
    public void i_should_see_the_user_in_the_user_list_with_details(String username, DataTable dataTable) throws InterruptedException {
        Assert.assertTrue(webTablesPage.isUserPresentInList(username), "User " + username + " not found");
        WebElement rowElem = driver.findElements(By.cssSelector("table.smart-table.table-striped tbody tr"))
                .stream()
                .filter(r -> r.getText().contains(username))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Row for user " + username + " not found"));

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expected = rows.get(0);
        List<WebElement> cells = rowElem.findElements(By.tagName("td"));

        Assert.assertEquals(cells.get(0).getText(), expected.get("First Name"));
        Assert.assertEquals(cells.get(1).getText(), expected.get("Last Name"));
        Assert.assertEquals(cells.get(2).getText(), username);
        Assert.assertEquals(cells.get(3).getText(), expected.get("Customer"));
        Assert.assertEquals(cells.get(4).getText(), expected.get("Role"));
        Assert.assertEquals(cells.get(5).getText(), expected.get("E-mail"));
        Assert.assertEquals(cells.get(6).getText(), expected.get("Cell Phone"));
        takeScreenshot("After_see_the_user_in_the_user_list");
        Thread.sleep(1000);
    }

    @Given("User load user data from CSV file {string} row {int}")
    public void i_load_user_data_from_csv_file_row(String fileName, int rowIndex) throws InterruptedException {
        UserData user = TestDataUtil.getUserDataFromCsv(fileName, rowIndex);
        TestDataUtil.setLatestUser(user);
        scenario.log("Loaded user from CSV: " + user.getUsername());
    }

    @When("User add the latest user")
    public void i_add_the_latest_user() throws InterruptedException {
        UserData user = TestDataUtil.getLatestUser();
        webTablesPage.clickAddUser();
        webTablesPage.addUser(user);
        scenario.log("Added latest user: " + user.getUsername());
        takeScreenshot("After_Added_New_user");
        Thread.sleep(1000);
    }

    @Then("User should see the latest user in the user list")
    public void i_should_see_the_latest_user_in_the_user_list() throws InterruptedException {
        UserData user = TestDataUtil.getLatestUser();
        Assert.assertTrue(webTablesPage.isUserPresentInList(user.getUsername()), "Latest user not found: " + user.getUsername());
        takeScreenshot("After_witness_latest_user");
        Thread.sleep(1000);
    }

    @And("I pause for one minute")
    public void i_pause_for_one_minute() throws InterruptedException {
        Thread.sleep(60000);
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
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator