package com.android.hospital.entity;

import java.util.ArrayList;

/**
 * 
* @ClassName: SignsLifeEntity 
* @Description: TODO(生命体征实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2013-1-5 下午8:29:04 
*
 */
public class SignsLifeEntity {

	public String recording_date;//记录日期(D) 例如：2008-11-16	
	public String time_point;//时间点(D) 例如：2008-11-23 6:44:45	
	public String vital_signs;//项目名称(C)	
	public String vital_signs_cvalues;//项目值(N)	
	public String units;//单位(C)	
	public String nurse;//记录人(C)
	
	public SignsLifeEntity(){
		
	}
	
	public static ArrayList<SignsLifeEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<SignsLifeEntity> list=new ArrayList<SignsLifeEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			SignsLifeEntity entity=new SignsLifeEntity();
			entity.recording_date=dataList.get(i).get("recording_date");
			entity.time_point=dataList.get(i).get("time_point");
			entity.vital_signs=dataList.get(i).get("vital_signs");
			entity.vital_signs_cvalues=dataList.get(i).get("vital_signs_cvalues");
			entity.units=dataList.get(i).get("units");
			entity.nurse=dataList.get(i).get("nurse");
			list.add(entity);
		}
		return list;
    }
}
