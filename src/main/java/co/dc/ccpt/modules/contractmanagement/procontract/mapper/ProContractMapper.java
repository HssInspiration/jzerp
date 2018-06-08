package co.dc.ccpt.modules.contractmanagement.procontract.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;

/**
 * 总包合同接口Mapper
 * 
 * @author Administrator
 * @version 2018-05-11
 */
@MyBatisMapper
public interface ProContractMapper extends BaseMapper<ProContract> {
	public String getLastProContractNum();// 获取最后一个创建的总包合同编号

	public ProContract getProContractByProgramId(ProContract proContract);//通过主项目id获取一个总包合同信息
	
	public List<ProContract> getProContractList(@Param("contractName") String proContractName);// 获取已生效或执行的总包合同集合（分包合同中的模糊匹配）
	
	public List<ProContract> getProContractListByName(@Param("contractName") String proContractName);// 获取总包合同集合（总包合同审核中的模糊匹配）
}
