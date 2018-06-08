package co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.mapper;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity.Enclosuretab;


/**
 * 附件信息管理MAPPER接口
 * @author lxh
 * @version 2018-03-25
 */
@MyBatisMapper
public interface EnclosuretabMapper extends BaseMapper<Enclosuretab> {
	Enclosuretab getByEnclosuretabNum(Enclosuretab enclosuretab);// 通过编号查询一条信息
	
	Integer countEnclosure(@Param("foreginId") String foreginId);
	
	Integer countEnclosureByType(Enclosuretab enclosuretab);
}