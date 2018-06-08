/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.pic;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.pic.TestPic;

/**
 * 图片管理MAPPER接口
 * @author lgf
 * @version 2017-06-19
 */
@MyBatisMapper
public interface TestPicMapper extends BaseMapper<TestPic> {
	
}