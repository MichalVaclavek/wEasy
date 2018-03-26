package cz.zutrasoft.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class to represent a Directory for the Images to be grouped together, especially to allow selection of the Image for the specific
 * web Article.<br>
 * If a new article is to be created, then both {@link  Category} and {@code Directory} has to be selected. The Images for that
 * article are being chossing from that Directory.<br> 
 * <br>
 * Every Directory must be assigned to only one {@code Category} (@ManyToOne relation)<br>	
 * One {@code Category} can contain more than one {@code Directory}
 *
 * @author Michal Václavek - added JPA Hibernate anotation
 * @author vitfo - original atributes
 *
 */
@Entity
@Table(name="t_directory", uniqueConstraints=@UniqueConstraint(columnNames={"name", "category_id"}) )
public class Directory implements Serializable
{
	private static final long serialVersionUID = -5497756559058040097L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
    @NotEmpty
	@Column(name="name", nullable=false)
	private String name;
	   
    @ManyToOne(fetch = FetchType.EAGER) // jde o pohled ze strany Directory, tedy Many Directories k One Category
    @JoinColumn(name = "category_id")
    private Category category;
	
	
	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public Directory()
	{		
	}
	
	public Directory(String directoryName)
	{
		setName(directoryName);
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
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
	
	
}
