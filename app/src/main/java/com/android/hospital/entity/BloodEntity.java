package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.impl.cookie.DateUtils;
/**
 * 
* @ClassName: BloodEntity 
* @Description: TODO(用血实体) 
* @author lll
* @date 2013-2-17  
*
 */
public class BloodEntity implements Serializable{

	public String apply_num;//申请号
	public String patient_id; //病人id
	public String dept_name; //科室名称
	public String dept_code; //科室代码
	public String pat_name ;//病人姓名
	public String blood_inuse; //血源
	public String blood_diagnose; //诊断
	public String pat_blood_group;//血型
	public String rh;//RH血型
	public String blood_sum;//输血总量
	public String apply_date;//申请时间
	public String price;//划价标志  1为划价
	public String fast_slow;//用血安排
	public String trans_date;//输血时间
	public String trans_capacity;//输血量
	public String blood_type_name ;//血液要求
	
	public BloodEntity(){
		
	}
	
	public static ArrayList<BloodEntity> init(ArrayList<DataEntity> dataList) {

		ArrayList<BloodEntity> list=new ArrayList<BloodEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			BloodEntity entity=new BloodEntity();
			entity.apply_num=dataList.get(i).get("apply_num").trim();
			entity.patient_id=dataList.get(i).get("patient_id").trim();
			entity.dept_name=dataList.get(i).get("dept_name").trim();
			entity.dept_code=dataList.get(i).get("dept_code").trim();
			entity.pat_name=dataList.get(i).get("pat_name").trim();
			entity.blood_inuse=dataList.get(i).get("blood_inuse").trim();
			entity.blood_diagnose=dataList.get(i).get("blood_diagnose").trim();
			entity.pat_blood_group=dataList.get(i).get("pat_blood_group").trim();
			entity.rh=dataList.get(i).get("rh").trim();
			entity.blood_sum=dataList.get(i).get("blood_sum").trim();
			entity.apply_date=dataList.get(i).get("apply_date").trim();
			entity.price=dataList.get(i).get("price").trim();
			entity.fast_slow=dataList.get(i).get("fast_slow").trim();
			entity.trans_date=dataList.get(i).get("trans_date").trim();
			entity.trans_capacity=dataList.get(i).get("trans_capacity").trim();
			entity.blood_type_name=dataList.get(i).get("blood_type_name").trim();
			list.add(entity);
		}
		
		//将fast_slow进行转化， 急诊1、常规2、备血3 （FAST_SLOW字段显示的为数值，需要进行相应转换）
		ArrayList<BloodEntity> arrayList=new ArrayList<BloodEntity>();
		for (int j = 0; j < list.size(); j++) {
			if ("1".equals(list.get(j).fast_slow)) {
				list.get(j).fast_slow="急诊";
				list.set(j, list.get(j));
				arrayList.add(list.get(j));
			}
			if ("2".equals(list.get(j).fast_slow)) { 
				list.get(j).fast_slow="常规";
				list.set(j, list.get(j));
				arrayList.add(list.get(j));
			}
			if ("3".equals(list.get(j).fast_slow)) {
				list.get(j).fast_slow="备血";
				list.set(j, list.get(j));
				arrayList.add(list.get(j));
			}
		}
		return arrayList;
	}
}
