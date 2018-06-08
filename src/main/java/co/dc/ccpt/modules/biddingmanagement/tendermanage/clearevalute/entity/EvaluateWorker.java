package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Worker;
import co.dc.ccpt.modules.sys.entity.DictValue;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 评标人员表Entity
 * @author lxh
 * @version 2018-04-22
 */
public class EvaluateWorker extends DataEntity<EvaluateWorker> {
	
	private static final long serialVersionUID = 1L;
	private ClearEvaluate clearEvaluate;		// 业务主表id 父类
	private Worker worker;      // 人员对象
	private DictValue dictValue; //字典值对象
	private User user; //人员对象
	
	public EvaluateWorker() {
		super();
	}

	public EvaluateWorker(String id){
		super(id);
	}

	public Worker getWorker() {
		return worker;
	}

	public DictValue getDictValue() {
		return dictValue;
	}

	public void setDictValue(DictValue dictValue) {
		this.dictValue = dictValue;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public EvaluateWorker(ClearEvaluate clearEvaluate) {
		this.clearEvaluate = clearEvaluate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ClearEvaluate getClearEvaluate() {
		return clearEvaluate;
	}

	public void setClearEvaluate(ClearEvaluate clearEvaluate) {
		this.clearEvaluate = clearEvaluate;
	}

}