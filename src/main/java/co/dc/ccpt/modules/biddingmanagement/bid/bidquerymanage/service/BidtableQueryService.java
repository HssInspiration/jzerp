package co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity.BidCompanyManage;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity.BidtableQuery;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.mapper.BidCompanyManageMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.mapper.BidtableQueryMapper;



/**
 * 投标综合查询Service
 * @author lxh
 * @version 2018-03-10
 */
@Service
@Transactional(readOnly = true)
public class BidtableQueryService extends CrudService<BidtableQueryMapper, BidtableQuery> {

	@Autowired
	private BidCompanyManageMapper bidCompanyManageMapper;
	
	public BidtableQuery get(String id) {
		BidtableQuery bidtableQuery = super.get(id);
		bidtableQuery.setBidCompanyManageList(bidCompanyManageMapper.findList(new BidCompanyManage(bidtableQuery)));
		return bidtableQuery;
	}
	
	public List<BidtableQuery> findList(BidtableQuery bidtableQuery) {
		return super.findList(bidtableQuery);
	}
	
	public Page<BidtableQuery> findPage(Page<BidtableQuery> page, BidtableQuery bidtableQuery) {
		return super.findPage(page, bidtableQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(BidtableQuery bidtableQuery) {
		super.save(bidtableQuery);
		for (BidCompanyManage bidCompanyManage : bidtableQuery.getBidCompanyManageList()){
			if (bidCompanyManage.getId() == null){
				continue;
			}
			if (BidCompanyManage.DEL_FLAG_NORMAL.equals(bidCompanyManage.getDelFlag())){
				if (StringUtils.isBlank(bidCompanyManage.getId())){
					bidCompanyManage.setBidtableQuery(bidtableQuery);
					bidCompanyManage.preInsert();
					bidCompanyManageMapper.insert(bidCompanyManage);
				}else{
					bidCompanyManage.preUpdate();
					bidCompanyManageMapper.update(bidCompanyManage);
				}
			}else{
				bidCompanyManageMapper.delete(bidCompanyManage);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(BidtableQuery bidtableQuery) {
		super.delete(bidtableQuery);
		bidCompanyManageMapper.delete(new BidCompanyManage(bidtableQuery));
	}
	
}