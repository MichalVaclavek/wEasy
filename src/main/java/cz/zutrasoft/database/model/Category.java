package cz.zutrasoft.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class representing category of the article.
 * 	
 * @author Michal Václavek - JPA Hibernate
 * @author vitfo - original atributes
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
