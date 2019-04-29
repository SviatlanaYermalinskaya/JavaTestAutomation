@vkapi
Feature: Test VK API
 
  @wallpost
  Scenario: Add, edit and delete post
    Given I get posts from "posts.xml"
    When I send post
    Then I check posted message
    When I edit post
    Then I check posted message
    When I delete message
    Then I check post deletion
