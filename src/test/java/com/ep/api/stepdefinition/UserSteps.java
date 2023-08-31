package com.ep.api.stepdefinition;

import java.util.List;

import org.picocontainer.annotations.Inject;

import com.ep.api.endpoints.Routes;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.cucumber.dto.DataContext;
import com.ep.cucumber.utils.RunConstants;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class UserSteps  {
	
	@Inject
	APIBaseRestActions restActions;

	@Given("Pass the multiple below query parameters and execute login user get request")
	public void pass_the_multiple_below_query_parameters_and_execute_login_user_get_request(DataTable dataTable) {
		List<Response> responsesData = restActions.executeNegativeDataTableTestDataRequests(dataTable,
				DataContext.getRequest(), Routes.GetLoinUser, Method.GET);
		DataContext.setResponsesData(responsesData);
	}

	@Then("Validate the response status is {string} for all responses")
	public void validate_the_response_status_is_for_all_responses(String string) {
		for (Response response : DataContext.getResponsesData()) {
			response.then().statusCode(200);
		}
	}

	@Given("Pass the negative loin user detail values from json file and execute the request")
	public void pass_the_negative_loin_user_detail_values_from_json_file_and_execute_the_request() {
		List<Response> responsesData = restActions.executeTheNegativeRequestUsingJsonFileData(
				RunConstants.Negative_Data_For_Login, DataContext.getRequest(), Routes.GetLoinUser, Method.GET,RunConstants.Negative_Input_dataset_For_Login);
		DataContext.setResponsesData(responsesData);
	}
}
