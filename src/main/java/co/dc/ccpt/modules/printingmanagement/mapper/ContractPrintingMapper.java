package co.dc.ccpt.modules.printingmanagement.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
/**
 * 总包合同用章管理Mapper
 * @author Administrator
 *
 */
@MyBatisMapper
public interface ContractPrintingMapper extends BaseMapper<ContractPrinting> {

	public String getLastInsertNum();//获取最新增加的编号

	public List<ContractPrinting> getContractPrintingByProId(ContractPrinting contractPrinting);//通过合同id获取用章对象id
}
