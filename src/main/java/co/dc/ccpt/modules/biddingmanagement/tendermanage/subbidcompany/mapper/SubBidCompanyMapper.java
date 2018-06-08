package co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;


/**
 * 子项目参投单位管理MAPPER接口
 * @author lxh
 * @version 2018-03-27
 */
@MyBatisMapper
public interface SubBidCompanyMapper extends BaseMapper<SubBidCompany> {
	
	public List<SubBidCompany> getAlreadyBidCompanyList(SubBidCompany subBidCompany);
	
	public List<SubBidCompany> getBidCompanyList(SubBidCompany subBidCompany);//子项目名称选择后传入子项目名称并通过名称查询出对应的参投单位集合
	
	public List<SubBidCompany> getSubBidCompanyByTenderId(SubBidCompany subBidCompany);
	
	public List<String> listAllSubBidProIdByCompId(SubBidCompany subBidCompany);//通过一个参投单位的id查询其以投标项目id的集合
	
	public List<SubBidCompany> getBidCompanyListByProId(SubBidCompany subBidCompany);//通过项目id查询参投
	
	public List<SubBidCompany> listSubBidCompanyForSubmit(SubBidCompany subBidCompany);//查询递交人信息
	
	public List<SubBidCompany> listSubBidCompanyByCompId(@Param("companyId") String companyId);//通过公司id查询投标记录（删除公司信息时的判断条件）
}