package co.dc.ccpt.modules.coreperson.basicinfo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.PersonCertificate;
import co.dc.ccpt.modules.coreperson.basicinfo.mapper.CorePersonMapper;
import co.dc.ccpt.modules.coreperson.basicinfo.mapper.PersonCertificateMapper;

/**
 * 投标综合查询Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class CorePersonService extends CrudService<CorePersonMapper, CorePerson>{

	@Autowired
	private PersonCertificateMapper personCertificateMapper;
	
	@Autowired
	private CorePersonMapper corePersonMapper;
	
	public CorePerson get(String id) {
		CorePerson corePerson = super.get(id);
		corePerson.setPersonCertificateList(personCertificateMapper.findList(new PersonCertificate(corePerson)));
		return corePerson;
	}
	
	public PersonCertificate getPersonCertificate(String id) {
		return personCertificateMapper.get(id);
	}
	
	public List<CorePerson> findList(CorePerson corePerson) {
		return super.findList(corePerson);
	}
	
	public Page<CorePerson> findPage(Page<CorePerson> page, CorePerson corePerson) {
		return super.findPage(page, corePerson);
	}
	
	@Transactional(readOnly = false)
	public void save(CorePerson corePerson) {
		super.save(corePerson);
	}
	
	@Transactional(readOnly = false)
	public void savePersonCertificate(PersonCertificate personCertificate) {
		if (StringUtils.isBlank(personCertificate.getId())){
			personCertificate.preInsert();
			personCertificateMapper.insert(personCertificate);
		}else{
			personCertificate.preUpdate();
			personCertificateMapper.update(personCertificate);
		}
	}
	
	@Transactional(readOnly = false)
	public void deletePersonCertificate(PersonCertificate personCertificate) {
		personCertificateMapper.delete(personCertificate);
	}
	
	@Transactional(readOnly = false)
	public void delete(CorePerson corePerson) {
		super.delete(corePerson);
		personCertificateMapper.delete(new PersonCertificate(corePerson));
	}
	// 通过身份证号查询一条信息
	public CorePerson getCorePersonByIdNum(String identityNum){
		CorePerson corePerson = new CorePerson();
		corePerson.setIdentityNum(identityNum);
		return corePersonMapper.getCorePersonByIdNum(corePerson);
	}; 
	
	//通过证书集合判断其中的失效时间与当前时间的大小
	@Transactional(readOnly = false)
	public List<PersonCertificate> compareInvalidDateAndNowByList(List<PersonCertificate> personCertificateList1){
		Date date = new Date();//当前时间
		Date invalidDate = new Date();//失效时间
		List<PersonCertificate> personCertificateList2 = new ArrayList<PersonCertificate>();
		for(PersonCertificate p:personCertificateList1){
			invalidDate = p.getInvalidDate();
			if(date.after(invalidDate)){//如果当前时间晚于失效时间设置失效
				p.setIsInvalid(1);
			}
			personCertificateList2.add(p);
			personCertificateMapper.update(p);
		}
		return personCertificateList2;
	}
	
	//获取所有核心人员集合
	public List<CorePerson> getAllCorePersonList(CorePerson corePerson){
		return corePersonMapper.getAllCorePersonList(corePerson);
	};
	
	//通过指定条件获取人员名称
	public List<CorePerson> getAppointCorePersonListByName(CorePerson corePerson){
		return corePersonMapper.getAppointCorePersonListByName(corePerson);
	};
	
	//通过用户id查询核心人员信息
	public CorePerson getCorePersonByUserId(CorePerson corePerson){
		return corePersonMapper.getCorePersonByUserId(corePerson);
	};
	
	//通过证书id查出一条对应的人员id
	public String getCorePersonIdByCertificateId(PersonCertificate personCertificate){
		return personCertificateMapper.getCorePersonIdByCertificateId(personCertificate);
	};
	
}
