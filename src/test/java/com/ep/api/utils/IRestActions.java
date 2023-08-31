package com.ep.api.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;

import io.cucumber.datatable.DataTable;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

/**
 * This interface defines a set of common REST API actions that can be performed
 * using RestAssured library. Classes implementing this interface can provide
 * concrete implementations for these actions.
 * 
 * @author Pearlson
 */

public interface IRestActions {

	RequestSpecification setBaseURI(String baseUrl);

	RequestSpecification setAuthorizationTokenAndBaseURI(String baseUrl, String token);

	RequestSpecification setTheContentType(RequestSpecification requestrequestSpecification);

	Response executeTheRequest(RequestSpecification requestrequestSpecification, Method httpMethod, String endPoint);

	boolean assertStatusCode(Response response, int expectedStatusCode);

	RequestSpecification setSinglePathParameter(RequestSpecification requestSpecification, String key, String value);

	RequestSpecification setSingleQueryParameter(RequestSpecification requestSpecification, String key, String value);

	void validateTheJsonSchema(Response response, String jsonSchemaFilePath);

	void validateTheHeaderContentType(Response response, String expectedValue);

	ValidatableResponse validateTheResponse(Response response);

	RequestSpecification addUploadImageInSpecification(RequestSpecification requestSpecification, String filePath);

	Map<String, String> readAndSetTheQueryParamsFromJson(String jsonFilePath);

	List<Response> executeNegativeDataTableTestDataRequests(DataTable dataTable,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod);

	List<Response> executeTheNegativeRequestUsingJsonFileData(String jsonFilePath,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod, String dataSet);

	Response executeTheRequestByPassingMultipleQueryParameter(RequestSpecification requestSpecification,
			String endPoint, Method httpMethod, Map<String, String> queryParams);

	Response executeTheRequest(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			String jsonBody);

	JsonNode parseResponseToJsonNode(Response response);

	Stream<JsonNode> extractJsonNodesAsStream(Response response);

	boolean compareAndAssertResponseAttributeValues(Object actualValue, Object expectedValue);

	Response action(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			Map<String, String> queryParams, String jsonBody);

	boolean validateJsonFileAgainstSchema(String inputJsonFilePath, String jsonSchemaFilePath);

	Stream<Map.Entry<String, JsonNode>> flatten(String prefix, JsonNode node);

	Optional<String> processAndFilterJsonNode(JsonNode node, Predicate<Map.Entry<String, JsonNode>> filterPredicate);
	
	String getFieldFromNestedJson(JsonNode parentNode, String... fieldNames);
}
