/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.sys.entity.DictValue;

/**
 * 数据字典MAPPER接口
 * @author lgf
 * @version 2017-01-16
 */
@MyBatisMapper
public interface DictValueMapper extends BaseMapper<DictValue> {
	//通过前台传回的证书名称字典值判断具体的字典类型id，而后查询出等级集合
	public List<DictValue> getDictValueListById(DictValue dictValue);
	
}