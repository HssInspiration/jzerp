/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.printingmanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
import co.dc.ccpt.modules.printingmanagement.mapper.ContractPrintingMapper;

/**
 * 总包合同用章管理Service
 * @author lxh
 * 
 */
@Service
@Transactional(readOnly = true)
public class ContractPrintingService extends CrudService<ContractPrintingMapper, ContractPrinting> {

	@Autowired
	public ContractPrintingMapper contractPrintingMapper;
	
	public ContractPrinting get(String id) {
		return super.get(id);
	}
	
	public List<ContractPrinting> findList(ContractPrinting contractPrinting) {
		return super.findList(contractPrinting);
	}
	
	public Page<ContractPrinting> findPage(Page<ContractPrinting> page, ContractPrinting contractPrinting) {
		return super.findPage(page, contractPrinting);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractPrinting contractPrinting) {
		super.save(contractPrinting);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractPrinting contractPrinting) {
		super.delete(contractPrinting);
	}
	//设置用章编号
	public String getProContractNum(){
		//设置编号自增规则：ZBYZ+001：
		//1.设置年月
		String proContractNum = contractPrintingMapper.getLastInsertNum();
		String countStr = "";
		Integer count=0;
		if(proContractNum!=null && !proContractNum.equals("")){//若不为空
			String lastNum = proContractNum.substring(proContractNum.length()-3, proContractNum.length());//先截取后三位
			if(lastNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
				lastNum = lastNum.substring(1, 3);
				if(lastNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
					lastNum = lastNum.substring(1, 2);
				}
			}
			//2.获取数据库中的count（*）数而后按条件增加
			count = Integer.parseInt(lastNum)+1;//转成integer类型并加一
		}else{
			count = 1;
		}
		if(count<10){//小于10
			countStr = "00" + count.toString();
		}else if(count>=10 & count<100){//大于等于10，小于100
			countStr = "0" + count.toString();
		}else{
			countStr = count.toString();
		}
		countStr = "ZBYZ-"+countStr;
		return countStr;	
	}

	public List<ContractPrinting> getContractPrintingByProId(ContractPrinting contractPrinting) {
		return contractPrintingMapper.getContractPrintingByProId(contractPrinting);
	}
	@Transactional(readOnly = false)
	public ContractPrinting updateStampStatus(ContractPrinting contractPrinting) {
		String isStamp = contractPrinting.getIsStamp();
		if(StringUtils.isNotBlank(isStamp) && isStamp.equals("1")){//若是否用章选择是则更新时间
			contractPrinting.setPrintDate(new Date());
		}
		return contractPrintingMapper.updateStampStatus(contractPrinting);
	}
}