package co.dc.ccpt.modules.coreperson.basicinfo.entity;

import java.util.List;

import com.google.common.collect.Lists;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 核心人员Entity
 * @author vuser
 *
 */
public class CorePerson extends DataEntity<CorePerson> {
	private static final long serialVersionUID = 1L;
	private String identityNum; // 身份证号
	private String identityOldNum;	   // 原身份证号码
	private Office office; // 机构对象（用于人员关联）
	private String phoneNum; // 手机号码
	private String staffName; // 人员名称
	
	private User user;//用户对象
	private String isBuild;//是否有在建项目
	private PersonCertificate personCertificate;//子表对象
	private List<PersonCertificate> personCertificateList = Lists.newArrayList();		// 子表列表

	public CorePerson() {
		super();
	}

	public CorePerson(String id) {
		super(id);
	}

	public String getIdentityNum() {
		return identityNum;
	}

	public Office getOffice() {
		return office;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public PersonCertificate getPersonCertificate() {
		return personCertificate;
	}

	public void setPersonCertificate(PersonCertificate personCertificate) {
		this.personCertificate = personCertificate;
	}

	public List<PersonCertificate> getPersonCertificateList() {
		return personCertificateList;
	}

	public void setPersonCertificateList(List<PersonCertificate> personCertificateList) {
		this.personCertificateList = personCertificateList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIsBuild() {
		return isBuild;
	}

	public void setIsBuild(String isBuild) {
		this.isBuild = isBuild;
	}

	public String getIdentityOldNum() {
		return identityOldNum;
	}

	public void setIdentityOldNum(String identityOldNum) {
		this.identityOldNum = identityOldNum;
	}

}
