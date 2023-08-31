package com.ep.api.stepdefinition;

import java.util.Optional;
import java.util.stream.Stream;

import org.picocontainer.annotations.Inject;
import org.testng.Assert;

import com.ep.api.endpoints.Routes;
import com.ep.api.pojo.EmployeeInputData;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.api.utils.Jsonconversion;
import com.ep.cucumber.dto.DataContext;
import com.ep.cucumber.utils.RunConstants;
import com.fasterxml.jackson.databind.JsonNode;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeeSteps{

	@Inject
	APIBaseRestActions restActions;
	@Inject
	Jsonconversion jsonconversion;
	
	@Given("Set the base url for the employee")
	public void set_the_base_url_for_the_employee() {
		RequestSpecification specification = restActions.setBaseURI(System.getenv("OcelotUrl"));
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get request to get employee details")
	public void execute_the_get_request_to_get_employee_details() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET,
				Routes.GetAllEmployeeDetails);
		DataContext.setResponse(responses);
	}

	@Given("Get the {string} employee {string} {string} from employee details")
	public void get_the_employee_from_employee_details(String emploeeName, String personalDetail, String phoneNumber) {
		Stream<JsonNode> streamObject = restActions.extractJsonNodesAsStream(DataContext.getResponse());
		Optional<String> optionalValue = streamObject
			    .filter(employee -> emploeeName.equals(employee.get("name").asText()))
			    .map(employee -> employee.path(personalDetail).path("phoneNo").asText())
			    .findFirst();
		String phoneNumberValue = (String)optionalValue.get();
		System.out.println(phoneNumberValue);
	}

	@Given("Pass the path parameter from employee input json file to get particular employee details")
	public void pass_the_path_parameter_to_get_particular_employee_details() {
		EmployeeInputData dataModel = jsonconversion.deserializeJsonData(RunConstants.Employee_Input_Data,
				EmployeeInputData.class);
		RequestSpecification specification = restActions.setSinglePathParameter(DataContext.getRequest(),
				dataModel.idText, dataModel.id);
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get request to get particular employee details")
	public void execute_the_get_request_to_get_particular_employee_details() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET,
				Routes.GetEmployeeDetailById);
		DataContext.setResponse(responses);
	}

	@Then("Validate the email id of the employee")
	public void validate_the_email_id_of_the_employee() {
		EmployeeInputData dataModel = jsonconversion.deserializeJsonData(RunConstants.Employee_Input_Data,
				EmployeeInputData.class);
		JsonNode employee = restActions.parseResponseToJsonNode(DataContext.getResponse());
		restActions.getFieldFromNestedJson(employee, dataModel.PersonalDetails, "phoneNo");
		String actualEmail = employee.get(dataModel.PersonalDetails).get(dataModel.EmailId).asText();
		Assert.assertTrue(restActions.compareAndAssertResponseAttributeValues(actualEmail, dataModel.ExpectedEmailId), "Condition should be true");
	}
}
