package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;
import listener.TestListener;

@CucumberOptions(
        features = "src/test/resources/tests/pet", // path to feature files
        glue = "cucumber_tests/pet",               // package for step definitions
        plugin = {"pretty", "html:target/cucumber-report.html",
                "listener.CustomCucumberListener"},
        monochrome = true
)
@Listeners(TestListener.class)
public class RunCucumberTestPet extends AbstractTestNGCucumberTests {
}
