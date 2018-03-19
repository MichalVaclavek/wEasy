/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import cz.zutrasoft.base.services.TrackInfoService;
import cz.zutrasoft.database.dao.ITrackingDao;
import cz.zutrasoft.database.daoimpl.TrackingDaoImpl;
import cz.zutrasoft.database.model.TrackInfo;

/**
 * @author Michal Václavek
 *
 */
public class TrackingInfoServiceImpl implements TrackInfoService
{

	private static class SingletonHolder
	{
        private static final TrackingInfoServiceImpl SINGLE_INSTANCE = new TrackingInfoServiceImpl();
    }
	
	/**
	 * @return singleton instance of the ImageServiceImpl
	 */
	public static TrackingInfoServiceImpl getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private TrackingInfoServiceImpl()
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
