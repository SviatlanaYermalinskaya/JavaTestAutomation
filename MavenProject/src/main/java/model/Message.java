package model;

public class Message
{
    private String to;
    private String subject;
    private String msgbody;
    
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getMsgbody()
    {
        return msgbody;
    }

    public void setMsgbody(String msgbody)
    {
        this.msgbody = msgbody;
    }

    @Override
    public String toString()
    {
        return "message:  to=" + this.to + " subject=" + this.subject 
                + " msgbody=" + this.msgbody;
    }

}
