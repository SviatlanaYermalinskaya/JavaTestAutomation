package facade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import dom.DomParser;
import model.Post;

public class PostsExtracting {
	private static final Logger logger = Logger.getLogger(PostsExtracting.class);
	private String parsing_file;
	
	public PostsExtracting (String parsing_file)
	{
		this.parsing_file = parsing_file;
    	logger.info("Extract posts from " + parsing_file);
	}
	
	public List<Post> getPosts()
	{
        try
        {
            logger.debug("DOM parser: posts extracting...");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(parsing_file);
            List<Post> posts = new DomParser().parsePosts(document);
            posts.forEach(post -> logger.debug(post));
            return posts;
        }
        catch (FileNotFoundException e)
        {
        	logger.error("Error: file " + parsing_file + " is not found.");
        }
        catch (XMLStreamException e)
        {
        	logger.error(e.getMessage());  
        }
        catch (IOException | SAXException   e)
        {
        	logger.error(e.getMessage());  
        }
        catch (ParserConfigurationException e)
        {
        	logger.error(e.getMessage()); 
        }
        logger.warn("Ectracting fails");
		return null;
	}

}
