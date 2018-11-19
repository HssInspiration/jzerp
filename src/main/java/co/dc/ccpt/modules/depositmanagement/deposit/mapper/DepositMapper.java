/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.deposit.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;

/**
 * 保证金信息管理MAPPER接口
 * @author lxh
 * @version 2018-04-11
 */
@MyBatisMapper
public interface DepositMapper extends BaseMapper<Deposit> {
	public String getLastInsertNum();// 获取最新的编号
	
	public List<Deposit> getProNameInDepositByProName(Deposit deposit);//通过项目名称模糊匹配保证金中已有的项目集合
	
	public List<String> getDepositTypeListById(Deposit deposit);//通过项目Id查询出对应的保证金类型集合
	
}