package com.android.hospital.entity;

import java.util.ArrayList;

/**
 * 
* @ClassName: PrescriptiondetailEntity 
* @Description: TODO(处方明细实体) 
* @author lll 
* @date 2012-12-20 
*
 */
public class PrescriptiondetailEntity {

	public String PRESC_DATE;//处方日期	
	public String PRESC_NO;//处方号	
	public String ITEM_NO;//序号	
	public String DRUG_NAME;//药品名称	
	public String PACKAGE_SPEC;//规格	
	public String FIRM_ID;//厂商	
	public String DOSAGE_EACH;//单次计量	
	public String DOSAGE_UNITS;//单位	
	public String ADMINISTRATION;//途径	
	public String FREQUENCY;//频次	
	public String FREQ_DETAIL;//医生说明	
	public String QUANTITY;//总量	
	public String PACKAGE_UNITS;//单位	
	public String COSTS;//计价	
	
	public PrescriptiondetailEntity(){
		
	}
	
	/**
	 * 
	* @Title: init 
	* @Description: TODO(获取处方明细集合) 
	* @param @param dataList
	* @param @return    设定文件 
	* @return ArrayList<PrescriptiondetailEntity>    返回类型 
	* @throws
	 */
	public static ArrayList<PrescriptiondetailEntity> init(ArrayList<DataEntity> dataList ){
		ArrayList<PrescriptiondetailEntity> arrayList=new ArrayList<PrescriptiondetailEntity>();
		for (int i = 0; i < dataList.size(); i++) {
			PrescriptiondetailEntity entity=new PrescriptiondetailEntity();
			entity.PRESC_DATE=dataList.get(i).get("presc_date");
			entity.PRESC_NO=dataList.get(i).get("presc_no");
			entity.ITEM_NO=dataList.get(i).get("item_no");
			entity.DRUG_NAME=dataList.get(i).get("drug_name");
			entity.PACKAGE_SPEC=dataList.get(i).get("package_spec");
			entity.FIRM_ID=dataList.get(i).get("firm_id");
			entity.DOSAGE_EACH=dataList.get(i).get("dosage_each");
			entity.DOSAGE_UNITS=dataList.get(i).get("dosage_units");
			entity.ADMINISTRATION=dataList.get(i).get("administration");
			entity.FREQUENCY=dataList.get(i).get("frequency");
			entity.FREQ_DETAIL=dataList.get(i).get("freq_detail");
			entity.QUANTITY=dataList.get(i).get("quantity");
			entity.PACKAGE_UNITS=dataList.get(i).get("package_units");
			entity.COSTS=dataList.get(i).get("costs");
			arrayList.add(entity);
		}
		return arrayList;
	}
	
	

}
