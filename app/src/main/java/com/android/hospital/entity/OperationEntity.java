package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.impl.cookie.DateUtils;

import android.util.Log;
/**
 * 
* @ClassName: OperationEntity 
* @Description: TODO(手术实体) 
* @author lll
* @date 2013-1-15  
*
 */
public class OperationEntity implements Serializable{


	public String scheduled_date_time;//手术日期
	public String operating_room; //手术室
	public String operating_room_no; //手术间
	public String sequence ;//台次
	public String name; //病人姓名
	public String sex; //性别
	public String bed_no;//床号
	public String diag_before_operation;//主要诊断
	public String operation;//手术名称
	public String surgeon;//手术者
	public String first_assistant,second_assistant,third_assistant,fourth_assistant;//助手四个
	public String anesthesia_method;//麻醉方法
	public String anesthesia_doctor;//麻醉医生
	public String blood_tran_doctor;//输血者
	public String notes_on_operation;//手术准备条件
	public String ack_indicator ;//手术室确认标志  0或空，未经手术室确认；1已确认
	
	public OperationEntity(){
		
	}
	
	public static ArrayList<OperationEntity> init(ArrayList<DataEntity> dataList) {

		ArrayList<OperationEntity> list=new ArrayList<OperationEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			OperationEntity entity=new OperationEntity();
			entity.scheduled_date_time=dataList.get(i).get("scheduled_date_time").trim();
			entity.operating_room=dataList.get(i).get("operating_room").trim();
			entity.operating_room_no=dataList.get(i).get("operating_room_no").trim();
			entity.sequence=dataList.get(i).get("sequence").trim();
			entity.name=dataList.get(i).get("name").trim();
			entity.sex=dataList.get(i).get("sex").trim();
			entity.bed_no=dataList.get(i).get("bed_no").trim();
			entity.diag_before_operation=dataList.get(i).get("diag_before_operation").trim();
			entity.operation=dataList.get(i).get("operation").trim();
			entity.surgeon=dataList.get(i).get("surgeon").trim();
			entity.first_assistant=dataList.get(i).get("first_assistant").trim();
			entity.second_assistant=dataList.get(i).get("second_assistant").trim();
			entity.third_assistant=dataList.get(i).get("third_assistant").trim();
			entity.fourth_assistant=dataList.get(i).get("fourth_assistant").trim();
			entity.anesthesia_method=dataList.get(i).get("anesthesia_method").trim();
			entity.anesthesia_doctor=dataList.get(i).get("anesthesia_doctor").trim();
			entity.blood_tran_doctor=dataList.get(i).get("blood_tran_doctor").trim();
			entity.notes_on_operation=dataList.get(i).get("notes_on_operation").trim();
			entity.ack_indicator=dataList.get(i).get("ack_indicator").trim();
			list.add(entity);
		}
		ArrayList<OperationEntity> arrayList=new ArrayList<OperationEntity>();
		for (int j = 0; j < list.size(); j++) {
			if ("52201".equals(list.get(j).operating_room)) {  
				list.get(j).operating_room="手术室";
				list.set(j, list.get(j));
				arrayList.add(list.get(j));
			}
		}
		return arrayList;
	}
}
