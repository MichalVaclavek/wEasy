/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cz.zutrasoft.base.services.IContactMessageService;
import cz.zutrasoft.base.servicesimpl.ContactMessageService;

/**
 * * Otestuje vsechny metody dulezite pro praci s ContactMessage tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestContactMessage
{

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	private static IContactMessageService cmServ;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//cmServ = new ContactMessageServiceImpl();
		cmServ = ContactMessageService.getInstance();
	}


	@Test
	public void test_Save_New_ContactMessage()
	{
		int numOfCmBeforeAdd = cmServ.getAllMessages().size();
		cmServ.saveContactMessage("Sandokan", "sandokan@moppracem.id", "Dobrý den, jsem tygr z Mopračemu, píšete tu bláboly.");
		
		assertTrue(cmServ.getAllMessages().size() == (numOfCmBeforeAdd + 1));		
	}
	

}
