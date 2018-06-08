package co.dc.ccpt.modules.constructormanagement.basicinfo.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.sys.entity.DictValue;

/**
 * 人员证书Entity
 * 
 * @author vuser
 *
 */
public class StaffCertificate extends DataEntity<StaffCertificate> {
	private static final long serialVersionUID = 1L;
	private String certificateClass;// 证书等级(dict)
	private String certificateFirstNum;// 证书编号一
	private String certificateMajor;// 对应专业名称(dict)
	private String certificateName;// 证书名称
	private String certificateSecondNum;// 证书编号二
	private String certificateThirdNum;// 证书编号三
	private CoreStaff coreStaff;// 核心人员表 父类
	private DictValue dictValueName; // 字典值对象
	private DictValue dictValueClass; // 字典值对象
	private DictValue dictValueMajor; // 字典值对象
	private String registrationNum;//注册证号
	private Date regisDate;//注册时间
	private Date startRegisDate;//开始 注册时间
	private Date endRegisDate;//结束 注册时间
	private Date invalidDate;//失效时间
	private Date startInvalidDate;//开始 失效时间
	private Date endInvalidDate;//结束 失效时间

	public StaffCertificate() {
		super();
	}

	public StaffCertificate(String id) {
		super(id);
	}

	public DictValue getDictValueName() {
		return dictValueName;
	}

	public void setDictValueName(DictValue dictValueName) {
		this.dictValueName = dictValueName;
	}

	public DictValue getDictValueClass() {
		return dictValueClass;
	}

	public void setDictValueClass(DictValue dictValueClass) {
		this.dictValueClass = dictValueClass;
	}

	public DictValue getDictValueMajor() {
		return dictValueMajor;
	}

	public void setDictValueMajor(DictValue dictValueMajor) {
		this.dictValueMajor = dictValueMajor;
	}

	public StaffCertificate(CoreStaff coreStaff) {
		this.coreStaff = coreStaff;
	}

	public String getCertificateClass() {
		return certificateClass;
	}

	public String getCertificateFirstNum() {
		return certificateFirstNum;
	}

	public String getCertificateMajor() {
		return certificateMajor;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public String getCertificateSecondNum() {
		return certificateSecondNum;
	}

	public String getCertificateThirdNum() {
		return certificateThirdNum;
	}

	public CoreStaff getCoreStaff() {
		return coreStaff;
	}

	public void setCertificateClass(String certificateClass) {
		this.certificateClass = certificateClass;
	}

	public void setCertificateFirstNum(String certificateFirstNum) {
		this.certificateFirstNum = certificateFirstNum;
	}

	public void setCertificateMajor(String certificateMajor) {
		this.certificateMajor = certificateMajor;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public void setCertificateSecondNum(String certificateSecondNum) {
		this.certificateSecondNum = certificateSecondNum;
	}

	public void setCertificateThirdNum(String certificateThirdNum) {
		this.certificateThirdNum = certificateThirdNum;
	}

	public void setCoreStaff(CoreStaff coreStaff) {
		this.coreStaff = coreStaff;
	}

	public String getRegistrationNum() {
		return registrationNum;
	}

	public void setRegistrationNum(String registrationNum) {
		this.registrationNum = registrationNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegisDate() {
		return regisDate;
	}

	public void setRegisDate(Date regisDate) {
		this.regisDate = regisDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartRegisDate() {
		return startRegisDate;
	}

	public void setStartRegisDate(Date startRegisDate) {
		this.startRegisDate = startRegisDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndRegisDate() {
		return endRegisDate;
	}

	public void setEndRegisDate(Date endRegisDate) {
		this.endRegisDate = endRegisDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartInvalidDate() {
		return startInvalidDate;
	}

	public void setStartInvalidDate(Date startInvalidDate) {
		this.startInvalidDate = startInvalidDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndInvalidDate() {
		return endInvalidDate;
	}

	public void setEndInvalidDate(Date endInvalidDate) {
		this.endInvalidDate = endInvalidDate;
	}

}
