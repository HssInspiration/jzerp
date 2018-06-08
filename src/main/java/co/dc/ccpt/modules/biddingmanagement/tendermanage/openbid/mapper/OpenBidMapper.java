package co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.entity.OpenBid;

/**
 * 开标信息管理MAPPER接口
 * @author lxh
 * @version 2018-03-27
 */
@MyBatisMapper
public interface OpenBidMapper extends BaseMapper<OpenBid> {
	
}