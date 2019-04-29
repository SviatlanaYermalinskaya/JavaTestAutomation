package builder;

import org.apache.log4j.Logger;

import model.Post;

public class VKPost {
	private static final Logger logger = Logger.getLogger(VKPost.class);
	private String access_token;
	private String post_id;
	private String owner_id;
	private String message;
	
	public String getAccess_token() 
	{
		return access_token;
	}
	public void setAccess_token(String access_token) 
	{
		this.access_token = access_token;
	}
	public String getPost_id() 
	{
		return post_id;
	}
	public String getOwner_id() 
	{
		return owner_id;
	}
	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
	
	private VKPost()
	{	
		logger.trace("VKPost: new instanse creation");
	}
	
	public String postDefinitions()
	{
		return owner_id + "_" + post_id;
	}
	
	public static class Builder
	{
		private VKPost vkpost;
		
		public Builder(Post post)
		{
			vkpost = new VKPost();
			vkpost.owner_id = post.getOwner_id();
			vkpost.access_token = post.getAccess_token();
			vkpost.message = post.getMessage();
		}
		
		public Builder setAccessToken(String access_token) 
		{
			vkpost.access_token = access_token;
			return this;
		}
		
		public Builder setMessage(String message) 
		{
			vkpost.message = message;
			return this;
		}
		
		public Builder setPostId(String post_id) 
		{
			vkpost.post_id = post_id;
			return this;
		}
		
		public VKPost build()
		{
			return vkpost;
		}
	}
	
	 @Override
	 public String toString()
	 {
	        return "post: access_token=\"" + this.access_token + "\" post_id=\"" + this.post_id + 
	        		"\" owner_id=\"" + this.owner_id +
	                "\" message=\"" + this.message + "\"";
	 }

}
