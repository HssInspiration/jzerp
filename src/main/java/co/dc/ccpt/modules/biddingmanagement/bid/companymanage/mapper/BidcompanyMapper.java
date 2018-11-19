/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;

/**
 * 参投单位管理MAPPER接口
 * @author lxh
 * @version 2018-02-08
 */
@MyBatisMapper
public interface BidcompanyMapper extends BaseMapper<Bidcompany> {
	Bidcompany getByBidcompanyId(Bidcompany bidcompany);// 通过id查询一条参投单位信息

	Bidcompany getByBidDate(Bidcompany bidcompany);// 通过日期查询一条参投单位信息

	List<Bidcompany> validateWorkerByCorePersonId(@Param("corePersonId") String corePersonId);// 验证人员是否重复

	List<Bidcompany> listAllBidtableByProName(
			@Param("program.programName") String programName);// 通过项目名称查询所有的投标及参投单位
	
	List<Bidcompany> findJzList(Bidcompany bidcompany);//条件为空时查出金卓的统计数据
	
	public Integer getTotalIsBidCount();//查询已中标的数量
	
	public Double getTotalLaborCost();//查询出所有的已中标劳务
	
	public Double getTotalBidPrice();//查询出所有的已中标的投标价
	
	public List<Bidcompany> getBidcompanyByBidtableId(Bidcompany bidcompany);//通过一条投标id查询对应的参投信息
	
	public List<String> listAllProgramIdByCompanyId(Bidcompany bidcompany);//通过一个参投单位的id查询其以投标项目id的集合
	
	public List<Integer> listAllIsBidStatusByProId(Bidcompany bidcompany);//通过项目id查询是否中标状态集合
	
	public List<Bidcompany> getBidcompanyByProName(Bidcompany bidcompany);//通过项目名称查询对应参投集合（模糊匹配）
	
	public List<Bidcompany> listBidcompanyByCompId(@Param("companyId") String companyId);//通过公司id查询参投信息（公司信息删除时判断）

	public Bidcompany getBidcompanyByWorkerId(@Param("corePersonId") String corePersonId);//通过人员id获取一条参投信息
	
	public List<Bidcompany> getBidcompanyByCorePersonId(Bidcompany bidcompany);//通过人员id获取参投信息集合--方便查询人员所在在建项目

	Bidcompany getBidPriceByProId(Bidcompany bidcomp);//通过项目id查询投标价---总包合同中市场投标项目的投标价
}