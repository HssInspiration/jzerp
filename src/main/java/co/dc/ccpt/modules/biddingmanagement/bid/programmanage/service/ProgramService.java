/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.mapper.ProgramMapper;

/**
 * 项目工程管理Service
 * @author lxh
 * @version 2018-02-01
 */
@Service
@Transactional(readOnly = true)
public class ProgramService extends CrudService<ProgramMapper, Program> {
	@Autowired
	private ProgramMapper programMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Program> listAllProgramByName(String programName){
		return programMapper.listAllProgramByName(programName);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Program> listAllProgram(){
		return programMapper.listAllProgram();
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Program> listProgramByisBid(String programName){
		return programMapper.listProgramByisBid(programName);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<String> getProgramTypeById(Program program){
		program = programMapper.getProgramTypeById(program);
		List<String> programTypeList = new ArrayList<String>();
		if(program!=null){
			String programType= program.getProgramType();//获取类型字符串
			String[] programTypeArr = programType.split(",");
			for(String str:programTypeArr){
				programTypeList.add(str);
			}
		}
		
		System.out.println(programTypeList);
		 return programTypeList;
	}
	
	public Program get(String id) {
		return super.get(id);
	}
	
	public Program getByProgramNum(String programNum) {
		Program program = new Program();
		program.setProgramNum(programNum);
		return programMapper.getByProgramNum(program);
	}
	
	public List<Program> findList(Program program) {
		return super.findList(program);
	}
	
	public Page<Program> findPage(Page<Program> page, Program program) {
		return super.findPage(page, program);
	}
	
	@Transactional(readOnly = false)
	public void save(Program program) {
		super.save(program);
	}
	
	@Transactional(readOnly = false)
	public void delete(Program program) {
		super.delete(program);
	}
	
	public List<Program> listProgramByCompId(String companyId){
		return programMapper.listProgramByCompId(companyId);
	};
	 // 通过项目名称获取已中标的项目集合（总包合同模块模糊匹配）
	public List<Program> getProgramByName(String programName){
		return programMapper.getProgramByName(programName);
	};
}