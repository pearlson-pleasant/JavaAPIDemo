package com.ep.cucumber.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags="@smoke",features = { "src\\test\\resources\\features\\PetStoreUser.feature"}, glue = { "com.ep.api.stepdefinition","com.ep.cucumber.hooks"}, plugin = { "pretty", "html:target/cucumber/report.html",
				"json:target/cucumber/report.json" }, monochrome = true, dryRun = !true)
public class RunnerClass extends AbstractTestNGCucumberTests {

}