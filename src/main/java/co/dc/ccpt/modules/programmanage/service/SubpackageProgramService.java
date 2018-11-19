package co.dc.ccpt.modules.programmanage.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;
import co.dc.ccpt.modules.programmanage.mapper.SubpackageProgramMapper;

/**
 * 子项目工程管理Service
 * @author lxh
 * @version 2018-03-27
 */
@Service
@Transactional(readOnly = true)
public class SubpackageProgramService extends CrudService<SubpackageProgramMapper, SubpackageProgram> {
	@Autowired
	private SubpackageProgramMapper subpackageProgramMapper;
	
	public SubpackageProgram get(String id) {
		return super.get(id);
	}
	
	public List<SubpackageProgram> findList(SubpackageProgram subpackageProgram) {
		return super.findList(subpackageProgram);
	}

	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<SubpackageProgram> getSubpackageProgramList(String subpackageProgramName){
		return subpackageProgramMapper.getSubpackageProgramList(subpackageProgramName);
	}
	
	public List<SubpackageProgram> getSubpackageProgramListByName(String subpackageProgramName){
		return subpackageProgramMapper.getSubpackageProgramListByName(subpackageProgramName);
	}
	
	public List<Integer> getTypeByParentId(SubpackageProgram subpackageProgram){
		System.out.println("id:"+subpackageProgram.getProgram().getId());
		return subpackageProgramMapper.getTypeByParentId(subpackageProgram);
	}
	
	public Integer getNumCount(){
		return subpackageProgramMapper.getNumCount();
	}
	
	public Page<SubpackageProgram> findPage(Page<SubpackageProgram> page, SubpackageProgram subpackageProgram) {
		return super.findPage(page, subpackageProgram);
	}
	
	@Transactional(readOnly = false)
	public void save(SubpackageProgram subpackageProgram) {
		super.save(subpackageProgram);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubpackageProgram subpackageProgram) {
		super.delete(subpackageProgram);
	}

	public String setSubPackageProgramNum() {
//		1.设置编号自增数
		String oldNum = subpackageProgramMapper.getLastInsertNum();
		String numberStr = "";
		Integer num = 0;
		if(oldNum!=null && !oldNum.equals("")){
			String subStringNum = oldNum.substring(oldNum.length()-6, oldNum.length()-3);
			if(subStringNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
				subStringNum = subStringNum.substring(1, 3);
				if(subStringNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
					subStringNum = subStringNum.substring(1, 2);
				}
			}
			num = Integer.parseInt(subStringNum)+1; //将截取到的编号设成integer类型并加1
			numberStr = num.toString();//转成String类型用于拼接 
		}else{
			numberStr = "1";
		}
		if(num<10){
			numberStr = "00"+numberStr;
		}else if(num>=10 & num<100){
			numberStr = "0"+numberStr;
		}
//		2.设置字符串拼接
		String numStr = "JSJZFB";//固定字符串
		String dateStr  = new SimpleDateFormat("yyyy").format(new Date());//日期字符串--后期要考虑实际使用中一年后的编号排序要从01开始
		/**
		 * 1.确认dateStr最后一位是否变化，若是进行第二步，若否，继续沿用前面的公式；
		 * 2.最后一位变化，说明年份增加，（不考虑录入2018之前的数据）取出当前表中所有的数据条数；
		 * 3.dateStr继续使用，但自增编号要用当前自增编号减去数据库中已有的数据条数，重新自01开始自增
		 */
		//3.赋值给编号
		String finalNum = numStr+dateStr+numberStr+"-";
		System.out.println(finalNum);
		return finalNum;
	}

	public String getLastInsertNum() {
		return subpackageProgramMapper.getLastInsertNum();
	}

	public List<SubpackageProgram> getByParentId(String programId) {
		return subpackageProgramMapper.getByParentId(programId);
	}
}