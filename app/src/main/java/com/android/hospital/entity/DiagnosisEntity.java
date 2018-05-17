package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
* @ClassName: DiagnosisEntity 
* @Description: TODO(诊断实体) 
* @author 
* @date 
*
 */
public class DiagnosisEntity implements Serializable{

    public String admission_date_time;//入院时间
    public String diagnosis_type;//诊断类型
    public String diagnosis_type_name;//诊断类型名称
    public String diagnosis_no;//诊断序号
    public String diagnosis_desc;//诊断描述
    
    public DiagnosisEntity(){
    	
    }
    
    public static ArrayList<DiagnosisEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<DiagnosisEntity> list=new ArrayList<DiagnosisEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			DiagnosisEntity entity=new DiagnosisEntity();
			entity.admission_date_time=dataList.get(i).get("admission_date_time");
			entity.diagnosis_type=dataList.get(i).get("diagnosis_type");
			entity.diagnosis_type_name=dataList.get(i).get("diagnosis_type_name");
			entity.diagnosis_no=dataList.get(i).get("diagnosis_no");
			entity.diagnosis_desc=dataList.get(i).get("diagnosis_desc");
			list.add(entity);
		}
		return list;
    }
}
