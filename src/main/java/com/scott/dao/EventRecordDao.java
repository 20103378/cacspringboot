package com.scott.dao;

import com.base.dao.BaseDao;
import com.base.page.BasePage;
import com.scott.entity.AlarmRecordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>JeecgPersonDao<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Repository
public interface EventRecordDao<T> extends BaseDao<T> {
    /**
     * 分页查询告警事件记录总页数
     *
     * @param page
     * @return
     */
     int AlarmRecordCount(BasePage page);

    /**
     * 分页查询告警事件记录
     *
     * @param page
     * @return
     */
     List<AlarmRecordEntity> getAlarmRecordList(BasePage page);
}

