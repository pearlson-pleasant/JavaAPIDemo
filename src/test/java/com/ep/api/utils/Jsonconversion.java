package com.ep.api.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

/**
 * Utility class for JSON conversion and handling operations.
 * 
 * @author Pearlson
 */
public class Jsonconversion {

	private static final Logger logger = LoggerManagement.getLogger();

	/**
	 * Converts a model object to JSON string.
	 *
	 * @param model The model object to be converted to JSON.
	 * @param <T>   The type of the model object.
	 * @return The JSON representation of the model object, or null if conversion
	 *         fails.
	 * @author Pearlson
	 */
	public <T> String modelToJson(T model) {
		try {
			Gson gson = new Gson();
			// Serialization
			String jsonValue = gson.toJson(model);
			return jsonValue;
		} catch (Exception e) {
			// Could not convert. Pass back default value...
			String result = null;
			return result;
		}
	}

	/**
	 * Reads and deserializes JSON data from a file into a specified data type.
	 *
	 * @param storeFilePath The path to the JSON file.
	 * @param type          The Type representing the desired data type for
	 *                      deserialization.
	 * @param <T>           The type of data to be deserialized.
	 * @return An instance of the deserialized data type, or null if parsing fails.
	 * @author Pearlson
	 */
	
	public <T> T jsonDataStore(String storeFilePath, Type type) {
		try {
			Path currentDirectory = Paths.get(System.getProperty("user.dir"));
			Path filePath = currentDirectory.resolve(storeFilePath);
			byte[] jsonData = Files.readAllBytes(filePath);
			String jsonString = new String(jsonData, "UTF-8");

			Gson gson = new Gson();
			// De-Serialization
			T dataModels = gson.fromJson(jsonString, type);
			return dataModels;
		} catch (IOException e) {
			// Could not read or parse the JSON file. Pass back default value...
			T result = null;
			return result;
		}
	}

	/**
	 * Reads and deserializes JSON data from a file into a specified class type.
	 *
	 * @param storeFilePath The path to the JSON file.
	 * @param type          The Class representing the desired class type for
	 *                      deserialization.
	 * @param <T>           The type of class to be deserialized.
	 * @return An instance of the deserialized class, or null if parsing fails.
	 * @author Pearlson
	 */

	public <T> T deserializeJsonData(String storeFilePath, Class<T> type) {
		try {
			Path currentDirectory = Paths.get(System.getProperty("user.dir"));
			Path filePath = currentDirectory.resolve(storeFilePath);
			byte[] jsonData = Files.readAllBytes(filePath);
			String jsonString = new String(jsonData, "UTF-8");

			Gson gson = new Gson();
			// De-Serialization
			T dataModels = gson.fromJson(jsonString, type);
			logger.info("JSON data deserialized successfully from file: {}", storeFilePath);
			return dataModels;
		} catch (IOException e) {
			// Could not read or parse the JSON file. Pass back default value...
			logger.error("Error reading or parsing JSON file: {}", storeFilePath, e);
			T result = null;
			return result;
		}
	}

	/**
	 * Reads and returns the JSON content from a file as a string.
	 *
	 * @param storeFilePath The path to the JSON file.
	 * @return The JSON content as a string, or an empty string if reading fails.
	 * @author Pearlson
	 */

	public String readJsonFileAsString(String storeFilePath) {
		Path currentDirectory = Paths.get(System.getProperty("user.dir"));
		Path filePath = currentDirectory.resolve(storeFilePath);
		byte[] jsonData = null;
		try {
			jsonData = Files.readAllBytes(filePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String jsonString = null;
		try {
			jsonString = new String(jsonData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	/**
	 * Reads and returns the content of a file as a string.
	 *
	 * @param storeFilePath The path to the file.
	 * @return The file content as a string, or an empty string if reading fails.
	 * @author Pearlson
	 */
	public String readTextFileAsString(String storeFilePath) {
		try {
			Path currentDirectory = Paths.get(System.getProperty("user.dir"));
			Path filePath = currentDirectory.resolve(storeFilePath);
			String jsonString = Files.readString(filePath, StandardCharsets.UTF_8);
			logger.info("Json body {}", jsonString);
			return jsonString;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
