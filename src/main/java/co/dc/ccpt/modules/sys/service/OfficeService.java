/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.service.TreeService;
import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.mapper.OfficeMapper;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author dckj
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeMapper, Office> {

	@Autowired
	private OfficeMapper officeMapper;
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		office.setParentIds(office.getParentIds()+"%");
		return officeMapper.findByParentIdsLike(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return officeMapper.getByCode(code);
	}
	
	public List<Office> getChildren(String parentId){
		return officeMapper.getChildren(parentId);
	}
	
	@Transactional(readOnly = true)
	public List<Office> listAllOfficeByName(String name){
		return officeMapper.listAllOfficeByName(name);
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	public String getByName(String officeName) {
		return officeMapper.getByName(officeName);
	}
}
