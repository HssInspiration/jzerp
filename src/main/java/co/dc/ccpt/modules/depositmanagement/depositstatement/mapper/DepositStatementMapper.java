package co.dc.ccpt.modules.depositmanagement.depositstatement.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;

/**
 * 保证金出账记录MAPPER接口
 * 
 * @author lxh
 * @version 2018-04-20
 */
@MyBatisMapper
public interface DepositStatementMapper extends BaseMapper<DepositStatement> {
	public String getLastInsertNum();// 获取最新的一个num
	// 通过项目名称获得对应的出账信息集合
	public List<DepositStatement> getDepositStatementList(DepositStatement depositStatement);
	// 通过审批id获得一个出账记录
	public DepositStatement getDepositStatementByApprovalId(DepositStatement depositStatement);
	
}