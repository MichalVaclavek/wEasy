package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;



/**
 * CREATE TABLE t_article<br>
	(<br>
  		id serial NOT NULL,<br>
  		saved timestamptz NOT NULL,<br>
  		category_id integer,<br>
  		directory_id integer,<br>
  		header character varying(1000) NOT NULL,<br>
  		"text" text NOT NULL,<br>

  		PRIMARY KEY (id),<br>
  		FOREIGN KEY (category_id) REFERENCES t_category (id)<br>
	);<br>
	<p>
	Třída reprezentující článek na stránce. Obsahuje všechny potřebné atributy a odkaz na {@code Category} (a {@code Directory} ?) 
 * <p>
 * @author Michal Václavek - přidání Hibernate, JPA anotací a rozchození příslušných DAO a Service tříd
 * @author vitfo - původní návrh atributů
 */
@Entity
@Table(name="t_article")
public class Article implements Serializable
{
	private static final long serialVersionUID = 613669306117338055L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;		
	
	@NotNull // @NotEmpty není vhodné, musel by se definovat i Validator pro třídu Category
	//@ManyToOne(fetch = FetchType.LAZY) 
	@ManyToOne(fetch = FetchType.EAGER) // jde o pohled ze strany Article, tedy mnoho Articles k One Category
    @JoinColumn(name = "category_id")
	private Category category;
	    	
	@NotNull
	@Column(name = "saved", nullable = false)
	private Timestamp saved;
		
    @NotEmpty
	@Column(name="header", nullable=false)
	private String header;
	
    @NotEmpty
	@Column(name="text", nullable=false)
	private String text;
	
   
    //TODO - nechybi dodelat odkaz na Directory ID ???, ktere se vyskytuje ve vytvareni tabulky
    
	/* ======================================================================================= */
	public Article() {}
	
	public Article(Timestamp saved, Category category, String header, String text)
	{
		setSaved(saved);
		setCategory(category);
		setHeader(header);
		setText(text);
	}
	
	public Timestamp getSaved()
	{
		return saved;
	}
	public void setSaved(Timestamp saved)
	{
		this.saved = saved;
	}
	
	public String getHeader()
	{
		return header;
	}
	public void setHeader(String header)
	{
		this.header = header;
	}
	
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Category getCategory()
	{
		return category;
	}
	public void setCategory(Category category)
	{
		this.category = category;
	}
	
	
}
