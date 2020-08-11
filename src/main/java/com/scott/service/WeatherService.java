package com.scott.service;


import com.base.service.BaseService;
import com.scott.dao.WeatherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>JeecgPersonService<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Service("WeatherService")
public class WeatherService<T> extends BaseService<T>{


    @Autowired
    private WeatherDao<T> dao;

    @Override
	public WeatherDao<T> getDao() {
        return dao;
    }

    public List<T> getWeather(){
        return getDao().getWeather();
    };

}
