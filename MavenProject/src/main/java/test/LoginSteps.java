package test;

import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import builder.VKPost;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import facade.LogPassExpXPathFromDBFacade;
import facade.MessagesExtracting;
import facade.PostsExtracting;

import model.Message;
import model.Post;
import pages.LoginPage;
import pages.MailPage;
import ru.yandex.qatools.allure.annotations.Attachment;
import vkapi.VkMessage;
import webdriver.WebDriverSingleton;

public class LoginSteps
{
	private static final Logger logger = Logger.getLogger(LoginSteps.class);
	private static final String MESSAGES_XML = "messages.xml";
	private static final String DB_CONNECTION = "dbconnection.xml";
    private static final String MAIN_URL = "http://mail.ru";
    
    private VkMessage vkMessage;
    private LoginPage loginPage;
    private MailPage mailPage;
    private WebDriver webDriver;

    private String expectedXPath; 
    private String message_text;
    private List<Message> messages;
    private List<Post> posts;
    private VKPost vkpost;

    public LoginSteps()
    {
    	vkMessage = new VkMessage();
    }
    
    @Before(value = "@send_message", order = 10)
    public void getMessagesFromXML() 
    {
    	MessagesExtracting msg_extract = new MessagesExtracting(MESSAGES_XML);
    	messages = msg_extract.getMessages();
    }

    @Given("^I am on main application page$")
    public void loadMainPage()
    {
    	logger.info("test is started...");
    	webDriver = WebDriverSingleton.getInstance();
    	loginPage = new LoginPage(webDriver);
        webDriver.get(MAIN_URL);
    }

    @When("^I login as user with \"([^\"]*)\"$")
    public void loginAsUserWithCredentials(String id)
    {    
    	logger.info("login test for user with id = " + id);
    	LogPassExpXPathFromDBFacade dbConnection = new LogPassExpXPathFromDBFacade(DB_CONNECTION, id);
    	
    	expectedXPath = dbConnection.getExpectedXPath();   	
    	try
    	{
    		Assert.assertNotNull("Error with BD connection:  expectedXPath is not initialized", expectedXPath);
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	}  	
        loginPage.enterLoginAndPass(dbConnection.getLogin(), dbConnection.getPassword());
        loginPage.clickEnterButton();
    }

