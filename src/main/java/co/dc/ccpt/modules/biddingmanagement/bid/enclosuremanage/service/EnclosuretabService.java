package co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity.Enclosuretab;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.mapper.EnclosuretabMapper;



/**
 * 附件信息管理Service
 * @author lxh
 * @version 2018-03-25
 */
@Service
@Transactional(readOnly = true)
public class EnclosuretabService extends CrudService<EnclosuretabMapper, Enclosuretab> {

	@Autowired
	private EnclosuretabMapper enclosuretabMapper;
	
	public Enclosuretab get(String id) {
		return super.get(id);
	}
	
	public List<Enclosuretab> findList(Enclosuretab enclosuretab) {
		return super.findList(enclosuretab);
	}
	
	public Integer countEnclosure(String foreginId){
		return enclosuretabMapper.countEnclosure(foreginId);
	}
	
	public Enclosuretab getByEnclosuretabNum(String enclosureNum) {
		Enclosuretab enclosuretab = new Enclosuretab();
		enclosuretab.setEnclosureNum(enclosureNum);
		return enclosuretabMapper.getByEnclosuretabNum(enclosuretab);
	}
	
	public String countEnclosureByType(Enclosuretab enclosuretab) {
		//1.获取个数
		Integer count = enclosuretabMapper.countEnclosureByType(enclosuretab)+1;
		System.out.println(count);
		String countNum = "";
		if(count<10&count>=1){
			countNum = "0000"+count.toString();
		}else if(count>=10&count<100){
			countNum = "000"+count.toString();
		}else if(count>=100&count<1000){
			countNum = "00"+count.toString();
		}else if(count>=1000&count<10000){
			countNum = "0"+count.toString();
		}else if(count>=10000){
			countNum = count.toString();
		}
		//2.设置规则(归属项首字母大写+日期+编号)
		Integer type = enclosuretab.getEnclosureType();
		String firstNo = "";
		if(type==1){
			firstNo = "XM";
		}else if(type==2){
			firstNo = "TB";
		}else if(type==3){
			firstNo = "CT";
		}else if(type==4){
			firstNo = "ZXM";
		}else if(type==5){
			firstNo = "ZB";
		}else if(type==6){
			firstNo = "TBJL";
		}else if(type==7){
			firstNo = "KPB";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String date = sdf.format(new Date());
		firstNo = firstNo+date+countNum;
		return firstNo;
	}
	
	public Page<Enclosuretab> findPage(Page<Enclosuretab> page, Enclosuretab enclosuretab) {
		return super.findPage(page, enclosuretab);
	}
	
	@Transactional(readOnly = false)
	public void save(Enclosuretab enclosuretab) {
		super.save(enclosuretab);
	}
	
	@Transactional(readOnly = false)
	public void delete(Enclosuretab enclosuretab) {
		super.delete(enclosuretab);
	}
	
	@Transactional(readOnly = false)
	public List<Enclosuretab> getEnclosureContByForeginId(String foreginId){
		List<Enclosuretab> enclosuretabList = new ArrayList<Enclosuretab>();
		enclosuretabList = enclosuretabMapper.getEnclosureContByForeginId(foreginId);
		return enclosuretabList;
	}
	
	@Transactional(readOnly = false)
	public Integer deleteEnclosureByForeginId(String foreginId){
		return enclosuretabMapper.deleteEnclosureByForeginId(foreginId);
	}
	
}