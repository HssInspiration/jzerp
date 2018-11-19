package co.dc.ccpt.modules.contractmanagement.contracttext.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;

/**
 * 合同正文mapper
 * 
 * @author Administrator
 *
 */
@MyBatisMapper
public interface ContractTextMapper extends BaseMapper<ContractText> {
	public List<ContractText> findAll(ContractText contractText);//通过合同id查询所有

	public String getLastProContractNum();//获取最后一个增加的编号

	public ContractText getByContractId(ContractText contractText);// 通过合同id获取对应的正文对象（一个外部合同id对应唯一一个有效的合同正文）
	
}
