/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.act.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.act.entity.Act;

/**
 * 审批Mapper接口
 * @author dckj
 * @version 2017-05-16
 */
@MyBatisMapper
public interface ActMapper extends BaseMapper<Act> {

	public int updateProcInsIdByBusinessId(Act act);
	
}
