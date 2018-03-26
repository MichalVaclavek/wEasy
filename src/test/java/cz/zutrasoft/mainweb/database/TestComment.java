/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.IArticleService;
import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.services.ICommentService;
import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.ArticleService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.base.servicesimpl.CommentService;
import cz.zutrasoft.base.servicesimpl.UserService;
import cz.zutrasoft.database.dao.ICommentDao;
import cz.zutrasoft.database.daoimpl.CommentDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;

/**
 * Otestuje vsechny metody dulezite pro praci s Comment tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestComment
{	
	private static IArticleService articleService;
	
	private static ICommentDao comentsDao;
	private static ICommentService commentsService;
	
	private static IUserService userServ;
	
	private static ICategoryService cs;
	
	@Parameter
	private static Article artToComment;
	
	@Parameter
	private static User testUser;
	
	@Parameter
	private static Category categ;

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{	
		//articleService = new ArticleServiceImpl();	
		articleService = ArticleService.getInstance();
		//userServ = new UserServiceImpl();
		userServ = UserService.getInstance();
		//cs = new CategoryServiceImpl();
		cs = CategoryService.getInstance();
		
		comentsDao = new CommentDaoImpl();
		//comentsService = new CommentServiceImpl();
		commentsService = CommentService.getInstance();
		
		// Create user - author of the comments		
		User testUser = new User();
		testUser.setFirstName("Adam");
	    testUser.setLastName("Prvni");
	    testUser.setUsername("test_u" );
	    testUser.setPassword("test");
		
	    String emailAddr = "goroadmin@tokio.jp";
	    testUser.setEmail(emailAddr);
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(1, "USER"));
	    testUser.setUserProfiles(userProfiles);	    
                 
        userServ.saveUser(testUser);	
        	
        // Create and save Category of the Article to be commented        
		categ = new Category("Test_comments_Category");		
		cs.saveCategory(categ);
		
		// Create at least one Article to be able to assign comments to		       
        String articleText = "<h1>Test of comments</h1>\r\n Toto je text testovacího článku v kategorii Test comments Category";
        // Save to DB      
        artToComment = articleService.saveTextAsArticle(articleText, categ);
              
        // Create few comments to the Article for testing by getComments ...
        Comment comment = new Comment();
        comment.setCreated(new Timestamp(25_808_000_004L));
        comment.setText("Testovací koment k testovacímu článku");

        comment.setUserId(testUser.getId());
              	
        comment.setArticle(artToComment);
        
        // Save comment to DB
        comentsDao.saveComment(comment);         		              	
	}
	

	
	@AfterClass
	public static void delete_Articles_And_Category() throws Exception
	{		
		articleService.getAllArticles().forEach(articleService::deleteArticle);	

		cs.deleteCategory(categ);
		
		userServ.deleteUserByUserId(testUser.getId());
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{		
		// At least one Article was created in {@code setUpBeforeClass()}
		testUser = userServ.findByUsername("test_u");
	}
	

	/* ======================== TESTS ================================ */
	
	
	/**
	 * Create and save on Comment using DAO
	 */
	@Test
    public void test_Create_And_Save_New_Comment_DAO()
	{
		int numberOfCommentsBeforeCreate = comentsDao.getAllComments().size();
		
        Comment comment = new Comment();
        comment.setCreated(new Timestamp(250_808_000_004L));
        comment.setText("Další a další Text komentáře k článku");
        comment.setUserId(testUser.getId());              	
        comment.setArticle(artToComment);
        
        // Save comment to DB
        comentsDao.saveComment(comment);  
                
        assertTrue(comentsDao.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 				
    }
	
	/**
	 * Create and save on Comment using {@code CommentsService}
	 */
	@Test
    public void test_New_Comment_Service()
	{		
		int numberOfCommentsBeforeCreate = commentsService.getAllComments().size();
		
		// New comment text
        String commentText = "No to je hrůza!";
        // Save comment to DB
        commentsService.saveTextAsComment(commentText, testUser.getId(), artToComment.getId());  
                
        assertTrue(commentsService.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 		
	}
    
	/*
	 * Tests retrieving all Comments for one Article using DAO
	 */
	@Test
    public void get_All_Comments_For_Article_DAO()
	{				
        List<Comment> comments = comentsDao.getAllCommentsForArticle(artToComment);
        
        assertNotNull(comments);
        assertTrue(comments.size() > 0);        
	}
    
	/*
	 * Tests retrieving all comments for one Article using Service object
	 */
	@Test
    public void get_All_Comments_For_Article_Service()
	{
       // Nalezeni komentaru podle Article
       List<Comment> comments = commentsService.getAllCommentsForArticleId(artToComment.getId());
        
       assertNotNull(comments);
       assertTrue(comments.size() > 0);
	}
    
	/*
	 * Tests retrieving all comments from one user using DAO
	 */
    @Test
    public void find_Comments_From_User_Service()
	{       
        List<Comment> comments = comentsDao.getAllCommentsFromUser(testUser.getId());
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
	}
        
    /**
     * Tests retrieving all comments from DB and deleting one using DAO
     */
    @Test
    public void test_Get_All_Comments_And_Delete_One_Comment_DAO()
	{    	
        List<Comment> comments = comentsDao.getAllComments();
        assertNotNull(comments);
        assertTrue(comments.size() > 0);
        
        Comment comment = comentsDao.getById(comments.get(0).getId());
        
        int numberOfCommentsBeforeDelete = comments.size();
        comentsDao.deleteComment(comment);
            
        assertTrue(comentsDao.getAllComments().size() == (numberOfCommentsBeforeDelete - 1));  
	}
    
    /**
     * Tests creating and deleting one Comment  using Service object
     */
    @Test
    public void test_Delete_Comment_Service()
	{
		int numberOfCommentsBeforeCreate = commentsService.getAllComments().size();
		
        String commentText = "No to je zase hrůza!";
        // Save one comment to DB
        Comment comment = commentsService.saveTextAsComment(commentText, testUser.getId(), artToComment.getId());  
                
        assertTrue(commentsService.getAllComments().size() == (numberOfCommentsBeforeCreate + 1)); 
        
        // Delete one comment from DB
        commentsService.deleteComment(comment);
        
        assertTrue(commentsService.getAllComments().size() == numberOfCommentsBeforeCreate); 
	}
    

}
