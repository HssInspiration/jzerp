package co.dc.ccpt.modules.constructormanagement.buildingpro.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.constructormanagement.buildingpro.entity.Building;

/**
 * 人员在建项目MAPPER接口
 * @author lxh
 * @version 2018-05-03
 */
@MyBatisMapper
public interface BuildingMapper extends BaseMapper<Building>{
	//通过人员id获取在人员在建项目信息
	public List<Building> getBuildingByCoreStaffId(Building building);
}
