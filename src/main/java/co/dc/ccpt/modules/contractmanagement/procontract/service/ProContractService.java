package co.dc.ccpt.modules.contractmanagement.procontract.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.DateUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.ProContractMapper;
import co.dc.ccpt.modules.oa.entity.ActContract;
import co.dc.ccpt.modules.oa.entity.AttachContract;
/**
 * 总包项目合同Service
 * @author Administrator
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class ProContractService extends CrudService<ProContractMapper, ProContract> {
	
	@Autowired
	public ProContractMapper proContractMapper;
	
	public ProContract get(String id) {
		return super.get(id);
	}
	
	public List<ProContract> findList(ProContract ProContract) {
		return super.findList(ProContract);
	}
	
	public Page<ProContract> findPage(Page<ProContract> page, ProContract ProContract) {
		return super.findPage(page, ProContract);
	}
	
	@Transactional(readOnly = false)
	public void save(ProContract ProContract) {
		super.save(ProContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProContract ProContract) {
		super.delete(ProContract);
	}
	
	//设置总包合同编号;
	public String setProContractNum(){
		//设置编号自增规则：ZBHT-+年份： 2018001
		//1.设置年月
		Date date = new Date();
		String year = new SimpleDateFormat("yyyy").format(date);
		String contractNum = proContractMapper.getLastProContractNum();
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
		
		countStr = "ZBHT-"+year+countStr;
		return countStr;
	}
	
	// 获取已生效或执行的总包合同集合（分包合同中的模糊匹配）
	public List<ProContract> getProContractList(String proContractName){
		return proContractMapper.getProContractList(proContractName);
	};
	
	// 获取总包合同集合（合同审核中的模糊匹配）
	public List<ProContract> getProContractListByName(String proContractName){
		return proContractMapper.getProContractList(proContractName);
	};
	
	//通过主项目id获取一个总包合同信息
	public ProContract getProContractByProgramId(ProContract proContract){
		return proContractMapper.getProContractByProgramId(proContract);
	};
	
	//通过合同名称获得未审批且为市场投标的合同
	public List<ProContract> getMarketProContractByName(String contractName){
		ProContract proContract = new ProContract();
		proContract.setContractName(contractName);
		return proContractMapper.getMarketProContractByName(proContract);
	}
	
	//通过合同名称获得未审批且为业主指定的合同
	public List<ProContract> getAppointProContractByName(String contractName){
		ProContract proContract = new ProContract();
		proContract.setContractName(contractName);
		return proContractMapper.getAppointProContractByName(proContract);
	}
	
	//更新市场投标合同审批状态
	@Transactional(readOnly = false)
	public int updateProContractStatus(ActContract actContract, Integer approvalStatus){
		ProContract proContract = actContract.getProContract();
		int i = 0;
		if(proContract != null){
			proContract = proContractMapper.get(proContract);
			if(proContract != null){
				//设置对应的合同审批状态为审批中
				proContract.setApprovalStatus(approvalStatus);
				i = proContractMapper.updateProContractStatus(proContract);
			}
		}
		return i;
	};
	
	//更新小合同审批状态
	@Transactional(readOnly = false)
	public int updateProContractStatus(AttachContract attachContract, Integer approvalStatus){
		ProContract proContract = attachContract.getProContract();
		int i = 0;
		if(proContract != null){
			proContract = proContractMapper.get(proContract);
			if(proContract != null){
				//设置对应的合同审批状态为审批中
				proContract.setApprovalStatus(approvalStatus);
				i = proContractMapper.updateProContractStatus(proContract);
			}
		}
		return i;
	};
	
	//时间处理
	public ProContract modifyProContract(ProContract proContract){
		
		return proContract;
	}
}
