Feature: Validate the local json server employee end points

@smoke
  Scenario: GetMethod- Get all employee details
    Given Set the base url for the employee
    And Execute the Get request to get employee details
    And Get the "michael" employee "PersonalDetails" "phoneNo" from employee details
    Then The status code should be "200"


    Scenario: GetMethod- Get particular employee detail
    Given Set the base url for the employee
    And Pass the path parameter from employee input json file to get particular employee details
    And Execute the Get request to get particular employee details
    Then Validate the email id of the employee
    Then The status code should be "200"