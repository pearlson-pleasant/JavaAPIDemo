package com.ep.cucumber.hooks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class ScenarioHooks {

	Logger logger = LogManager.getLogger(ScenarioHooks.class);

	@Before
	public void scenarioSetup(Scenario scenario) {
		// System.out.println("\n");
        ThreadContext.put("scenarioName", scenario.getName());
        logger.info("\nscenarioName :  "  +scenario.getName()+"\n");
	}

	@After
	public void scenarioTearDown(Scenario scenario) {
		 ThreadContext.remove("scenarioName");
		    logger.info("\n"+"..................End of Scenario: " + scenario.getName() + "......................\n");
	}
	
	 private Map<Scenario, Integer> stepCounters = new HashMap<>();
	 int stepCounter;

	    @BeforeStep
	    public void beforeStep(Scenario scenario) {
	        stepCounter = stepCounters.getOrDefault(scenario, stepCounter);
	        stepCounter++;
	        stepCounters.put(scenario, stepCounter);
	        logger.info("Step : " + stepCounter);
	    }

	    @AfterStep
	    public void afterStep(Scenario scenario) {
	    	 logger.info("*******************************************\n");
	    }
    
	@BeforeAll
	public static void deleteScreenshotFolder() throws Exception {
		File screenshotFolder = new File("screenshots");
		if (screenshotFolder.exists()) {
			FileUtils.deleteDirectory(screenshotFolder);
		}
	}
}