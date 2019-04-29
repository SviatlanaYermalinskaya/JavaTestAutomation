package pages;

import org.apache.log4j.Logger;

public abstract class Page {

	protected static final Logger logger = Logger.getLogger(Page.class);
	private String type;
	
	public Page(String type)
	{
		this.type = type;
		logger.debug(this.type + " page initialization...");
	}
	
	protected String getType()
	{
		return type;
	}
}
