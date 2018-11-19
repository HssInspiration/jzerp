/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositreturn.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.depositmanagement.depositreturn.entity.DepositReturn;

/**
 * 保证金催退MAPPER接口
 * 
 * @author lxh
 * @version 2018-04-20
 */
@MyBatisMapper
public interface DepositReturnMapper extends BaseMapper<DepositReturn> {
	public String getLastInsertNum();// 获取最新的一个num

	public DepositReturn getDepositReturnById(DepositReturn depositReturn);// 通过出账id在催退出中匹配是否已登记

	// 查询未中标的待退保证金个数
	public Integer countDeposit();

	// 查询未中标的待退保证金总金额
	public Double countDepositTotalPrice();
}