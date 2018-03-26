package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *	Class to keep track on user's activity on web i.e. history of visited pages.
 *
 * @author Michal Václavek - added JPA Hibernate
 * @author vitfo - original atributes
 *
 */
@Entity
@Table(name="t_tracking")
public class TrackInfo implements Serializable
{
	private static final long serialVersionUID = -5037260703516787376L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="ip", nullable=false)
	private String ip;
	
	@Column(name="url", nullable=false)
	private String url;
	
	@Column(name="session", nullable=false)
	private String session;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time", nullable=false)
	private Date date;
	
	
	public TrackInfo() {}
	
	public TrackInfo(String ip, String url, String session)
	{
		this.date = new Date();
		this.ip = ip;
		this.url = url;
		this.session = session;
	}

	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getIp()
	{
		return ip;
	}

	public String getUrl()
	{
		return url;
	}

	public String getSession()
	{
		return session;
	}

	public Date getDate()
	{
		return date;
	}
	
}
