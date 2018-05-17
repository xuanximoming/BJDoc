package com.android.hospital.entity;

import java.util.ArrayList;

/**
 * 
* @ClassName: NonDrugEntity 
* @Description: TODO(非药品实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午11:34:00 
*
 */
public class NonDrugEntity {

	public String item_name;//别名
	public String input_code;//输入码，检索用	
	public String item_code;//药品代码	
    public String item_class;//类别
    
    public NonDrugEntity(){
    	
    }
      
    public static ArrayList<NonDrugEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<NonDrugEntity> list=new ArrayList<NonDrugEntity>();
    	int size=dataList.size();
    	for (int i = 0; i < size; i++) {
    		NonDrugEntity entity=new NonDrugEntity();
			entity.item_name=dataList.get(i).get("item_name");
			entity.input_code=dataList.get(i).get("input_code");
			entity.item_code=dataList.get(i).get("item_code");
			entity.item_class=dataList.get(i).get("item_class");
			list.add(entity);
		}
		return list;
    }
}
