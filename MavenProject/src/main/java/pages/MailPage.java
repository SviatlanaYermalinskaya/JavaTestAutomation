package pages;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MailPage extends Page{
	
	@FindBy (xpath = ".//div[@data-bem=\"b-datalist__item\"]")
	List<WebElement> incoming_messages;
		
	@FindBy (xpath = ".//span[@class=\"js-txt _js-title notify-message__title__text notify-message__title__text_ok\"]")
	WebElement popup_moved_ok;
	
	@FindBy (xpath = ".//div[@data-id=\"950\"]")
	WebElement spam;
	
	@FindBy (xpath = ".//div[@data-cache-key=\"950_undefined_false\"]//div[@class=\"b-datalist__body\"]//div[@data-bem=\"b-datalist__item\"]")
	List<WebElement> spam_messages;	
	
	@FindBy (xpath = ".//div[@class=\"b-checkbox__box\"]")
	List<WebElement> checkboxes;
	
	@FindBy (xpath = "(.//span[@class=\"b-toolbar__btn__text b-toolbar__btn__text_pad\"])[1]")
	WebElement button_write_message;
	
	@FindBy (xpath = "//textarea[@class=\"js-input compose__labels__input\"]")
	WebElement input_addressees;
	
	@FindBy (xpath = ".//input[@name=\"Subject\"]")
	WebElement input_subject;
	
	@FindBy(xpath = "//iframe[contains(@id, 'composeEditor_ifr')]")
	WebElement msg_text_frame;
	
	@FindBy (xpath = "//body[@id='tinymce']")
	WebElement msg_text;
	
	@FindBy (xpath = ".//span[@class=\"b-toolbar__btn__text\"]")
	WebElement button_send_message;
	
	@FindBy (xpath = ".//div[@class=\"message-sent__title\"]")
	WebElement message_sent;
	
	private Actions action;
	private WebDriver driver;
	private WebDriverWait wait;
	
    public MailPage(WebDriver webdriver)
    {
    	super("mail");
        PageFactory.initElements(webdriver, this);
        this.driver = webdriver;
        wait = new WebDriverWait(driver, 20);
        action = new Actions(driver);
        logger.debug(super.getType() + " page is created");
    }
    
	public boolean hasIncomingMessage ()
	{
		if (incoming_messages.isEmpty())
		{
			logger.info("There are no imcoming messages");
			return false;
		}
		logger.debug("There are " + incoming_messages.size() + " imcoming messages");
		return true;
	}
		
	public String moveMessageToSpam (int message_number)
	{
		if (message_number >= incoming_messages.size())
		{
			logger.warn("Number of message moving to spam is more than quantity of available messages");
			return null;
		}			
		String result = incoming_messages.get(message_number).getText();
		logger.info("Moving to spam message: " + result);
		action.contextClick(incoming_messages.get(message_number)).sendKeys("j").build().perform();
		return result;
	}
	
	public boolean isPopupMessageMovingOkAppears()
	{	
		try
		{
			wait.until(ExpectedConditions.visibilityOf(popup_moved_ok));
			logger.debug("Popup with confirmation is appeared");
			logger.info("Message is successfully moved.");
		}
		catch (TimeoutException e)
		{
			logger.warn("Notification about message successfully moving is not appeared in required time");
			return false;
		}
		
		try
		{
			wait.until(ExpectedConditions.invisibilityOf(popup_moved_ok));
		}
		catch (TimeoutException e)
		{
			logger.warn("Notification about message successfully moving is not disappeared in required time");
		}
		
		return true;		
	}
	
	public void openSpam()
	{		
		logger.info("Open spam");
		spam.click();
		wait.until(ExpectedConditions.titleContains("Спам"));	
	}
	
	public boolean hasMovedMessageFromSpam(String msg_text)
	{
		for (WebElement msg: spam_messages)
		{
			if (msg_text.equals(msg.getText()))
			{
				logger.info("Required message is found in spam");
				action.contextClick(msg).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).
				sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
				return true;
			}
		}		
		logger.warn("Required message is not found in spam");
		return false;
	}
	
	public void sendMessage(String address, String subject, String text)
	{
		try
		{
			button_write_message.click();
			wait.until(ExpectedConditions.titleContains("Новое письмо"));
			
			input_addressees.clear();
			input_addressees.sendKeys(address);
			
			input_subject.clear();
			input_subject.sendKeys(subject);		

			driver.switchTo().frame(msg_text_frame);
			msg_text.clear();
			msg_text.sendKeys(text);
			
			driver.switchTo().defaultContent();
			button_send_message.click();			
		}
		catch (Exception e)
		{
			logger.error("Exception appears during message sending: " + e.getMessage());
			throw e;			
		}
	}
	
	public boolean isMessageSent()
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(message_sent));			
		}
		catch (TimeoutException e)
		{
			logger.warn("Notification that massage is set is not appeared in required time");
		}
		
		return message_sent.isDisplayed();
	}
	
	public boolean isAllMessagesUnselected()
	{	
		for (WebElement msg: incoming_messages)
		{
			if (msg.getAttribute("class").contains("selected"))
			{
				logger.error("Error: selected message is detected after all messages unselecting");
				return false;
			}
		}
		logger.info("All messages are unselected");
		return true;
	}	
	
	public void unselectAllMessages()
	{
		action.keyDown(Keys.CONTROL).sendKeys("a")
        .keyUp(Keys.CONTROL).build().perform();
	}
	
	public boolean selectMessages(int number)
	{
		logger.debug("Messages selection is started. Number of requared messages to select is " + number);
		int selected_message_number = 0;
		boolean is_all_selected = true; 
		if (number > incoming_messages.size())
		{
			logger.warn("Number of required messages to select is less than number of incoming messages");
		}
		for (int i = 0; i < incoming_messages.size(); i++)
		{
			try
			{
				wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(i+2)));
				checkboxes.get(i+2).click();
				logger.debug("Message " + (i+1) + " is selected");
			}
			catch (TimeoutException e)
			{
				logger.warn("Message " + (i+1) + " is not selected because it's not bacame clickable in required time");
				is_all_selected = false;
			}
			selected_message_number++;
			if (number == selected_message_number)
			{			
				if (is_all_selected)
				{
					logger.info("All required messages are selected");
				}
				else
				{
					logger.info("Message selection is comleted");
				}
				return is_all_selected;
			}				
		}
		logger.warn("Message selection is completed. Number of required messages to select is less than ectual selected messages");
		return false;
	}
}
