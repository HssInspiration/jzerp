package co.dc.ccpt.modules.contractmanagement.contracttemp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.contracttemp.entity.ContractTemp;
import co.dc.ccpt.modules.contractmanagement.contracttemp.mapper.ContractTempMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.ProContractMapper;
/**
 * 总包项目合同Service
 * @author Administrator
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class ContractTempService extends CrudService<ContractTempMapper, ContractTemp> {
	
	@Autowired
	public ContractTempMapper contractTempMapper;
	
	public ContractTemp get(String id) {
		return super.get(id);
	}
	
	public List<ContractTemp> findList(ContractTemp contractTemp) {
		return super.findList(contractTemp);
	}
	
	public Page<ContractTemp> findPage(Page<ContractTemp> page, ContractTemp contractTemp) {
		return super.findPage(page, contractTemp);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractTemp contractTemp) {
		super.save(contractTemp);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractTemp contractTemp) {
		super.delete(contractTemp);
	}
	
	//设置编号;
	public String setTempNum(){
		//设置编号自增规则：TEMP-00X
		//1.获取最后一个创建的编号
		String contractNum = contractTempMapper.getLastContractTempNum();
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
		countStr = "TEMP-"+countStr;
		return countStr;
	}
	
}
