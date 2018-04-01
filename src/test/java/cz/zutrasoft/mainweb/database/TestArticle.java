package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.IArticleService;
import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.servicesimpl.ArticleService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

/** 
 *	Performes all the important tests for operationg Articles - DAO (package {@code cz.zutrasoft.database.daoimpl}) and
 *  Service level (package {@code cz.zutrasoft.base.servicesimpl}) methods for basic CRUD operations.
 * 
 * @author Michal Václavek
 *
 */
public class TestArticle
{

	@Parameter
    public static String articleCategoryName = "Veverky";
		
	private static IArticleDao articleDao;
	private static IArticleService articleService;
	private static ICategoryService categoryService;
	private static Category testCategory;

	/**
	 * Crates Articles for tetsing methods retrieving Lists of Articles
	 * Also creates DAO and Service objects for all tests
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void create_Articles() throws Exception
	{
		// First create DAO and Service objects for all tests
		articleDao = new ArticleDaoImpl();
		//articleService = new ArticleServiceImpl();
		articleService = ArticleService.getInstance();
		
		// Create few articles to be counted by getArticles() methods
		//categoryService = new CategoryServiceImpl();
		categoryService = CategoryService.getInstance();
		testCategory = new Category(articleCategoryName);		
		categoryService.saveCategory(testCategory);
        
        //Category categ = categoryService.getCategoryByName(articleCategoryName);
       
        String article1Text = "<h1>New Header - Další skvělý článek do DB.</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + articleCategoryName;
        // Save to DB      
        articleService.saveTextAsArticle(article1Text, testCategory);
        
        String article2Text = "<h1>New Header - Další skvělý článek do DB.</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + articleCategoryName;
        // Save to DB      
        articleService.saveTextAsArticle(article2Text, testCategory);
		
	}
	
	@AfterClass
	public static void delete_Articles() throws Exception
	{		
		articleService.getAllArticles().forEach(articleService::deleteArticle);	
		categoryService.deleteCategory(testCategory);
	}

	
	/** 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}
	
	/* ================== START OF TESTING ============ */
	
	/**
	 * Tests creation, saving, reading and deletition of the Article using {@code ArticleDaoImpl} object
	 */
	@Test
    public void test_Create_New_Article_Dao()
	{						
		// Create Article
        Article article = new Article();
                
        article.setSaved(new Timestamp(2_800_500_800_004L));
        article.setText("Další skvělý článek do DB, kategorie " + articleCategoryName);
               
        ICategoryDao categoryDao = new CategoryDaoImpl();
           
        Category cater = categoryDao.getCategoryByName(articleCategoryName);
        article.setCategory(cater);        
        article.setHeader("Header - Toto je další článek o ničem v kategorii " + articleCategoryName);

        // Save Article to DB      
        articleDao.saveArticle(article);
                                     
        // Read saved Article again from DB
        Article lastArticle = articleDao.getArticleById(article.getId());
        
        assertTrue(lastArticle.getSaved().getTime() == 2_800_500_800_004L);
              
        int numberOfArticlesBeforeDelete = articleDao.getAllArticles().size();        
        articleDao.deleteArticle(lastArticle);      
        // Number of saved Articles decreased after deletition of one Article
        assertTrue(articleDao.getAllArticles().size() == (numberOfArticlesBeforeDelete - 1)); 
                        
	}
	
	/**
	 * Tests loading all articles related to one {@code Category} using {@code ArticleDaoImpl} object
	 */
	@Test
    public void test_Load_All_Articles_In_Category_Dao()
	{
		// Load all Articles in one Category
		ICategoryDao categoryDao = new CategoryDaoImpl();
        
        Category cater = categoryDao.getCategoryByName(articleCategoryName);
		
        List<Article> articles3 = articleDao.getAllArticlesInCategory(cater);
        assertNotNull(articles3);
        assertTrue(articles3.size() > 0); 
	}
	
	/**
	 * Tests loading all articles related to one {@code Category} using {@code ArticleServiceImpl} object
	 */
	@Test
    public void test_Load_All_Articles_In_Category_Service()
	{      
        Category cater = categoryService .getCategoryByName(articleCategoryName);
		
        // Load all Articles in one Category
        List<Article> articles3 = articleService.getAllArticlesInCategory(cater.getId());
        assertNotNull(articles3);
        assertTrue(articles3.size() > 0); 
	}
	
