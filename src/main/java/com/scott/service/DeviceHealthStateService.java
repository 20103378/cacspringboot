package com.scott.service;

import com.base.page.BasePage;
import com.base.service.BaseService;
import com.scott.dao.DeviceHealthStateDao;
import com.scott.entity.*;
import com.scott.page.HistoryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("deviceHealthStateService")
public class DeviceHealthStateService<T> extends BaseService<T> {




	@Autowired
    private DeviceHealthStateDao<T> dao;


	@Override
	public DeviceHealthStateDao<T> getDao() {
		return dao;
	}

	/**
     *YX历史数据查询refname
     *@return
     */
     /*public List<DataEntity> getYXHistoryRefname(HistoryPage page) throws Exception{
         return getDao().getYXHistoryRefname(page);
     }*/
//    /**
//     *YX历史数据查询通过refname
//     *@return
//     */
//     public int YXHistoryCountByrefname(HistoryPage page)throws Exception{
//         return getDao().YXHistoryCountByrefname(page);
//     }
//    /**
//     *YX历史数据查询Byrefname
//     *@return
//     */
//     public List<DataEntity> getYXHistoryByrefname(HistoryPage page) throws Exception{
//         Integer rowCount = YXHistoryCountByrefname(page);
//         page.getPager().setRowCount(rowCount);
//         return getDao().getYXHistoryByrefname(page);//返回给Contriller查询到的结果
//     }
     /**
      * DetailList
      * @param Type
      * @return
      */
     public List<DeviceEntity> getDetailListDevice(String Type){
    	 return getDao().getDetailListDevice(Type);
     }
//     /**
//      * Detaillist
//      * @return
//      * @throws Exception
//      */
//     public List<StomYxEntity> getStomDetail(String equipmentId) throws Exception{
//    	 return getDao().getStomDetail(equipmentId);
//     }
//     public List<Sf6YxEntity> getSf6Detail(String equipmentId) throws Exception{
//    	 return getDao().getSf6Detail(equipmentId);
//     }
//     public List<SmoamYxEntity> getSmoamDetail(String equipmentId) throws Exception{
//    	 return getDao().getSmoamDetail(equipmentId);
//     }
//     public List<ScomYxEntity> getScomDetail(String equipmentId) throws Exception{
//    	 return getDao().getScomDetail(equipmentId);
//     }
//     public List<SpdmYxEntity> getSpdmDetail(String equipmentId) throws Exception{
//    	 return getDao().getSpdmDetail(equipmentId);
//     }
//     public List<WeatherEntity> getWeatherDetail(String equipmentId) throws Exception{
//    	 return getDao().getWeatherDetail(equipmentId);
//     }
//
//     public List<DeviceEntity> getEquipment(String equipmentId) throws Exception{
//    	 return getDao().getEquipment(equipmentId);
//     }
     public List<StomYxEntity> getStomDetail() throws Exception{
    	 return getDao().getStomDetail();
     }
     public List<StomYxEntity> getStomDetailDate() throws Exception{
    	 return getDao().getStomDetailDate();
     }
     public List<Sf6YxEntity> getSf6Detail() throws Exception{
    	 return getDao().getSf6Detail();
     }
     public List<Sf6YxEntity> getSf6DetailDate() throws Exception{
    	 return getDao().getSf6DetailDate();
     }
     public List<SmoamYxEntity> getSmoamDetail() throws Exception{
    	 return getDao().getSmoamDetail();
     }
     public List<SmoamYxEntity> getSmoamDetailDate() throws Exception{
    	 return getDao().getSmoamDetailDate();
     }
     public List<ScomYxEntity> getScomDetail() throws Exception{
    	 return getDao().getScomDetail();
     }
     public List<ScomYxEntity> getScomDetailDate() throws Exception{
    	 return getDao().getScomDetailDate();
     }
     public List<SpdmYxEntity> getSpdmDetail() throws Exception{
    	 return getDao().getSpdmDetail();
     }
     public List<SpdmYxEntity> getSpdmDetailDate() throws Exception{
    	 return getDao().getSpdmDetailDate();
     }
//     public List<WeatherEntity> getWeatherDetail() throws Exception{
//    	 return getDao().getWeatherDetail();
//     }

//     public List<WeatherEntity> getWeatherDetailDate() throws Exception{
//    	 return getDao().getWeatherDetailDate();
//     }
     public List<StomYxEntity> getStomDetailByDate(String _time) throws Exception{
    	 return getDao().getStomDetailByDate(_time);
     }
     public List<StomYxEntity> getStomDetailDateByDate(String _time) throws Exception{
    	 return getDao().getStomDetailDateByDate(_time);
     }
     public List<Sf6YxEntity> getSf6DetailByDate(String _time) throws Exception{
    	 return getDao().getSf6DetailByDate(_time);
     }
     public List<Sf6YxEntity> getSf6DetailDateByDate(String _time) throws Exception{
    	 return getDao().getSf6DetailDateByDate(_time);
     }
     public List<SmoamYxEntity> getSmoamDetailByDate(String _time) throws Exception{
    	 return getDao().getSmoamDetailByDate(_time);
     }
     public List<SmoamYxEntity> getSmoamDetailDateByDate(String _time) throws Exception{
    	 return getDao().getSmoamDetailDateByDate(_time);
     }
     public List<ScomYxEntity> getScomDetailByDate(String _time) throws Exception{
    	 return getDao().getScomDetailByDate(_time);
     }
     public List<ScomYxEntity> getScomDetailDateByDate(String _time) throws Exception{
    	 return getDao().getScomDetailDateByDate(_time);
     }
     public List<SpdmYxEntity> getSpdmDetailByDate(String _time) throws Exception{
    	 return getDao().getSpdmDetailByDate(_time);
     }
     public List<SpdmYxEntity> getSpdmDetailDateByDate(String _time) throws Exception{
    	 return getDao().getSpdmDetailDateByDate(_time);
     }
//     public List<WeatherEntity> getWeatherDetailByDate(String _time) throws Exception{
//    	 return getDao().getWeatherDetailByDate(_time);
//     }
//     public List<WeatherEntity> getWeatherDetailDateByDate(String _time) throws Exception{
//    	 return getDao().getWeatherDetailDateByDate(_time);
//     }
     /**
      * DetailBySpace
      * @param Spaceng
      * @return
      * @throws Exception
      */
     public List<StomYxEntity> getStomDetailBySpace(String Spaceng) throws Exception{
    	 return getDao().getStomDetailBySpace(Spaceng);
     }
     public List<Sf6YxEntity> getSf6DetailBySpace(String Spaceng) throws Exception{
    	 return getDao().getSf6DetailBySpace(Spaceng);
     }
     public List<SmoamYxEntity> getSmoamDetailBySpace(String Spaceng) throws Exception{
    	 return getDao().getSmoamDetailBySpace(Spaceng);
     }

