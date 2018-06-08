/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.iim.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.iim.entity.LayGroup;
import co.dc.ccpt.modules.iim.entity.LayGroupUser;
import co.dc.ccpt.modules.iim.mapper.LayGroupMapper;
import co.dc.ccpt.modules.iim.mapper.LayGroupUserMapper;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 群组Service
 * @author lgf
 * @version 2016-08-07
 */
@Service
@Transactional(readOnly = true)
public class LayGroupService extends CrudService<LayGroupMapper, LayGroup> {

	@Autowired
	private LayGroupUserMapper layGroupUserMapper;
	
	public LayGroup get(String id) {
		LayGroup layGroup = super.get(id);
		return layGroup;
	}

	public List<LayGroupUser> getUsersByGroup (LayGroup layGroup){
		return  layGroupUserMapper.findList(new LayGroupUser(layGroup));
	}
	
	public List<LayGroup> findList(LayGroup layGroup) {
		List<LayGroup> layGroupList= new ArrayList<LayGroup>();
		List<LayGroup> list = super.findList(layGroup);
		for(LayGroup u:list){
			layGroupList.add(this.get(u.getId()));
		}
		return layGroupList;
	}
	
	public List<LayGroup> findGroupList(User user) {
		List<LayGroup> layGroupList= new ArrayList<LayGroup>();
		LayGroupUser layGroupUser = new LayGroupUser();
		layGroupUser.setUser(user);
		List<LayGroupUser> list = layGroupUserMapper.findList(layGroupUser);
		for(LayGroupUser u:list){
			layGroupList.add(this.get(u.getGroup().getId()));
		}
		return layGroupList;
	}
	
	public Page<LayGroup> findPage(Page<LayGroup> page, LayGroup layGroup) {
		return super.findPage(page, layGroup);
	}
	
	@Transactional(readOnly = false)
	public void save(LayGroup layGroup) {
		super.save(layGroup);
		for (LayGroupUser layGroupUser : layGroup.getLayGroupUserList()){
			if (layGroupUser.getId() == null){
				continue;
			}
			if (LayGroupUser.DEL_FLAG_NORMAL.equals(layGroupUser.getDelFlag())){
				if (StringUtils.isBlank(layGroupUser.getId())){
					layGroupUser.setGroup(layGroup);
					layGroupUser.preInsert();
					layGroupUserMapper.insert(layGroupUser);
				}else{
					layGroupUser.preUpdate();
					layGroupUserMapper.update(layGroupUser);
				}
			}else{
				layGroupUserMapper.delete(layGroupUser);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(LayGroup layGroup) {
		super.delete(layGroup);
		layGroupUserMapper.delete(new LayGroupUser(layGroup));
	}
	
}