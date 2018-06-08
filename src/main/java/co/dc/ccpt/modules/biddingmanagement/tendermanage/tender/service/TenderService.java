package co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.mapper.TenderMapper;


/**
 * 招标信息管理Service
 * @author lxh
 * @version 2018-03-27
 */
@Service
@Transactional(readOnly = true)
public class TenderService extends CrudService<TenderMapper, Tender> {

	@Autowired
	private TenderMapper tenderMapper;
	
	public Tender get(String id) {
		return super.get(id);
	}
	
	public List<Tender> findList(Tender tender) {
		return super.findList(tender);
	}
	
	public Page<Tender> findPage(Page<Tender> page, Tender tender) {
		return super.findPage(page, tender);
	}
	
	public List<Tender> ListTenderBySubproName(Tender tender){
		return tenderMapper.ListTenderBySubproName(tender);
	}
	
	public Tender getTenderBySubProId(Tender tender){
		return tenderMapper.getTenderBySubProId(tender);
	};
	
	@Transactional(readOnly = false)
	public void save(Tender tender) {
		super.save(tender);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tender tender) {
		super.delete(tender);
	}
	
	//通过招标id获取主项目业主方信息，投标记录中排除项目发包公司名称
	public Tender getCompanyNameByTenderId(Tender tender){
		return tenderMapper.getCompanyNameByTenderId(tender);
	}
	
}