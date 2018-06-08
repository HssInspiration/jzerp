package co.dc.ccpt.modules.constructormanagement.basicinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.StaffCertificate;
import co.dc.ccpt.modules.constructormanagement.basicinfo.mapper.CoreStaffMapper;
import co.dc.ccpt.modules.constructormanagement.basicinfo.mapper.StaffCertificateMapper;

/**
 * 投标综合查询Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class CoreStaffService extends CrudService<CoreStaffMapper, CoreStaff>{

	@Autowired
	private StaffCertificateMapper staffCertificateMapper;
	
	@Autowired
	private CoreStaffMapper coreStaffMapper;
	
	public CoreStaff get(String id) {
		CoreStaff coreStaff = super.get(id);
		coreStaff.setStaffCertificateList(staffCertificateMapper.findList(new StaffCertificate(coreStaff)));
		return coreStaff;
	}
	
	public List<CoreStaff> findList(CoreStaff coreStaff) {
		return super.findList(coreStaff);
	}
	
	public Page<CoreStaff> findPage(Page<CoreStaff> page, CoreStaff coreStaff) {
		return super.findPage(page, coreStaff);
	}
	
	@Transactional(readOnly = false)
	public void save(CoreStaff coreStaff) {
		super.save(coreStaff);
		for (StaffCertificate staffCertificate : coreStaff.getStaffCertificateList()){
			if (staffCertificate.getId() == null){
				continue;
			}
			if (StaffCertificate.DEL_FLAG_NORMAL.equals(staffCertificate.getDelFlag())){
				if (StringUtils.isBlank(staffCertificate.getId())){
					staffCertificate.setCoreStaff(coreStaff);;
					staffCertificate.preInsert();
					staffCertificateMapper.insert(staffCertificate);
				}else{
					staffCertificate.preUpdate();
					staffCertificateMapper.update(staffCertificate);
				}
			}else{
				staffCertificateMapper.delete(staffCertificate);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(CoreStaff coreStaff) {
		super.delete(coreStaff);
		staffCertificateMapper.delete(new StaffCertificate(coreStaff));
	}
	//获取所有核心人员集合
	public List<CoreStaff> getAllCoreStaffList(CoreStaff coreStaff){
		return coreStaffMapper.getAllCoreStaffList(coreStaff);
	};
	
	//通过指定条件获取人员名称
	public List<CoreStaff> getAppointCoreStaffListByName(CoreStaff coreStaff){
		return coreStaffMapper.getAppointCoreStaffListByName(coreStaff);
	};
	
	//通过用户id查询核心人员信息
	public CoreStaff getCoreStaffByUserId(CoreStaff coreStaff){
		return coreStaffMapper.getCoreStaffByUserId(coreStaff);
	};
}
