package co.dc.ccpt.modules.depositmanagement.depositstatement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;
import co.dc.ccpt.modules.depositmanagement.depositstatement.mapper.DepositStatementMapper;

/**
 * 保证金出账记录Service
 * @author lxh
 * @version 2018-04-20
 */
@Service
@Transactional(readOnly = true)
public class DepositStatementService extends CrudService<DepositStatementMapper, DepositStatement> {

	@Autowired
	public DepositStatementMapper depositStatementMapper;
	public DepositStatement get(String id) {
		return super.get(id);
	}
	
	public List<DepositStatement> findList(DepositStatement depositStatement) {
		return super.findList(depositStatement);
	}
	
	public Page<DepositStatement> findPage(Page<DepositStatement> page, DepositStatement depositStatement) {
		return super.findPage(page, depositStatement);
	}
	
	@Transactional(readOnly = false)
	public void save(DepositStatement depositStatement) {
		super.save(depositStatement);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositStatement depositStatement) {
		super.delete(depositStatement);
	}
	
	public String getLastInsertNum(){
		String oldNum = depositStatementMapper.getLastInsertNum();//获取最新增加的一个编号
		//设置编号规则：
		String countStr = "";
		Integer count = 0;
		if(oldNum!=null && !oldNum.equals("")){//若不为空
			String subOldNum = oldNum.substring(oldNum.length()-4, oldNum.length());//截取编号最后五个字符
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
	
	//通过审批id获得一个出账记录
	public DepositStatement getDepositStatementByApprovalId(DepositStatement depositStatement){
		return depositStatementMapper.getDepositStatementByApprovalId(depositStatement);
	};
	// 通过项目名称获得对应的出账信息集合
	public List<DepositStatement> getDepositStatementList(DepositStatement depositStatement){
		return depositStatementMapper.getDepositStatementList(depositStatement);
	};
}