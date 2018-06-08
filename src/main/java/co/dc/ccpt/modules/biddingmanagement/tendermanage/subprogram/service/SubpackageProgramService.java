package co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.mapper.SubpackageProgramMapper;

/**
 * 子项目工程管理Service
 * @author lxh
 * @version 2018-03-27
 */
@Service
@Transactional(readOnly = true)
public class SubpackageProgramService extends CrudService<SubpackageProgramMapper, SubpackageProgram> {
	@Autowired
	private SubpackageProgramMapper subpackageProgramMapper;
	
	public SubpackageProgram get(String id) {
		return super.get(id);
	}
	
	public List<SubpackageProgram> findList(SubpackageProgram subpackageProgram) {
		return super.findList(subpackageProgram);
	}

	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<SubpackageProgram> getSubpackageProgramList(String subpackageProgramName){
		return subpackageProgramMapper.getSubpackageProgramList(subpackageProgramName);
	}
	
	public List<Integer> getTypeByParentId(SubpackageProgram subpackageProgram){
		System.out.println("id:"+subpackageProgram.getProgram().getId());
		return subpackageProgramMapper.getTypeByParentId(subpackageProgram);
	}
	
	public Integer getNumCount(){
		return subpackageProgramMapper.getNumCount();
	}
	
	public String getLastInsertNum(){
		return subpackageProgramMapper.getLastInsertNum();
	};
	
	public Page<SubpackageProgram> findPage(Page<SubpackageProgram> page, SubpackageProgram subpackageProgram) {
		return super.findPage(page, subpackageProgram);
	}
	
	@Transactional(readOnly = false)
	public void save(SubpackageProgram subpackageProgram) {
		super.save(subpackageProgram);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubpackageProgram subpackageProgram) {
		super.delete(subpackageProgram);
	}
	
}