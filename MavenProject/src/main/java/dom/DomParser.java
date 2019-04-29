package dom;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.DBConnection;
import model.Message;
import model.Post;

public class DomParser
{
    public List<Message> parse(Document document) throws FileNotFoundException, XMLStreamException
    {
        NodeList nodeList = document.getElementsByTagName("message");
        List<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            messages.add(getMessage(nodeList.item(i)));
        }
        return messages;
    }

    private static Message getMessage(Node node)
    {
        Message message = new Message();
        Element element = (Element) node;
        message.setTo(getTagValue("to", element));
        message.setSubject(getTagValue("subject", element));
        message.setMsgbody(getTagValue("msgbody", element));
        return message;
    }

    private static String getTagValue(String tag, Element element)
    {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    
    public List<DBConnection> parseConnection(Document document) throws FileNotFoundException, XMLStreamException
    {
        NodeList nodeList = document.getElementsByTagName("dbconnection");
        List<DBConnection> connections = new ArrayList<DBConnection>();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
        	connections.add(getConnection(nodeList.item(i)));
        }
        return connections;
    }

    private static DBConnection getConnection(Node node)
    {
    	DBConnection connection = new DBConnection();
        Element element = (Element) node;
        connection.setUrl(getTagValue("url", element));
        connection.setUser(getTagValue("user", element));
        connection.setDBpassword(getTagValue("dbpassword", element));
        return connection;
    }
    
    public List<Post> parsePosts(Document document) throws FileNotFoundException, XMLStreamException
    {
        NodeList nodeList = document.getElementsByTagName("post");
        List<Post> posts = new ArrayList<Post>();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
        	posts.add(getPost(nodeList.item(i)));
        }
        return posts;
    }

    private static Post getPost(Node node)
    {
    	Post post = new Post();
        Element element = (Element) node;
        post.setAccess_token(getTagValue("access_token", element));
        post.setOwner_id(getTagValue("owner_id", element));
        post.setMessage(getTagValue("message", element));
        return post;
    }
}
