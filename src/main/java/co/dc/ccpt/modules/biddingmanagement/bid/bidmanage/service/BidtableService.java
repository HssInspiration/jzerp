/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.mapper.BidtableMapper;

/**
 * 投标管理Service
 * @author lxh
 * @version 2018-02-07
 */
@Service
@Transactional(readOnly = true)
public class BidtableService extends CrudService<BidtableMapper, Bidtable> {

	@Autowired
	private BidtableMapper bidtableMapper;
	
	public Bidtable get(String id) {
		return super.get(id);
	}
	// 通过投标编号查询一条信息
	public Bidtable getByBidtableNum(String bidNum) {
		Bidtable bidtable = new Bidtable();
		bidtable.setBidNum(bidNum);
		return bidtableMapper.getByBidtableNum(bidtable);
	}
	
	// 查询所有投标数量
	public Integer getTotalBidCount(){
		return bidtableMapper.getTotalBidCount();
	}
	
	// 查询所有投标信息
	public List<Bidtable> listAllBidtable(){
		return bidtableMapper.listAllBidtable();
	}
	
	//设置一条新增的投标编号
	public String getBidtableNum(){
		//设置编号自增规则：JSJZTB+年份+月份+招标次数 ： 201804004-- 投标次数月份变更不回归（次月不重新计数），随年份而动
		//1.设置年月
		Date date = new Date();
		String yearMonth = new SimpleDateFormat("yyyyMM").format(date);
		String bidtableNum = bidtableMapper.getLastInsertBidtableNum();
		System.out.println(bidtableNum);
		String countStr = "";
		Integer count=0;
		if(bidtableNum!=null && !bidtableNum.equals("")){//若不为空
			String lastNum = bidtableNum.substring(bidtableNum.length()-3, bidtableNum.length());//先截取后三位
			if(lastNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
				lastNum = lastNum.substring(1, 3);
				if(lastNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
					lastNum = lastNum.substring(1, 2);
				}
			}
			//2.获取数据库中的count（*）数而后按条件增加
			count = Integer.parseInt(lastNum)+1;//转成integer类型并加一
			System.out.println(count);
		}else{
			count = 1;
		}
		if(count<10){//小于10
			countStr = "00" + count.toString();
		}else if(count>=10 & count<100){//大于等于10，小于100
			countStr = "0" + count.toString();
		}else{
			countStr = count.toString();
		}
		countStr = "JZTB"+yearMonth+countStr;
		return countStr;
	}
	// 通过项目名称查询一条投标信息
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Bidtable> listAllBidtableByProName(Bidtable bidtable){
		return bidtableMapper.listAllBidtableByProName(bidtable);
	}
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public String validateProgramId(Bidtable bidtable){
		String bidtableId = bidtableMapper.validateProgramId(bidtable);
		return bidtableId;
	};
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Bidtable getBidtableByProId(Bidtable bidtable){
		return bidtableMapper.getBidtableByProId(bidtable);
	}
	public List<Bidtable> findList(Bidtable bidtable) {
		return super.findList(bidtable);
	}
	
	public Page<Bidtable> findPage(Page<Bidtable> page, Bidtable bidtable) {
		return super.findPage(page, bidtable);
	}
	
	@Transactional(readOnly = false)
	public void save(Bidtable bidtable) {
		super.save(bidtable);
	}
	
	@Transactional(readOnly = false)
	public void delete(Bidtable bidtable) {
		super.delete(bidtable);
	}
	
	public List<Bidtable> listBidtableByCompId(String companyId){
		return bidtableMapper.listBidtableByCompId(companyId);
	}; 
	
}