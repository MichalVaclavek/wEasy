/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import static org.junit.Assert.assertNotNull;

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

/**
 * Trida pro encodovani a dekodovani hesel pro uzivatelska jmena.
 * Prevzato ze Stackoverflow.com
 * 
 * Je potreba osetrit zakodovani kodovaciho hesla. Obvykle se pry
 * dela obfuskaci zdrojoveho kodu, pokud je toto heslo v kodu.
 * Toto heslo lze dat i do konfig. souboru a ten zakodovat ... ?
 * 
 * @author Michal V. podle snippetu na stackoverflow.com
 *
 */
public class EncoderDecoder
{

	private static SecretKeySpec key; 
	
	/**
	 * Pasword pro dekoder primo ve zdrojovem kodu. Neni asi uplne vhodna konstrukce, lepsi je 
	 * nacteni napr. z parametru spustene javy viz nize System.getProperty("password"); Toto se
	 * ale nehodi, pokud spoustim na hostovanem serveru, kde nemam kontrolu nad parametry javy.
	 */
	private static String password;
	
	/**
	 * Konstrukce Singletonu podle https://sourcemaking.com/design_patterns/singleton/java/1
	 * 
	 * Jde pry o asi nejpouzivanejsi a nejjednodussi zpusob, ktery je i Thread safe
	 * 
	 * Vnitrni Trida, ktera vytvori a drzi odkaz na instanci EncoderDecoder, v tomto pripade tedy
	 * SingletonHolder, se pry instancuje (a tim se zaroven vytvari vraceny odkaz na EcoderDecoder), az kdyz
	 * je volana metoda EncoderDecoder init().
	 * 
	 * @author Michal
	 *
	 */	
	private EncoderDecoder()
	{				
		// Nacteni hesla z System.getProperty("password"); tj. java -Dpassword=<password>
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
			key = EncoderDecoder.createSecretKey(password.toCharArray(), salt, iterationCount, keyLength);
		} 
		catch ( GeneralSecurityException e)
		{
			System.out.println("Chyba pri kodovani hesla." + e.getMessage());
		} 
		 		
	}
	
	
		private static class SingletonHolder
		{
			private static final EncoderDecoder SINGLE_INSTANCE = new EncoderDecoder();
		}
	
	/**
	 * Init pro nacteni hesla z System.getProperty("password"); tj. java -Dpassword=<password>
	 * @return singleton instance of the EncoderDecoder
	 */
	public static EncoderDecoder init()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	/**
	 * Init pro pripad zadani hesla primo v kodu. Pokud je ale zaroven zadan i parametr java -Dpassword=<password>
	 * pri spusteni javy, vezme se heslo z tohoto parametru.
	 * 
	 * @param passwd
	 * @return
	 */
	public static EncoderDecoder init(String passwd)
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

    
    //public static String encrypt(String property, SecretKeySpec key)
    //				throws GeneralSecurityException, UnsupportedEncodingException
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
