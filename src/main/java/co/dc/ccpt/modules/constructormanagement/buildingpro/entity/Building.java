package co.dc.ccpt.modules.constructormanagement.buildingpro.entity;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.StaffCertificate;

public class Building extends DataEntity<Building> {
	private static final long serialVersionUID = 1L;
	private Bidcompany bidcompany;// 参投单位对象
	private CoreStaff coreStaff;// 核心人员对象
	private StaffCertificate staffCertificate;// 人员证书对象

	public Building() {
		super();
	}

	public Building(String id) {
		super(id);
	}

	public Bidcompany getBidcompany() {
		return bidcompany;
	}

	public CoreStaff getCoreStaff() {
		return coreStaff;
	}

	public StaffCertificate getStaffCertificate() {
		return staffCertificate;
	}

	public void setBidcompany(Bidcompany bidcompany) {
		this.bidcompany = bidcompany;
	}

	public void setCoreStaff(CoreStaff coreStaff) {
		this.coreStaff = coreStaff;
	}

	public void setStaffCertificate(StaffCertificate staffCertificate) {
		this.staffCertificate = staffCertificate;
	}

}
