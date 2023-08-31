Feature: Validate the local json server student end points

@smoke
  Scenario: GetMethod- Get all employee details
    Given Set the base url for the student
    And Execute the Get request for student
    Then Validate the student universal rank is "1098"
    Then The status code should be "200"