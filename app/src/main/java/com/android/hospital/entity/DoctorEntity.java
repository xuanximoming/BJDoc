package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
* @ClassName: DoctorEntity 
* @Description: TODO(医生实体) 
* @author 
* @date 
*
 */
public class DoctorEntity implements Serializable{

    public String name;//姓名
    public String input_code;//输入法
    public String title;//级别
    public String user_name;//用户名
    
    public DoctorEntity(){
    	
    }
    
    public static ArrayList<DoctorEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<DoctorEntity> list=new ArrayList<DoctorEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			DoctorEntity entity=new DoctorEntity();
			entity.name=dataList.get(i).get("name");
			entity.input_code=dataList.get(i).get("input_code");
			entity.title=dataList.get(i).get("title");
			entity.user_name=dataList.get(i).get("user_name");
			list.add(entity);
		}
		return list;
    }
}
