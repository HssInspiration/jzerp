/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositreturn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.depositmanagement.depositreturn.entity.DepositReturn;
import co.dc.ccpt.modules.depositmanagement.depositreturn.mapper.DepositReturnMapper;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;

/**
 * 保证金催退Service
 * @author lxh
 * @version 2018-04-20
 */
@Service
@Transactional(readOnly = true)
public class DepositReturnService extends CrudService<DepositReturnMapper, DepositReturn> {
	
	@Autowired
	public DepositReturnMapper depositReturnMapper;
	
	public DepositReturn get(String id) {
		return super.get(id);
	}
	
	public List<DepositReturn> findList(DepositReturn depositReturn) {
		return super.findList(depositReturn);
	}
	
	public Page<DepositReturn> findPage(Page<DepositReturn> page, DepositReturn depositReturn) {
		return super.findPage(page, depositReturn);
	}
	
	@Transactional(readOnly = false)
	public void save(DepositReturn depositReturn) {
		super.save(depositReturn);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositReturn depositReturn) {
		super.delete(depositReturn);
	}
	
	// 设置催退编号：
	public String getLastInsertNum(){
		String oldNum = depositReturnMapper.getLastInsertNum();//获取最新增加的一个编号
		//设置编号规则：
		String countStr = "";
		Integer count = 0;
		if(oldNum!=null && !oldNum.equals("")){//若不为空
			String subOldNum = oldNum.substring(oldNum.length()-4, oldNum.length());//截取编号最后四个字符
			if(subOldNum.substring(0, 1).equals("0")){//若后四位中的前一位是0
				subOldNum = subOldNum.substring(1, 4);
				if(subOldNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
					subOldNum = subOldNum.substring(1, 3);
					if(subOldNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
						subOldNum = subOldNum.substring(1, 2);
					}
				}
			}
			count = Integer.parseInt(subOldNum)+1;
			System.out.println(count);
		}else{
			count = 1;
		}
		if(count<10){//小于10
			countStr = "000" + count.toString();
		}else if(count>=10 & count<100){//大于等于10，小于100
			countStr = "00" + count.toString();
		}else if(count>=100 & count<1000){//大于等于100，小于1000
			countStr = "0" + count.toString();
		}else{
			countStr = count.toString();
		}
		return countStr;
	};
	
	// 通过出账id在催退出中匹配是否已登记
	public DepositReturn getDepositReturnById(DepositReturn depositReturn){
		return depositReturnMapper.getDepositReturnById(depositReturn);
	};
	
	// 查询未中标的待退保证金个数
	public Integer countDeposit(){
		return depositReturnMapper.countDeposit();
	};

	// 查询未中标的待退保证金总金额
	public Double countDepositTotalPrice(){
		return depositReturnMapper.countDepositTotalPrice();
	};
}