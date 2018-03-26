package cz.zutrasoft.database.model;

import java.io.IOException;
//import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * Class representing an image to be inserted into article. Contains {@link Directory} atribute, i.e. directory
 * the Image belongs to.
 *
 * @author Michal Václavek - added JPA Hibernate
 * @author vitfo - original atributes
 *
 */
@Entity
@Table(name="t_image")
public class Image implements Serializable
{	
	private static final long serialVersionUID = 4976306313068414172L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	  
    @ManyToOne(fetch = FetchType.EAGER) // Many Images to One Directory
    @JoinColumn(name = "directory_id")
    private Directory directory;
	
    @NotNull
	@Column(name = "saved", nullable = false)
	private Timestamp saved;
		
	@NotEmpty
	@Column(name="name", nullable=false)
	private String fileName;
	
	@Lob // Large Object Binary
	@Column(name="data", nullable=false)
	private byte[] imageBytes;
	
	
	public Image(Directory directory, FileUpload uploadedImageFile)
	{
		setDirectory(directory);
		setFileName(uploadedImageFile.getClientFileName());
		setBytes(uploadedImageFile.getBytes());
	}
	
	/**
	 * "Helping" constructor to allow saving image file to the DB during testing.
	 * 
	 * @param directory
	 * @param imageFile
	 */
	public Image(Directory directory, Path imageFile)
	{
		setDirectory(directory);
		setFileName(imageFile.getFileName().toString());

		byte[] data;
		try
		{
			data = Files.readAllBytes(imageFile);
			setBytes(data);
		} catch (IOException e)
		{
			System.err.println("Error loading image from file " + imageFile.getFileName());
		}		
	}
	
	public Image() {}

	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public byte[] getBytes()
	{
		return imageBytes;
	}
	public void setBytes(byte[] bytes)
	{
		this.imageBytes = bytes;
	}

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}

	public Timestamp getSaved()
	{
		return saved;
	}
	public void setSaved(Timestamp saved)
	{
		this.saved = saved;
	}
	
	public Directory getDirectory()
	{
		return directory;
	}
	public void setDirectory(Directory directory)
	{
		this.directory = directory;
	}
	
	
}
