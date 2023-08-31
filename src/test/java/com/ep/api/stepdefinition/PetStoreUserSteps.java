package com.ep.api.stepdefinition;

import java.util.HashMap;
import java.util.Map;

import org.picocontainer.annotations.Inject;
import org.testng.Assert;

import com.ep.api.endpoints.Routes;
import com.ep.api.pojo.UserPojo;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.api.utils.Jsonconversion;
import com.ep.cucumber.dto.DataContext;
import com.ep.cucumber.utils.RunConstants;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetStoreUserSteps {

	@Inject
	APIBaseRestActions restActions;
	@Inject
	Jsonconversion jsonconversion;
	
	@Given("Set the base url as petstore for the Request Specification")
	public void set_the_base_url_as_petstore_for_the_request_specification() {
		RequestSpecification specification = restActions.setBaseURI(System.getenv("OcelotUrl"));
		DataContext.setRequest(specification);
	}

	@Given("set the content type as Json")
	public void set_the_content_type_as_json() {
		RequestSpecification specification = restActions.setTheContentType(DataContext.getRequest());
		DataContext.setRequest(specification);
	}
	
	@Then("Check the Json Schema for input json body")
	public void check_the_json_schema_for_input_json_body() {
		boolean result=restActions.validateJsonFileAgainstSchema(RunConstants.Create_User_Data, RunConstants.Create_New_User_Input_Schema);
		Assert.assertTrue(result, "The validation result for the input JSON file's schema should be true");
	}

	@Given("Set the json body for create new user")
	public void set_the_json_body_for_create_new_user() {
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Create_User_Data);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("Execute Create user POST request")
	public void execute_create_user_post_request() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.createPostUrl, Method.POST,
				DataContext.getJsonBody());
		DataContext.setResponse(responses);
	}

	@Then("The status code should be {string}")
	public void the_status_code_should_be(String value) {
		int statusCode = Integer.parseInt(value);
		boolean assertionResult = restActions.assertStatusCode(DataContext.getResponse(), statusCode);
		Assert.assertTrue(assertionResult);
	}

	@Then("Check the Json schema for the create user post request")
	public void check_the_json_schema_for_the_create_user_post_request() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.Create_User_Schema);
		System.out.println("the json schema pass");
	}

	@Given("Pass the path parameter key as {string} and value as {string}")
	public void pass_the_path_parameter_key_as_and_value_as(String string, String string2) {
		RequestSpecification specification = restActions.setSinglePathParameter(DataContext.getRequest(), string,
				string2);
		DataContext.setRequest(specification);
	}

	@When("Execute the GET the user request")
	public void execute_the_get_the_user_request() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET, Routes.GetUserUrl);
		DataContext.setResponse(responses);
	}

	@Then("Check the Json schema for the get user response")
	public void check_the_json_schema_for_the_get_user_response() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.Get_User_Schema);
	}

	@Then("Get the user password using json path")
	public void get_the_user_mail_id_using_json_path() {
		Response response = DataContext.getResponse();
		String value = response.jsonPath().getString("password");
		DataContext.setPassword(value);
		System.out.println(value);
	}

	@Given("Pass the {string} values as {string} and pass the {string} value as  and execute Get loin user request")
	public void pass_the_values_as_and_pass_the_value_as(String username, String userValue, String password) {
		Map<String, String> queryParams = new HashMap<String, String>();
		String userNameValue = DataContext.getUserName();
		String passwodValue = DataContext.getPassword();
		queryParams.put(username, userNameValue);
		queryParams.put(password, passwodValue);
		RequestSpecification request = DataContext.getRequest();
		Response responses = restActions.executeTheRequestByPassingMultipleQueryParameter(request, Routes.GetLoinUser, Method.GET, queryParams);
		DataContext.setResponse(responses);
	}

	@Then("Check the Json schema for the login user response")
	public void check_the_json_schema_for_the_login_user_response() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.Create_User_Schema);
	}

	@Given("Pass the below query parameters and execute login user get request")
	public void pass_the_below_query_parameters_and_execute_login_user_get_request(DataTable dataTable) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams = dataTable.asMap(String.class, String.class);
		Response responses = restActions.executeTheRequestByPassingMultipleQueryParameter(DataContext.getRequest(), Routes.GetLoinUser, Method.GET, queryParams);
		DataContext.setResponse(responses);
	}

	@Then("print the log response")
	public void print_the_log_response() {
		Response responses = DataContext.getResponse();
		responses.print();
	}

	@Then("Get the response body {string} using json path")
	public void get_the_response_body_using_json_path(String message) {
		Response response = DataContext.getResponse();
		String value = response.jsonPath().getString(message);
		System.out.println(value);
	}

	@Given("Set the Json Body for list of user")
	public void set_the_json_body_and_execute_the_list_of_user_post_request() {
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Create_List_OfUser_Data);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("Execute the post request or list of users")
	public void execute_the_post_request_or_list_of_users() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.CreateWithList, Method.POST,
				DataContext.getJsonBody());
		DataContext.setResponse(responses);
	}

	@Then("Check the Json schema for the list of user response")
	public void check_the_json_schema_for_the_list_of_user_response() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.Create_User_Schema);
	}

	@Given("Set the Json Body for update the user")
	public void set_the_json_body_for_update_the_user() {
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Create_User_Data);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("Execute the put request for update user")
	public void execute_the_put_request_for_update_user_by_using_below_path_parameter() {
		 restActions.executeTheRequest(DataContext.getRequest(), Routes.UpdateTheUser, Method.PUT,
				DataContext.getJsonBody());
	}

	@Given("Execute the delete request for respective user")
	public void execute_the_delete_request_for_respective_user() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.UpdateTheUser,
				Method.DELETE, DataContext.getJsonBody());
		responses.then().log().all();
	}

	@Given("Pass the loin user detail parameter from json file and execute the request")
	public void pass_the_loin_user_detail_parameter_from_json_file() {
		Map<String, String> queryParams = restActions
				.readAndSetTheQueryParamsFromJson(RunConstants.Login_User_Input_Data);
		Response responses = restActions.executeTheRequestByPassingMultipleQueryParameter(DataContext.getRequest(), Routes.GetLoinUser, Method.GET, queryParams);
		DataContext.setResponse(responses);
	}

	@Then("Get and set the username from datamodels")
	public void get_and_set_the_username_from_datamodels() {
		// Type type = new TypeToken<List<UserPojo>>() {}.getType();
		UserPojo dataModel = jsonconversion.deserializeJsonData(RunConstants.Create_User_Data, UserPojo.class);
		String value = dataModel.getUsername();
		DataContext.setUserName(value);
	}
}
