package vkapi;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import builder.VKPost;
import model.Post;


public class VkMessage {
	private HttpClient client;
	private static final Logger logger = Logger.getLogger(VkMessage.class);
	
	public VkMessage()
	{
		client = HttpClientBuilder.create().build();
	}	
	
	public String sendMessage(Post post) // return response result or error message
	{
		try
		{
            URIBuilder builder = new URIBuilder("https://api.vk.com/method/wall.post?");
            builder.setParameter("access_token", post.getAccess_token())
            .setParameter("owner_id", post.getOwner_id())
            .setParameter("message", post.getMessage())
            .setParameter("v", "5.92");
            HttpGet request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);           
          
            return EntityUtils.toString(response.getEntity());                   	
		}
		catch (URISyntaxException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (ClientProtocolException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (IOException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
	}
	
	public String editMessage(VKPost vkpost, Post post) // return response result or error message
	{
		try
		{
            URIBuilder builder = new URIBuilder("https://api.vk.com/method/wall.edit?");
            builder.setParameter("access_token", vkpost.getAccess_token())
            .setParameter("owner_id", vkpost.getOwner_id())
            .setParameter("post_id", vkpost.getPost_id())
            .setParameter("message", post.getMessage())
            .setParameter("v", "5.92");
            HttpGet request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);           
          
            return EntityUtils.toString(response.getEntity());                   	
		}
		catch (URISyntaxException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (ClientProtocolException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (IOException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
	}
	
	
	public String deleteMessage(VKPost vkpost) // return response result or error message
	{
		try
		{      
            URIBuilder builder = new URIBuilder("https://api.vk.com/method/wall.delete?");
            builder.setParameter("access_token", vkpost.getAccess_token())
            .setParameter("owner_id", vkpost.getOwner_id())
            .setParameter("post_id", vkpost.getPost_id())
            .setParameter("v", "5.92");
            HttpGet request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            
             return EntityUtils.toString(response.getEntity()); 		
		}
		catch (URISyntaxException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (ClientProtocolException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
		catch (IOException ex)
		{
			return "error: ".concat(ex.getMessage());
		}
	}
	
	public boolean isPostedTextEqualsMsg(VKPost post)
	{
		String[] msg_parts;
		String msg_part;
		
		try
		{      
            URIBuilder builder = new URIBuilder("https://api.vk.com/method/wall.getById?");
            builder.setParameter("access_token", post.getAccess_token())
            .setParameter("posts", post.postDefinitions())
            .setParameter("v", "5.92");
            HttpGet request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            
            msg_part = EntityUtils.toString(response.getEntity()); 	
            logger.debug(msg_part);
		}
		catch (URISyntaxException ex)
		{
			return false;
		}
		catch (ClientProtocolException ex)
		{
			return false;
		}
		catch (IOException ex)
		{
			return false;
		}

		msg_parts = msg_part.split("\"text\":\"");
		msg_parts = msg_parts[1].split("\",\"can_edit\"");
		logger.debug("Posted message is \"" + msg_parts[0] + "\"");
		
		return post.getMessage().equals(msg_parts[0]);
	}
	
	public boolean isMessageDeleted(VKPost post)
	{
		String msg_part;
		
		try
		{      
            URIBuilder builder = new URIBuilder("https://api.vk.com/method/wall.getById?");
            builder.setParameter("access_token", post.getAccess_token())
            .setParameter("posts", post.postDefinitions())
            .setParameter("v", "5.92");
            HttpGet request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            
            msg_part = EntityUtils.toString(response.getEntity()); 		
		}
		catch (URISyntaxException ex)
		{
			return false;
		}
		catch (ClientProtocolException ex)
		{
			return false;
		}
		catch (IOException ex)
		{
			return false;
		}
		
		return "{\"response\":[]}".equals(msg_part);
	}

}
