package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.impl.cookie.DateUtils;
/**
 * 
* @ClassName: BloodItemEntity 
* @Description: TODO(新增用血项目实体) 
* @author lll
* @date 2013-3-28 
*
 */
public class BloodItemEntity implements Serializable{

	
	public String fast_slow; //用血安排
	public String unit;//单位
	public String trans_date;//预定输血时间
	public String trans_capacity;//输血量
	public String blood_type ;//血液要求 (编号）
	public String blood_type_name;//血液要求（名称）
	
	public BloodItemEntity(){
		
	}
	
	public static ArrayList<BloodItemEntity> init(ArrayList<DataEntity> dataList) {

		ArrayList<BloodItemEntity> list=new ArrayList<BloodItemEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			BloodItemEntity entity=new BloodItemEntity();
			entity.fast_slow=dataList.get(i).get("fast_slow").trim();
			entity.unit=dataList.get(i).get("unit").trim();
			entity.trans_date=dataList.get(i).get("trans_date").trim();
			entity.trans_capacity=dataList.get(i).get("trans_capacity").trim();
			entity.blood_type=dataList.get(i).get("blood_type").trim();
			entity.blood_type_name=dataList.get(i).get("blood_type_name").trim();
			list.add(entity);
		}
		//将fast_slow进行转化， 急诊1、计划2、备血3 （FAST_SLOW字段显示的为数值，需要进行相应转换）
		ArrayList<BloodItemEntity> arrayList=new ArrayList<BloodItemEntity>();
		for (int j = 0; j < list.size(); j++) {
			if ("1".equals(list.get(j).fast_slow)) {
				list.get(j).fast_slow="急诊";
				list.set(j, list.get(j));
				arrayList.add(list.get(j));
			}
			if ("2".equals(list.get(j).fast_slow)) { 
				list.get(j).fast_slow="计划";
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
