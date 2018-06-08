/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.CacheUtils;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.sys.entity.DictType;
import co.dc.ccpt.modules.sys.entity.DictValue;
import co.dc.ccpt.modules.sys.mapper.DictTypeMapper;
import co.dc.ccpt.modules.sys.mapper.DictValueMapper;
import co.dc.ccpt.modules.sys.utils.DictUtils;

/**
 * 数据字典Service
 * @author lgf
 * @version 2017-01-16
 */
@Service
@Transactional(readOnly = true)
public class DictTypeService extends CrudService<DictTypeMapper, DictType> {

	@Autowired
	private DictValueMapper dictValueMapper;
	
	public DictType get(String id) {
		DictType dictType = super.get(id);
		dictType.setDictValueList(dictValueMapper.findList(new DictValue(dictType)));
		return dictType;
	}
	
	public DictValue getDictValue(String id) {
		return dictValueMapper.get(id);
	}
	
	public List<DictType> findList(DictType dictType) {
		return super.findList(dictType);
	}
	
	public Page<DictType> findPage(Page<DictType> page, DictType dictType) {
		return super.findPage(page, dictType);
	}
	
	@Transactional(readOnly = false)
	public void save(DictType dictType) {
		super.save(dictType);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void saveDictValue(DictValue dictValue) {
		if (StringUtils.isBlank(dictValue.getId())){
			dictValue.preInsert();
			dictValueMapper.insert(dictValue);
		}else{
			dictValue.preUpdate();
			dictValueMapper.update(dictValue);
		}
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void deleteDictValue(DictValue dictValue) {
		dictValueMapper.delete(dictValue);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(DictType dictType) {
		super.delete(dictType);
		dictValueMapper.delete(new DictValue(dictType));
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	//通过前台传回的证书名称字典值判断具体的字典类型id，而后查询出等级集合
	@Transactional(readOnly = false)
	public List<DictValue> getDictValueListById(DictValue dictValue){
		List<DictValue> dictValueList = new ArrayList<DictValue>();
		DictType dictType = new DictType();
		if(dictValue.getValue().equals("1")){
			dictType.setId("9d15bede96384ffe827fd23872c1d1b6");
			dictValue.setDictType(dictType);
			dictValueList = dictValueMapper.getDictValueListById(dictValue);
		}else if(dictValue.getValue().equals("2")){
			dictType.setId("ca75d16c97d2456091f6d258a2b7f6ac");
			dictValue.setDictType(dictType);
			dictValueList = dictValueMapper.getDictValueListById(dictValue);
		}else if(dictValue.getValue().equals("11")){
			dictType.setId("b5c2582ae39347da8d3620b95e86f4c3");
			dictValue.setDictType(dictType);
			dictValueList = dictValueMapper.getDictValueListById(dictValue);
		}
		return dictValueList;
	};
	
}