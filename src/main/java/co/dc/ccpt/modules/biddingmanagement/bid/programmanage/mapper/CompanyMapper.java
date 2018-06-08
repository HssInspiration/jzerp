package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;

@MyBatisMapper
public interface CompanyMapper extends BaseMapper<Company> {
	List<Company> listAllCompany();//查询所有单位

	Company getCompanyIdByName(@Param("companyname") String companyName);// 通过名称查询一条对应的id
	
	List<Company> listAllCompanyIdAndName();//查询对应的单位id和名称

	List<Company> listAllCompanyByName(@Param("companyName") String companyname);//通过单位名称查询对应集合（模糊匹配）
	
//	List<Company> listAllCompanyByNameExceptJz(@Param("companyName") String companyname);//通过单位名称查询对应集合（模糊匹配）
	
	Integer getCompanyTotalCount();//获取总条数，便于设置编号自增
	
	String getLastInsertNum();//获取最新增的一条记录的编号
}