    @When("^I login as user with login \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void loginAsUserWithCorrectPassword(String login, String password)
    {
        loginPage.enterLoginAndPass(login, password);
        loginPage.clickEnterButton();
        mailPage = new MailPage(webDriver);
    }
    
    @Then("^I see expected element$")
    public void seeLogoutLink()
    {
    	logger.info("Test login...");  
    	try
    	{
    		Assert.assertTrue("Login test is failed!", loginPage.isLogLinkPresents(expectedXPath));
        	logger.info("Login is completed successfully");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    	makeScreenshot();
    }
    
    @Then("^I see expected element with xpath \"([^\"]*)\"$")
    public void seeLink(String expectedXPath)
    {
    	logger.info("Test login...");  
    	try
    	{
    		Assert.assertTrue("Login test is failed!", loginPage.isLogLinkPresents(expectedXPath));
        	logger.info("Login is completed successfully");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 
    	makeScreenshot();
    }
    
    @Then("^I check incoming messages presence$")
    public void checkIncomingMessages()
    {
    	logger.debug("Check incoming messages presence...");   	
    	try
    	{
    		Assert.assertTrue("Test fails caused there are no incomming messages", mailPage.hasIncomingMessage());
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	 	
    }
    
    @When("^I move (\\d+) message to spam$")
    public void moveOneMessageToSpam(int number)
    {
    	logger.debug(number + " message is moving to spam...");
    	message_text = mailPage.moveMessageToSpam(number - 1);
    }
    
    @Then("^I see popup with confirmation$")
    public void checkPopupConfirmation()
    {
    	logger.debug("Wait message moving confirmation...");   	
    	try
    	{
    		Assert.assertTrue("Test fails caused there is no popup message with successful moving confirmation", mailPage.isPopupMessageMovingOkAppears());
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    }
    
    @When("^I go to spam$")
    public void goToSpam()
    {
    	mailPage.openSpam();
    	makeScreenshot();
    }
    
    @And("^I move message back from spam to income$")
    public void moveMessageBackFromSpam()
    {
    	logger.info("Moving message back to income");
    	try
    	{
    		Assert.assertTrue("Test fails because required message is not found", mailPage.hasMovedMessageFromSpam(message_text));
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 
    	makeScreenshot();
    }
    
    @When("^I check (\\d+) messages$")
    public void checkMessages(int messages_number_to_select)
    {
    	try
    	{
    		Assert.assertTrue("Error: number of selected messages is less than required", mailPage.selectMessages(messages_number_to_select));
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    	makeScreenshot();
    } 
    
    @And("^I uncheck all messages$")
    public void uncheckAllMessage()
    {
    	logger.info("Unselect all messages");
    	mailPage.unselectAllMessages(); 
    	makeScreenshot();
    }
     
    @Then("^I don't see any selection$")
    public void checkSelection()
    {
    	try
    	{
    		Assert.assertTrue("Error: not all messages are unselected", mailPage.isAllMessagesUnselected());
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    } 
    
    @When("^I send message using (\\d+) data$")
    public void sendMessage(int index)
    { 
    	logger.info("Send " + messages.get(index).toString());
    	mailPage.sendMessage(messages.get(index).getTo(), messages.get(index).getSubject(), messages.get(index).getMsgbody());
    	logger.info("Message is sent");
    } 
    
    @Then("^I see expected confirmation$")
    public void hasMessageSent()
    {
    	try
    	{
    		Assert.assertTrue("Error: message is not sent", mailPage.isMessageSent());
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    } 
    
    // steps fo VK api tests
    
    @Given("^I get posts from \"([^\"]*)\"$")
    public void postsExtracting(String posts_file)
    {
    	logger.info("test is started...");
    	PostsExtracting post_extract = new PostsExtracting(posts_file);
    	posts = post_extract.getPosts();      
    }
    
    @When("^I send post$")
    public void sendWallPost()
    {    	
    	try
    	{
    		String response_msg = vkMessage.sendMessage(posts.get(0));
    		logger.debug("Wall post sending response: " + response_msg);
    		Assert.assertFalse("Error appears: wall post is not sent", response_msg.contains("error"));	
    		vkpost = new VKPost.Builder(posts.get(0))
    		.setPostId(response_msg.replaceAll("\\D", ""))
    		.build();

    		logger.debug("Added post: " + vkpost.toString());   	
    		logger.info("Message is posted");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 
    	catch (Exception e)
    	{
    		logger.error("Exception appears: " + e.getMessage());
    		throw e;
    	}
    }
    
    @Then("^I check posted message$")
    public void checkPostedMessage()
    {
    	try
    	{
    		Assert.assertTrue("Assert error: extual message doesn't equal posted message", vkMessage.isPostedTextEqualsMsg(vkpost));
    		logger.info("Assert result: message is posted correctly!");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    } 
    
    @When("^I edit post$")
    public void editWallPost()
    {
    	try
    	{
    		String response_msg = vkMessage.editMessage(vkpost, posts.get(1));
    		logger.debug("Wall post sending response: " + response_msg);
    		Assert.assertFalse("Error appears: wall post is not sent", response_msg.contains("error"));	
    		vkpost.setMessage(posts.get(1).getMessage()); 

    		logger.debug("Edited post: " + vkpost.toString());   	
    		logger.info("Message is changed");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 
    	catch (Exception e)
    	{
    		logger.error("Exception appears: " + e.getMessage());
    		throw e;
    	}
    }
    
    @When("^I delete message$")
    public void removeWallPost()
    {
    	try
    	{
    		String response_msg = vkMessage.deleteMessage(vkpost);
    		logger.debug("Wall post deletion sends response: " + response_msg);
            Assert.assertFalse(response_msg.contains("error"));
    		
    		Assert.assertFalse("Error appears: wall post is not deleted", response_msg.contains("error"));	  	
    		logger.info("Message is removed");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 
    	catch (Exception e)
    	{
    		logger.error("Exception appears: " + e.getMessage());
    		throw e;
    	}
    }
    
    @Then("^I check post deletion$")
    public void checkPostDeletion()
    {
    	try
    	{
    		Assert.assertTrue(vkMessage.isMessageDeleted(vkpost));
    		logger.info("Assert result: message is deleted!");
    	}
    	catch (AssertionError e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	} 	
    } 
    
    @Attachment(value = "Attachment Screenshot", type = "image/png")
    public byte[] makeScreenshot()
    {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }
  
    
    @After
    public void afterClass()
    {
    	logger.info("Test is completed!\n ---------------------------------------------------------------------");
    	WebDriverSingleton.quit();
    }
}
