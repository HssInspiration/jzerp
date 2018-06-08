package co.dc.ccpt.modules.sys.entity;

public class NewUser {

	private String id;
	private String name;
	private Role role; // 根据角色查询用户条件

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
