package com.ep.api.stepdefinition;

import org.picocontainer.annotations.Inject;

import com.ep.api.endpoints.Routes;
import com.ep.api.pojo.PetInputPojo;
import com.ep.api.utils.APIBaseRestActions;
import com.ep.api.utils.Jsonconversion;
import com.ep.cucumber.dto.DataContext;
import com.ep.cucumber.utils.RunConstants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetStorePetSteps{
	
	@Inject
	APIBaseRestActions restActions;
	@Inject
	Jsonconversion jsonconversion;
	
	@Given("pass the query parameter has pending status")
	public void pass_the_query_parameter_has_pending_status() {
		PetInputPojo dataModel = jsonconversion.deserializeJsonData(RunConstants.Pet_Input_Data, PetInputPojo.class);
		RequestSpecification specification = restActions.setSingleQueryParameter(DataContext.getRequest(), dataModel.status,
				dataModel.pending);
		DataContext.setRequest(specification);
	}
	
	@Given("pass the query parameter has available status")
	public void pass_the_query_parameter_has_available_status() {
		PetInputPojo dataModel = jsonconversion.deserializeJsonData(RunConstants.Pet_Input_Data, PetInputPojo.class);
		RequestSpecification specification = restActions.setSingleQueryParameter(DataContext.getRequest(), dataModel.status,
				dataModel.available);
		DataContext.setRequest(specification);
	}

	@Given("pass the query parameter has sold status")
	public void pass_the_query_parameter_has_sold_status() {
		PetInputPojo dataModel = jsonconversion.deserializeJsonData(RunConstants.Pet_Input_Data, PetInputPojo.class);
		RequestSpecification specification = restActions.setSingleQueryParameter(DataContext.getRequest(), dataModel.status,
				dataModel.sold);
		DataContext.setRequest(specification);
	}

	@Given("Pass the {string} values as {string}")
	public void pass_the_values_as_and_execute_get_pet_status_request(String string, String string2) {
		RequestSpecification specification = restActions.setSingleQueryParameter(DataContext.getRequest(), string,
				string2);
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get status of the pet")
	public void execute_the_get_status_of_the_pet() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET, Routes.PetFindStatus);
		DataContext.setResponse(responses);
	}

	@Given("Set the Json Body to add new pet to the store")
	public void set_the_json_body_to_add_new_pet_to_the_store() {
		String jsonBody = jsonconversion.readTextFileAsString(RunConstants.Add_New_Pet);
		DataContext.setJsonBody(jsonBody);
	}

	@Given("Execute the post request to add new pet")
	public void execute_the_post_request_to_add_new_pet() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.AddNewPet, Method.POST,
				DataContext.getJsonBody());
		DataContext.setResponse(responses);
	}

	@Given("Execute the put request to update pet")
	public void execute_the_put_request_to_update_pet() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Routes.UpdateThePet, Method.PUT,
				DataContext.getJsonBody());
		responses.then().log().all();
		DataContext.setResponse(responses);
	}

	@Then("Check the Json schema for the pet in the store")
	public void check_the_json_schema_for_the_pet_in_the_store() {
		restActions.validateTheJsonSchema(DataContext.getResponse(), RunConstants.Add_Updatethe_Pet_Schema);
	}

	@Given("Pass the path parameter as {string} and values as {string}")
	public void pass_the_path_parameter_as_and_values_as(String string, String string2) {
		RequestSpecification specification = restActions.setSinglePathParameter(DataContext.getRequest(), string,
				string2);
		DataContext.setRequest(specification);
	}

	@Given("Execute the Get particular pet detail")
	public void execute_the_get_particular_pet_detail() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.GET,
				Routes.GetParticularPet);
		DataContext.setResponse(responses);
	}
	
	@Given("set the pet image path in the specification")
	public void set_the_image_path() {
		RequestSpecification specification=restActions.addUploadImageInSpecification(DataContext.getRequest(), RunConstants.Sample_Image_Path);
		DataContext.setRequest(specification);
	}
	
	@Given("Execute the post request to upload the image")
	public void execute_the_post_request_to_upload_the_image() {
		Response responses = restActions.executeTheRequest(DataContext.getRequest(), Method.POST,
				Routes.UploadImageFOrPet);
		DataContext.setResponse(responses);
	}
}
