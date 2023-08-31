package com.ep.api.stepdefinition;

import java.util.Optional;

import org.picocontainer.annotations.Inject;

import com.ep.api.endpoints.Routes;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.cucumber.dto.DataContext;
import com.fasterxml.jackson.databind.JsonNode;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StudentSteps {

	@Inject
	APIBaseRestActions restActions;

	@Given("Set the base url for the student")
	public void set_the_base_url_for_the_student() {
		RequestSpecification specification = restActions.setBaseURI(System.getenv("OcelotUrl"));
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get request for student")
	public void execute_the_get_request_for_student() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET,
				Routes.GetAllStudentDetails);
		DataContext.setResponse(responses);
	}

	@Then("Validate the student universal rank is {string}")
	public void validate_the_student_universal_rank_is(String string) {
		JsonNode student = restActions.parseResponseToJsonNode(DataContext.getResponse());
		
		Optional<String> values = restActions.processAndFilterJsonNode(student,
				entry -> entry.getKey().endsWith("UniversityRank"));
		String universityRankValue = (String) values.get();
		restActions.compareAndAssertResponseAttributeValues(string, universityRankValue);

		String rank = restActions.getFieldFromNestedJson(student, "StudentInformation", "Details", "AdditionalInfo", "Academic",
				"Performance", "Rank", "UniversityRank");
		restActions.compareAndAssertResponseAttributeValues(string, rank);
	}


}
