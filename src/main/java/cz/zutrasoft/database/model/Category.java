package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * CREATE TABLE t_category
(
  id serial NOT NULL,
  name character varying NOT NULL,

  PRIMARY KEY (id)
);
 * @author Michal
 *
 */

@Entity
@Table(name="t_category")
public class Category implements Serializable
{	
	private static final long serialVersionUID = -3706381276655919313L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    @NotEmpty
	@Column(name="name", nullable=false, unique = true)
	private String name;
	
    //@OneToMany(fetch = FetchType.LAZY)
	//@JoinTable(name = "t_article", joinColumns = { @JoinColumn(name = "category_id")  })
    //@OneToMany(mappedBy="id")
    //@OneToMany(mappedBy="category") // jde o pohled ze strany Category, tedy mnoho Category k Many Articles
    //private List<Article> articlesInCategory;
    
	
    //@OneToMany(mappedBy="id") // jde o pohled ze strany Category, tedy mnoho Category k Many Articles
    /*
	public List<Article> getArticlesInCategory()
	{
		return articlesInCategory;
	}

	public void setArticlesInCategory(List<Article> articlesInCategory)
	{
		this.articlesInCategory = articlesInCategory;
	}
	*/

	public Category() {}
	
	public Category(Long id, String name)
	{
		setId(id);
		setName(name);
	}

	public Category(String categoryName)
	{
		setName(categoryName);
	}

	public Long getId()
	{
		return id;
	}
	public void setId(Long id) 
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	
}
