package com.ep.cucumber.dto;

import java.util.List;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DataContext {
	
	public static RequestSpecification request;
	
	public static Response response;
	
	public static String jsonBody;
	
	public static String password;
	
	public static String userName;
	
	public static List<Response> responsesData;


	public static List<Response> getResponsesData() {
		return responsesData;
	}

	public static void setResponsesData(List<Response> responsesData) {
		DataContext.responsesData = responsesData;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		DataContext.userName = userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		DataContext.password = password;
	}

	public static String getJsonBody() {
		return jsonBody;
	}

	public static void setJsonBody(String jsonBody) {
		DataContext.jsonBody = jsonBody;
	}

	public static Response getResponse() {
		return response;
	}

	public static void setResponse(Response response) {
		DataContext.response = response;
	}

	public static RequestSpecification getRequest() {
		return request;
	}

	public static void setRequest(RequestSpecification request) {
		DataContext.request = request;
	}	
}
