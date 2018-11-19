package co.dc.ccpt.modules.contractmanagement.procontract.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
/**
 * 分包合同mapper
 * @author Administrator
 * @version 2018-05-14
 */
@MyBatisMapper
public interface SubProContractMapper extends BaseMapper<SubProContract>{
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	public List<SubProContract> getList(SubProContract subProContract);
	
	public String getLastSubProContractNum();//获取最后一个增加的分包合同编号（为设置编号做准备）
	
	public SubProContract getSubProContractBySubProId(SubProContract subProContract);// 通过子项目id获取分包合同对象
	
	public List<SubProContract> getSubProContractList(@Param("subProContractName")String subProContractName);// 根据名称模糊匹配

	public int updateSubProContractStatus(SubProContract subProContract);//更新分包合同的审批状态
	
	public List<SubProContract> getAppointSubProContractByName(SubProContract subProContract);//通过合同名称获得未审批的分包合同集合

	public List<SubProContract> getSubProContractListById(SubProContract subProContract);//获取当前总包下的分包合同

	public List<SubProContract> getSubProContractByName(SubProContract subProContract);//通过合同名称获得未审批或不通过的分包合同集合

}
