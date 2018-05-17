package com.android.hospital.entity;

import java.util.ArrayList;

public class InspectionItemEntity {

	public String item_class;//项目类别	
	public String item_name;  //项目名称	
	public String item_code; //项目编码	
	public String std_indicator;  // 0或1	
	public String input_code;//输入码	
	public String input_code_wb;  //	
	public String performed_by;  //检验科室代码？	
	public String expand1; //标本
	public String expand2; //类别	
	public String expand3;  //科室代码	
	
	public InspectionItemEntity(){
		
	}
	
	public static ArrayList<InspectionItemEntity> init(ArrayList<DataEntity> dataList){
		ArrayList<InspectionItemEntity> list=new ArrayList<InspectionItemEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			InspectionItemEntity entity=new InspectionItemEntity();
			entity.item_class=dataList.get(i).get("item_class").trim();
			entity.item_name=dataList.get(i).get("item_name").trim();
			entity.item_code=dataList.get(i).get("item_code").trim();
			entity.std_indicator=dataList.get(i).get("std_indicator").trim();
			entity.input_code=dataList.get(i).get("input_code").trim();
			entity.input_code_wb=dataList.get(i).get("input_code_wb").trim();
			entity.performed_by=dataList.get(i).get("performed_by").trim();
			entity.expand1=dataList.get(i).get("expand1").trim();
			entity.expand2=dataList.get(i).get("expand2").trim();
			entity.expand3=dataList.get(i).get("expand3").trim();
			list.add(entity);
		}
		return list;
	}
}
