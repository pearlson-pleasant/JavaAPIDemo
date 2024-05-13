package com.ep.api.utils;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.testng.Assert;

import com.ep.cucumber.utils.RunConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class APiBasePage implements RestActions {

	public RequestSpecification setBaseURI(String baseUrl) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification specification = given();
		return specification;
	}

	public RequestSpecification setAuthorizationTokenAndBaseURI(String baseUrl) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification specification = given().auth()
				.oauth2("88238e66df6940c43b2eae0f8cd0a14d6cc8dc69b1e2947df093fa217e742db5");
		return specification;
	}

	public RequestSpecification SetTheContentTypeWithCharSet(RequestSpecification request) {
		RequestSpecification specification = request.contentType("application/json");
		return specification;
	}

	public RequestSpecification setTheContentType(RequestSpecification request) {
		RequestSpecification specification = request.contentType(ContentType.JSON).accept(ContentType.JSON);
		return specification;
	}

	public Response executeTheRequest(RequestSpecification requestSpecification, Method httpMethod, String endPoint) {
		Response response = requestSpecification.request(httpMethod, endPoint);
		return response;
	}

	public boolean assertStatusCode(Response response, int expectedStatusCode) {
		return response.then().statusCode(expectedStatusCode).extract().statusCode() == expectedStatusCode;
	}

	public RequestSpecification setSinglePathParameter(RequestSpecification requestSpecification, String key,
			String value) {
		RequestSpecification specification = requestSpecification.pathParam(key, value);
		return specification;
	}

	public RequestSpecification setSingleQueryParameter(RequestSpecification requestSpecification, String key,
			String value) {
		RequestSpecification specification = requestSpecification.queryParam(key, value);
		return specification;
	}

	public void validateTheJsonSchema(Response response, String jsonSchemaFilePath) {
		String path = RunConstants.PROJECT_ROOT_LOCATION + jsonSchemaFilePath;
		File schemaFile = new File(path);
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
	}

	public void validateTheHeaderContentType(Response response, String expectedHeaderContentType) {
		String actualHeaderContentType = response.header("Content-Type");
		Assert.assertEquals(actualHeaderContentType, expectedHeaderContentType);
	}

	public ValidatableResponse validateTheResponse(Response response) {
		ValidatableResponse validatableResponse = response.then();
		return validatableResponse;
	}

	public RequestSpecification addUploadImageInSpecification(RequestSpecification requestSpecification,
			String filePath) {
		File uploadFile = new File(filePath);
		RequestSpecification specification = requestSpecification.multiPart(uploadFile);
		return specification;
	}

	public Map<String, String> readAndSetTheQueryParamsFromJson(String jsonFilePath) {
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		String filePath = currentDirectory + jsonFilePath;
		Map<String, String> queryParams = new HashMap<String, String>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			Map<String, String> jsonParams = new Gson().fromJson(bufferedReader, Map.class);
			bufferedReader.close();

			queryParams.putAll(jsonParams);

			// Now the queryParams map contains the key-value pairs from the JSON file
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return queryParams;
	}

	public List<Response> executeTheRequestWithDatableQueryParameter(DataTable dataTable,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod) {
		List<Response> responsesData = new ArrayList<Response>();
		List<Map<String, String>> queryParams = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> testData : queryParams) {
			Response response = action(requestSpecification, endPoint, httpMethod, testData);
			responsesData.add(response);
			response.then().log().all();
		}
		return responsesData;
	}

	public List<Response> executeTheNegativeRequestUsingJsonFileData(String jsonFilePath,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod) {
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		String filePath = currentDirectory + jsonFilePath;
		List<Response> responseList = new ArrayList<Response>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			Map<String, Object> jsonData = new Gson().fromJson(bufferedReader, Map.class);
			bufferedReader.close();

			List<Map<String, String>> negativeDataList = (List<Map<String, String>>) jsonData.get("negativeData");

			for (Map<String, String> negativeData : negativeDataList) {
				Map<String, String> queryParams = new HashMap<String, String>();
				queryParams.putAll(negativeData);

				Response response = action(requestSpecification, endPoint, httpMethod, queryParams);
				responseList.add(response);
				response.prettyPrint();

				// Now the queryParams map contains the key-value pairs from the current
				// negative data
				for (Map.Entry<String, String> entry : queryParams.entrySet()) {
					System.out.println(entry.getKey() + ": " + entry.getValue());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseList;
	}

	public Response action(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			Map<String, String> queryParams) {
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			requestSpecification.queryParams(entry.getKey(), entry.getValue());
		}
		Response response = requestSpecification.request(httpMethod, endPoint);
		return response;
	}

	public Response executeTheRequest(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			String jsonBody) {
		// Add the JSON body to the request
		requestSpecification.body(jsonBody);
		// Build the request with the resource and HTTP method
		Response response = requestSpecification.request(httpMethod, endPoint);
		return response;
	}

	public JsonNode parseResponseToJsonNode(Response response) {
		String responseBody = response.getBody().asString();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(responseBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonNode;
	}

	public Stream<JsonNode> extractJsonNodesAsStream(Response response) {
		Stream<JsonNode> stream = null;
		JsonNode employees = parseResponseToJsonNode(response);
		stream = StreamSupport.stream(employees.spliterator(), false);
		return stream;
	}
	
	public boolean compareAndAssertResponseAttributeValues(Object actualValue, Object expectedValue) {
		if (actualValue instanceof String && expectedValue instanceof String) {
			return Objects.equals((String) actualValue, (String) expectedValue);
		} else if (actualValue instanceof Integer && expectedValue instanceof Integer) {
			return Objects.equals((Integer) actualValue, (Integer) expectedValue);
		} else {
			throw new IllegalArgumentException("Unsupported data types for assertion.");
		}
	}

	public Response action(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			Map<String, String> queryParams, String jsonBody) {
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			requestSpecification.queryParams(entry.getKey(), entry.getValue());
		}
		// Add the JSON body to the request
		requestSpecification.body(jsonBody);
		// Build the request with the resource and HTTP method
		Response response = requestSpecification.request(httpMethod, endPoint);
		return response;
	}
}
