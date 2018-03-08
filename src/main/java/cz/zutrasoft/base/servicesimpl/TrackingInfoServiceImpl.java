/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import cz.zutrasoft.base.services.TrackInfoService;
import cz.zutrasoft.database.dao.ITrackingDao;
import cz.zutrasoft.database.daoimpl.TrackingDaoImpl;
import cz.zutrasoft.database.model.TrackInfo;

/**
 * @author Michal
 *
 */
public class TrackingInfoServiceImpl implements TrackInfoService
{

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
