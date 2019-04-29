package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends Page
{
    private static final int LINK_PRESENSE_TIMEOUT = 20;
    private static final String CORRECT_LOGIN_AND_PASSWORD = ".//a[@id='PH_logoutLink']";
    private static final String INCORRECT_LINK = ".//div[text()=\"Неверное имя или пароль\"]";
    private static final String NO_LOGIN_NO_PASSWORD = ".//div[text()=\"Введите имя ящика и пароль\"]";
    private static final String NO_LOGIN = ".//div[text()=\"Введите имя ящика\"]";
    private static final String NO_PASSWORD = ".//div[text()=\"Введите пароль\"]";

	private WebDriver driver;

    @FindBy(id = "mailbox:login")
    private WebElement loginField;

    @FindBy(id = "mailbox:password")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@id='mailbox:submit']/input")
    private WebElement buttonEnter;
    
    @FindBy(xpath = CORRECT_LOGIN_AND_PASSWORD)
    private WebElement correctLink;
    
    @FindBy(xpath = INCORRECT_LINK)
    private WebElement incorrectLink;
    
    @FindBy(xpath = NO_LOGIN_NO_PASSWORD)
    private WebElement noLoginNoPassword;
    
    @FindBy(xpath = NO_LOGIN)
    private WebElement noLogin;
    
    @FindBy(xpath = NO_PASSWORD)
    private WebElement noPassword;

    public LoginPage(WebDriver webdriver)
    {
    	super("login");
        PageFactory.initElements(webdriver, this);
        this.driver = webdriver;
        logger.debug(super.getType() + " page is created");
    }

    public void enterLoginAndPass(String login, String password)
    {
    	try
    	{
    		loginField.clear();
            loginField.sendKeys(login);
            passwordField.clear();
            passwordField.sendKeys(password);
    	}
    	catch (NoSuchElementException e)
    	{
    		logger.error(e.getMessage());
    		throw e;
    	}       
    }

    public void clickEnterButton()
    {
        buttonEnter.click();
	}

	public boolean isLogLinkPresents(String expectedXPath) {
		WebDriverWait wait = new WebDriverWait(driver, LINK_PRESENSE_TIMEOUT);
		try
		{
			if (CORRECT_LOGIN_AND_PASSWORD.equals(expectedXPath))
			{
				return wait.until(ExpectedConditions.visibilityOf(correctLink))
						.isDisplayed();
			}
			else if (INCORRECT_LINK.equals(expectedXPath))
			{
				return wait.until(ExpectedConditions.visibilityOf(incorrectLink))
						.isDisplayed();
			}
			else if (NO_LOGIN_NO_PASSWORD.equals(expectedXPath))
			{
				return wait.until(ExpectedConditions.visibilityOf(noLoginNoPassword))
						.isDisplayed();
			}
			else if (NO_LOGIN.equals(expectedXPath))
			{
				return wait.until(ExpectedConditions.visibilityOf(noLogin))
						.isDisplayed();
			}
			else if (NO_PASSWORD.equals(expectedXPath))
			{
				return wait.until(ExpectedConditions.visibilityOf(noPassword))
						.isDisplayed();
			}
			logger.warn(getClass().getName() + ": element with given expected XPath is not defined for this page");
		}
		catch (TimeoutException e)
		{
			logger.error(getClass().getName() + " TimeoutException: " +  e.getMessage());
		}
		catch (Exception e)
		{
			logger.error(getClass().getName() + " Exception: " +  e.getMessage());
			throw e;
		}
		return false;
	}
	
}
