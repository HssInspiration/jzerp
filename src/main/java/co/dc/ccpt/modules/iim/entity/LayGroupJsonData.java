package co.dc.ccpt.modules.iim.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * LayJson--->Data对象
 * 
 * @author Swhite
 * 
 */
public class LayGroupJsonData {
	
	private HashMap<Object, Object> owner = new HashMap<Object, Object>(); // 我的信息

	
	private List<Friend> list = new ArrayList<Friend>(); // 群组中的成员列表


	public void setOwner(HashMap<Object, Object> owner) {
		this.owner = owner;
	}


	public HashMap<Object, Object> getOwner() {
		return owner;
	}


	public void setList(List<Friend> list) {
		this.list = list;
	}


	public List<Friend> getList() {
		return list;
	}


}
