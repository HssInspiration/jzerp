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

	//通过合同名称获得未审批且为市场投标的合同
	public List<ProContract> getMarketProContractByName(ProContract proContract);

	//通过合同名称获得未审批且为业主指定的合同
	public List<ProContract> getAppointProContractByName(ProContract proContract);
	
	//更新合同审批状态
	public Integer updateProContractStatus(ProContract proContract);
	
	public Integer getTotalProContractCount();//查询所有的总包合同数量

	public Integer getProContractCountEffect();//查询已生效的总包合同数量
	
	public Double getTotalProContractPrice();//查询所有的总包合同总价
	
	public List<ProContract> getEffectList(ProContract proContract);//根据合同生效状态获取合同集合
	
	public List<ProContract> getAppointList(ProContract proContract);//获取业主指定的合同集合
	
	public List<ProContract> getMarketList(ProContract proContract);//获取市场投标的合同集合

	public List<ProContract> getProContractByName(ProContract proContract);//通过合同名称获得未审批、不通过且为市场投标的合同

	public List<ProContract> getAppointContractByName(ProContract proContract);//通过合同名称获得未审批、不通过且为业主指定的合同
	
}
