package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


/**
 * <p>
 * Class representing article on the web pages. Contains all needed attributes, including {@code Category} (and {@code Directory} ?) 
 * </p>
 * @author Michal Václavek - added Hibernate, JPA annotation
 * @author vitfo - original atributes 
 */
@Entity
@Table(name="t_article")
public class Article implements Serializable
{
	private static final long serialVersionUID = 613669306117338055L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;		
	
	@NotNull // if @NotEmpty would be used, then the respective Validator should be defined 
	@ManyToOne(fetch = FetchType.EAGER) // from Article perspective, i.e many Articles to one Category
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
	
   
    //TODO - what about Directory ID ???, which is used in Category table creation
    
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
