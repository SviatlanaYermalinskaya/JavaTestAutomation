# JavaTestAutomation
Maven project is used to show automation tests for mail.ru (login, messages moving, checking and sending) and 
vk.com (API tests: wall post adding, editing and deletion). 

Using Tools, Technologies and Frameworks: Maven, Java, JUnit, HTTP, Selenium, Cucumber, MySQL, PageObject, Web API, 
Allure Framework. 

File "TC_MailRu.txt" contains all test cases automated in this project for mail.ru website. First six test cases
are login tests. They are described in "MavenProject\src\test\java\test\Login.feature" on Gherkin language. 
Others mail.ru test cases are in "MavenProject\src\test\java\test\Mail.feature".

User name, password and expected XPath for every login test are taken from local database with Users table (see "BD.jpg" file).
You can create similar database to run login test and change "dbconnection.xml" with your database url, user name and 
password or exclude login tests from running by uncommenting 10 line (tags = "~@login") of Cucumber Options in 
"MavenProject\src\test\java\test\LoginTest.java".

"MavenProject\src\test\java\test\VKapi.feature" contains Gherkin description for vk.com API tests.

Open "MavenProject\target\site\allure-maven-plugin.html" using Firefox to see tests resulting Allure report with 
attachments.
