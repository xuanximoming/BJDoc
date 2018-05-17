package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

/**
 * 
* @ClassName: CheckEntity 
* @Description: TODO(检查实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午7:37:51 
*
 */
public class CheckEntity implements Serializable{

	private static final long serialVersionUID = -7091365283485985530L;
	
	public String result_status;//状态
	public String exam_no;//申请序号
    public String exam_item;//检查项目
    public String exam_class;//检查类别（C）
    public String exam_sub_class;//检查子类（C）
    public String req_date_time;//申请日期及时间 （Date）
    public String req_physician;//申请医生（C）
    public String report_date_time;//报告日期及时间（Date） 
    public String reporter;//报告者（C）
    public String description;//检查所见（C）
    public String impression;//印象（C）
    
    public CheckEntity(){
    	
    }
    
    public static ArrayList<CheckEntity> init(ArrayList<DataEntity> dataList){
    	ArrayList<CheckEntity> list=new ArrayList<CheckEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			CheckEntity entity=new CheckEntity();
			if("3".equals(dataList.get(i).get("result_status")))
			{
				entity.result_status="已报告";
			}else {
				if("2".equals(dataList.get(i).get("result_status")))
				{
					entity.result_status="提交";
				}
			}
			//entity.result_status=dataList.get(i).get("result_status");
			entity.exam_no=dataList.get(i).get("exam_no");
			entity.exam_item=dataList.get(i).get("exam_item");
			entity.exam_class=dataList.get(i).get("exam_class");
			entity.exam_sub_class=dataList.get(i).get("exam_sub_class");
			entity.req_date_time=dataList.get(i).get("req_date_time");
			entity.req_physician=dataList.get(i).get("req_physician");
			entity.report_date_time=dataList.get(i).get("report_date_time");
			entity.reporter=dataList.get(i).get("reporter");
			entity.description=dataList.get(i).get("description");
			entity.impression=dataList.get(i).get("impression");
			list.add(entity);
		}
		ArrayList<CheckEntity> list_end=new ArrayList<CheckEntity>();
		int k=0;
		for (int j = 0; j < list.size(); j++) {
			if (k!=0){
					String  test_n1=list.get(j).exam_no;  
					String  test_n0=list.get(j-1).exam_no; 
					if (test_n0.equals(test_n1)){					
						list.get(k-1).exam_item=list.get(k-1).exam_item + "//" +list.get(j).exam_item;
						list.set(k-1, list.get(k-1));
					}else{
							list_end.add(list.get(k-1));
							Log.e("申请时间-->", list.get(k-1).req_date_time.toString());
							k=j;
							list.get(k).exam_item = list.get(j).exam_item;
							list.set(k, list.get(k));
							k++;
					}
			}else{
					list.get(j).exam_item = list.get(j).exam_item+" ";
					list.set(j, list.get(j));
					k++;
			}
		}
		if (k!=0) {
			list_end.add(list.get(k-1));	
		}		
		return list_end;
    }
}
