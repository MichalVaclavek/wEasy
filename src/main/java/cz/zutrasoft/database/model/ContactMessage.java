/**
 * 
 */
package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Class representing a message user can sent to web admin via respective UI form.
 * 
 * @author Michal Václavek
 */
@Entity
@Table(name="t_contact_message")
public class ContactMessage implements Serializable
{
	private static final long serialVersionUID = -2996183922875213247L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
    @Column(name="author_name", length = 60, nullable=false)
    private String userName;
	  
	@NotNull
	@Column(name = "created_at", nullable = false) 
	private Timestamp createdTime;
	
    @Column(name="email", length = 60, nullable=false)
    private String email;
	 
    @Column(name="message", length = 900, nullable=false)
    private String textOfMessage;

        
  	public ContactMessage()
  	{}
    
    public ContactMessage(String userName, String email, Timestamp createdTime, String messageText)
	{
    	this.userName = userName;
		this.createdTime = createdTime;
		this.email = email;
		this.textOfMessage = messageText;
	}
    
	
    public Timestamp getCreatedTime()
	{
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime)
    {
        this.createdTime = createdTime;
    }
    
    public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getTextOfMessage()
    {
        return textOfMessage;
    }
    public void setTextOfMessage(String textOfMessage)
    {
        this.textOfMessage = textOfMessage;
    }
    
    
}
