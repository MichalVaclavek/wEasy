package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;


import cz.zutrasoft.base.EncoderDecoder;
import cz.zutrasoft.base.exceptions.UserSsoNotUniqueException;
import cz.zutrasoft.base.services.UserService;
import cz.zutrasoft.base.servicesimpl.UserServiceImpl;
//import cz.zutrasoft.base.services.UserServiceImpl;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.dao.ICommentDao;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.dao.ImageDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.CommentDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;
import cz.zutrasoft.database.model.UserProfileType;

/**
 * Otestovani DAO vrstvy v {@code cz.zutrasoft.database.daoimpl} vsech typu objektu.
 *  
 * @author Michal Václavek
 *
 */
public class TestDAO
{

	/**
	 * Otestuje operace s Comments jak jsou implementované v DaoImpl
	 */
	//@Test
    public void testCommentsDao()
	{
        // Vytvoření nového komentáře a ulozeni k 
        Comment comment = new Comment();
        comment.setCreated(new Timestamp(25_808_000_004L));
        comment.setText("32145 Další a další Text komentáře k článku 7  666666666666666");
        //comment.setArticleId(6);
        comment.setUserId(3);
              
        IArticleDao articleDao = new ArticleDaoImpl();
		
		// Ziskani clanku k ulozeni komentare
        Article article = articleDao.getArticleById(50);
                
        comment.setArticle(article);
        
        // uložení komentáře do databáze
        ICommentDao comentsDao = new CommentDaoImpl();
        comentsDao.saveComment(comment);
        
                
        // Nalezeni komentaru podle Article
        List<Comment> comments2 = comentsDao.getAllCommentsForArticle(article);
        
        assertNotNull(comments2);
        assertTrue(comments2.size() > 0);
        
        
        // Získání všech komentářů z databáze
        List<Comment> comments = comentsDao.getAllComments();
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
        
        //Smazani jednoho komentare ?  
        Comment coment2 = comentsDao.getById(50);
        
        int numberOfCommentsBeforeDelete = comments.size();
        comentsDao.deleteComment(coment2);
            
        assertTrue(comentsDao.getAllComments().size() == (numberOfCommentsBeforeDelete - 1));  
       
          
        //TODO - vyzkoušení, že se smažou komentáře, pokud se smaže člaánek ???
        /*
        Article article2 = articleDao.getArticleById(3);
        
        articleDao.deleteArticle(article2);
               
        // Jak otestovat ?  asi nejdriv ziskat pocty komentaru k clanku i pocet clanku a pak porovnat po smazani
        */
                
        // Nalezeni komentaru podle userID
        List<Comment> comments1 = comentsDao.getAllCommentsFromUser(3);
        assertNotNull(comments1);
        assertTrue(comments1.size() > 0);
        
    }
	
    @Parameter
    public String knownCategoryName = "Pejsci";
	
	@Test
    public void testArticlesDao()
	{
		IArticleDao articleDao = new ArticleDaoImpl();
						
		// Vytvoreni a ulozeni noveho clanku
        Article article = new Article();
                
        article.setSaved(new Timestamp(800_500_800_004L));
        article.setText("1111222 Další skvělý článek do DB, kategorie " + knownCategoryName);
               
        ICategoryDao categoryDao = new CategoryDaoImpl();
           
        Category cater = categoryDao.getCategoryByName(knownCategoryName);
        article.setCategory(cater);
        
        article.setHeader("11112222 Nadpis - Toto je další článek o ničem v kategorii " + knownCategoryName);

        // uložení článku do DB      
        articleDao.saveArticle(article);
                       
                  
        // Update clanku a jeho nacteni podle id
        String newArticleText = "11112222 Opět Upravíme text článku Nový text 5 ...";
       
        article.setText(newArticleText);
        articleDao.updateArticle(article);
               
        Article a = articleDao.getArticleById(article.getId());
        // Kontrola jestli se text uložil a změnil
        assertNotNull(a);        
        assertTrue(newArticleText.equals(a.getText()));            
        
        // Nacteni vsech articles v Category
        List<Article> articles3 = articleDao.getAllArticlesInCategory(cater);
        assertNotNull(articles3);
        assertTrue(articles3.size() > 0); 
        
        // Nacteni vsech articles
        List<Article> articles = articleDao.getAllArticles();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        //Kontrola, ze clanky maji Category
        
        for (Article art : articles)
        {
        	assertNotNull(art.getCategory().getName());
        }
        
        // Nacteni posledniho clanku a zjisteni, ze se spravne ulozil cas vytvoreni ?? Bude se cist správne posledni článek?
        Article lastArticle = articles.get(articles.size() - 1);
        assertTrue(lastArticle.getSaved().getTime() == 800_500_800_004L);
        
        // Smazani clanku  
        /*
        Article article2 = articleDao.getArticleById(56);
        
        int numberOfArticlesBeforeDelete = articles.size();
        articleDao.deleteArticle(article2);
        
        // Pocet vsech clanku se snizil o 1
        assertTrue(articleDao.getAllArticles().size() == (numberOfArticlesBeforeDelete - 1));  
        */
                 
	}
	
	
    
    //@Parameter
    public String imageFileNameToSaveToDB = "M:\\Download\\Vydry\\vydra-2.jpg";
	
	//@Test
    public void testImagesDao()
	{
		ImageDao imageDao = new ImageDaoImpl();
		
		// Nacteni vsech images v Direcoty
        IDirectoryDao directoryDao = new DirectoryDaoImpl();
	    Directory dir = directoryDao.getFirstDirectoryByName(knownCategoryName);  
		// UNacteni obrazku ze souboru a ulozeni do DB
	    // V real aplikaci se pouzije konstruktor new Image(dir, FileUpload uploadedImageFile);
		Path fileLocation = Paths.get(imageFileNameToSaveToDB);
        Image image = new Image(dir, fileLocation);
                       
        //image.setDirectory(dir);
        
        // uložení obrázku resp.  do DB      
        imageDao.saveImageFile(image);
                       
        Image i = imageDao.getImageById(image.getId());
        // Kontrola jestli se ulozil uložil
        assertNotNull(i);        
         
        // Nacteni vsech images v Directory    
        List<Image> imagesDir = imageDao.getAllImagesInDirectory(dir);
        assertNotNull(imagesDir);
        assertTrue(imagesDir.size() > 0); 
      
        
        // Nacteni vsech images
        List<Image> allImages = imageDao.getAllImages();
        assertNotNull(allImages);
        assertTrue(allImages.size() > 0);
        
        
        // Smazani 1 obrazku   
        /*
        Image image2 = imageDao.getImageById(1);
        
        int numberOfImagesBeforeDelete = allImages.size();
        imageDao.deleteImageById(image2.getId());
        
        //List<Article> articles = dao.getAllArticles().size();
        assertTrue(imageDao.getAllImages().size() == (numberOfImagesBeforeDelete - 1));  
        */
	}
	
	
	
	
	@Test
    public void testTrackInfoDao()
	{
		
	}
	
	
	
}
