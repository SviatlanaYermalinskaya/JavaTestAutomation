package model;

public class Post {
	private String access_token;
	private String owner_id;
	private String message;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	 @Override
	 public String toString()
	 {
	        return "post: access_token=\"" + this.access_token +  
	        		"\" owner_id=\"" + this.owner_id +
	                "\" message=\"" + this.message + "\"";
	 }

}
