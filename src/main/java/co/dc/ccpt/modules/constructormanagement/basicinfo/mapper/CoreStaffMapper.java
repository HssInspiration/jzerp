package co.dc.ccpt.modules.constructormanagement.basicinfo.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;

/**
 * 核心人员MAPPER接口
 * @author lxh
 * @version 2018-05-03
 */
@MyBatisMapper
public interface CoreStaffMapper extends BaseMapper<CoreStaff>{
	public List<CoreStaff> getAllCoreStaffList(CoreStaff coreStaff);//获取所有核心人员集合
	
	public List<CoreStaff> getAppointCoreStaffListByName(CoreStaff coreStaff);//通过指定条件获取人员名称
	
	public CoreStaff getCoreStaffByUserId(CoreStaff coreStaff);//通过用户id查询核心人员信息
}