     public List<ScomYxEntity> getScomDetailBySpace(String Spaceng) throws Exception{
    	 return getDao().getScomDetailBySpace(Spaceng);
     }
     public List<SpdmYxEntity> getSpdmDetailBySpace(String Spaceng) throws Exception{
    	 return getDao().getSpdmDetailBySpace(Spaceng);
     }
//     public List<WeatherEntity> getWeatherDetailBySpace(String Spaceng) throws Exception{
//    	 return getDao().getWeatherDetailBySpace(Spaceng);
//     }
     /**
      * DetailDateB
      * @param Spaceng
      * @return
      * @throws Exception
      */
     public List<StomYxEntity> getStomDetailDateBySpace(String Spaceng) throws Exception{
    	 return getDao().getStomDetailDateBySpace(Spaceng);
     }
     public List<Sf6YxEntity> getSf6DetailDateBySpace(String Spaceng) throws Exception{
    	 return getDao().getSf6DetailDateBySpace(Spaceng);
     }
     public List<SmoamYxEntity> getSmoamDetailDateBySpace(String Spaceng) throws Exception{
    	 return getDao().getSmoamDetailDateBySpace(Spaceng);
     }
     public List<ScomYxEntity> getScomDetailDateBySpace(String Spaceng) throws Exception{
    	 return getDao().getScomDetailDateBySpace(Spaceng);
     }
     public List<SpdmYxEntity> getSpdmDetailDateBySpace(String Spaceng) throws Exception{
    	 return getDao().getSpdmDetailDateBySpace(Spaceng);
     }
//     public List<WeatherEntity> getWeatherDetailDateBySpace(String Spaceng) throws Exception{
//    	 return getDao().getWeatherDetailDateBySpace(Spaceng);
//     }


	/**
    *油色谱历史数据查询
	*@return
    */
	public int YSPHistoryCount(BasePage page)throws Exception{
		return getDao().YSPHistoryCount(page);
	}
	public List<stom_dataEntity> getYSPHistory(BasePage page) throws Exception{
		//getDao().deleteStom();
		Integer rowCount = YSPHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getYSPHistory(page);
	}

