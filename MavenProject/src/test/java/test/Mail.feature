@mail
Feature: Test mail.ru mail
 
  @spam
  Scenario: Message moving to spam and back
    Given I am on main application page
    When I login as user with login "syermolinskaya" and password "ytdthjznyjtbpj,htntybt"
    Then I see expected element with xpath ".//a[@id='PH_logoutLink']"
    Then I check incoming messages presence 
    When I move 1 message to spam
    Then I see popup with confirmation
    When I go to spam
    And I move message back from spam to income
    Then I see popup with confirmation
  
  @check_messages
  Scenario: Check messages and uncheck all
    Given I am on main application page
    When I login as user with login "syermolinskaya" and password "ytdthjznyjtbpj,htntybt"
    Then I see expected element with xpath ".//a[@id='PH_logoutLink']"
    Then I check incoming messages presence 
    When I check 3 messages
    And I uncheck all messages
    Then I don't see any selection
 
  @send_message
  Scenario Outline: Send messages from messages.xml
    Given I am on main application page
    When I login as user with login "syermolinskaya" and password "ytdthjznyjtbpj,htntybt"
    Then I see expected element with xpath ".//a[@id='PH_logoutLink']" 
    When I send message using <index> data
    Then I see expected confirmation
        Examples: 
      | index | 
      | 0     | 
      | 1     | 