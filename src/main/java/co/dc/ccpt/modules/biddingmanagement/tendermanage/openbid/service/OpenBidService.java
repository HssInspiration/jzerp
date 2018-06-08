package co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.entity.OpenBid;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.mapper.OpenBidMapper;

/**
 * 开标信息管理Service
 * @author lxh
 * @version 2018-03-27
 */
@Service
@Transactional(readOnly = true)
public class OpenBidService extends CrudService<OpenBidMapper, OpenBid> {

	public OpenBid get(String id) {
		return super.get(id);
	}
	
	public List<OpenBid> findList(OpenBid openBid) {
		return super.findList(openBid);
	}
	
	public Page<OpenBid> findPage(Page<OpenBid> page, OpenBid openBid) {
		return super.findPage(page, openBid);
	}
	
	@Transactional(readOnly = false)
	public void save(OpenBid openBid) {
		super.save(openBid);
	}
	
	@Transactional(readOnly = false)
	public void delete(OpenBid openBid) {
		super.delete(openBid);
	}
	
}