package com.ep.api.endpoints;

public class Routes {
	
	// pet-store User end-points
	public static String createPostUrl="/user";
	public static String GetUserUrl="/user/{username}"; 
	public static String GetLoinUser="/user/login";
	public static String CreateWithList="/user/createWithList";
	public static String UpdateTheUser="/user/{username}"; 
	
	// pet-store Pet end points
	public static String PetFindStatus="/pet/findByStatus";
	public static String AddNewPet="/pet";
	public static String UpdateThePet="/pet";
	public static String GetParticularPet="/pet/{petId}";
	public static String UploadImageFOrPet="/pet/{petId}/uploadImage";	
	
	// Go-rest API end-points
	public static String GetGorestUserDetails="/users";
	public static String CreateNewUserDetails="/users";
	public static String UpdateTheGorestUserDetails="/users/{id}";
	
	// Local host json server Employee end-points
	public static String GetAllEmployeeDetails="/Employee";
	public static String GetEmployeeDetailById="/Employee/{id}";
	public static String CreateNewEmployee="/Employee";
	public static String UpdateTheEmployeeDetail="/Employee/{id}";
	public static String DeleteTheEmployeeDetail="/Employee/{id}";
	
	// Local host json server Employee end-points
		public static String GetAllStudentDetails="/College";
}
