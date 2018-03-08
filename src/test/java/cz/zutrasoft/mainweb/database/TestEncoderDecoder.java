package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cz.zutrasoft.base.EncoderDecoder;

public class TestEncoderDecoder
{

	private static EncoderDecoder ed;
	
	/**
	 */
	@BeforeClass
	public static void setUpBeforeClass()
	{
		ed = EncoderDecoder.init("12goro.45.7");		
	}


	@Before
	public void setUp() throws Exception
	{
	}



	/**
	 * Testovani zasifrovani a desifrovani hesla
	 */
	@Test
	public void testPasswdEncryptDecrypt()
	{		
		try
		{					
			String originalPassword = "se545t2553ss";
	        System.out.println("Original password: " + originalPassword);
	        
	        String encryptedPassword;
	        
	        encryptedPassword = ed.encrypt(originalPassword);
			
	        assertNotNull(encryptedPassword);
	        
			System.out.println("Encrypted password: " + encryptedPassword);
			String decryptedPassword = ed.decrypt(encryptedPassword);
			
			assertNotNull(decryptedPassword);
			
			assertTrue(originalPassword.equals(decryptedPassword));
			
			System.out.println("Decrypted password: " + decryptedPassword);
		} 
		catch (UnsupportedEncodingException | GeneralSecurityException e)
		{
			System.out.println("Chyba pri kodovani hesla." + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("Chyba pri kodovani hesla." + e.getMessage());
		} 
		       		
	}

}
