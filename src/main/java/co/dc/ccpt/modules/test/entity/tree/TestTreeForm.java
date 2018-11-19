/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.tree;

import co.dc.ccpt.core.persistence.TreeEntity;

/**
 * 组织机构Entity
 * @author liugf
 * @version 2017-06-11
 */
public class TestTreeForm extends TreeEntity<TestTreeForm> {
	
	private static final long serialVersionUID = 1L;
	
	
	public TestTreeForm() {
		super();
	}

	public TestTreeForm(String id){
		super(id);
	}

	public  TestTreeForm getParent() {
			return parent;
	}
	
	@Override
	public void setParent(TestTreeForm parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}