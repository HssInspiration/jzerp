/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.printingmanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
import co.dc.ccpt.modules.printingmanagement.entity.SubContractPrinting;
import co.dc.ccpt.modules.printingmanagement.mapper.SubContractPrintingMapper;

/**
 * 分包合同用章管理Service
 * @author lxh
 * 
 */
@Service
@Transactional(readOnly = true)
public class SubContractPrintingService extends CrudService<SubContractPrintingMapper, SubContractPrinting> {

	@Autowired
	public SubContractPrintingMapper subContractPrintingMapper;
	
	public SubContractPrinting get(String id) {
		return super.get(id);
	}
	
	public List<SubContractPrinting> findList(SubContractPrinting subContractPrinting) {
		return super.findList(subContractPrinting);
	}
	
	public Page<SubContractPrinting> findPage(Page<SubContractPrinting> page, SubContractPrinting subContractPrinting) {
		return super.findPage(page, subContractPrinting);
	}
	
	@Transactional(readOnly = false)
	public void save(SubContractPrinting subContractPrinting) {
		super.save(subContractPrinting);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubContractPrinting subContractPrinting) {
		super.delete(subContractPrinting);
	}

	public String getSubContractPrintingNum() {
		//设置编号自增规则：FBYZ+001
		//1.设置年月
		String subProContractNum = subContractPrintingMapper.getLastInsertNum();
		String countStr = "";
		Integer count=0;
		if(subProContractNum != null && !subProContractNum.equals("")){//若不为空
			String lastNum = subProContractNum.substring(subProContractNum.length()-3, subProContractNum.length());//先截取后三位
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
		countStr = "FBYZ-"+countStr;
		return countStr;
	}

	public List<SubContractPrinting> getSubContractPrintingBySubId(SubContractPrinting subContractPrinting) {
		return subContractPrintingMapper.getSubContractPrintingBySubId(subContractPrinting);
	}
	
	@Transactional(readOnly = false)
	public ContractPrinting updateStampStatus(SubContractPrinting subContractPrinting) {
		subContractPrinting.setPrintDate(new Date());
		return subContractPrintingMapper.updateStampStatus(subContractPrinting);
	}
}