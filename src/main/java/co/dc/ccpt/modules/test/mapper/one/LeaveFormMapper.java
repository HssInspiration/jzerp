/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.one;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.one.LeaveForm;

/**
 * 请假表单MAPPER接口
 * @author lgf
 * @version 2017-06-11
 */
@MyBatisMapper
public interface LeaveFormMapper extends BaseMapper<LeaveForm> {
	
}