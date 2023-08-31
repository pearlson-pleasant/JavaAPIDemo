Feature: Validate the pet store user details

@smoke
  Scenario: PostMethod- Create new user by passing json body
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    Then Check the Json Schema for input json body
    And Set the json body for create new user
    And Execute Create user POST request
    Then The status code should be "200"
    Then Check the Json schema for the create user post request
    And Get and set the username from datamodels

  Scenario: GetMethod- Get the user details by passing parameter
    Given Set the base url as petstore for the Request Specification
    And Pass the path parameter key as "username" and value as "dhkk"
    When Execute the GET the user request
    Then The status code should be "200"
    Then Check the Json schema for the get user response
    And Get the user password using json path


  Scenario: GetMethod- Get the loin user details by passing multiple parameter
    Given Set the base url as petstore for the Request Specification
    And Pass the "username" values as "dhkk" and pass the "password" value as  and execute Get loin user request
    Then The status code should be "200"
    Then Check the Json schema for the login user response


  Scenario: GetMethod- Get the loin user details by passing parameter in datatable
    Given Set the base url as petstore for the Request Specification
    And Pass the below query parameters and execute login user get request
      | username | dhkk     |
      | password | dasfaswe |
    Then The status code should be "200"
    Then Check the Json schema for the login user response
    And print the log response
    Then Get the response body "message" using json path

  Scenario: PostMethod- Create List of user by passing json body
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    And Set the Json Body for list of user
    And Execute the post request or list of users
    Then The status code should be "200"
    Then Check the Json schema for the list of user response

@smoke
  Scenario: PutMethod- Update the user details by passing json body and query parameter
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    Then Check the Json Schema for input json body
    And Set the Json Body for update the user
    And Pass the path parameter key as "username" and value as "dhkk"
    And Execute the put request for update user

  Scenario: DeleteMethod- Delete the user details by passing json body and query parameter
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    And Set the Json Body for update the user
    And Pass the path parameter key as "username" and value as "dhkk"
    And Execute the delete request for respective user

  Scenario Outline: PostMethod- create new user details
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    When I start building new Create user
    And I set create user id "<id>"
    And I set create user username "<username>"
    And I set create user firstName "<firstName>"
    And I set create user lastName "<lastName>"
    And I set create user email "<email>"
    And I set create user password  "<password>"
    And I set create user phone "<phone>"
    And I set the current create new user details as body
    And Execute the post request for create user details

    Examples: 
      | id | username | firstName | lastName | email           | password     | phone      |
      | 17 | Sofia    | Ivan      | Bell     | Mutlu@gmail.com | Password@123 | 8769201013 |

  Scenario: GetMethod- Get the loin user details by passing parameter from json body
    Given Set the base url as petstore for the Request Specification
    And Pass the loin user detail parameter from json file and execute the request
    Then The status code should be "200"
    Then Check the Json schema for the login user response
