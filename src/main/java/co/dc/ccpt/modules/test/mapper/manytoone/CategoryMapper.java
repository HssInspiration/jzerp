/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.manytoone;

import co.dc.ccpt.core.persistence.TreeMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.manytoone.Category;

/**
 * 商品类型MAPPER接口
 * @author lf
 * @version 2017-06-11
 */
@MyBatisMapper
public interface CategoryMapper extends TreeMapper<Category> {
	
}