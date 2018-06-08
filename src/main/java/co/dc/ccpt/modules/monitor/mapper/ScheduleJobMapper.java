/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.monitor.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.monitor.entity.ScheduleJob;

/**
 * 定时任务MAPPER接口
 * @author lgf
 * @version 2017-02-04
 */
@MyBatisMapper
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

	
}