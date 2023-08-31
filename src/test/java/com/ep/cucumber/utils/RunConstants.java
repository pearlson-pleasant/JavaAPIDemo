package com.ep.cucumber.utils;

public class RunConstants {

	public static final String PROJECT_ROOT_LOCATION = System.getProperty("user.dir");
	
	// Json conversion Input data file path
	public static final String Create_User_Data="src\\test\\resources\\apiInputData\\createuserinputdata.json";
	public static final String Add_New_Pet="src\\test\\resources\\apiInputData\\AddNewPetToStore.json";
	public static final String Create_List_OfUser_Data="src\\test\\resources\\apiInputData\\CreateListOfUser.json";
	public static final String Pet_Input_Data="src\\test\\resources\\apiInputData\\PetInputData.json";
	public static final String Employee_Input_Data="src\\test\\resources\\apiInputData\\EmployeeInputData.json";
	public static final String Create_New_User_Details_Gorest="src\\test\\resources\\apiInputData\\CreateNewUserDetailsInGorest.json";

	// Input data file path
	public static final String Login_User_Input_Data="\\src\\test\\resources\\apiInputData\\loinuserinputvalues.json";
	public static final String Negative_Data_For_Login="\\src\\test\\resources\\apiInputData\\NegativeInputDataForLogin.json";
	public static final String Sample_Image_Path=PROJECT_ROOT_LOCATION +"\\src\\test\\resources\\images\\sampleimage.jpg";

	// Json schema file path
	public static final String Create_User_Schema ="\\src\\test\\resources\\apiJsonSchema\\createuserschema.json";	
	public static final String Get_User_Schema ="\\src\\test\\resources\\apiJsonSchema\\GetUserSchema.json";	
	public static final String GetAll_User_Schema="\\src\\test\\resources\\apiJsonSchema\\GetAllUserSchema.json";
	public static final String Add_Updatethe_Pet_Schema="\\src\\test\\resources\\apiJsonSchema\\AddAndUpdateThePetSchema.json";
	public static final String CreateNewUserGorestInputSchema="\\src\\test\\resources\\apiJsonSchema\\CreateNewUserGorestInputSchema.json";
	public static final String Create_New_User_Input_Schema="\\src\\test\\resources\\apiJsonSchema\\CreateUserInputDataSchema.json";
	
	// Negative data sets json object name
	public static final String Negative_Input_dataset_For_Login="negativeData";
}