	@Test
    public void test_Load_All_Articles_Dao()
	{		        
		// Load all Articles
        List<Article> articles = articleDao.getAllArticles();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        
        // Check that every Article has a not-null Category
        for (Article art : articles)
        {
        	assertNotNull(art.getCategory().getName());
        }
	}
	
	@Test
    public void test_Load_All_Articles_Service()
	{
		List<Article> articles = articleService.getAllArticles();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        
        // Check that every Article has a not-null Category
        for (Article art : articles)
        {
        	assertNotNull(art.getCategory().getName());
        }
	}
	
	
	/**
	 * Tests creation, saving, reading and deletition of the Article using Service object
	 */
	@Test
    public void testCreateNewArticleService()
	{
		//CategoryService categoryService = new CategoryServiceImpl();
		ICategoryService categoryService = CategoryService.getInstance();
           
        Category categ = categoryService.getCategoryByName(articleCategoryName);
       
        String articleText = "<h1>New Header - Další skvělý článek do DB.</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + articleCategoryName;

        // Save to DB      
        articleService.saveTextAsArticle(articleText, categ);
                 
        // Read all Articles
        List<Article> articles = articleService.getAllArticles();
        
        // Get last saved Article and check its creation DateTime, its Header and its Category 
        Article lastArticle = articles.get(articles.size() - 1);
        assertNotNull(lastArticle.getSaved().getTime());
        // Z textu se vytvoril i Header
        assertNotNull(lastArticle.getHeader());
        // Spravne se priradila kategorie
        assertNotNull(lastArticle.getCategory());
        assertTrue(lastArticle.getCategory().getName().equalsIgnoreCase(articleCategoryName));
        
        // Delete Article
        articleService.deleteArticle(lastArticle);
                                
	}
	
	/**
	 * Tests updating of the Articels functions of the DAO {@code ArticleDaoImpl} object 
	 */
	@Test
    public void test_Update_Article_Dao()
	{
		// Create and save new Article
        Article article = new Article();
                
        article.setSaved(new Timestamp(2_800_500_800_004L));
        article.setText("Další skvělý článek do DB, kategorie " + articleCategoryName);
               
        ICategoryDao categoryDao = new CategoryDaoImpl();
           
        Category cater = categoryDao.getCategoryByName(articleCategoryName);
        article.setCategory(cater);
        
        article.setHeader("Header - Toto je další článek o ničem v kategorii " + articleCategoryName);
    
        articleDao.saveArticle(article);
		       
		// Update Article		
		List<Article> articles = articleDao.getAllArticles();
        
        // Load last saved Article in the DB - does this solution really loads last saved article?
		// Is other words, is this Article the previously created and saved one 
        Article lastArticle = articles.get(articles.size() - 1);
        // Updates Text and Header of the last saved Article
        String newArticleText = "Opět Upravíme text a header článku pomocí DAO /r/n Nový text 5 ...";
        String newArticleHeader = "Upravíme i Header článku pomocí DAO";
     
        lastArticle.setText(newArticleText);
        lastArticle.setHeader(newArticleHeader);
        articleDao.updateArticle(lastArticle);
             
        // Check if Text and header are updated
        Article a = articleDao.getArticleById(lastArticle.getId());

        assertNotNull(a);        
        assertTrue(newArticleText.equals(a.getText()));
        assertTrue(newArticleHeader.equals(a.getHeader())); 
        
        articleService.deleteArticle(a);
	}
	
	@Test
    public void test_Update_Article_Service()
	{
		List<Article> articles = articleService.getAllArticles();
        
        // Read one Article from DB
        Article lastArticle = articles.get(articles.size() - 1);
        
        // Update Article's Text and Header
        String header = "New Service Header - Další skvělý článek do DB.";
        String newArticleText = "<h1>" + header + "</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + articleCategoryName;
     
        lastArticle.setText(newArticleText);

        articleService.updateArticle(lastArticle);
             
        // Read updated Article from DB again
        Article articleReadedAfterSave = articleService.getArticleById(lastArticle.getId());
        // Check if Text and header are updated
        assertNotNull(articleReadedAfterSave);        
        assertTrue(newArticleText.equals(articleReadedAfterSave.getText()));
        assertTrue(header.equals(articleReadedAfterSave.getHeader())); 
        
        articleService.deleteArticle(articleReadedAfterSave);
	}
	

}
