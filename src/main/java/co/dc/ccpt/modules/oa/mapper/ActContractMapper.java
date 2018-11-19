package co.dc.ccpt.modules.oa.mapper;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.oa.entity.ActContract;
/**
 * 总包合同审批mapper
 * @author Administrator
 *
 */
@MyBatisMapper
public interface ActContractMapper extends BaseMapper<ActContract> {

	public ActContract getByProcInsId(String procInsId);
	
	public int updateInsId(ActContract actContract);
	
	public int updateLeadText(ActContract actContract);
	
	public int updateMainLeadText(ActContract actContract);

	public ActContract getByContractId(@Param("contractId") String contractId);//通过proContracId获取一条审批对象
}
