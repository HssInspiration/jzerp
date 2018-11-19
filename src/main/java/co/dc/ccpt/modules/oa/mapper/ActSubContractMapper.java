package co.dc.ccpt.modules.oa.mapper;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
/**
 * 总包合同审批mapper
 * @author Administrator
 *
 */
@MyBatisMapper
public interface ActSubContractMapper extends BaseMapper<ActSubContract> {

	public ActSubContract getByProcInsId(String procInsId);
	
	public int updateInsId(ActSubContract actSubContract);
	
	public int updateSubLeadText(ActSubContract actSubContract);
	
	public int updateLeadText(ActSubContract actSubContract);
	
	public int updateMainLeadText(ActSubContract actSubContract);

	public ActSubContract getByContractId(@Param("contractId") String subContractId);
}
