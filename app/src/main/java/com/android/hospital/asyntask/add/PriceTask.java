package com.android.hospital.asyntask.add;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.BaseAsyncTask;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.webservice.WebServiceHelper;

/**
 * 
* @ClassName: PriceTask 
* @Description: TODO(计价任务) 
* @author lll
* @date 2012-12-25 
*
 */
public class PriceTask extends BaseAsyncTask{

	private Context mContext;
	
	private DcAdviceEntity mAdviceEntity;
	
	private int isDrug=0;//是否为药品，0为是，1为非药品
	
	private String item_name ;//计价项目名称。
	
	private String amount;//数量。
	
	private String units;//计价单位。
	
	private String item_code;//计价项目代码。
	
	private String item_class;//计价项目类别。
	
	private String item_no="1";//计价项目序号。
	
	private String costs;//本项目累计费用 
	
	private String item_spec;//计价项目规格 
	
	private String backbill_rule;//计价规则。
	
	private String price; //单价
	
	private String dose_per_unit;//单位剂量
	
	private String charge_item_class;//途径中的项目类别
	
	private String charge_item_code;//非药品中的项目代码
	
	private String charge_item_spec;//非药品中的项目规格
	
	private String charge_item_amount;//途径中的项目数量
	
	private String charge_item_name;//途径中的项目名称
	
	private String charge_units;//途径中的项目单位
	
	private HospitalApp app;
	
