package com.android.hospital.entity;

import java.util.ArrayList;

public class OperationItemEntity {

	public String operation_code;//项目代码
	public String operation_name;  //项目名称
	public String operation_scale; //项目等级
	public String std_indicator;  // 0或1
	public String input_code;//输入码
	public String approved_indicator;  //标准化标志 1-已标准化 0-临时项目
	
	public OperationItemEntity(){
		
	}
	public static ArrayList<OperationItemEntity> init(ArrayList<DataEntity> dataList){
		ArrayList<OperationItemEntity> list=new ArrayList<OperationItemEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			OperationItemEntity entity=new OperationItemEntity();
			entity.operation_code=dataList.get(i).get("operation_code").trim();
			entity.operation_name=dataList.get(i).get("operation_name").trim();
			entity.operation_scale=dataList.get(i).get("operation_scale").trim();
			entity.std_indicator=dataList.get(i).get("std_indicator").trim();
			entity.input_code=dataList.get(i).get("input_code").trim();
			entity.approved_indicator=dataList.get(i).get("approved_indicator").trim();
			list.add(entity);
		}
		return list;
	}
}
