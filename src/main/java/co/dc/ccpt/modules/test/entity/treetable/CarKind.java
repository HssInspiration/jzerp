/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.treetable;

import java.util.List;
import com.google.common.collect.Lists;

import co.dc.ccpt.core.persistence.TreeEntity;

/**
 * 车系Entity
 * @author lgf
 * @version 2017-06-12
 */
public class CarKind extends TreeEntity<CarKind> {
	
	private static final long serialVersionUID = 1L;
	
	private List<Car> carList = Lists.newArrayList();		// 子表列表
	
	public CarKind() {
		super();
	}

	public CarKind(String id){
		super(id);
	}

	public  CarKind getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CarKind parent) {
		this.parent = parent;
		
	}
	
	public List<Car> getCarList() {
		return carList;
	}

	public void setCarList(List<Car> carList) {
		this.carList = carList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}