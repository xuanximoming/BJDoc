package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.android.hospital.HospitalApp;

import android.util.Log;



/**
 * 
* @ClassName: InspectionEntity 
* @Description: TODO(检验实体) 
* @author lll
* @date 2012-12-18  
*
 */
public class InspectionEntity implements Serializable{

	/** 
	* @user - @date 
	*/
	private static final long serialVersionUID = -611386808382331665L;
	
	public String item_no;//项目序号	
	public String item_name;//项目名称	
	public String specimen;//标本	
	public String item_code;//项目代码	
	public String test_no;//申请序号	
	public String dept_name;//科室名称	
	public String result_status;//结果状态
	public String requested_date_time;//申请日期及时间	
	public String billing_indicator;//计价标志	
	public String priority_indicator;//优先标志	
	public String charge_type;//费别	
	public String notes_for_spcm;//标本说明	
	public String performed_by;//执行科室	
	public String relevant_clinic_diag;//临床诊断	
	public String name;//姓名	
	public String sex;//性别	
	public String age;//年龄	
	public String ordering_dept;//申请科室	
	public String patient_id;//病人标识号	
	public String prdering_provider;//申请医生	
	public String item_name_temp;//项目名称整合，用|隔开
	
	public InspectionEntity() {
		
	}
	/**
	 * 
	* @Title: init 
	* @Description: TODO(初始化实体) 
	* @param @param arrayList
	* @param @return    设定文件 
	* @return ArrayList<InspectionEntity>    实体集合
	* @throws
	 */
	public static ArrayList<InspectionEntity> init(ArrayList<DataEntity> arrayList){
		ArrayList<InspectionEntity> list=new ArrayList<InspectionEntity>();
		for (int i = 0; i < arrayList.size(); i++) {
			InspectionEntity entity=new InspectionEntity();
			if("4".equals(arrayList.get(i).get("result_status"))){
				entity.result_status="确认报告";
			}else{
				entity.result_status="未确认报告";
			}
			entity.item_no=arrayList.get(i).get("item_no");
			entity.item_name=arrayList.get(i).get("item_name");
			entity.item_name_temp=arrayList.get(i).get("item_name"); //检验曲线图使用
			entity.specimen=arrayList.get(i).get("specimen");
			entity.item_code=arrayList.get(i).get("item_code");
			entity.test_no=arrayList.get(i).get("test_no");
			entity.dept_name=arrayList.get(i).get("dept_name");
			//entity.result_status=arrayList.get(i).get("result_status");
			entity.requested_date_time=arrayList.get(i).get("requested_date_time");
			entity.billing_indicator=arrayList.get(i).get("billing_indicator");
			entity.priority_indicator=arrayList.get(i).get("priority_indicator");
			entity.charge_type=arrayList.get(i).get("charge_type");
			entity.notes_for_spcm=arrayList.get(i).get("notes_for_spcm");
			entity.performed_by=arrayList.get(i).get("performed_by");
			entity.relevant_clinic_diag=arrayList.get(i).get("relevant_clinic_diag");
			entity.name=arrayList.get(i).get("name");
			entity.sex=arrayList.get(i).get("sex");
			entity.age=arrayList.get(i).get("age");
			entity.ordering_dept=arrayList.get(i).get("ordering_dept");
			entity.patient_id=arrayList.get(i).get("patient_id");
			//entity.prdering_provider=arrayList.get(i).get("prdering_provider");
			entity.prdering_provider=arrayList.get(i).get("ordering_provider");
			list.add(entity);	
		}
		//将申请序号相同的项目名称合并在一起
		ArrayList<InspectionEntity> list_end=new ArrayList<InspectionEntity>();
		int k=0;
		for (int j = 0; j < list.size(); j++) {
			if (k!=0){
					String  test_n1=list.get(j).test_no;  
					String  test_n0=list.get(j-1).test_no; 
					if (test_n0.equals(test_n1)){					
						list.get(k-1).item_name=list.get(k-1).item_name + '('+list.get(j).item_no+')'+list.get(j).item_name;
						list.get(k-1).item_name_temp=list.get(k-1).item_name_temp+')'+list.get(j).item_name_temp;
						list.set(k-1, list.get(k-1));
					}else{
							list_end.add(list.get(k-1));
							Log.e("申请时间-->", list.get(k-1).requested_date_time.toString());
							k=j;
							list.get(k).item_name ='('+list.get(j).item_no+')'+list.get(j).item_name;
							list.get(k).item_name_temp=')'+list.get(j).item_name_temp;
							list.set(k, list.get(k));
							k++;
					}
			}else{
					list.get(j).item_name ='('+list.get(j).item_no+')'+list.get(j).item_name;
					list.get(j).item_name_temp=')'+list.get(j).item_name_temp;
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
