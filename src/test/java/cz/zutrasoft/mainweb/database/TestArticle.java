package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

/**
 * Otestuje vsechny metody dulezite pro praci s Article tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestArticle
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}


	private IArticleDao articleDao;
	private ArticleService articleService;
	
	@Before
	public void setUp() throws Exception
	{
		articleDao = new ArticleDaoImpl();
		articleService = new ArticleServiceImpl();
	}


	@Parameter
    public String knownCategoryName = "Veverky";
	
	@Test
    public void testCreateNewArticleDao()
	{						
		// Vytvoreni a ulozeni noveho clanku
        Article article = new Article();
                
        article.setSaved(new Timestamp(2_800_500_800_004L));
        article.setText("1111222 Další skvělý článek do DB, kategorie " + knownCategoryName);
               
        ICategoryDao categoryDao = new CategoryDaoImpl();
           
        Category cater = categoryDao.getCategoryByName(knownCategoryName);
        article.setCategory(cater);
        
        article.setHeader("11112222 Header - Toto je další článek o ničem v kategorii " + knownCategoryName);

        // uložení článku do DB      
        articleDao.saveArticle(article);
                              
        List<Article> articles = articleDao.getAllArticles();
        // Nacteni posledniho clanku a zjisteni, ze se spravne ulozil cas vytvoreni ?? Bude se cist správne posledni článek?
        Article lastArticle = articles.get(articles.size() - 1);
        assertTrue(lastArticle.getSaved().getTime() == 2_800_500_800_004L);
        
        articleDao.deleteArticle(article);
                        
	}
	
	@Test
    public void testLoadAllArticlesInCategoryDao()
	{
		// Nacteni vsech articles v Category
		ICategoryDao categoryDao = new CategoryDaoImpl();
        
        Category cater = categoryDao.getCategoryByName(knownCategoryName);
		
        List<Article> articles3 = articleDao.getAllArticlesInCategory(cater);
        assertNotNull(articles3);
        assertTrue(articles3.size() > 0); 
	}
	
	@Test
    public void testLoadAllArticlesInCategoryService()
	{
		CategoryService categoryService = new CategoryServiceImpl();
        
        Category cater = categoryService .getCategoryByName(knownCategoryName);
		
		// Nacteni vsech articles v Category
        List<Article> articles3 = articleService.getAllArticlesInCategory(cater.getId());
        assertNotNull(articles3);
        assertTrue(articles3.size() > 0); 
	}
	
	@Test
    public void testLoadAllArticlesDao()
	{		        
		// Nacteni vsech articles
        List<Article> articles = articleDao.getAllArticles();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        
        //Kontrola, ze clanky maji Category
        for (Article art : articles)
        {
        	assertNotNull(art.getCategory().getName());
        }
	}
	
	@Test
    public void testLoadAllArticlesService()
	{
		List<Article> articles = articleService.getAllArticles();
        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        
        //Kontrola, ze clanky maji Category
        for (Article art : articles)
        {
        	assertNotNull(art.getCategory().getName());
        }
	}
	
	@Test
    public void testCreateNewArticleService()
	{
		CategoryService categoryService = new CategoryServiceImpl();						               
           
        Category categ = categoryService.getCategoryByName(knownCategoryName);
       
        String articleText = "<h1>New Header - Další skvělý článek do DB.</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + knownCategoryName;

        // uložení článku do DB      
        articleService.saveTextAsArticle(articleText, categ);
                 
        
        List<Article> articles = articleService.getAllArticles();
        
        // Nacteni posledniho clanku a zjisteni, ze se vytvoril a ulozil i cas vytvoreni ...
        Article lastArticle = articles.get(articles.size() - 1);
        assertNotNull(lastArticle.getSaved().getTime());
        // Z textu se vytvoril i Header
        assertNotNull(lastArticle.getHeader());
        // Spravne se priradila kategorie
        assertNotNull(lastArticle.getCategory());
        assertTrue(lastArticle.getCategory().getName().equalsIgnoreCase(knownCategoryName));
        
        articleService.deleteArticle(lastArticle);
                                
	}
	
	
	@Test
    public void testUpdateArticleDao()
	{
		// Vytvoreni clanku
		// Vytvoreni a ulozeni noveho clanku
        Article article = new Article();
                
        article.setSaved(new Timestamp(2_800_500_800_004L));
        article.setText("1111222 Další skvělý článek do DB, kategorie " + knownCategoryName);
               
        ICategoryDao categoryDao = new CategoryDaoImpl();
           
        Category cater = categoryDao.getCategoryByName(knownCategoryName);
        article.setCategory(cater);
        
        article.setHeader("11112222 Header - Toto je další článek o ničem v kategorii " + knownCategoryName);

        // uložení článku do DB      
        articleDao.saveArticle(article);
		
		// Update clanku
		
		List<Article> articles = articleDao.getAllArticles();
        
        // Nacteni posledniho clanku ze seznamu, ktery byl nacten z DB
        Article lastArticle = articles.get(articles.size() - 1);
        // Update clanku a jeho nacteni podle id
        String newArticleText = "11112222 Opět Upravíme text a header článku pomocí DAO /r/n Nový text 5 ...";
        String newArticleHeader = "Upravíme i Header článku pomocí DAO";
     
        lastArticle.setText(newArticleText);
        lastArticle.setHeader(newArticleHeader);
        articleDao.updateArticle(lastArticle);
             
        Article a = articleDao.getArticleById(lastArticle.getId());
        // Kontrola jestli se text a Header uložil a změnil
        assertNotNull(a);        
        assertTrue(newArticleText.equals(a.getText()));
        assertTrue(newArticleHeader.equals(a.getHeader())); 
        
        articleService.deleteArticle(a);

	}
	
	@Test
    public void testUpdateArticleService()
	{
		List<Article> articles = articleService.getAllArticles();
        
        // Nacteni posledniho clanku ze seznamu, ktery byl nacten z DB
        Article lastArticle = articles.get(articles.size() - 1);
        // Update clanku a jeho nacteni podle id

        String header = "New Service Header - Další skvělý článek do DB.";
        //String newArticleText = "Opět Upravíme text článku pomocí Service.";
        //String newArticleHeader = "Upravíme i Header článku pomocí Service";
        String newArticleText = "<h1>" + header + "</h1>\r\n Toto je text testovacího článku v kategorii uložený pomocí ArticleServiceImpl " + knownCategoryName;

     
        lastArticle.setText(newArticleText);
        //lastArticle.setHeader(newArticleHeader);
        articleService.updateArticle(lastArticle);
             
        Article a = articleService.getArticleById(lastArticle.getId());
        // Kontrola jestli se text uložil a změnil
        assertNotNull(a);        
        assertTrue(newArticleText.equals(a.getText()));
        // Updatoval se i Header?
        assertTrue(header.equals(a.getHeader())); 
        
        articleService.deleteArticle(a);
	}
	
	
	//@Test
    public void testDeleteArticleDao()
	{
		 // Smazani clanku  
		List<Article> articles = articleDao.getAllArticles();
        
        // Nacteni posledniho clanku ze seznamu, ktery byl nacten z DB
        Article lastArticle = articles.get(articles.size() - 1);
        
        int numberOfArticlesBeforeDelete = articles.size();
        
        articleDao.deleteArticle(lastArticle);
        
        // Pocet vsech clanku se snizil o 1
        assertTrue(articleDao.getAllArticles().size() == (numberOfArticlesBeforeDelete - 1));  
        
	}
	
	//@Test
    public void testDeleteArticleService()
	{
		List<Article> articles = articleService.getAllArticles();
        
        // Nacteni posledniho clanku ze seznamu, ktery byl nacten z DB
        Article lastArticle = articles.get(articles.size() - 1);
        
        int numberOfArticlesBeforeDelete = articles.size();
        
        articleService.deleteArticle(lastArticle);
        
        // Pocet vsech clanku se snizil o 1
        assertTrue(articleService.getAllArticles().size() == (numberOfArticlesBeforeDelete - 1));  
	}
	

}
