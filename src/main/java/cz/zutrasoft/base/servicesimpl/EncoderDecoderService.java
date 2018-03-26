/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for encoding and decoding passwords (or any String)
 * based on snippet taken from Stackoverflow.com
 * 
 * @author Michal Václavek
 */
public class EncoderDecoderService
{
	static final Logger logger = LoggerFactory.getLogger(EncoderDecoderService.class);
	
	private static SecretKeySpec key; 
	
	/**
	 * Default password for encoding/decoding, if it is not entered by {@link#init(String passwd)} or using System.getProperty("password")
	 * i.e. java -Dpassword=<password> <br> In such case, the source code should be obfuscated.<br>
	 * <br>
	 * System.getProperty("password"); is not suitable in case you use hosting server with no java control.
	 */
	private static String password = "DeafultPasswd.1";
	
	/**
	 * Private constructor because of Singleton class
	 */
	private EncoderDecoderService()
	{				
		// Reading password for encoding/decoding using java -Dpassword=<password>
		// If null, either default password is used or password from init(String passwd)
		String passwdFromParam = System.getProperty("password");
			
		if (passwdFromParam != null)
		{
			password = passwdFromParam;
		}

		// The salt (probably) can be stored along with the encrypted data
		byte[] salt = new String("97834115678a").getBytes();

		// Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
		int iterationCount = 40000;
		// Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
		int keyLength = 128;
    
		try
		{			
			key = EncoderDecoderService.createSecretKey(password.toCharArray(), salt, iterationCount, keyLength);
		} 
		catch ( GeneralSecurityException e)
		{
			logger.error("Encrypting error: " + e.getMessage());
		} 
		 		
	}
		/**
		 * Singleton patern implementation taken from https://sourcemaking.com/design_patterns/singleton/java/1
		 * <br>
		 * The most simple and used method for creating Thread safe Singleton class.
		 * <br>
		 * Inner class to hold just one reference to {@code EncoderDecoderService} class instance. This class is loaded when {@link EncoderDecoderService#init()} is
		 * called for the first time i.e. it's Thread safe.
		 */	
		private static class SingletonHolder
		{
			private static final EncoderDecoderService SINGLE_INSTANCE = new EncoderDecoderService();
		}
	
	/**
	 * If this method is used, then java -Dpassword=<password> is taken as encoding/decoding password, if available.<br>
	 * Otherwise the default password is used.
	 * 
	 * @return singleton instance of the EncoderDecoder
	 */
	public static EncoderDecoderService init()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	/**
	 * If this method is used, the {@code passwd} parameter is taken as encoding/decoding password<br>
	 * If {@code passwd} parameter is null or empty, then java -Dpassword=<password> is taken as encoding/decoding password, if available.
	 * 
	 * @param passwd - encoding/decoding password.
	 * @return singleton instance of the EncoderDecoder
	 */
	public static EncoderDecoderService init(String passwd)
	{				
		if ((passwd == null) || passwd.isEmpty())
		{
			throw new IllegalArgumentException("Password cannot be empty!");
		}
		
		password = passwd;
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
    /**
     * 
     * @param password
     * @param salt
     * @param iterationCount
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
	public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength)
    								throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    
	public String encrypt(String property)	throws GeneralSecurityException, UnsupportedEncodingException
	{
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    
    private static String base64Encode(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
    }


    public String decrypt(String string)	throws GeneralSecurityException, IOException
    {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    
    private static byte[] base64Decode(String property) throws IOException
    {
        return Base64.getDecoder().decode(property);
    }
	
    
}
