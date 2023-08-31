Feature: Validate the Gorest user details

  Scenario: GetMethod- Get all user details
    Given Set the Authorization token and base url for Gorest
    And Execute the Get user detail request
    Then The status code should be "200"
    Then The header content type should be equal to "application/json; charset=utf-8"  
    Then Check the Json schema for the Gorest Get user request

@smoke
  Scenario: PostMethod- Create new user for gorest by passing json body
    Given Set the Authorization token and base url for Gorest
    And Set the content type as json char uf8
    And Set the Json Body for create new user values in gorest using json file
    And Execute the post request to create new user in gorest data
    Then The status code should be "201"
    Then The header content type should be equal to "application/json; charset=utf-8"

  Scenario: PutMethod- update the user details for gorest by passing json body
    Given Set the Authorization token and base url for Gorest
    And Set the content type as json char uf8
    And set the json body to update the details of user
    And set the path parameter "id" and value as "4087685"
    And Execute the put request for update gorest user
    Then The header content type should be equal to "application/json; charset=utf-8"


  Scenario: DeleteMethod- Delete the user for gorest by passing json body
    Given Set the Authorization token and base url for Gorest
    And Set the content type as json char uf8
    And set the json body to update the details of user
    And set the path parameter "id" and value as "4087685"
    And Execute the Delete request for update gorest user
    Then The status code should be "204"
    Then The header content type should be equal to "application/json; charset=utf-8"