	public List<stom_dataEntity> exportYSPHistory(BasePage page) throws Exception{
		Integer rowCount = YSPHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportYSPHistory(page);
	}
	public String getDeviceName(String id){
		return getDao().getDeviceName(id);
	}
	/**
	    *SF6历史数据查询
		*@return
	 */
	public int SF6HistoryCount(BasePage page)throws Exception{
		return getDao().SF6HistoryCount(page);
	}
	public List<Sf6_dataEntity> getSF6History(BasePage page) throws Exception{
		Integer rowCount = SF6HistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSF6History(page);
	}

	public List<Sf6_dataEntity> exportSF6History(BasePage page) throws Exception{
		Integer rowCount = SF6HistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportSF6History(page);
	}
	/**
	    *红外测温历史数据查询
		*@return
	 */
	public int InfraredHistoryCount(BasePage page)throws Exception{
		return getDao().InfraredHistoryCount(page);
	}
	public List<Sf6_dataEntity> getInfraredHistory(BasePage page) throws Exception{
		Integer rowCount = InfraredHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getInfraredHistory(page);
	}
	public List<Sf6_dataEntity> exportInfraredHistory(BasePage page) throws Exception{
		Integer rowCount = InfraredHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportInfraredHistory(page);
	}
	/**
	    *避雷器历史数据查询
		*@return
	 */
	public int SMOAMHistoryCount(BasePage page)throws Exception{
		return getDao().SMOAMHistoryCount(page);
	}
	public List<SMOAM_dataEntity> getSMOAMHistory(BasePage page) throws Exception{
		Integer rowCount = SMOAMHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSMOAMHistory(page);
	}
	public List<SMOAM_dataEntity> exportSMOAMHistory(BasePage page) throws Exception{
		Integer rowCount = SMOAMHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportSMOAMHistory(page);
	}
	/**
	    *铁芯历史数据查询
		*@return
	 */
	public int SCOMHistoryCount(BasePage page)throws Exception{
		return getDao().SCOMHistoryCount(page);
	}
	public List<SCOM_dataEntity> getSCOMHistory(BasePage page) throws Exception{
		Integer rowCount = SCOMHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSCOMHistory(page);
	}
	public List<SCOM_dataEntity> exportSCOMHistory(BasePage page) throws Exception{
		Integer rowCount = SCOMHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportSCOMHistory(page);
	}
//	/**
//	    *气象历史数据查询
//		*@return
//	 */
//	public int WeatherHistoryCount(BasePage page)throws Exception{
//		return getDao().WeatherHistoryCount(page);
//	}
//	public List<WeatherEntity> getWeatherHistory(BasePage page) throws Exception{
//		Integer rowCount = WeatherHistoryCount(page);
//		page.getPager().setRowCount(rowCount);
//		return getDao().getWeatherHistory(page);
//	}
//	public List<WeatherEntity> exportWeatherHistory(BasePage page) throws Exception{
//		Integer rowCount = WeatherHistoryCount(page);
//		page.getPager().setRowCount(rowCount);
//		return getDao().exportWeatherHistory(page);
//	}

	/**
	    *工况历史数据查询
		*@return
	 */
	public int SpdmHistoryCount(BasePage page)throws Exception{
		return getDao().SpdmHistoryCount(page);
	}
	public List<Spdm_dataEntity> getSpdmHistoryData(BasePage page) throws Exception{
		Integer rowCount = SpdmHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSpdmHistoryData(page);
	}
	public List<Spdm_dataEntity> exportSpdmHistoryData(BasePage page) throws Exception{
		Integer rowCount = SpdmHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().exportSpdmHistoryData(page);
	}
//    public List<DataEntity> exportYXHistory(BasePage page) throws Exception{
//        return getDao().exportYXHistory(page);
//    }

