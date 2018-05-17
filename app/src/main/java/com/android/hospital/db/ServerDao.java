package com.android.hospital.db;

import java.nio.Buffer;
import java.util.Map;

import android.R.string;
import android.net.UrlQuerySanitizer.ValueSanitizer;

import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.util.DebugUtil;

/**
 * 
* @ClassName: ServerDao 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 ����9:00:51 
*
 */
public class ServerDao {

	/**
	 * 
	* @Title: getInsert 
	* @param @param orders
	* @throws
	 */
	public static String getInsertOrders(DcAdviceEntity entity){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into orders (start_date_time,order_text,dosage" +
				                          ",dosage_units,freq_detail,administration,frequency" +
				                          ",freq_counter,freq_interval,freq_interval_unit" +
				                          ",doctor,order_class,repeat_indicator,stop_date_time" +
				                          ",order_code,order_status" +
				                          ",enter_date_time,patient_id,visit_id" +
						                  ",order_no,order_sub_no,perform_schedule" +
						                  ",billing_attr,ordering_dept" +
				                          ",drug_billing_attr,doctor_user) "+
				       "values (");
		buffer.append("TO_DATE('"+entity.start_date_time+"','yyyy-MM-dd hh24:mi:ss')").append(",");
		buffer.append("'"+entity.order_text+"'").append(",");
		buffer.append("'"+entity.dosage+"'").append(",");
		buffer.append("'"+entity.dosage_units+"'").append(",");
		buffer.append("'"+entity.freq_detail+"'").append(",");
		buffer.append("'"+entity.administration+"'").append(",");
		buffer.append("'"+entity.frequency+"'").append(",");
		buffer.append("'"+entity.freq_counter+"'").append(",");
		buffer.append("'"+entity.freq_interval+"'").append(",");
		buffer.append("'"+entity.freq_interval_unit+"'").append(",");
		buffer.append("'"+entity.doctor+"'").append(",");
		buffer.append("'"+entity.order_class+"'").append(",");
		buffer.append("'"+entity.repeat_indicator+"'").append(",");
		buffer.append("TO_DATE('"+entity.stop_date_time+"','yyyy-MM-dd hh24:mi:ss')").append(",");
		buffer.append("'"+entity.order_code+"'").append(",");
		buffer.append("'"+entity.order_status+"'").append(",");
		buffer.append("TO_DATE('"+entity.enter_date_time+"','yyyy-MM-dd hh24:mi:ss')").append(",");
		buffer.append("'"+entity.patient_id+"'").append(",");
		buffer.append("'"+entity.visit_id+"'").append(",");
		buffer.append("'"+entity.order_no+"'").append(",");
		buffer.append("'"+entity.order_sub_no+"'").append(",");
		buffer.append("'"+entity.perform_schedule+"'").append(",");
		buffer.append("'"+entity.billing_attr+"'").append(",");
		buffer.append("'"+entity.ordering_dept+"'").append(",");
		buffer.append("'"+entity.drug_billing_attr+"'").append(",");
		buffer.append("'"+entity.doctor_user+"'").append(")");
		DebugUtil.debug("Serverdao--->"+buffer.toString());
		return buffer.toString();
	}
	
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(����ҽ���Ƽ۱���Ϣ) 
	* @param @param Orders_costs
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws
	 */
	
