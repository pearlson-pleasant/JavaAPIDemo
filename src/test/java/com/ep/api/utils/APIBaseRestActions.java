package com.ep.api.utils;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;

import com.ep.cucumber.utils.RunConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

/**
 * This class provides a set of common utility methods for performing RESTful
 * API actions using RestAssured library. It encapsulates functionalities such
 * as setting base URIs, handling authentication tokens, executing requests, and
 * validating responses. This class is designed to simplify the process of
 * interacting with REST APIs and performing common operations like sending
 * requests, handling responses, and validating data.
 * 
 * @author Pearlson
 */

public class APIBaseRestActions implements IRestActions {

	private static final Logger logger = LoggerManagement.getLogger();

	/**
	 * Sets the base URI for REST API requests. This method sets the base URI to the
	 * provided value and returns a RequestSpecification object that can be used for
	 * further request configuration.
	 * 
	 * @parambaseUrl The base URI to be set for the REST API requests.
	 * @return A RequestSpecification object for further request configuration.
	 * @throws Exception If an error occurs while setting the base URI.
	 * @author Pearlson
	 */

	public RequestSpecification setBaseURI(String baseUrl) {
		try {
			RestAssured.baseURI = baseUrl;
			RequestSpecification specification = given();
			logger.info("Base URI set to: {}", baseUrl);
			return specification;
		} catch (Exception e) {
			logger.error("Error setting base URI: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Sets the base URI for REST API requests and adds an authorization token using
	 * OAuth2. This method sets the base URI to the provided value and configures
	 * the request specification with an OAuth2 token for authentication.
	 *
	 * @param baseUrl The base URI to be set for the REST API requests.
	 * @param token   The OAuth2 token to be included in the request for
	 *                authentication.
	 * @return A RequestSpecification object configured with the authorization token
	 *         and base URI.
	 * @author Pearlson
	 */

	public RequestSpecification setAuthorizationTokenAndBaseURI(String baseUrl, String token) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification specification = given().auth().oauth2(token);
		return specification;
	}

	/**
	 * Sets the content type for a given request specification to JSON.
	 * 
	 * @param request The request specification to set the content type for.
	 * @return The updated RequestSpecification object.
	 * @throws Exception If an error occurs while setting the content type.
	 * @author Pearlson
	 */

	public RequestSpecification setTheContentType(RequestSpecification request) {
		try {
			RequestSpecification specification = request.contentType(ContentType.JSON).accept(ContentType.JSON);
			FilterableRequestSpecification filterableRequestSpec = printRequestSpecification(specification);
			logger.info("Set the content type has: {}", filterableRequestSpec.getContentType());
			return specification;
		} catch (Exception e) {
			logger.error("Error setting content type: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Prints the content of the given RequestSpecification as a
	 * FilterableRequestSpecification.
	 * 
	 * @param requestSpecification The RequestSpecification to print.
	 * @return The FilterableRequestSpecification object containing the request
	 *         details.
	 * @author Pearlson
	 */

	public FilterableRequestSpecification printRequestSpecification(RequestSpecification requestSpecification) {
		FilterableRequestSpecification filterableRequestSpec = (FilterableRequestSpecification) requestSpecification;
		return filterableRequestSpec;
	}

	/**
	 * Executes a REST API request using the provided RequestSpecification, HTTP
	 * method, and endpoint.
	 * 
	 * @param requestSpecification The RequestSpecification to use for the request.
	 * @param httpMethod           The HTTP method to use (GET, POST, PUT, DELETE,
	 *                             etc.).
	 * @param endPoint             The endpoint of the API.
	 * @return The Response object containing the API response.
	 * @author Pearlson
	 */

	public Response executeTheRequest(RequestSpecification requestSpecification, Method httpMethod, String endPoint) {
		try {
			Response response = requestSpecification.request(httpMethod, endPoint);
			logger.info("Response body:\n{}", response.body().asPrettyString()); // Log response body
			return response;
		} catch (Exception e) {
			logger.error("Error executing the request: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Validates the status code of the API response.
	 * 
	 * @param response           The Response object to validate.
	 * @param expectedStatusCode The expected HTTP status code.
	 * @return True if the status code matches the expected code, false otherwise.
	 * @author Pearlson
	 */

	public boolean assertStatusCode(Response response, int expectedStatusCode) {
		int actualStatusCode = response.then().extract().statusCode();
		boolean assertionResult = actualStatusCode == expectedStatusCode;
		if (assertionResult) {
			logger.info("Status code assertion passed. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
		} else {
			logger.error("Status code assertion failed. Expected: {}, Actual: {}", expectedStatusCode,
					actualStatusCode);
		}
		return assertionResult;
	}

	/**
	 * Sets a single path parameter in the given RequestSpecification.
	 * 
	 * @param requestSpecification The RequestSpecification to add the path
	 *                             parameter to.
	 * @param key                  The parameter key.
	 * @param value                The parameter value.
	 * @return The updated RequestSpecification with the path parameter.
	 * @author Pearlson
	 */

	public RequestSpecification setSinglePathParameter(RequestSpecification requestSpecification, String key,
			String value) {
		try {
			RequestSpecification specification = requestSpecification.pathParam(key, value);
			logger.info("set the Path parameter has. Key: {}, Value: {}", key, value);
			return specification;
		} catch (Exception e) {
			logger.error("Error setting path parameter. Key: {}, Value: {}, Error: {}", key, value, e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Sets a single query parameter in the given RequestSpecification.
	 * 
	 * @param requestSpecification The RequestSpecification to add the query
	 *                             parameter to.
	 * @param key                  The parameter key.
	 * @param value                The parameter value.
	 * @return The updated RequestSpecification with the query parameter.
	 * @author Pearlson
	 */

	public RequestSpecification setSingleQueryParameter(RequestSpecification requestSpecification, String key,
			String value) {
		try {
			RequestSpecification specification = requestSpecification.queryParam(key, value);
			logger.info(" set the Query parameter has. Key: {}, Value: {}", key, value);
			return specification;
		} catch (Exception e) {
			logger.error("Error setting query parameter: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Validates the JSON response against a JSON schema.
	 * 
	 * @param response           The Response object to validate.
	 * @param jsonSchemaFilePath The path to the JSON schema file.
	 * @author Pearlson
	 */

	public void validateTheJsonSchema(Response response, String jsonSchemaFilePath) {
		String path = RunConstants.PROJECT_ROOT_LOCATION + jsonSchemaFilePath;
		File schemaFile = new File(path);
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
		logger.info("JSON schema validation is successful for file: /n{}", jsonSchemaFilePath);
	}

	/**
	 * Validates the "Content-Type" header of the API response.
	 * 
	 * @param response                  The Response object to validate.
	 * @param expectedHeaderContentType The expected "Content-Type" header value.
	 * @author Pearlson
	 */

	public void validateTheHeaderContentType(Response response, String expectedHeaderContentType) {
		String actualHeaderContentType = response.header("Content-Type");
		Assert.assertEquals(actualHeaderContentType, expectedHeaderContentType);
	}

	/**
	 * Validates the API response and returns a ValidatableResponse.
	 * 
	 * @param response The Response object to validate.
	 * @return The ValidatableResponse for further validation.
	 * @author Pearlson
	 */

	public ValidatableResponse validateTheResponse(Response response) {
		ValidatableResponse validatableResponse = response.then();
		return validatableResponse;
	}

	/**
	 * Adds an uploaded image file to the request specification as a multi-part form
	 * data.
	 *
	 * @param requestSpecification The existing request specification.
	 * @param filePath             The path to the image file to be uploaded.
	 * @return A modified request specification with the image file attached.
	 * @throws Exception If an error occurs while adding the image to the
	 *                   specification.
	 * @author Pearlson
	 */

	public RequestSpecification addUploadImageInSpecification(RequestSpecification requestSpecification,
			String filePath) {
		try {
			File uploadFile = new File(filePath);
			RequestSpecification specification = requestSpecification.multiPart(uploadFile);
			logger.info("Upload image added to specification. File Path: {}", filePath);
			return specification;
		} catch (Exception e) {
			logger.error("Error adding upload image to specification. File Path: {}, Error: {}", filePath,
					e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Reads JSON parameters from a file and sets them as query parameters in a map.
	 *
	 * @param jsonFilePath The path to the JSON file containing query parameters.
	 * @return A map containing key-value pairs of query parameters.
	 * @author Pearlson
	 */

	public Map<String, String> readAndSetTheQueryParamsFromJson(String jsonFilePath) {
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		String filePath = currentDirectory + jsonFilePath;
		Map<String, String> queryParams = new HashMap<String, String>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			Type mapType = new TypeToken<Map<String, Object>>() {
			}.getType();
			Map<String, String> jsonParams = new Gson().fromJson(bufferedReader, mapType);
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

	/**
	 * Executes multiple requests with negative test data using a DataTable.
	 *
	 * @param dataTable            The DataTable containing negative test data.
	 * @param requestSpecification The existing request specification.
	 * @param endPoint             The API endpoint.
	 * @param httpMethod           The HTTP method for the requests.
	 * @return A list of Response objects containing the results of each request.
	 * @author Pearlson
	 */

	public List<Response> executeNegativeDataTableTestDataRequests(DataTable dataTable,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod) {
		List<Response> responsesData = new ArrayList<Response>();
		List<Map<String, String>> queryParams = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> testData : queryParams) {
			Response response = executeTheRequestByPassingMultipleQueryParameter(requestSpecification, endPoint,
					httpMethod, testData);
			responsesData.add(response);
		}
		return responsesData;
	}

	/**
	 * Executes multiple requests with negative test data using data from a JSON
	 * file.
	 *
	 * @param jsonFilePath         The path to the JSON file containing negative
	 *                             test data.
	 * @param requestSpecification The existing request specification.
	 * @param endPoint             The API endpoint.
	 * @param httpMethod           The HTTP method for the requests.
	 * @return A list of Response objects containing the results of each request.
	 * @author Pearlson
	 */

	public List<Response> executeTheNegativeRequestUsingJsonFileData(String jsonFilePath,
			RequestSpecification requestSpecification, String endPoint, Method httpMethod, String dataSet) {
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		String filePath = currentDirectory + jsonFilePath;
		List<Response> responseList = new ArrayList<>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

			// Specify the type information when De-serialize JSON
			Type mapType = new TypeToken<Map<String, Object>>() {
			}.getType();
			Map<String, Object> jsonData = new Gson().fromJson(bufferedReader, mapType);

			bufferedReader.close();

			@SuppressWarnings("unchecked")
			List<Map<String, String>> negativeDataList = (List<Map<String, String>>) jsonData.get(dataSet);

			for (Map<String, String> negativeData : negativeDataList) {
				Map<String, String> queryParams = new HashMap<>();
				queryParams.putAll(negativeData);

				Response response = executeTheRequestByPassingMultipleQueryParameter(requestSpecification, endPoint,
						httpMethod, queryParams);
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

	/**
	 * Executes a request by passing multiple query parameters to the request
	 * specification.
	 *
	 * @param requestSpecification The existing request specification.
	 * @param endPoint             The API endpoint.
	 * @param httpMethod           The HTTP method for the request.
	 * @param queryParams          A map containing the query parameters to be added
	 *                             to the request.
	 * @return The Response object containing the result of the request.
	 * @author Pearlson
	 */

	public Response executeTheRequestByPassingMultipleQueryParameter(RequestSpecification requestSpecification,
			String endPoint, Method httpMethod, Map<String, String> queryParams) {
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			requestSpecification.queryParams(entry.getKey(), entry.getValue());
		}
		FilterableRequestSpecification filterableRequestSpec = printRequestSpecification(requestSpecification);
		logger.info("Set the query parameter has :\n{}", filterableRequestSpec.getQueryParams());
		Response response = requestSpecification.request(httpMethod, endPoint);
		logger.info("Response body:\n{}", response.body().asPrettyString());
		return response;
	}

	/**
	 * Executes a request with a JSON body.
	 *
	 * @param requestSpecification The existing request specification.
	 * @param endPoint             The API endpoint.
	 * @param httpMethod           The HTTP method for the request.
	 * @param jsonBody             The JSON body to be included in the request.
	 * @return The Response object containing the result of the request.
	 * @throws Exception If an error occurs while executing the request.
	 * @author Pearlson
	 */

	public Response executeTheRequest(RequestSpecification requestSpecification, String endPoint, Method httpMethod,
			String jsonBody) {
		try {
			// Add the JSON body to the request
			requestSpecification.body(jsonBody);
			// Build the request with the resource and HTTP method
			Response response = requestSpecification.request(httpMethod, endPoint);
			// Log the response body
			logger.info("Response body:\n{}", response.body().asPrettyString());
			return response;
		} catch (Exception e) {
			logger.error("Error executing the request: {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Parses the response body of a Response object to a JSON node.
	 *
	 * @param response The Response object containing the response body.
	 * @return A JsonNode representing the parsed JSON response.
	 * @author Pearlson
	 */

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

	/**
	 * Extracts JSON nodes from a Response object and returns them as a stream.
	 *
	 * @param response The Response object containing the JSON response.
	 * @return A Stream of JsonNode objects representing JSON nodes from the
	 *         response.
	 * @author Pearlson
	 */

	public Stream<JsonNode> extractJsonNodesAsStream(Response response) {
		Stream<JsonNode> stream = null;
		JsonNode employees = parseResponseToJsonNode(response);
		stream = StreamSupport.stream(employees.spliterator(), false);
		return stream;
	}

	/**
	 * Compares and asserts the equality of actual and expected attribute values.
	 *
	 * @param actualValue   The actual attribute value to be compared.
	 * @param expectedValue The expected attribute value to compare against.
	 * @return True if the values are equal, false otherwise.
	 * @throws IllegalArgumentException If unsupported data types are provided for
	 *                                  assertion.
	 * @author Pearlson
	 */

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

	/**
	 * Validates an input JSON file against a JSON schema.
	 *
	 * @param inputJsonFilePath  The path to the input JSON file.
	 * @param jsonSchemaFilePath The path to the JSON schema file.
	 * @return True if validation is successful, false otherwise.
	 * @author Pearlson
	 */

	public boolean validateJsonFileAgainstSchema(String inputJsonFilePath, String jsonSchemaFilePath) {
		String schemaPath = RunConstants.PROJECT_ROOT_LOCATION + jsonSchemaFilePath;
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		Path filePath = currentDirectory.resolve(inputJsonFilePath);

		File inputFile = new File(filePath.toString());
		File schemaFile = new File(schemaPath);
		try {
			// Load and parse the JSON schema
			InputStream schemaInputStream = new FileInputStream(schemaFile);
			JSONObject schemaJson = new JSONObject(new JSONTokener(schemaInputStream));
			Schema schema = SchemaLoader.load(schemaJson);

			// Load and parse the input JSON data
			InputStream inputInputStream = new FileInputStream(inputFile);
			JSONObject inputJson = new JSONObject(new JSONTokener(inputInputStream));

			// Validate the input JSON data against the schema
			schema.validate(inputJson);
			logger.info("JSON schema validation file: \n{}\n Schema Data Structure: \n{}", inputJson, schemaJson);
			return true;
		} catch (IOException e) {
			logger.error("Error reading file: {}", e.getMessage());
			return false;
		} catch (org.everit.json.schema.ValidationException e) {
			logger.error("JSON schema validation failed for file: {}", inputJsonFilePath);
			logger.error("Validation error message: {}", e.getMessage());
			return false;
		}
	}

	/**
     * Flattens a JSON node into a stream of key-value pairs.
     *
     * @param prefix The prefix to use for keys.
     * @param node   The JSON node to flatten.
     * @return A stream of key-value pairs representing the flattened JSON structure.
     * @author Pearlson
     */
	
	public Stream<Map.Entry<String, JsonNode>> flatten(String prefix, JsonNode node) {
		try {
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

		Spliterator<Map.Entry<String, JsonNode>> spliterator = Spliterators.spliteratorUnknownSize(fieldsIterator,
				Spliterator.ORDERED);

		return StreamSupport.stream(spliterator, false).flatMap(entry -> {
			String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
			JsonNode value = entry.getValue();

			if (value.isObject()) {
				return flatten(key, value);
			} else {
				return Stream.of(Map.entry(key, value));
			}
		});
		}
		catch (Exception e) {
            logger.error("An error occurred while flattening JSON node with prefix '{}'", prefix, e);
            return Stream.empty();
        }
	}

	/**
     * Processes and filters a JSON node using the provided filter predicate.
     *
     * @param node            The JSON node to process.
     * @param filterPredicate The predicate used for filtering.
     * @return An optional string containing the first filtered value, if present.
     * @author Pearlson
     */
	
	public Optional<String> processAndFilterJsonNode(JsonNode node,
			Predicate<Map.Entry<String, JsonNode>> filterPredicate) {
		Stream<Map.Entry<String, JsonNode>> flattenedStream = flatten("", node);
		return flattenedStream.filter(filterPredicate).map(entry -> entry.getValue().asText()).findFirst();
	}
	
	/**
	 * Retrieves a field from a nested JSON structure represented by the given parent node.
	 * This method iterates through the nested structure using the provided field names and returns
	 * the value of the final field as a String.
	 *
	 * @param parentNode The parent JSON node representing the root of the nested structure.
	 * @param fieldNames An array of field names specifying the path to the desired field.
	 * @return The value of the specified nested field as a String, or null if the field is not found.
	 * @author Pearlson
	 */
	
	public String getFieldFromNestedJson(JsonNode parentNode, String... fieldNames) {
		JsonNode currentNode = parentNode;
		for (String fieldName : fieldNames) {
			currentNode = currentNode.get(fieldName);
			if (currentNode == null) {
				// Handle error or return default value
				return null;
			}
		}
		return currentNode.asText();
	}
}