    public List<DeviceEntity> getDeviceByType(String DeviceType) throws Exception{
		return getDao().getDeviceByType(DeviceType);
	}
	public DeviceEntity getDeviceByDeviceId(String deviceID){
		return getDao().getDeviceByDeviceId(deviceID);
	}
    /**
     * 选择设备类型获取红外测温设备
     */
    public List<DeviceEntity> getInfraredByType() throws Exception{
		return getDao().getInfraredByType();
	}
	/**
	 *  *用于获取曲线图中所需的SF6数据
		*@return
	 */
	public List<Sf6_dataEntity> getSF6Chart_history(Map<String, Object> param) throws Exception{
		return getDao().getSF6Chart_history(param);
	}
	public List<Sf6_dataEntity> getSF6Chart_dealt(Map<String, Object> param) throws Exception{
		return getDao().getSF6Chart_dealt(param);
	}
	/**
	 *  *用于获取曲线图中所需的红外测温数据
		*@return
	 */
	public List<Sf6_dataEntity> getInfraredChart_history(Map<String, Object> param) throws Exception{
		return getDao().getInfraredChart_history(param);
	}
	/**
	 * *用于获取曲线图中所需的油色谱数据
	*@return
	*/
	public List<stom_dataEntity> getStomChart_history(Map<String, Object> param) throws Exception{
		return getDao().getStomChart_history(param);
	}
	public List<stom_dataEntity> getStomChart_dealt(Map<String, Object> param) throws Exception{
		return getDao().getStomChart_dealt(param);
	}
	/**
	 * *用于获取曲线图中所需的避雷器数据
	*@return
	*/
	public List<SMOAM_dataEntity> getSmoamChart_history(Map<String, Object> param) throws Exception{
	return getDao().getSmoamChart_history(param);
	}
	public List<SMOAM_dataEntity> getSmoamChart_dealt(Map<String, Object> param) throws Exception{
	return getDao().getSmoamChart_dealt(param);
	}
	/**
	 * *用于获取曲线图中所需的铁芯数据
	*@return
	*/
	public List<SCOM_dataEntity> getScomChart_history(Map<String, Object> param) throws Exception{
	return getDao().getScomChart_history(param);
	}
	public List<SCOM_dataEntity> getScomChart_dealt(Map<String, Object> param) throws Exception{
	return getDao().getScomChart_dealt(param);
	}
//	/**
//	 * *用于获取曲线图中所需的气象数据
//	*@return
//	*/
//	public List<WeatherEntity> getWeatherChart_history(Map<String, Object> param) throws Exception{
//	return getDao().getWeatherChart_history(param);
//	}
//	public List<WeatherEntity> getWeatherChart_dealt(Map<String, Object> param) throws Exception{
//		return getDao().getWeatherChart_dealt(param);
//	}
	/**
	 * *用于获取曲线图中所需的局放数据
	*@return
	*/
	public List<Spdm_dataEntity> getSpdmChart_history(Map<String, Object> param) throws Exception{
	return getDao().getSpdmChart_history(param);
	}

	/**
	 * *用于LN
	*@return
	*/
	public String getDeviceLN(String id) throws Exception{
		return getDao().getDeviceLN(id);
	}

	//遥信数据
  /**
    *实时数据查询
    *@return
    */
    /*public int getYCDataCount(HistoryPage page)throws Exception{
        return getDao().getYCDataCount(page);
    }*/
    /**
    *
    * @param page
    * @return
    * @throws Exception
    */
    /*public List<DataEntity> getYCData(HistoryPage page) throws Exception{
       Integer rowCount = getYCDataCount(page);
       page.getPager().setRowCount(rowCount);
       return getDao().getYCData(page);
    }*/
    //遥信数据
//    /**
//      *实时数据查询
//      *@return
//      */
//      public int getYCDataCount_yx(HistoryPage page)throws Exception{
//          return getDao().getYCDataCount_yx(page);
//      }
//    /**
//    *
//    * @param page
//    * @return
//    * @throws Exception
//    */
//    public List<DataEntity> getYCData_yx(HistoryPage page) throws Exception{
//       Integer rowCount = getYCDataCount_yx(page);
//       page.getPager().setRowCount(rowCount);
//       List<DataEntity> dataList =getDao().getYCData_yx(page);
//       return dataList;
//    }
    //遥信数据
//    /**
//      *实时数据查询
//      *@return
//      */
//      public int getYCDataCount_yc(HistoryPage page)throws Exception{
//          return getDao().getYCDataCount_yc(page);
//      }
//    /**
//    *
//    * @param page
//    * @return
//    * @throws Exception
//    */
//    public List<DataEntity> getYCData_yc(HistoryPage page) throws Exception{
//       Integer rowCount = getYCDataCount_yc(page);
//       page.getPager().setRowCount(rowCount);
//       return getDao().getYCData_yc(page);
//    }
    /*public String getyxlnldByid(String id) throws Exception{
        return getDao().getyxlnldByid(id);
    }*/
    public String getyxLDLN(String IEC61850LD_LN) throws Exception{
        return getDao().getyxLDLN(IEC61850LD_LN);
    }
    public String getyxDesc(String refname) throws Exception{
        return getDao().getyxDesc(refname);
    }
    public List<DataEntity> getYXData(String IEC61850LD_LN) throws Exception{
        return getDao().getYXData(IEC61850LD_LN);
     }
	public List<StomYxEntity> getStomYx(Map<String, Object> param ) throws Exception{
		return getDao().getStomYx(param);
	}
	public List<StomYxEntity> getStomYxDate(Map<String, Object> param ) throws Exception{
		return getDao().getStomYxDate(param);
	}
	public List<Sf6YxEntity> getInfraredYx(Map<String, Object> param ) throws Exception{
		return getDao().getInfraredYx(param);
	}
	public List<Sf6YxEntity> getInfraredYxDate(Map<String, Object> param ) throws Exception{
		return getDao().getInfraredYxDate(param);
	}
	public List<Sf6YxEntity> getSf6Yx(Map<String, Object> param ) throws Exception{
		return getDao().getSf6Yx(param);
	}
	public List<Sf6YxEntity> getSf6YxDate(Map<String, Object> param ) throws Exception{
		return getDao().getSf6YxDate(param);
	}

