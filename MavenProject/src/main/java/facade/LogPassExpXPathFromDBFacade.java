package facade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import model.DBConnection;

public class LogPassExpXPathFromDBFacade 
{
	private static final Logger logger = Logger.getLogger(LogPassExpXPathFromDBFacade.class);
	private String connection_file;
	
	private String id;
    private String login;
    private String password;
    private String expectedXPath; 
    private boolean isLogPassExpXPathSet;
    
    public LogPassExpXPathFromDBFacade(String connection_file, String id)
    {
    	this.id = id;
    	this.connection_file = connection_file;
    	isLogPassExpXPathSet = false;
    }

	public String getLogin() 
	{
		checkConnection("login");
		return login;
	}

	public String getPassword() 
	{
		checkConnection("password");
		return password;
	}

	public String getExpectedXPath() 
	{
		checkConnection("expectedXPath");
		return expectedXPath;
	}
	
	private void checkConnection(String field)
	{
		if (!isLogPassExpXPathSet)
		{
			logger.debug("Database connection...");
			isLogPassExpXPathSet = setLoginPasswordExpFromDB();
			if (isLogPassExpXPathSet)
			{
				logger.debug("Login=\"" + login +
						"\" , password=\"" + password +
						"\" and expectedXpath=\"" + expectedXPath + 
						"\" are successfully extracted from database");
			}
			else
			{
				logger.error("Error with DB connection: " + field + " is not extracted");
			}
		}
	}
    
    private boolean setLoginPasswordExpFromDB()
    {  
		DBConnectionsExtracting msg_extract = new DBConnectionsExtracting(connection_file);
		List<DBConnection> connections = msg_extract.getConnections();
        String query = "SELECT * FROM testautomation.users WHERE id = " + id;
       try (Connection con = DriverManager.getConnection(connections.get(0).getUrl(), 
    		   connections.get(0).getUser(), connections.get(0).getDBpassword());
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query))
       {
           if(rs.next())
           {
           	login = rs.getString(2);
           	password = rs.getString(3);
           	expectedXPath = rs.getString(4);
           }
           rs.close();
           return true;
       }
       catch (SQLException ex)
       {
    	   logger.error(getClass().getName() + " SQLException: " + ex.getMessage());
    	   return false;
       }
       catch (Exception ex)
       {
    	   logger.error(getClass().getName() + " Exception: " + ex.getMessage());
    	   return false;
    	   
       }
    } 

}
