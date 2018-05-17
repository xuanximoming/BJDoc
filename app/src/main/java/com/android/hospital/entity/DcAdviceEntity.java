package com.android.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
* @ClassName: DcAdviceEntity 
* @Description: TODO(医嘱实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:38:27 
*
 */
public class DcAdviceEntity implements Serializable{

	private static final long serialVersionUID = 7911361690361081062L;

	public String order_no;//医嘱代码，区分子医嘱和主医嘱	
	public String freq_counter;//频率次数	
    public String freq_interval;//频率间隔   
    public String freq_interval_unit;//频率间隔单位   
    public String order_status;//在ORDER_STATUS_DICT中查看（新开医嘱标识为1即可）   
    public String ordering_dept;//开医嘱科室 staff_dict.dept_code    
    public String drug_billing_attr;//药品计价属性 反映药品是否计价，0-正常，1-自带药(一般是0）   
    public String order_sub_no;//子医嘱序号   
    public String order_code;//药品代码   
    public String order_class;//医嘱类别，A为药品   
    public String repeat_indicator;//长期   
    public String start_date_time;//下达时间--------           
    public String order_text;//医嘱内容-   
    public String dosage;//剂量   
    public String dosage_units;//单位-------  
    public String administration;//途径
    public String frequency;//频次    
    public String perform_schedule;//执行时间    
	public String stop_date_time;//结束时间	
	public String freq_detail;//医生说明-	
	public String doctor;//执行医生	
	public String is_basic;//是否基药	
	public String antibiotic;//是否抗生素
	
	//新增医嘱所需的属性
	public String enter_date_time;//录入时间	
	public String patient_id;//病人id
	public String visit_id;	//住院标识
	public String billing_attr;	
	public String doctor_user;	
	public String drug_spec;//药品规格	
	
	public DcAdviceEntity(){
		
	}
		
	public static ArrayList<DcAdviceEntity> init(ArrayList<DataEntity> dataList) {

		ArrayList<DcAdviceEntity> list=new ArrayList<DcAdviceEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			DcAdviceEntity entity=new DcAdviceEntity();
			entity.order_no=dataList.get(i).get("order_no").trim();
			entity.freq_counter=dataList.get(i).get("freq_counter").trim();
			entity.freq_interval=dataList.get(i).get("freq_interval").trim();
			entity.freq_interval_unit=dataList.get(i).get("freq_interval_units").trim();
			entity.order_status=dataList.get(i).get("order_status").trim();
			entity.ordering_dept=dataList.get(i).get("ordering_dept").trim();
			entity.drug_billing_attr=dataList.get(i).get("drug_billing_attr").trim();
			entity.order_sub_no=dataList.get(i).get("order_sub_no").trim();
			entity.order_code=dataList.get(i).get("order_code").trim();
			entity.order_class=dataList.get(i).get("order_class").trim();
			entity.repeat_indicator=dataList.get(i).get("repeat_indicator").trim();
			entity.start_date_time=dataList.get(i).get("start_date_time").trim();
			entity.enter_date_time=dataList.get(i).get("enter_date_time").trim();
			entity.order_text=dataList.get(i).get("order_text").trim();
			entity.dosage=dataList.get(i).get("dosage").trim();
			entity.dosage_units=dataList.get(i).get("dosage_units").trim();
			entity.administration=dataList.get(i).get("administration").trim();
			entity.perform_schedule=dataList.get(i).get("perform_schedule").trim();
			entity.stop_date_time=dataList.get(i).get("stop_date_time").trim();
			entity.enter_date_time=dataList.get(i).get("enter_date_time").trim();
			entity.freq_detail=dataList.get(i).get("freq_detail").trim();
			entity.doctor=dataList.get(i).get("doctor").trim();
			entity.frequency=dataList.get(i).get("frequency").trim();
			entity.is_basic=dataList.get(i).get("is_basic").trim();
			entity.antibiotic=dataList.get(i).get("antibiotic").trim();
			list.add(entity);
		}
		return list;
	}
}
