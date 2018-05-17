package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
* @ClassName: UserEntity 
* @Description: TODO(用户实体) 
* @author 
* @date 
*
 */
public class UserEntity implements Serializable{

    public String db_user;//登录名
    public String user_dept;//科室
    public String title;//职称
    public String name;//姓名
    public String surgery_class;//手术级别
    public String inputcode;//输入码
    
    public UserEntity(){
    	
    }  
    public static ArrayList<UserEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<UserEntity> list=new ArrayList<UserEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			UserEntity entity=new UserEntity();
			entity.db_user=dataList.get(i).get("db_user");
			entity.user_dept=dataList.get(i).get("user_dept");
			entity.title=dataList.get(i).get("title");
			entity.name=dataList.get(i).get("name");
			entity.surgery_class=dataList.get(i).get("surgery_class");
			entity.inputcode=dataList.get(i).get("input_code");
			list.add(entity);
		}
		return list;
    }
}
