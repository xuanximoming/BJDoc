package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
* @ClassName: PrescriptionEntity 
* @Description: TODO(处方实体) 
* @author lll
* @date 2012-12-19  
*
 */
public class PrescriptionEntity implements Serializable{
	
	public String presc_no;//处方号	
	public String presc_date;//申请日期	
	public String prescribed_by;//开单医生	
	public String presc_type;//处方类型 0 西药 1中药	
	public String repetition;//剂数	
	public String costs;//总花费	
	public String presc_status;//处方状态 1发药 0未发药	
	public String dept_name;//发药药房
	
	public PrescriptionEntity(){
		
	}
	
	public static ArrayList<PrescriptionEntity> init(ArrayList<DataEntity> arrayList){
		ArrayList<PrescriptionEntity> list=new ArrayList<PrescriptionEntity>();
		for (int i = 0; i < arrayList.size(); i++) {
			PrescriptionEntity entity=new PrescriptionEntity();
			if("0".equals(arrayList.get(i).get("presc_type")))
			{
				entity.presc_type="西药";
			}else{
				if("1".equals(arrayList.get(i).get("presc_type")))
				{
					entity.presc_type="中药";
				}
			}
			if("0".equals(arrayList.get(i).get("presc_status")))
			{
				entity.presc_status="未发药";
			}else{
				if("1".equals(arrayList.get(i).get("presc_status")))
				{
					entity.presc_status="已发药";
				}
			}
			entity.presc_no=arrayList.get(i).get("presc_no");
			entity.presc_date=arrayList.get(i).get("presc_date");
			entity.prescribed_by=arrayList.get(i).get("prescribed_by");
			//entity.presc_type=arrayList.get(i).get("presc_type");
			entity.repetition=arrayList.get(i).get("repetition");
			entity.costs=arrayList.get(i).get("costs");
			//entity.presc_status=arrayList.get(i).get("presc_status");
			entity.dept_name=arrayList.get(i).get("dept_name");
			list.add(entity);	
		}
		return list;		
	}
}
