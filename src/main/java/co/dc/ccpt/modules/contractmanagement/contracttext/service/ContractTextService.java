package co.dc.ccpt.modules.contractmanagement.contracttext.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;
import co.dc.ccpt.modules.contractmanagement.contracttext.mapper.ContractTextMapper;

@Service
@Transactional(readOnly = true)
public class ContractTextService extends CrudService<ContractTextMapper, ContractText>{
	@Autowired
	public ContractTextMapper contractTextMapper;
	
	public ContractText get(String id){
		return super.get(id);
	}
	
	public List<ContractText> findList(ContractText contractText){
		return super.findList(contractText);
	}
	
	public Page<ContractText> findPage(Page<ContractText> page, ContractText contractText){
		return super.findPage(page, contractText);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractText contractText){
		 super.save(contractText);
		 //保存之后修改其他的合同正文状态为无效
		 List<ContractText> contractTextList = contractTextMapper.findAll(contractText);
		 if(contractTextList != null && contractTextList.size()>0){
			 contractTextList.remove(contractText);
			 for(ContractText ct : contractTextList){
				 ct.setContractTextStatus(0);
				 super.save(ct);
			 }
		 }
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractText contractText){
		super.delete(contractText);
	}
	
	//设置编号
	public String setContractTextNum(){
		//设置编号规则：HTZW-001
		//1.设置年月
		String contractNum = contractTextMapper.getLastProContractNum();
		System.out.println(contractNum);
		String countStr = "";
		Integer count=0;
		if(contractNum!=null && !contractNum.equals("")){//若不为空
			String lastNum = contractNum.substring(contractNum.length()-3, contractNum.length());//先截取后三位
			if(lastNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
				lastNum = lastNum.substring(1, 3);
				if(lastNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
					lastNum = lastNum.substring(1, 2);
				}
			}
			//2.获取编号的最后一个数而后按条件增加
			count = Integer.parseInt(lastNum)+1;//转成integer类型并加一
			System.out.println(count);
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
		countStr = "HTZW-" + countStr;
		return countStr;
	}

	public ContractText getByContractId(ContractText contractText) {
		return contractTextMapper.getByContractId(contractText);
	}
}
