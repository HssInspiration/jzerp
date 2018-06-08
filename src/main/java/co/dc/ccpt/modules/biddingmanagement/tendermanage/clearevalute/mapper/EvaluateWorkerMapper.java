package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.EvaluateWorker;

/**
 * 评标人员表MAPPER接口
 * @author lxh
 * @version 2018-04-22
 */
@MyBatisMapper
public interface EvaluateWorkerMapper extends BaseMapper<EvaluateWorker> {
	public List<EvaluateWorker> getUserList(@Param("subpackageProgramId") String subpackageProgramId);
}