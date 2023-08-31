package com.ep.api.stepdefinition;

import org.picocontainer.annotations.Inject;
import org.testng.Assert;

import com.ep.api.endpoints.Routes;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.api.utils.Jsonconversion;
import com.ep.cucumber.dto.DataContext;
import com.ep.cucumber.utils.RunConstants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GorestUserSteps  {
	
	@Inject
	APIBaseRestActions restActions;
	@Inject
	Jsonconversion jsonconversion;

	@Given("Set the Authorization token and base url for Gorest")
	public void set_the_authorization_token_and_base_url_for_gorest() {
		RequestSpecification specification = restActions.setAuthorizationTokenAndBaseURI(System.getenv("OcelotUrl"), (System.getenv("OctToken")));
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get user detail request")
	public void execute_the_get_user_detail_request() {
		Response response = restActions.executeTheRequest(DataContext.getRequest(), Method.GET,
				Routes.GetGorestUserDetails);
		DataContext.setResponse(response);
	}

	@Then("The header content type should be equal to {string}")
	public void the_header_content_type_should_be_equal_to(String expected) {
		restActions.validateTheHeaderContentType(DataContext.getResponse(), expected);
	}

	@Then("Check the Json schema for the Gorest Get user request")
	public void check_the_json_schema_for_the_gorest_get_user_request() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.GetAll_User_Schema);
	}

	@Given("Set the Json Body for create new user values in gorest using json file")
	public void set_the_json_body_for_create_new_user_values_in_gorest_using_json_file() {
		boolean result=restActions.validateJsonFileAgainstSchema(RunConstants.Create_New_User_Details_Gorest, RunConstants.CreateNewUserGorestInputSchema);
		Assert.assertTrue(result, "The validation result for the input JSON file's schema should be true");
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Create_New_User_Details_Gorest);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("Execute the post request to create new user in gorest data")
	public void execute_the_post_request_to_create_new_user_in_gorest_data() {
		Response response = restActions.executeTheRequest(DataContext.getRequest(), Routes.CreateNewUserDetails, Method.POST,
				DataContext.getJsonBody());
		DataContext.setResponse(response);
	}

	@Given("Set the content type as json char uf8")
	public void set_the_content_type_as_json_char_uf8() {
		RequestSpecification specification = restActions.setTheContentType(DataContext.getRequest());
		DataContext.setRequest(specification);		
	}

	@Given("set the json body to update the details of user")
	public void set_the_json_body_to_update_the_details_of_user() {
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Create_New_User_Details_Gorest);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("set the path parameter {string} and value as {string}")
	public void set_the_path_parameter_and_value_as(String string, String string2) {
		RequestSpecification specification = restActions.setSinglePathParameter(DataContext.getRequest(), string,
				string2);
		DataContext.setRequest(specification);
	}
	
	@Given("Execute the put request for update gorest user")
	public void execute_the_put_request_for_update_gorest_user() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.UpdateTheGorestUserDetails, Method.PUT,
				DataContext.getJsonBody());
		DataContext.setResponse(responses);
		responses.then().log().all();
	}
	
	@Given("Execute the Delete request for update gorest user")
	public void execute_the_delete_request_for_update_gorest_user() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.UpdateTheGorestUserDetails, Method.DELETE,
				DataContext.getJsonBody());
		DataContext.setResponse(responses);
		responses.prettyPrint();
	}
}
