package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
* @ClassName: BloodTypeEntity 
* @Description: TODO(血液要求实体) 
* @author 
* @date 
*
 */
public class BloodTypeEntity implements Serializable{

	
    public String blood_type_name;//项目名称
    public String blood_type;//类别代码
    public String blood_match;//
    public String useful_life;//
    public String temperature;
    
    public BloodTypeEntity(){
    	
    }
    
    public static ArrayList<BloodTypeEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<BloodTypeEntity> list=new ArrayList<BloodTypeEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			BloodTypeEntity entity=new BloodTypeEntity();
			entity.blood_type_name=dataList.get(i).get("blood_type_name");
			entity.blood_type=dataList.get(i).get("blood_type");
			entity.blood_match=dataList.get(i).get("blood_match");
			entity.useful_life=dataList.get(i).get("useful_life");
			entity.temperature=dataList.get(i).get("temperature");
			list.add(entity);
		}
		return list;
    }
}
