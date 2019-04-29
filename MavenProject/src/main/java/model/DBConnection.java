package model;

public class DBConnection
{
    private String url;
    private String user;
    private String dbpassword;
    
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getDBpassword()
    {
        return dbpassword;
    }

    public void setDBpassword(String dbpassword)
    {
        this.dbpassword = dbpassword;
    }

    @Override
    public String toString()
    {
        return "DB connection:  url=" + this.url + " user=" + this.user 
                + " password=" + this.dbpassword;
    }

}
