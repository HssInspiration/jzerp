package co.dc.ccpt.modules.oa.mapper;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.oa.entity.AttachContract;

/**
 * 总包合同审批mapper
 * 
 * @author Administrator
 *
 */
@MyBatisMapper
public interface AttachContractMapper extends BaseMapper<AttachContract> {

	public AttachContract getByProcInsId(String procInsId);

	public int updateInsId(AttachContract attachContract);

	public int updateSubLeadText(AttachContract attachContract);

	public int updateLeadText(AttachContract attachContract);

	public int updateMainLeadText(AttachContract attachContract);

	// 通过合同id查询一条审批对象--在合同页面查询对应的流程信息
	public AttachContract getByContractId(@Param("contractId") String proContractId);
}
