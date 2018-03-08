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

import org.hibernate.validator.constraints.NotEmpty;

/**
 * CREATE TABLE t_tracking<br>
(<br>
  id serial NOT NULL,<br>
  ip character varying (1000),<br>
  url character varying (1000),<br>
  session character varying (100),<br>
  "time" timestamptz,<br>

  PRIMARY KEY (id)<br>
);
<p>
	Mělo by jít o třídu uchovávající informace o historii procházení stránek kvůli možnosti správně se vracet
	při prohlížení v prohlížeči. Jde o objekt podporovaný přímo Wicketem?
<p>
 * @author Michal Václavek - přidání Hibernate, JPA anotací a rozchození příslušných DAO a Service tříd
 * @author vitfo - původní návrh atributů
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
