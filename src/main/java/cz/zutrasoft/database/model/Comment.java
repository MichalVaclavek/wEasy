package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * Class representing comment to the article. One of the atributes is the reference to the {@link Article} the comment relates to.
 * 
 * @author Michal Václavek - added JPA Hibernate
 * @author vitfo - original atributes
 */
@Entity
@Table(name="t_comment")
public class Comment implements Serializable
{
	private static final long serialVersionUID = -4668072504757454270L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
    @NotEmpty
	@Column(name="text", nullable=false)
	private String text;
	
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	private Date created;
	
    
    //@NotEmpty
    //@Column(name = "article_id", nullable = false)
    //@JoinTable(name = "t_article", joinColumns = { @JoinColumn(name = "id") })
	//private int articleId;
	
    /*
    @OneToOne
    @NotEmpty
    //@JoinColumn(name="article_id", nullable=false)
    @JoinTable(name = "t_article", joinColumns = { @JoinColumn(name = "id") })
    */
    //@ManyToOne(cascade=CascadeType.REMOVE) // Dává se i cascade=CascadeType.All , ale při tomto nastavení mi to nefungovalo, Hibernate si stěžoval na
    										// problém s uložením referencovaného article, i když ten byl načtený a uložený v DB a nebylo potřeba ho ukládat
    										// cascade=CascadeType.REMOVE by mělo znamenat, že pouze při mazání referencovaného article se smaže i Comment, který na něj odkazuje
    										//TODO vyzkoušet mazání referencovaného artile ??? a je vubec potreba davat cascade ?
    
    @ManyToOne(fetch = FetchType.EAGER) // jde o pohled ze strany Comment, tedy mnoho Comments k One Article
    @JoinColumn(name = "article_id")
    private Article article;
	
    	
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinTable(name = "t_user", joinColumns = { @JoinColumn(name = "id") })
    @NotNull
    @Column(name = "user_id", nullable = false)
    //@JoinTable(name = "t_user", joinColumns = { @JoinColumn(name = "id") })
	private int userId;
    
    /*
    @OneToMany(fetch = FetchType.LAZY)
  	@JoinTable(name = "t_article", joinColumns = { @JoinColumn(name = "id") })
    private List<Comment> commentsForArticle = new ArrayList<Comment>();
	*/
    
	/**
	 * Defaultni konstruktor
	 */
	public Comment() {}
	
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	@OrderBy("created DESC")
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	
    public Article getArticle()
    {
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}
	
	
	public int getUserId()
	{
		return userId;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	public Date getCreated()
	{
		return created;
	}
	public void setCreated(Date created)
	{
		this.created = created;
	}

	
}
