package test;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(
		//tags = "~@login", // use to except tests with database connection
        features= "src/test/java/test", 
        glue=""
)
public class LoginTest {

}
