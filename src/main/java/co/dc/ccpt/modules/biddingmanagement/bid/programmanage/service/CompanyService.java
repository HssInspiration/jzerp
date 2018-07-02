package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.mapper.CompanyMapper;
@Service
@Transactional
public class CompanyService extends CrudService<CompanyMapper, Company>{

	@Autowired
	private CompanyMapper companyMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Company> listAllCompany(){
		return companyMapper.listAllCompany();
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Company> listAllCompanyIdAndName(){
		return companyMapper.listAllCompanyIdAndName();
	} 
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Company> listAllCompanyByName(String companyName){
		return companyMapper.listAllCompanyByName(companyName);
	} 
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Company> listAllCompanyByNameExceptJz(String companyName){
		return companyMapper.listAllCompanyByName(companyName);
	} 
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Company getCompanyIdByName(String companyname){
		return companyMapper.getCompanyIdByName(companyname);
	}
	
	public Company get(String id) {
		return super.get(id);
	}
	
	//设置公司编号
	public String getCompanyNum(){
		String num = companyMapper.getLastInsertNum();
		String countStr = "";
		Integer count=0;
		if(num!=null && !num.equals("")){//若不为空
			String lastNum = num.substring(num.length()-3, num.length());//先截取后三位
			if(lastNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
				lastNum = lastNum.substring(1, 3);
				if(lastNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
					lastNum = lastNum.substring(1, 2);
				}
		}
			//2.获取数据库中的count（*）数而后按条件增加
			count = Integer.parseInt(lastNum)+1;//转成integer类型并加一
			countStr = count.toString();
			System.out.println(count);
		}else{
			count = 1;
		}
		if(count<10){
			countStr = "00"+countStr;
		}else if(count>=10 & count<100){
			countStr = "0"+countStr;
		}
		String companyNum = "Comp"+countStr;
		return companyNum;
	}
	
	public List<Company> findList(Company company) {
		return super.findList(company);
	}
	
	public Page<Company> findPage(Page<Company> page, Company company) {
		return super.findPage(page, company);
	}
	
	@Transactional(readOnly = false)
	public void save(Company company) {
		super.save(company);
	}
	
	@Transactional(readOnly = false)
	public void delete(Company company) {
		super.delete(company);
	}
}
