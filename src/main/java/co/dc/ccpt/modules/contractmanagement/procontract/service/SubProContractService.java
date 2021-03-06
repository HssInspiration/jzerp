package co.dc.ccpt.modules.contractmanagement.procontract.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.SubProContractMapper;
import co.dc.ccpt.modules.oa.entity.ActSubContract;

/**
 * 建造师在建项目管理Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class SubProContractService extends CrudService<SubProContractMapper, SubProContract> {
	
	@Autowired
	public SubProContractMapper subProContractMapper;
	
	public SubProContract get(String id) {
		return super.get(id);
	}
	
	public List<SubProContract> findList(SubProContract subProContract) {
		return super.findList(subProContract);
	}
	
	public Page<SubProContract> findPage(Page<SubProContract> page, SubProContract subProContract) {
		return super.findPage(page, subProContract);
	}
	
	@Transactional(readOnly = false)
	public void save(SubProContract subProContract) {
		super.save(subProContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubProContract subProContract) {
		super.delete(subProContract);
	}
	
	//设置分包合同编号
	public String setSubProContractNum(){
		//设置编号自增规则：FBHT-+年份： 2018001
		//1.设置年月
		Date date = new Date();
		String year = new SimpleDateFormat("yyyy").format(date);
		String contractNum = subProContractMapper.getLastSubProContractNum();
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
		
		countStr = "FBHT-"+year+countStr;
		return countStr;
	}
	
	//通过子项目id获取分包合同对象
	public SubProContract getSubProContractBySubProId(SubProContract subProContract){
		return subProContractMapper.getSubProContractBySubProId(subProContract);
	};
	
	// 根据名称模糊匹配
	public List<SubProContract> getSubProContractList(String subProContractName){
		return subProContractMapper.getSubProContractList(subProContractName);
	};
	
	//通过合同名称获得未审批的分包合同
	public List<SubProContract> getAppointSubProContractByName(String subProContractName){
		SubProContract subProContract = new SubProContract();
		subProContract.setSubProContractName(subProContractName);
		return subProContractMapper.getAppointSubProContractByName(subProContract);
	}
	
	//更新分包合同审批状态
	@Transactional(readOnly = false)
	public int updateSubProContractStatus(ActSubContract actSubContract, Integer approvalStatus){
		SubProContract subProContract = actSubContract.getSubProContract();
		int i = 0;
		if(subProContract != null){
			subProContract = subProContractMapper.get(subProContract);
			if(subProContract != null){
				//设置对应的合同审批状态为审批中
				subProContract.setApprovalStatus(approvalStatus);
				i = subProContractMapper.updateSubProContractStatus(subProContract);
			}
		}
		return i;
	}
	//模糊匹配
	public List<SubProContract> getSubProContractListByName(String subProContractName) {
		return subProContractMapper.getSubProContractList(subProContractName);
	};
	
	// 通过总包id获取分包对象集合
	public List<SubProContract> getSubProContractListById(SubProContract subProContract){
		return subProContractMapper.getSubProContractListById(subProContract);
	}

	public List<SubProContract> getSubProContractByName(String subProContractName) {
		SubProContract subProContract = new SubProContract();
		subProContract.setSubProContractName(subProContractName);
		return subProContractMapper.getSubProContractByName(subProContract);
	}
}
