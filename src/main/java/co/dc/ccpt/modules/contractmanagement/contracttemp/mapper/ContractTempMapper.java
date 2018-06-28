package co.dc.ccpt.modules.contractmanagement.contracttemp.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.contracttemp.entity.ContractTemp;

/**
 * 合同模板mapper
 * 
 * @author Administrator
 *
 */
@MyBatisMapper
public interface ContractTempMapper extends BaseMapper<ContractTemp> {

	public String getLastContractTempNum();// 获取最后一个创建的合同模板编号
}
