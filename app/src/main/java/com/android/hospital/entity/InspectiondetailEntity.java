package com.android.hospital.entity;

import java.util.ArrayList;

/**
 * 
* @ClassName: InspectiondetailEntity 
* @Description: TODO(检验实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-19 下午4:16:35 
*
 */
public class InspectiondetailEntity {

	public String result_date_time;//检验日期
	public String report_item_name;//检验项目	
	public String result;//检验结果值	
	public String units;//单位	
	public String print_context;//参考范围	
	public String abnormal_indicator;//检验结果
	
	public InspectiondetailEntity(){
		
	}
	
	/**
	 * 
	* @Title: init 
	* @Description: TODO(获取检验明细集合) 
	* @param @param dataList
	* @param @return    设定文件 
	* @return ArrayList<InspectiondetailEntity>    返回类型 
	* @throws
	 */
	public static ArrayList<InspectiondetailEntity> init(ArrayList<DataEntity> dataList){
		ArrayList<InspectiondetailEntity> list=new ArrayList<InspectiondetailEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			InspectiondetailEntity entity=new InspectiondetailEntity();
			entity.result_date_time=dataList.get(i).get("result_date_time");
			entity.report_item_name=dataList.get(i).get("report_item_name");
			entity.result=dataList.get(i).get("result");
			entity.units=dataList.get(i).get("units");
			entity.print_context=dataList.get(i).get("print_context");
			entity.abnormal_indicator=dataList.get(i).get("abnormal_indicator");
			list.add(entity);
		}
		return list;
	}
}
