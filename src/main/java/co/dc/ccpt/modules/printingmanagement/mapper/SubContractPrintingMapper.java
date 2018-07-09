package co.dc.ccpt.modules.printingmanagement.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
import co.dc.ccpt.modules.printingmanagement.entity.SubContractPrinting;
/**
 * 分包合同用章管理Mapper
 * @author Administrator
 *
 */
@MyBatisMapper
public interface SubContractPrintingMapper extends BaseMapper<SubContractPrinting> {

	public String getLastInsertNum();//获取最新增一个记录id

	public List<SubContractPrinting> getSubContractPrintingBySubId(SubContractPrinting subContractPrinting);//通过分包合同id查询出对应的的集合

	public List<SubProContract> getSubProContractListByName();

	public ContractPrinting updateStampStatus(SubContractPrinting subContractPrinting);//更新用章状态
}
