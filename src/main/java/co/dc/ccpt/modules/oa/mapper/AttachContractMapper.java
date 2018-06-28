package co.dc.ccpt.modules.oa.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.oa.entity.AttachContract;
/**
 * 总包合同审批mapper
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
}
