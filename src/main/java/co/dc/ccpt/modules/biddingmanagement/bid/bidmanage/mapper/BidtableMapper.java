/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;

/**
 * 投标管理MAPPER接口
 * @author lxh
 * @version 2018-02-07
 */
@MyBatisMapper
public interface BidtableMapper extends BaseMapper<Bidtable> {
	public Bidtable getByBidtableNum(Bidtable bidtable); // 通过投标编号查询一条信息

	public List<Bidtable> listAllBidtable();// 查询所有投标信息
	
	public Bidtable getBidtableByProId(Bidtable bidtable);//通过项目id查询一条投标信息（项目列表操作投标管理时调用）

	public List<Bidtable> listAllBidtableByProName(Bidtable bidtable);// 通过项目名称查询出对应投标信息
	
	public String validateProgramId(Bidtable bidtable);//验证项目id
	
	public Integer getTotalBidCount();//获取所有投标工程数量
	
	public String getLastInsertBidtableNum();//获取最新增加的一条投标数据
	
	public List<Bidtable> listBidtableByCompId(@Param("companyId") String companyId);  //通过公司id查询投标信息
	
}