/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.services.CommentService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.base.servicesimpl.CommentServiceImpl;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.ICommentDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CommentDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Comment;

/**
 * Otestuje vsechny metody dulezite pro praci s Comment tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestComment
{	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{		
	}

	private IArticleDao articleDao;
	private ArticleService articleService;
	
	private ICommentDao comentsDao;
	private CommentService comentsService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		articleDao = new ArticleDaoImpl();
		articleService = new ArticleServiceImpl();
		
		comentsDao = new CommentDaoImpl();
		comentsService = new CommentServiceImpl();
	}

	
	@Parameter
	int articleID = 1;
	
	@Parameter
	int userID = 1;

	@Test
    public void testNewCommentDao()
	{
		int numberOfCommentsBeforeCreate = comentsDao.getAllComments().size();
		
        Comment comment = new Comment();
        comment.setCreated(new Timestamp(250_808_000_004L));
        comment.setText("32145 Další a další Text komentáře k článku 7  666666666666666");

        comment.setUserId(userID);
              	
		// Ziskani clanku k ulozeni komentare
        Article article = articleDao.getArticleById(articleID);
                
        comment.setArticle(article);
        
        // uložení komentáře do databáze
        comentsDao.saveComment(comment);  
                
        assertTrue(comentsDao.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 
		
		
    }
	
	@Test
    public void testNewCommentService()
	{
		// Vytvoření nového komentáře a ulozeni k 
		int numberOfCommentsBeforeCreate = comentsService.getAllComments().size();
		
        String commentText = "No to je hrůza!";
        // uložení komentáře do databáze
        comentsService.saveTextAsComment(commentText, userID, articleID);  
                
        assertTrue(comentsService.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 		
	}
    
	@Test
    public void getAllCommentaForArticleDao()
	{		
		// Ziskani clanku k ulozeni komentare
        Article article = articleDao.getArticleById(articleID);
        // Nalezeni komentaru podle Article
        List<Comment> comments2 = comentsDao.getAllCommentsForArticle(article);
        
        assertNotNull(comments2);
        assertTrue(comments2.size() > 0);
        
	}
    
	@Test
    public void getAllCommentaForArticleService()
	{
		// Ziskani clanku k ulozeni komentare
        Article article = articleService.getArticleById(articleID);
        // Nalezeni komentaru podle Article
        List<Comment> comments = comentsService.getAllCommentsForArticleId(article.getId());
        
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
	}
    
    @Test
    public void findCommentFromUserService()
	{       
        // Nalezeni komentaru podle userID
        List<Comment> comments1 = comentsDao.getAllCommentsFromUser(userID);
        assertNotNull(comments1);
        assertTrue(comments1.size() > 0);
	}
        
    
    @Parameter
	int commentID = 1;
    
    @Test
    public void testGet_All_Comments_And_Delete_One_CommentDao()
	{
    	// Získání všech komentářů z databáze
        List<Comment> comments = comentsDao.getAllComments();
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
        
    	//Smazani jednoho komentare ?  
        Comment coment2 = comentsDao.getById(commentID);
        
        int numberOfCommentsBeforeDelete = comments.size();
        comentsDao.deleteComment(coment2);
            
        assertTrue(comentsDao.getAllComments().size() == (numberOfCommentsBeforeDelete - 1));  
	}
    
    @Test
    public void testDeleteCommentService()
	{
    	// VNejdrive se clanek vytvori
		int numberOfCommentsBeforeCreate = comentsService.getAllComments().size();
		
        String commentText = "No to je zase hrůza!";
        // uložení komentáře do databáze
        comentsService.saveTextAsComment(commentText, userID, articleID);  
                
        assertTrue(comentsService.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 
        
        //TODO ziskani odkazu na Comment a jeho smazani pomoci comentsService
	}
    
    @Test
    public void testDeleteCommentsIfArticleDeletedService()
	{
    	fail("Not yet implemented");
    	//TODO - vyzkoušení, že se smažou komentáře, pokud se smaže člaánek ???
        /*
        Article article2 = articleDao.getArticleById(3);
        
        articleDao.deleteArticle(article2);
               
        // Jak otestovat ?  asi nejdriv ziskat pocty komentaru k clanku i pocet clanku a pak porovnat po smazani
        */
	}
    

}
