/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositapproval.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;


/**
 * 保证金审批MAPPER接口
 * @author lxh
 * @version 2018-04-20
 */
@MyBatisMapper
public interface DepositApprovalMapper extends BaseMapper<DepositApproval> {
	
	public String getLastInsertNum();//获取最新的一个num
	
	public List<DepositApproval> getDepositApprovalList(DepositApproval depositApproval);//通过项目名称查询对应的集合
	
	public DepositApproval getDepositApprovalByDepositId(DepositApproval depositApproval);//通过保证金id获取一条审批信息
	
	public List<String> getDepositTypeListByProName(@Param("programName") String programName);//通过项目Id查询出对应的保证金类型集合
}