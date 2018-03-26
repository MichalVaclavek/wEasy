package cz.zutrasoft.mainweb.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestArticle.class, TestCategory.class, TestComment.class, TestContactMessage.class, TestDirectory.class,
				TestImage.class, TestUser.class, TestEncoderDecoder.class })
public class AllObjectsTests
{

}
