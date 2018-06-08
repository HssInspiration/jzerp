package co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;

/**
 * 招标信息管理MAPPER接口
 * @author lxh
 * @version 2018-03-27
 */
@MyBatisMapper
public interface TenderMapper extends BaseMapper<Tender> {
	public List<Tender> ListTenderBySubproName(Tender tender);
	
	public Tender getTenderBySubProId(Tender tender);
	
	public Tender getCompanyNameByTenderId(Tender tender);//通过招标id获取主项目业主方信息，投标记录中排除项目发包公司名称
}