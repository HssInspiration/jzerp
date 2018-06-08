/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositapproval.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.mapper.DepositApprovalMapper;

/**
 * 保证金审批Service
 * @author lxh
 * @version 2018-04-20
 */
@Service
@Transactional(readOnly = true)
public class DepositApprovalService extends CrudService<DepositApprovalMapper, DepositApproval> {

	@Autowired
	public DepositApprovalMapper depositApprovalMapper;
	
	public DepositApproval get(String id) {
		return super.get(id);
	}
	
	public List<DepositApproval> findList(DepositApproval depositApproval) {
		return super.findList(depositApproval);
	}
	
	public Page<DepositApproval> findPage(Page<DepositApproval> page, DepositApproval depositApproval) {
		return super.findPage(page, depositApproval);
	}
	
	@Transactional(readOnly = false)
	public void save(DepositApproval depositApproval) {
		super.save(depositApproval);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositApproval depositApproval) {
		super.delete(depositApproval);
	}
	
	public String getLastInsertNum(){
		String oldNum = depositApprovalMapper.getLastInsertNum();//获取最新增加的一个编号
		//设置编号规则：
		String countStr = "";
		Integer count = 0;
		if(oldNum!=null && !oldNum.equals("")){//若不为空
			String subOldNum = oldNum.substring(oldNum.length()-5, oldNum.length());//截取编号最后五个字符
			if(subOldNum.substring(0, 1).equals("0")){//若后五位中的前一位是0
				subOldNum = subOldNum.substring(1, 5);
				if(subOldNum.substring(0, 1).equals("0")){//若后四位中的前一位是0
					subOldNum = subOldNum.substring(1, 4);
					if(subOldNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
						subOldNum = subOldNum.substring(1, 3);
						if(subOldNum.substring(0, 1).equals("0")){//若后二位中的前一位是0
							subOldNum = subOldNum.substring(1, 2);
						}
					}
				}
			}
			count = Integer.parseInt(subOldNum)+1;
			System.out.println(count);
		}else{
			count = 1;
		}
		if(count<10){//小于10
			countStr = "0000" + count.toString();
		}else if(count>=10 & count<100){//大于等于10，小于100
			countStr = "000" + count.toString();
		}else if(count>=100 & count<1000){//大于等于100，小于1000
			countStr = "00" + count.toString();
		}else if(count>=1000 & count<10000){//大于等于1000，小于10000
			countStr = "0" + count.toString();
		}else{
			countStr = count.toString();
		}
		return countStr;
	}
	
	public List<DepositApproval> getDepositApprovalList(DepositApproval depositApproval){
		return depositApprovalMapper.getDepositApprovalList(depositApproval);
	}
	
	public DepositApproval getDepositApprovalByDepositId(DepositApproval depositApproval){
		return depositApprovalMapper.getDepositApprovalByDepositId(depositApproval);
	};
	
	public List<String> getDepositTypeListByProName(String programName){
		return depositApprovalMapper.getDepositTypeListByProName(programName);
	} 
	
}