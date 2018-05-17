package com.android.hospital.entity;

import java.util.ArrayList;

import android.util.Log;

import com.android.hospital.util.DebugUtil;
/**
 * 
* @ClassName: EvaluationInhospitalEntity 
* @Description: TODO(住院评估实体) 
* @author lll 
* @date 2013-1-23  
*
 */
public class EvaluationInhospitalEntity {

	public String patient_id;//病人ID	
	public String visit_id;//住院次数	
	public String dict_id;//字典id
	public String item_id;//项目id	
	public String item_name;//项目名称	
	public String item_value;//项目值	
	public String dept_code; //科室代码	
	public String record_date;//记录时间
   
	public EvaluationInhospitalEntity(){
		
	}
	
	/**
	 * 
	* @Title: init 
	* @Description: TODO(初始化实体) 
	* @param @param arrayList
	* @param @return    设定文件 
	* @return ArrayList<EvaluationInhospitalEntity>    实体集合
	* @throws
	 */
	public static ArrayList<EvaluationInhospitalEntity> init(ArrayList<DataEntity> arrayList){
		ArrayList<EvaluationInhospitalEntity> list=new ArrayList<EvaluationInhospitalEntity>();
		for (int i = 0; i < arrayList.size(); i++) {
			EvaluationInhospitalEntity entity=new EvaluationInhospitalEntity();
			
			entity.patient_id=arrayList.get(i).get("patient_id").trim();
			entity.visit_id=arrayList.get(i).get("visit_id").trim();
			entity.dict_id=arrayList.get(i).get("dict_id").trim();
			entity.item_id=arrayList.get(i).get("item_id").trim();
			entity.item_name=arrayList.get(i).get("item_name").trim();
			entity.item_value=arrayList.get(i).get("item_value").trim();
			entity.dept_code=arrayList.get(i).get("dept_code").trim();
			entity.record_date=arrayList.get(i).get("record_date").trim();
			list.add(entity);
		}
		return list;
	}
}
