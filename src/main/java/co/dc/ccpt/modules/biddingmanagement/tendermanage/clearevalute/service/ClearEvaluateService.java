package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.ClearEvaluate;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.EvaluateWorker;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.mapper.ClearEvaluateMapper;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.mapper.EvaluateWorkerMapper;


/**
 * 清评标管理Service
 * @author lxh
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class ClearEvaluateService extends CrudService<ClearEvaluateMapper, ClearEvaluate> {

	@Autowired
	private EvaluateWorkerMapper evaluateWorkerMapper;
	
	@Autowired
	private ClearEvaluateMapper clearEvaluateMapper;
	
	public ClearEvaluate get(String id) {
		ClearEvaluate clearEvaluateBid = super.get(id);
		clearEvaluateBid.setEvaluateUserList(evaluateWorkerMapper.findList(new EvaluateWorker(clearEvaluateBid)));;
		return clearEvaluateBid;
	}
	
	public List<ClearEvaluate> findList(ClearEvaluate clearEvaluateBid) {
		return super.findList(clearEvaluateBid);
	}
	
	public Page<ClearEvaluate> findPage(Page<ClearEvaluate> page, ClearEvaluate clearEvaluateBid) {
		return super.findPage(page, clearEvaluateBid);
	}
	
	public ClearEvaluate getClearEvaluateBySubCompId(ClearEvaluate clearEvaluate){
		return clearEvaluateMapper.getClearEvaluateBySubCompId(clearEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void save(ClearEvaluate clearEvaluate) {
		super.save(clearEvaluate);
		for (EvaluateWorker evaluateWorker : clearEvaluate.getEvaluateUserList()){
			if (evaluateWorker.getId() == null){
				continue;
			}
			if (EvaluateWorker.DEL_FLAG_NORMAL.equals(evaluateWorker.getDelFlag())){
				if (StringUtils.isBlank(evaluateWorker.getId())){
					evaluateWorker.setClearEvaluate(clearEvaluate);
					evaluateWorker.preInsert();
					evaluateWorkerMapper.insert(evaluateWorker);
				}else{
					evaluateWorker.preUpdate();
					evaluateWorkerMapper.update(evaluateWorker);
				}
			}else{
				evaluateWorkerMapper.delete(evaluateWorker);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ClearEvaluate clearEvaluate) {
		super.delete(clearEvaluate);
		evaluateWorkerMapper.delete(new EvaluateWorker(clearEvaluate));
	}
	
	public List<EvaluateWorker> getUserList(String subpackageProgramId){
		return evaluateWorkerMapper.getUserList(subpackageProgramId);
	};
	
	public List<ClearEvaluate> getClearEvaluateByProId(ClearEvaluate clearEvaluate){
		return clearEvaluateMapper.getClearEvaluateByProId(clearEvaluate);
	};
}