package cz.zutrasoft.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * CREATE TABLE t_directory <br>
 * (<br>
 * 		id serial NOT NULL,<br>
 * 		category_id integer,<br>
 * 		name character varying(1000) NOT NULL,<br>
 *
 *  	PRIMARY KEY (id),<br>
 *  	FOREIGN KEY (category_id) REFERENCES t_category (id)<br>
 *	);
 * <p>
 * Tato třída je použita jako "úložiště obrázků" v DB. Jde tedy o identifikaci, která umožňuje obrázky
 * v DB poskládat do skupin "Directory". Při vytváření článku je pak vybrána nejen {@link  Category} článku,
 * ale i {@code Directory}, ve které jsou {@link  Image} pro daný článek. V rámci {@link Category} lze vytvořit více {@code Directory}.
 *	
 * @author Michal Václavek - přidání Hibernate, JPA anotací a rozchození příslušných DAO a Service tříd
 * @author vitfo - původní návrh atributů
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
