package com.qa.dummyjson.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",      // Feature files ka path
    glue = "com.qa.dummyjson.stepdefinitions",     // Step implementation classes ka package
    		tags = "@DummyJSON",                              // Sirf wahi tests chalenge jisme yeh tag hai
    plugin = {
        "pretty",                                  // Clean console readable output ke liye
        "html:target/cucumber-reports/dummyjson-report.html", // HTML Report link
        "json:target/cucumber-reports/dummyjson-report.json"  // CI/CD integrations ke liye JSON
    },
    monochrome = true,                             // Console formatting logs clean karne ke liye
    dryRun = false                                 // Agar steps check karne ho bina execute kiye toh true karein
)
public class TestRunner {
    // Yeh class khali rahegi, iska kaam sirf Cucumber configuration hold karna hai
}