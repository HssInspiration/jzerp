package co.dc.ccpt.modules.programmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;

/**
 * 子项目工程管理MAPPER接口
 * @author lxh
 * @version 2018-03-27
 */
@MyBatisMapper
public interface SubpackageProgramMapper extends BaseMapper<SubpackageProgram> {
	//获取个数，编号生成组成部分
	public Integer getNumCount();
	
	public String getLastInsertNum();//获取编号
	
	public List<Integer> getTypeByParentId(SubpackageProgram subpackageProgram);//通过父项目id获取对应类型
	
	// 通过分包项目名称查询出对应集合，模糊匹配
	public List<SubpackageProgram> getSubpackageProgramList(@Param("subpackageProgramName") String subpackageProgramName);

	// 通过分包项目名称查询出对应集合，模糊匹配--分包合同（已中标的分包项目）
	public List<SubpackageProgram> getSubpackageProgramListByName(@Param("subpackageProgramName") String subpackageProgramName);

	public List<SubpackageProgram> getByParentId(@Param("programId") String programId);//通过主项目获取子项目
}