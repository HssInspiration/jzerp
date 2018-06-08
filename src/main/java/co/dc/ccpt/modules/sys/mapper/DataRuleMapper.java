/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.sys.entity.DataRule;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 数据权限MAPPER接口
 * @author lgf
 * @version 2017-04-02
 */
@MyBatisMapper
public interface DataRuleMapper extends BaseMapper<DataRule> {

	public void deleteRoleDataRule(DataRule dataRule);
	
	public List<DataRule> findByUserId(User user);
}