	public static String getInsertOrders_costs(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into orders_costs ( item_name,amount,units,item_code,item_class,item_no,"+
					           "order_sub_no,order_no,visit_id,patient_id,costs,item_spec)  "+
                      "values(");
		buffer.append("'"+map.get("item_name")+"'").append(",");
		buffer.append("'"+map.get("amount")+"'").append(",");
		buffer.append("'"+map.get("units")+"'").append(",");
		buffer.append("'"+map.get("item_code")+"'").append(",");
		buffer.append("'"+map.get("item_class")+"'").append(",");
		buffer.append("'"+map.get("item_no")+"'").append(",");
		buffer.append("'"+map.get("order_sub_no")+"'").append(",");
		buffer.append("'"+map.get("order_no")+"'").append(",");
		buffer.append("'"+map.get("visit_id")+"'").append(",");
		buffer.append("'"+map.get("patient_id")+"'").append(",");
		buffer.append("'"+map.get("costs")+"'").append(",");
		buffer.append("'"+map.get("item_spec")+"'").append(")");
		//buffer.append("'"+map.get("backbill_rule")+"'").append(")");
		return buffer.toString();
	}
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(������ԤԼ��¼����Ϣ) 
	* @param @param ���� exam_appoints
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertExam_appoints(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into exam_appoints (exam_no, patient_id,name,name_phonetic,"+
		                       "sex,date_of_birth, birth_place,identity,charge_type,mailing_address,"+
						       "zip_code,phone_number,exam_class,exam_sub_class,clin_symp,phys_sign,"+
						       "relevant_lab_test,relevant_diag,clin_diag,exam_mode,exam_group,"+
						       "performed_by,patient_source,facility,req_date_time,req_dept,req_physician,"+
						       "visit_id,doctor_user,notice) " +
				     "values (");
		buffer.append("'"+map.get("exam_no")+"'").append(",");
		buffer.append("'"+map.get("patient_id")+"'").append(",");
		buffer.append("'"+map.get("name")+"'").append(",");
		buffer.append("'"+map.get("name_phonetic")+"'").append(",");
		buffer.append("'"+map.get("sex")+"'").append(",");
		buffer.append("'"+map.get("date_of_birth")+"'").append(",");
		buffer.append("'"+map.get("birth_place")+"'").append(",");
		buffer.append("'"+map.get("identity")+"'").append(",");
		buffer.append("'"+map.get("charge_type")+"'").append(",");
		buffer.append("'"+map.get("mailing_address")+"'").append(",");
		buffer.append("'"+map.get("zip_code")+"'").append(",");
		buffer.append("'"+map.get("phone_number")+"'").append(",");
		buffer.append("'"+map.get("exam_class")+"'").append(",");
		buffer.append("'"+map.get("exam_sub_class")+"'").append(",");
		buffer.append("'"+map.get("clin_symp")+"'").append(",");
		buffer.append("'"+map.get("phys_sign")+"'").append(",");
		buffer.append("'"+map.get("relevant_lab_test")+"'").append(",");
		buffer.append("'"+map.get("relevant_diag")+"'").append(",");
		buffer.append("'"+map.get("clin_diag")+"'").append(",");
		buffer.append("'"+map.get("exam_mode")+"'").append(",");
		buffer.append("'"+map.get("exam_group")+"'").append(",");
		buffer.append("'"+map.get("performed_by")+"'").append(",");
		buffer.append("'"+map.get("patient_source")+"'").append(",");
		buffer.append("'"+map.get("facility")+"'").append(",");
		buffer.append(map.get("req_date_time")).append(",");
		buffer.append("'"+map.get("req_dept")+"'").append(",");
		buffer.append("'"+map.get("req_physician")+"'").append(",");
		buffer.append("'"+map.get("visit_id")+"'").append(",");
		buffer.append("'"+map.get("doctor_user")+"'").append(",");
		buffer.append("'"+map.get("notice")+"'").append(")");
		return buffer.toString();
	}
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(��������Ŀ��¼����Ϣ) 
	* @param @param ����exam_items
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertExam_items(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into  exam_items (exam_no,exam_item_no, exam_item,exam_item_code) "+
		               "values(");
		buffer.append("'"+map.get("exam_no")+"'").append(",");
		buffer.append("'"+map.get("exam_item_no")+"'").append(",");
		buffer.append("'"+map.get("exam_item")+"'").append(",");
		buffer.append("'"+map.get("exam_item_code")+"'").append(")");
		return buffer.toString();
		
	}
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(�����������¼) 
	* @param @param ����lab_test_master
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertLab_test_master(Map<String, String> map ){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into lab_test_master (test_no,priority_indicator,patient_id,visit_id,name,sex,age,"+
								 "relevant_clinic_diag ,specimen,notes_for_spcm,requested_date_time,ordering_dept,"+
								 "ordering_provider,performed_by,name_phonetic ,charge_type ,result_status) "+
				      "values(");
		buffer.append("'"+map.get("test_no")+"'").append(",");
		buffer.append("'"+map.get("priority_indicator")+"'").append(",");
		buffer.append("'"+map.get("patient_id")+"'").append(",");
		buffer.append("'"+map.get("visit_id")+"'").append(",");
		buffer.append("'"+map.get("name")+"'").append(",");
		buffer.append("'"+map.get("sex")+"'").append(",");
		buffer.append("'"+map.get("age")+"'").append(",");
		buffer.append("'"+map.get("relevant_clinic_diag")+"'").append(",");
		buffer.append("'"+map.get("specimen")+"'").append(",");
		buffer.append("'"+map.get("notes_for_spcm")+"'").append(",");
		buffer.append(map.get("requested_date_time")).append(",");
		buffer.append("'"+map.get("ordering_dept")+"'").append(",");
		buffer.append("'"+map.get("ordering_provider")+"'").append(",");
		buffer.append("'"+map.get("performed_by")+"'").append(",");
		buffer.append("'"+map.get("name_phonetic")+"'").append(",");
		buffer.append("'"+map.get("charge_type")+"'").append(",");
		buffer.append("'"+map.get("result_status")+"'").append(")");	
		return buffer.toString();
	}
	
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(���������Ŀ��) 
	* @param @param ����lab_test_items
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertLab_test_items(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into lab_test_items( test_no,item_no,item_name,item_code) "+
						"values(");
		buffer.append("'"+map.get("test_no")+"'").append(",");
		buffer.append("'"+map.get("item_no")+"'").append(",");
		buffer.append("'"+map.get("item_name")+"'").append(",");
		buffer.append("'"+map.get("item_code")+"'").append(")");
		return buffer.toString();
	}
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(���뵽��ҩסԺ��������¼��) 
	* @param @param ����doct_drug_presc_master
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertDoct_drug_presc_master(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into doct_drug_presc_master(patient_id,visit_id,name,name_phonetic,presc_date,"+
								"identity,charge_type,ordered_by,prescribed_by,presc_attr,dispensary,presc_source,"+
								"unit_in_contract,presc_no,presc_type,repetition,costs,payments,entered_by,"+
								"presc_status,dispensing_provider,count_per_repetition,usage,binding_presc_title,"+
								"discharge_taking_indicator,doctor_user,decoction,newly_print,diagnosis_name)  "+
						"values (");
		buffer.append("'"+map.get("patient_id")+"'").append(",");
		buffer.append("'"+map.get("visit_id")+"'").append(",");
		buffer.append("'"+map.get("name")+"'").append(",");
		buffer.append("'"+map.get("name_phonetic")+"'").append(",");
		buffer.append(map.get("presc_date")).append(",");
		buffer.append("'"+map.get("identity")+"'").append(",");
		buffer.append("'"+map.get("charge_type")+"'").append(")");
		buffer.append("'"+map.get("ordered_by")+"'").append(",");
		buffer.append("'"+map.get("prescribed_by")+"'").append(",");
		buffer.append("'"+map.get("presc_attr")+"'").append(",");
		buffer.append("'"+map.get("dispensary")+"'").append(",");
		buffer.append("'"+map.get("presc_source")+"'").append(",");
		buffer.append("'"+map.get("unit_in_contract")+"'").append(")");	
		buffer.append("'"+map.get("presc_no")+"'").append(",");
		buffer.append("'"+map.get("presc_type")+"'").append(",");
		buffer.append("'"+map.get("repetition")+"'").append(",");
		buffer.append("'"+map.get("costs")+"'").append(",");
		buffer.append("'"+map.get("payments")+"'").append(",");
		buffer.append("'"+map.get("entered_by")+"'").append(")");	
		buffer.append("'"+map.get("presc_status")+"'").append(",");
		buffer.append("'"+map.get("dispensing_provider")+"'").append(",");
		buffer.append("'"+map.get("count_per_repetition")+"'").append(",");
		buffer.append("'"+map.get("usage")+"'").append(",");
		buffer.append("'"+map.get("binding_presc_title")+"'").append(",");
		buffer.append("'"+map.get("discharge_taking_indicator")+"'").append(",");	
		buffer.append("'"+map.get("doctor_user")+"'").append(",");	
		buffer.append("'"+map.get("decoction")+"'").append(",");	
		buffer.append("'"+map.get("newly_print")+"'").append(",");	
		buffer.append("'"+map.get("diagnosis_name")+"'").append(")");		
		return buffer.toString();
	}
	/**
	 * 
	* @Title: getInsert 
	* @Description: TODO(���뵽��ҩסԺ������ϸ��¼��) 
	* @param @param ����doct_drug_presc_detail
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws 
	 */
	public static String getInsertDoct_drug_presc_detail(Map<String, String> map){
		StringBuffer buffer=new StringBuffer();
		buffer.append("insert into doct_drug_presc_detail(presc_date,presc_no,item_no,order_no,order_sub_no,"+
									"drug_code,drug_spec,drug_name,firm_id,package_spec,package_units,"+
									"quantity,costs,payments,administration,dosage,dosage_units,amount_per_package,"+
									"frequency,dosage_each,freq_detail,is_basic) "+  
						"values (");
		buffer.append(map.get("presc_date")).append(",");
		buffer.append("'"+map.get("presc_no")+"'").append(",");
		buffer.append("'"+map.get("item_no")+"'").append(",");
		buffer.append("'"+map.get("order_no")+"'").append(",");
		buffer.append("'"+map.get("order_sub_no")+"'").append(",");
		buffer.append("'"+map.get("drug_code")+"'").append(",");
		buffer.append("'"+map.get("drug_spec")+"'").append(",");
		buffer.append("'"+map.get("drug_name")+"'").append(",");
		buffer.append("'"+map.get("firm_id")+"'").append(",");
		buffer.append("'"+map.get("package_spec")+"'").append(",");
		buffer.append("'"+map.get("package_units")+"'").append(",");
		buffer.append("'"+map.get("quantity")+"'").append(",");
		buffer.append("'"+map.get("costs")+"'").append(",");
		buffer.append("'"+map.get("payments")+"'").append(",");
		buffer.append("'"+map.get("administration")+"'").append(",");
		buffer.append("'"+map.get("dosage")+"'").append(",");
		buffer.append("'"+map.get("dosage_units")+"'").append(",");
		buffer.append("'"+map.get("amount_per_package")+"'").append(",");
		buffer.append("'"+map.get("frequency")+"'").append(",");
		buffer.append("'"+map.get("dosage_each")+"'").append(",");
		buffer.append("'"+map.get("freq_detail")+"'").append(",");
		buffer.append("'"+map.get("is_basic")+"'").append(")");
		return buffer.toString();
	}
	
	public static String getQuery(String tableName,String[] paramArray1){
		return getQuery(tableName, paramArray1, null, null);
	}
	/**
	 * 
	* @Title: getQuery 
	* @Description: TODO(�õ���ѯ���) 
	* @param @param ����
	* @param @param �ֶ�1
	* @param @param �ֶ�2
	* @param @param ��ѯ���� 
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws
	 */
	//select group_name,group_code from staff_group_dict where group_class='����ҽ��'
	public static String getQuery(String tableName,String[] paramArray1,String[] whereArr,String[] paramArray2){
		StringBuffer buffer=new StringBuffer("select ");
		for (int i = 0; i < paramArray1.length; i++) {
			if (i==(paramArray1.length-1)) {
				buffer.append(paramArray1[i]);
			}else {
				buffer.append(paramArray1[i]).append(",");
			}
		}
		buffer.append(" from "+tableName);
		if (paramArray2==null||whereArr==null) {
			DebugUtil.debug("getQuery--->"+buffer.toString());
			return buffer.toString();
		}else {
			buffer.append(" where "+whereArr[0]+"='"+paramArray2[0]+"'");
			if (paramArray2.length>1) {
				for (int i = 1; i < paramArray2.length; i++) {
					buffer.append(" and "+whereArr[i]+"='"+paramArray2[i]+"'");
				}
			}
			DebugUtil.debug("getQuery--->"+buffer.toString());
			return buffer.toString();
		}
	}
	
	/**
	 * 
	* @Title: getQueryCustom 
	* @Description: TODO(�Զ���where�������) 
	* @param @param tableName
	* @param @param paramArray1
	* @param @param custom
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws
	 */
	public static String getQueryCustom(String tableName,String[] paramArray1,String customWhere){
		String sql1=getQuery(tableName, paramArray1);
		String sql2=sql1+" "+customWhere;
		//DebugUtil.debug("getQueryCustom--->"+sql2);
		return sql2;
	}
	
	/**
	 * 
	* @Title: getDelete 
	* @Description: TODO(�õ�ɾ������) 
	* @param @return    �趨�ļ� 
	* @return String    �������� 
	* @throws
	 */
	public static String getDelete(String tableName,String[] paramArray1,String[] paramArray2){
		return null;
	}
}
