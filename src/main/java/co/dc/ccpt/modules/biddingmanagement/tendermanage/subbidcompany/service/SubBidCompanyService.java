package co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.mapper.SubBidCompanyMapper;


/**
 * 子项目参投单位管理Service
 * @author lxh
 * @version 2018-03-27
 */
@Service
@Transactional(readOnly = true)
public class SubBidCompanyService extends CrudService<SubBidCompanyMapper, SubBidCompany> {

	@Autowired
	public SubBidCompanyMapper subBidCompanyMapper;
	
	public SubBidCompany get(String id) {
		return super.get(id);
	}
	
	public List<SubBidCompany> findList(SubBidCompany subBidCompany) {
		return super.findList(subBidCompany);
	}
	
	public List<SubBidCompany> getSubBidCompanyByTenderId(SubBidCompany subBidCompany){
		return subBidCompanyMapper.getSubBidCompanyByTenderId(subBidCompany);
	};
	
	public List<String> listAllSubBidProIdByCompId(SubBidCompany subBidCompany){
		return subBidCompanyMapper.listAllSubBidProIdByCompId(subBidCompany);
	};
	
	@Transactional(readOnly = false)
	public List<SubBidCompany> getAlreadyBidCompanyList(SubBidCompany subBidCompany) {
		return subBidCompanyMapper.getAlreadyBidCompanyList(subBidCompany);
	}
	
	@Transactional(readOnly = false)
	public List<SubBidCompany> getBidCompanyList(SubBidCompany subBidCompany) {
		return subBidCompanyMapper.getBidCompanyList(subBidCompany);
	}
	
	public Page<SubBidCompany> findPage(Page<SubBidCompany> page, SubBidCompany subBidCompany) {
		return super.findPage(page, subBidCompany);
	}
	
	@Transactional(readOnly = false)
	public void save(SubBidCompany subBidCompany) {
		super.save(subBidCompany);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubBidCompany subBidCompany) {
		super.delete(subBidCompany);
	}
	
	public List<SubBidCompany> getBidCompanyListByProId(SubBidCompany subBidCompany){
		return subBidCompanyMapper.getBidCompanyListByProId(subBidCompany);
	}
	
	public List<SubBidCompany> listSubBidCompanyForSubmit(SubBidCompany subBidCompany){
		 return subBidCompanyMapper.listSubBidCompanyForSubmit(subBidCompany);
	};
	
	public List<SubBidCompany> listSubBidCompanyByCompId(String companyId){
		return subBidCompanyMapper.listSubBidCompanyByCompId(companyId);
	};
}