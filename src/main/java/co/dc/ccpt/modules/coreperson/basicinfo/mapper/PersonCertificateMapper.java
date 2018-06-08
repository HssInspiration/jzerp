package co.dc.ccpt.modules.coreperson.basicinfo.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.PersonCertificate;

/**
 * 人员证书MAPPER接口
 * @author lxh
 * @version 2018-05-03
 */
@MyBatisMapper
public interface PersonCertificateMapper extends BaseMapper<PersonCertificate> {

	public String getCorePersonIdByCertificateId(PersonCertificate personCertificate);//通过证书id查出一条对应的人员id
}
