/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import cz.zutrasoft.base.services.ITrackInfoService;
import cz.zutrasoft.database.dao.ITrackingDao;
import cz.zutrasoft.database.daoimpl.TrackingDaoImpl;
import cz.zutrasoft.database.model.TrackInfo;

/**
 * @author Michal Václavek
 *
 */
public class TrackingInfoService implements ITrackInfoService
{

	private static class SingletonHolder
	{
        private static final TrackingInfoService SINGLE_INSTANCE = new TrackingInfoService();
    }
	
	/**
	 * @return singleton instance of the TrackingInfoService
	 */
	public static TrackingInfoService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private TrackingInfoService()
	{}
	
	private ITrackingDao trackInfoDao = new TrackingDaoImpl();
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.TrackInfoService#save(cz.zutrasoft.database.model.TrackInfo)
	 */
	@Override
	public void save(TrackInfo info)
	{
		trackInfoDao.save(info);
	}

}
