Feature: Validate the pet store pet details


  Scenario: GetMethod- Get the pet pending details by passing query parameter
    Given Set the base url as petstore for the Request Specification
    And pass the query parameter has pending status
    And Execute the Get status of the pet
    Then The status code should be "200"


  Scenario: GetMethod- Get the pet available details by passing query parameter
    Given Set the base url as petstore for the Request Specification
    And pass the query parameter has available status
    And Execute the Get status of the pet
    Then The status code should be "200"


  Scenario: GetMethod- Get the pet sold details by passing query parameter
    Given Set the base url as petstore for the Request Specification
    And pass the query parameter has sold status
    And Execute the Get status of the pet
    Then The status code should be "200"


  Scenario: PostMethod- Add new pet to the store using json body
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    And Set the Json Body to add new pet to the store
    And Execute the post request to add new pet
    Then The status code should be "200"
    Then Check the Json schema for the pet in the store


  Scenario: PutMethod- Update the pet details in store using json body
    Given Set the base url as petstore for the Request Specification
    And set the content type as Json
    And Set the Json Body to add new pet to the store
    And Execute the put request to update pet
    Then The status code should be "200"
    Then Check the Json schema for the pet in the store


  Scenario: GetMethod- Get the particular pet using id
    Given Set the base url as petstore for the Request Specification
    And Pass the path parameter as "petId" and values as "31"
    And Execute the Get particular pet detail
    Then The status code should be "200"
    Then Check the Json schema for the pet in the store


  Scenario: PostMethod-Add the image for the respective pet
    Given Set the base url as petstore for the Request Specification
    And Pass the path parameter as "petId" and values as "31"
    And set the pet image path in the specification
    And Execute the post request to upload the image
