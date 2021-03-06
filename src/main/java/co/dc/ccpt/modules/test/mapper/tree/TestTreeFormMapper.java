/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.tree;

import co.dc.ccpt.core.persistence.TreeMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.tree.TestTreeForm;

/**
 * 组织机构MAPPER接口
 * @author liugf
 * @version 2017-06-11
 */
@MyBatisMapper
public interface TestTreeFormMapper extends TreeMapper<TestTreeForm> {
	
}