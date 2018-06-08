/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.validation;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.validation.TestValidation;

/**
 * 测试校验功能MAPPER接口
 * @author lgf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface TestValidationMapper extends BaseMapper<TestValidation> {
	
}