package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.ClearEvaluate;

/**
 * 清评标管理MAPPER接口
 * 
 * @author lxh
 * @version 2018-04-22
 */
@MyBatisMapper
public interface ClearEvaluateMapper extends BaseMapper<ClearEvaluate> {
	public ClearEvaluate getClearEvaluateBySubCompId(ClearEvaluate clearEvaluate);

	public List<ClearEvaluate> getClearEvaluateByProId(ClearEvaluate clearEvaluate);
}