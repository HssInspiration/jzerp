/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.manytoone;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.manytoone.Goods;

/**
 * 商品MAPPER接口
 * @author liugf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface GoodsMapper extends BaseMapper<Goods> {
	
}