	public List<SmoamYxEntity> getSmoamYx(Map<String, Object> param ) throws Exception{
		return getDao().getSmoamYx(param);
	}
	public List<SmoamYxEntity> getSmoamYxDate(Map<String, Object> param ) throws Exception{
		return getDao().getSmoamYxDate(param);
	}
	public List<ScomYxEntity> getScomYx(Map<String, Object> param ) throws Exception{
		return getDao().getScomYx(param);
	}
	public List<ScomYxEntity> getScomYxDate(Map<String, Object> param ) throws Exception{
		return getDao().getScomYxDate(param);
	}
	public List<SpdmYxEntity> getSpdmYxDate(Map<String, Object> param ) throws Exception{
		return getDao().getSpdmYxDate(param);
	}

	//遥信历史数据
	public int StomYXHistoryCount(BasePage page)throws Exception{
		return getDao().StomYXHistoryCount(page);
	}
	public List<YXhistroyData> getstomYXHistoryData(BasePage page) throws Exception{
		Integer rowCount = StomYXHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getstomYXHistoryData(page);
	}

	public int Sf6YXHistoryCount(BasePage page)throws Exception{
		return getDao().Sf6YXHistoryCount(page);
	}
	public List<YXhistroyData> getSf6YXHistoryData(BasePage page) throws Exception{
		Integer rowCount = Sf6YXHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSf6YXHistoryData(page);
	}

	public int SmoamYXHistoryCount(BasePage page)throws Exception{
		return getDao().SmoamYXHistoryCount(page);
	}
	public List<YXhistroyData> getSmoamYXHistoryData(BasePage page) throws Exception{
		Integer rowCount = SmoamYXHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSmoamYXHistoryData(page);
	}

	public int ScomYXHistoryCount(BasePage page)throws Exception{
		return getDao().ScomYXHistoryCount(page);
	}
	public List<YXhistroyData> getScomYXHistoryData(BasePage page) throws Exception{
		Integer rowCount = ScomYXHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getScomYXHistoryData(page);
	}

	public int SpdmYXHistoryCount(BasePage page)throws Exception{
		return getDao().SpdmYXHistoryCount(page);
	}
	public List<YXhistroyData> getSpdmYXHistoryData(BasePage page) throws Exception{
		Integer rowCount = SpdmYXHistoryCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSpdmYXHistoryData(page);
	}
	//套管
	public List<SbushDataEntity> getSbushHistoryData(HistoryPage page) throws Exception {
		Integer rowCount = getSbushHistoryDataCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSbushHistoryData(page);
	}
	private Integer getSbushHistoryDataCount(HistoryPage page) throws Exception {
		return getDao().getSbushHistoryDataCount(page);
	}
	
	//局放
	public List<SbushDataEntity> getSpdcHistoryData(HistoryPage page) {
		Integer rowCount = getSpdcHistoryDataCount(page);
		page.getPager().setRowCount(rowCount);
		return getDao().getSpdcHistoryData(page);
	}

	private Integer getSpdcHistoryDataCount(HistoryPage page) {
		return getDao().getSpdcHistoryDataCount(page);
	}
//	//油色谱实时数据
//	public List<DataEntity> getStomYXData(String id) {
//		return getDao().getStomYXData(id);
//	}

	

	

}