	private PatientEntity patientEntity;//单个病人实体
	
	
	public PriceTask(Context context,DcAdviceEntity dcAdviceEntity,int isDrug){
		this.mContext=context;
		this.mAdviceEntity=dcAdviceEntity; //医嘱实体
		this.isDrug=isDrug;
		this.app=(HospitalApp) mContext.getApplicationContext();
		patientEntity=app.getPatientEntity();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		first_getData();	
		return null;
	}
	/**
	 * 
	* 计价过程描述：
	* 1）方法：first_getData()  由order_code 获得价表中的项目代码，以及项目的规格（根据药品/非药品，获取不同的item_code） 
	* 2）方法：second_getData(String item_code_temp)   由项目代码，以及项目的规格  获得项目对应价表的信息  
	* 3）方法：getCountValue()   如果是药品，需要换算药品数量
	* 4）方法：insertData()  插入到orders_costs表中
	* 5）方法：administration_costs() 如果有途径，根据途径获得计价项目 ，执行4）
	* @author lll
	* @date 2012-12-25 
	*
	 */
	//1）获得价表中的项目代码，以及项目的规格
	public void first_getData(){
		String sqlString="SELECT "+
    					"PRICE_ITEM_NAME_DICT.ITEM_NAME,"+ //价表项目名称
    					"CLINIC_VS_CHARGE.CHARGE_ITEM_CLASS,"+ //计价项目类别
    					"CLINIC_VS_CHARGE.CHARGE_ITEM_SPEC,"+ //计价项目规格
    					"CLINIC_VS_CHARGE.CLINIC_ITEM_CODE,"+ //诊疗项目代码
    					"CLINIC_VS_CHARGE.CHARGE_ITEM_CODE,"+ //计价项目代码
    					"PRICE_ITEM_NAME_DICT.ITEM_CODE,"+ // 价表项目代码
    					"CLINIC_VS_CHARGE.UNITS,"+ //单位
    					"CLINIC_VS_CHARGE.AMOUNT,"+ //数量
    					"CLINIC_VS_CHARGE.BACKBILL_RULE"+ //后台计费规则
    			 " FROM CLINIC_VS_CHARGE, CLINIC_ITEM_NAME_DICT, PRICE_ITEM_NAME_DICT"+
    			 " WHERE (CLINIC_VS_CHARGE.CLINIC_ITEM_CLASS ="+
    			        "CLINIC_ITEM_NAME_DICT.ITEM_CLASS) "+
    			   "and (CLINIC_VS_CHARGE.CLINIC_ITEM_CODE = CLINIC_ITEM_NAME_DICT.ITEM_CODE) "+
    			   "and (CLINIC_VS_CHARGE.CHARGE_ITEM_CLASS = PRICE_ITEM_NAME_DICT.ITEM_CLASS) "+
    			   "and (CLINIC_VS_CHARGE.CHARGE_ITEM_CODE = PRICE_ITEM_NAME_DICT.ITEM_CODE) "+
    			   "and ((CLINIC_ITEM_NAME_DICT.ITEM_CODE = '"+mAdviceEntity.order_code+"')) "+
    			   "and (PRICE_ITEM_NAME_DICT.STD_INDICATOR = 1) "+
    			   "and (CLINIC_ITEM_NAME_DICT.STD_INDICATOR = 1) "+
    			   "and (CLINIC_ITEM_NAME_DICT.ITEM_CLASS IN ('A', 'B', 'H','I','E','Z'))";
		ArrayList<DataEntity> dcbeacs=WebServiceHelper.getWebServiceData(sqlString);
		//循环得到clinic_item_code 的值，以便赋给second_getData（）
        String clinic_item_code=""; //诊疗项目代码
	    for (int i = 0; i < dcbeacs.size(); i++) {
			clinic_item_code=dcbeacs.get(i).get("clinic_item_code").trim();
			item_name = dcbeacs.get(i).get("item_name").trim();
			backbill_rule = dcbeacs.get(i).get("backbill_rule").trim();
			item_class = dcbeacs.get(i).get("charge_item_class").trim();
			charge_item_code = dcbeacs.get(i).get("charge_item_code").trim();
			charge_item_spec= dcbeacs.get(i).get("charge_item_spec").trim();
			amount= dcbeacs.get(i).get("amount").trim();	
		}
		if (isDrug==0) {
			second_getData(clinic_item_code);//药品
			getCountValue();//进行药品数量换算
			insertData();
			int sub_no=Integer.parseInt(mAdviceEntity.order_sub_no);
    	    if(sub_no<=1){
    	    	administration_costs();
    	    }
		}else {
			second_getData(charge_item_code);//非药品
			insertData();
		}
	}
	//2)获得项目对应价表的信息
	public void second_getData(String item_code_temp){
		if(isDrug==0){
			item_spec = mAdviceEntity.drug_spec;//药品的规格，过后由实体传值，mAdviceEntity-----------------------
		}else{
			item_spec = charge_item_spec;//非药品获得的规格，由1）中的sql语句得到	
		}	
		String sqlString="SELECT PRICE_LIST.ITEM_CODE,"+
								"PRICE_LIST.ITEM_SPEC,"+
								"PRICE_LIST.ITEM_NAME,"+
								"PRICE_LIST.UNITS,"+
								"PRICE_LIST.PRICE"+
						" FROM PRICE_LIST"+
						" WHERE PRICE_LIST.ITEM_CLASS IN ('A', 'B', 'H','K','I','J','L','Z','E')"+
						" and PRICE_LIST.ITEM_CODE ='"+item_code_temp+"'"+
						" and sysdate >= START_DATE"+
						" and (PRICE_LIST.STOP_DATE IS NULL or SYSDATE <= STOP_DATE)"+
						" and item_spec ='"+item_spec+"'";
		ArrayList<DataEntity> dcbeacs=WebServiceHelper.getWebServiceData(sqlString);
		//循环得到clinic_item_code 的值，以便赋给getCountValue（）
		for (int i = 0; i < dcbeacs.size(); i++) {
			units =dcbeacs.get(i).get("units");	
			item_spec = dcbeacs.get(i).get("item_spec");
			price = dcbeacs.get(i).get("price");
			item_code = dcbeacs.get(i).get("item_code");
			if(price != null){
    			costs = price;
    		}else{
    			costs = "";
    		}
		}
	}
	//3）如果是药品，需要换算药品数量
	public void getCountValue(){
    	String string = "select drug_dict.dose_per_unit, drug_dict.units, drug_dict.dose_units"+
    				    " from drug_dict, drug_price_list "+
    			        " where drug_price_list.drug_code = drug_dict.drug_code "+
    			          " and drug_price_list.drug_spec = drug_dict.drug_spec "+
    			          " and drug_price_list.stop_date is null  "+
    			          " and drug_price_list.drug_code = '"+mAdviceEntity.order_code+"'  "+
    			          " and  drug_price_list.drug_spec||drug_price_list.firm_id = '"+mAdviceEntity.drug_spec+"'";//------由实体传回-----
    	ArrayList<DataEntity> aList=WebServiceHelper.getWebServiceData(string);
		for (int i = 0; i < aList.size(); i++) {
			dose_per_unit=aList.get(i).get("dose_per_unit");//单位剂量
		}
		if(dose_per_unit!= null && !dose_per_unit.equals("") ){
			double double1  =  Double.parseDouble(dose_per_unit);//单位剂量
			double double2;//药品实体的单次剂量
			if(mAdviceEntity.dosage.equals("")){
				double2 = 0.0;
			}else{
				double2 = Double.parseDouble(mAdviceEntity.dosage);
			}
			double  count = Math.ceil(double2/double1); // 总数量=单次剂量/单位剂量
			amount = String.valueOf(count);
			int int_count = (int)count; //总数量转换成int型
			//计算累计花费
			if(price != null){
				float fcosts = Float.parseFloat(price);
				costs = String.valueOf(fcosts*int_count);
			}
		}
		amount= amount.trim();
//		insertData();		
    }
	//4）插入到orders_costs表中
	public void insertData(){	    	
	    	if(units == null||units == " "){
	    		units = "";
	    	}
	    	if(amount == null||amount == " "){
	    		amount = "";
	    	}
	    	if(item_spec == null||item_spec == " "){
	    		item_spec = "";
	    	}
	    	if(item_code == null||item_code == " "){
	    		item_code = "";
	    	}
	    	int sub_no=Integer.parseInt(mAdviceEntity.order_sub_no);
	    	if(sub_no>1){
	    		String sqlQuery="select max(item_no) from orders_costs  "+
	    	                    "where patient_id ='"+patientEntity.patient_id+"' "+
	    				        " and order_no='"+mAdviceEntity.order_no+"'";
	    		ArrayList<DataEntity> cList=WebServiceHelper.getWebServiceData(sqlQuery);	    		
	    		for (int i = 0; i < cList.size(); i++) {
	    			String numberStr=cList.get(i).get("max(item_no)").trim();
	    			if (!numberStr.equals("")) {
	    				int number=Integer.parseInt(numberStr)+1;
	    				item_no=String.valueOf(number);
					}				
				}
	    	}
	    	String sql="insert into orders_costs ("+ 
	    			"item_name,"+  //计价项目名称
	    			"amount,"+      //数量
	    			"units,"+       //计价单位
	    			"item_code,"+   //计价项目代码
	    			"item_class,"+  //计价项目类别
	    			"item_no,"+     //计价项目序号
	    			"order_sub_no,"+//医嘱子序号 
	    			"order_no,"+    //医嘱序号 
	    			"visit_id,"+    //病人本次住院标识 
	    			"patient_id,"+ //病人标识号 
	    			"costs,"+      //本项目累计费用 
	    			"item_spec,"+    //计价项目规格 
	    			"backbill_rule )"+ //计价规则
	    	" values ( '"+item_name+"', '"+amount+"', '"+units+"', '"+item_code+"','"+item_class+"',"+
	    			 "'"+item_no+"', '"+mAdviceEntity.order_sub_no+"', '"+mAdviceEntity.order_no+"'," +
	    			 "'"+patientEntity.visit_id+"', '"+patientEntity.patient_id+"','"+costs+"',"+
	    			 "'"+item_spec+"', '"+backbill_rule+"')";
	    	WebServiceHelper.insertWebServiceData(sql);
	}
	//5）根据途径获得的计价项目
	public void  administration_costs(){
		//如果途径不为空， 则通过以下语句取出  项目类别、项目代码、项目规格 
		String sql="SELECT PRICE_ITEM_NAME_DICT.ITEM_NAME, "+ //价表项目名称
						"CLINIC_VS_CHARGE.CHARGE_ITEM_CLASS, "+  //计价项目类别
						"CLINIC_VS_CHARGE.CHARGE_ITEM_SPEC, "+ //计价项目规格
						"CLINIC_VS_CHARGE.CLINIC_ITEM_CODE, "+  //诊疗项目代码
						"CLINIC_VS_CHARGE.CHARGE_ITEM_CODE,  "+ //计价项目代码
						"PRICE_ITEM_NAME_DICT.ITEM_CODE,  "+ //价表代码
						"CLINIC_VS_CHARGE.UNITS,  "+ //规格
						"CLINIC_VS_CHARGE.AMOUNT, "+ //数量
						"CLINIC_VS_CHARGE.BACKBILL_RULE "+  // 后台计费规则
				  " FROM CLINIC_VS_CHARGE, CLINIC_ITEM_NAME_DICT, PRICE_ITEM_NAME_DICT "+ 
				  "WHERE (CLINIC_VS_CHARGE.CLINIC_ITEM_CLASS = CLINIC_ITEM_NAME_DICT.ITEM_CLASS) "+ 
				    "and (CLINIC_VS_CHARGE.CLINIC_ITEM_CODE = CLINIC_ITEM_NAME_DICT.ITEM_CODE) "+ 
				    "and (CLINIC_VS_CHARGE.CHARGE_ITEM_CLASS = PRICE_ITEM_NAME_DICT.ITEM_CLASS) "+ 
				    "and (CLINIC_VS_CHARGE.CHARGE_ITEM_CODE = PRICE_ITEM_NAME_DICT.ITEM_CODE) "+ 
				    "and ((CLINIC_ITEM_NAME_DICT.ITEM_NAME = '"+mAdviceEntity.administration+"')) "+ 
				    "and (PRICE_ITEM_NAME_DICT.STD_INDICATOR = 1) "+ 
				    "and (CLINIC_ITEM_NAME_DICT.ITEM_CLASS = 'E') ";
	    ArrayList<DataEntity> blist=WebServiceHelper.getWebServiceData(sql);
	    for (int i = 0; i < blist.size(); i++) {
	    	charge_item_class=blist.get(i).get("charge_item_class");
	    	charge_item_code=blist.get(i).get("charge_item_code");
	    	charge_item_spec = blist.get(i).get("charge_item_spec");
	    	backbill_rule=blist.get(i).get("backbill_rule");
	    	amount = blist.get(i).get("amount").trim();
	    	item_name = blist.get(i).get("item_name");
	    	//通过 项目类别、项目代码、项目规格 及以下语句取出价格
	    	String sqlprice = "select price_list.item_code,"+
	     		                    " price_list.item_spec,"+
	     		                    " price_list.item_name,"+
	     		                    " price_list.units,"+
	     		                    " price_list.price,"+
	     		                    " price_list.item_class"+
	     		              "  from price_list"+
	     		              " where price_list.item_class = '"+charge_item_class+"'"+
	     		              "   and price_list.item_code = '"+charge_item_code+"'"+
	     		              "   and sysdate >= start_date"+
	     		              "   and (price_list.stop_date is null or sysdate <= stop_date)"+
	     		              "   and item_spec = '"+charge_item_spec+"'";
	     	ArrayList<DataEntity> cList=WebServiceHelper.getWebServiceData(sqlprice);    	
	    	item_no=String.valueOf(((i+1)+1));
	    	for (int j = 0; j < cList.size(); j++) {
	    		item_spec = cList.get(j).get("item_spec");
		    	item_name = cList.get(j).get("item_name");
		    	item_class = cList.get(j).get("item_class");
		    	item_code = cList.get(j).get("item_code");
		    	costs = cList.get(j).get("price");
		    	units =  cList.get(j).get("units");
		    	insertData();//插入到orders_costs
			}
	    	
	    }
	}
}
