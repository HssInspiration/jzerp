package co.dc.ccpt.modules.coreperson.basicinfo.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;

/**
 * 核心人员MAPPER接口
 * @author lxh
 * @version 2018-05-03
 */
@MyBatisMapper
public interface CorePersonMapper extends BaseMapper<CorePerson>{
	public List<CorePerson> getAllCorePersonList(CorePerson CorePerson);//获取所有核心人员集合
	
	public List<CorePerson> getAppointCorePersonListByName(CorePerson corePerson);//通过指定条件获取人员名称
	
	public CorePerson getCorePersonByUserId(CorePerson corePerson);//通过用户id查询核心人员信息
	
	public CorePerson getCorePersonByIdNum(CorePerson corePerson); // 通过身份证号查询一条信息
}
