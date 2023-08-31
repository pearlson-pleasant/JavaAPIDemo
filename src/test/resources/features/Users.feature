Feature: Validate the user module End points

  Scenario: GetMethod- Get the loin user details by passing password parameter value from another feature file
    Given Set the base url as petstore for the Request Specification
    And Pass the "username" values as "dhkk" and pass the "password" value as  and execute Get loin user request
    Then The status code should be "200"
    Then Check the Json schema for the login user response

  Scenario Outline: Negative scenario GetMethod- Get the user details by passing parameter
    Given Set the base url as petstore for the Request Specification
    And Pass the path parameter key as "username" and value as "<usernamevalue>"
    When Execute the GET the user request
    Then The status code should be "404"

    Examples: 
      | usernamevalue |
      | aaaa          |
      | A124          |
      | @q#erf        |

  
 
  Scenario: Negative scenario GetMethod- Check the loin user details by passing negative query parameter using datatable
    Given Set the base url as petstore for the Request Specification
    And Pass the multiple below query parameters and execute login user get request
      | username | password |
      | aaaaa    | dsaff    |
      | bbbbb    | asdsd    |
      | W@fgg    | 123Sdd   |
    Then Validate the response status is "200" for all responses
    
    @smoke
      Scenario: Negative scenario GetMethod- Check the loin user details by passing query parameter from json body
    Given Set the base url as petstore for the Request Specification
    And Pass the negative loin user detail values from json file and execute the request
    
