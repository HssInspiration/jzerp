package co.dc.ccpt.modules.contractmanagement.procontract.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
/**
 * 分包合同mapper
 * @author Administrator
 * @version 2018-05-14
 */
@MyBatisMapper
public interface SubProContractMapper extends BaseMapper<SubProContract>{
	
	public String getLastSubProContractNum();//获取最后一个增加的分包合同编号（为设置编号做准备）
	
	public SubProContract getSubProContractBySubProId(SubProContract subProContract);// 通过子项目id获取分包合同对象
	
	public List<SubProContract> getSubProContractList(@Param("subProContractName")String subProContractName);// 根据名称模糊匹配

	public int updateSubProContractStatus(SubProContract subProContract);//更新分包合同的审批状态
	
	public List<SubProContract> getAppointSubProContractByName(SubProContract subProContract);//通过合同名称获得未审批的分包合同集合
}