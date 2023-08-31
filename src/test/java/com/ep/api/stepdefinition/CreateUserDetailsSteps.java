package com.ep.api.stepdefinition;

import com.ep.api.endpoints.Routes;
import com.ep.api.pojo.PojoFactory;
import com.ep.api.pojo.World;
import com.ep.cucumber.dto.DataContext;

import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CreateUserDetailsSteps {

	private World world;
	
	PojoFactory pojoFactory;
	
	public CreateUserDetailsSteps() {
		world = new World();
		pojoFactory=new PojoFactory();
	}
	
	@When("I start building new Create user")
	public void i_start_building_new_client() {
		 world.userPojo=pojoFactory.createUserPojo();
	}

	@When("I set create user id {string}")
	public void i_set_create_user_id(String string) {
		int value =Integer.parseInt(string);
		world.userPojo.setId(value);
	}

	@When("I set create user username {string}")
	public void i_set_create_user_username(String string) {
		world.userPojo.setUsername(string);
	}

	@When("I set create user firstName {string}")
	public void i_set_create_user_first_name(String string) {
		world.userPojo.setFirstName(string);
	}

	@When("I set create user lastName {string}")
	public void i_set_create_user_last_name(String string) {
		world.userPojo.setLastName(string);
	}

	@When("I set create user email {string}")
	public void i_set_create_user_email(String string) {
		world.userPojo.setEmail(string);
	}

	@When("I set create user password  {string}")
	public void i_set_create_user_password(String string) {
		world.userPojo.setPassword(string);
	}

	@When("I set create user phone {string}")
	public void i_set_create_user_phone(String string) {
		world.userPojo.setPhone(string);
	}

	@When("I set content type to Json")
	public void i_set_create_user_user_status() {
		DataContext.request.contentType(ContentType.JSON)
		.accept(ContentType.JSON);
	}

	@When("I set the current create new user details as body")
	public void i_set_the_current_create_new_user_details_as_body() {
		RequestSpecification body=DataContext.request.body(world.userPojo);
		System.out.println(body);
	}
	
	@When("Execute the post request for create user details")
	public void execute_the_post_request_for_create_user_details() {
	Response response=	DataContext.request.post(Routes.createPostUrl);
	response.prettyPrint();
	response.then().statusCode(200);
	}
}
