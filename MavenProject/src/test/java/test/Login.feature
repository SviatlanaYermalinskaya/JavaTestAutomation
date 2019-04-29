@login
Feature: Test mail.ru login

  Scenario Outline: Test login page
    Given I am on main application page
    When I login as user with "<id>"
    Then I see expected element 
    Examples: 
      | id | 
      | 1  | 
      | 2  | 
      | 3  |
      | 4  |
      | 5  |
      | 6  |