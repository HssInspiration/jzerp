/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;

/**
 * 项目工程管理MAPPER接口
 * @author lxh
 * @version 2018-02-01
 */
@MyBatisMapper
public interface ProgramMapper extends BaseMapper<Program> {
	public Program getByProgramNum(Program program); // 通过项目编号查询一条项目信息

	public List<Program> listAllProgram(); // 查询所有项目

	public List<Program> listAllProgramByName(@Param("programName") String programName); // 通过项目名称查询对应项目集合(模糊匹配)

	public List<Program> listProgramByisBid(@Param("programName") String programName); // 查出所有已中标项目名称，供分包项目模块调用
	
	public Program getProgramTypeById(Program program); // 通过id获取对应的类型集合
	
	public List<Program> getProgramByName(@Param("programName") String programName); // 通过项目名称获取已中标的项目集合（总包合同模块模糊匹配）
	
	public List<Program> listProgramByCompId(@Param("companyId") String companyId);//通过公司id查询对应项目信息（公司信息删除时判断） 
}