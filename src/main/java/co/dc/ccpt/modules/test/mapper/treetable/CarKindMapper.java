/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.treetable;

import co.dc.ccpt.core.persistence.TreeMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.treetable.CarKind;

/**
 * 车系MAPPER接口
 * @author lgf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface CarKindMapper extends TreeMapper<CarKind> {
	
}