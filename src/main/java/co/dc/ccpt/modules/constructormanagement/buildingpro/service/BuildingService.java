package co.dc.ccpt.modules.constructormanagement.buildingpro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.constructormanagement.basicinfo.service.CoreStaffService;
import co.dc.ccpt.modules.constructormanagement.buildingpro.entity.Building;
import co.dc.ccpt.modules.constructormanagement.buildingpro.mapper.BuildingMapper;

/**
 * 建造师在建项目管理Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class BuildingService extends CrudService<BuildingMapper, Building> {
	
	@Autowired
	public BuildingMapper buildingMapper;
	
	@Autowired
	private CoreStaffService coreStaffService;
	public Building get(String id) {
		return super.get(id);
	}
	
	public List<Building> findList(Building building) {
		return super.findList(building);
	}
	
	public Page<Building> findPage(Page<Building> page, Building building) {
		return super.findPage(page, building);
	}
	
	@Transactional(readOnly = false)
	public void save(Building building) {
		super.save(building);
	}
	
	@Transactional(readOnly = false)
	public void delete(Building building) {
		super.delete(building);
	}
	
	//通过人员id获取在人员在建项目信息
	public List<Building> getBuildingByCoreStaffId(String id){
		CoreStaff coreStaff = coreStaffService.get(id);
		Building building = new Building();
		if(coreStaff != null){
			building.setCoreStaff(coreStaff);
		}
		return buildingMapper.getBuildingByCoreStaffId(building);
	};
}
