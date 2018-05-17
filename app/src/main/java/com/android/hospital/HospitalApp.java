package com.android.hospital;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.hospital.entity.BloodItemEntity;
import com.android.hospital.entity.BloodTypeEntity;
import com.android.hospital.entity.DoctorEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.GroupOrderEntity;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.entity.NonDrugEntity;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.entity.UserEntity;

import android.R.string;
import android.app.Application;
import android.util.Log;
/**
 * 
* @ClassName:  
* @Description: TODO(静态变量) 
* 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 上午8:57:34 
*
 */
public class HospitalApp extends Application{

	private PatientEntity patientEntity;//单个病人实体
	
	private UserEntity userEntity;//医生实体
	
	private ArrayList<DrugEntity> drugList;//药品集合
	
	private ArrayList<DrugEntity> middleDrugList;//处方中药房药品集合
	
	private ArrayList<NonDrugEntity> nondrugList;//非药品集合
	
	private String maxNumber;//最大序号，每次点击新增按钮，值被改变
	
	private List<Map<String, String>> freqList;//频次集合
	
	private List<Map<String, String>> wayList;//途径集合
	
	private List<Map<String, String>> classList;//检查类别集合
	
	private List<Map<String, String>> deptList;//检查科室集合
	
	private List<Map<String, String>> inspecitonClassList;//检验类别集合
	
	private List<Map<String, String>> inspectionDeptList;//检验科室集合
	
	private String loginName="";//登录账号
	
	private String doctor="";//医生名
	
	private ArrayList<InspectionItemEntity> inspectionItemList;//检验项目集合
	
	private ArrayList<OperationItemEntity> operationItemList;//手术项目集合
	
	private String nextval="";//触发器
	
	private ArrayList<GroupOrderEntity> groupOrderList;//套餐医嘱集合
	
	private ArrayList<String> departcodeList;//科室代码(手术用）
	
	private ArrayList<String> departnameList;//科室名称（手术用）
	
	private ArrayList<String> anaesthesiaList; //麻醉方法（手术预约）
	
	private ArrayList<DoctorEntity> staffdoctorList; //医生列表（手术预约）
	
	private ArrayList<BloodTypeEntity> bloodTypeList;//血液要求（用血申请）
	
	private ArrayList<UserEntity>  userList;//用户信息
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public PatientEntity getPatientEntity() {
		return patientEntity;
	}

	public void setPatientEntity(PatientEntity patientEntity) {
		this.patientEntity = patientEntity;
	}
	
	public UserEntity getUserEntity(){
		return userEntity;
	}
	
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public ArrayList<DrugEntity> getDrugList() {
		return drugList;
	}

	public void setDrugList(ArrayList<DrugEntity> drugList) {
		this.drugList = drugList;
	}

	public ArrayList<NonDrugEntity> getNondrugList() {
		return nondrugList;
	}

	public void setNondrugList(ArrayList<NonDrugEntity> nondrugList) {
		this.nondrugList = nondrugList;
	}

	public List<Map<String, String>> getFreqList() {
		return freqList;
	}

	public void setFreqList(List<Map<String, String>> freqList) {
		this.freqList = freqList;
	}

	public List<Map<String, String>> getWayList() {
		return wayList;
	}

	public void setWayList(List<Map<String, String>> wayList) {
		this.wayList = wayList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(String maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public List<Map<String, String>> getClassList() {
		return classList;
	}

	public void setClassList(List<Map<String, String>> classList) {
		this.classList = classList;
	}

	public List<Map<String, String>> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Map<String, String>> deptList) {
		this.deptList = deptList;
	}

	public List<Map<String, String>> getInspecitonClassList() {
		return inspecitonClassList;
	}

	public void setInspecitonClassList(List<Map<String, String>> inspecitonClassList) {
		this.inspecitonClassList = inspecitonClassList;
	}

	public List<Map<String, String>> getInspectionDeptList() {
		return inspectionDeptList;
	}

	public void setInspectionDeptList(List<Map<String, String>> inspectionDeptList) {
		this.inspectionDeptList = inspectionDeptList;
	}

	public ArrayList<InspectionItemEntity> getInspectionItemList() {
		return inspectionItemList;
	}

	public void setInspectionItemList(
			ArrayList<InspectionItemEntity> inspectionItemList) {
		this.inspectionItemList = inspectionItemList;
	}
	
	public ArrayList<OperationItemEntity> getOperationItemList(){
		return operationItemList;
	}
	public void setOperationItemList(
			ArrayList<OperationItemEntity> operationItemList){
		this.operationItemList = operationItemList;
	}

	public ArrayList<DrugEntity> getMiddleDrugList() {
		return middleDrugList;
	}

	public void setMiddleDrugList(ArrayList<DrugEntity> middleDrugList) {
		this.middleDrugList = middleDrugList;
	}

	public String getNextval() {
		return nextval;
	}

	public void setNextval(String nextval) {
		this.nextval = nextval;
	}

	public ArrayList<GroupOrderEntity> getGroupOrderList() {
		return groupOrderList;
	}

	public void setGroupOrderList(ArrayList<GroupOrderEntity> groupOrderList) {
		this.groupOrderList = groupOrderList;
	}
	public ArrayList<String> getDepartcodeList() {
		return departcodeList;
	}

	public void setDepartcodeList(ArrayList<String> departcodeList) {
		this.departcodeList = departcodeList;
	}
	
	public ArrayList<String> getDepartnameList(){
		return departnameList;
	}
	public void setDepartnameList(ArrayList<String> departnameList){
		this.departnameList=departnameList;
	}
	
	public ArrayList<String> getAnaesthesiaList(){
		return anaesthesiaList;
	}
	public void setAnaesthesiaList(ArrayList<String> anaesthesiaList){
		this.anaesthesiaList=anaesthesiaList;
	}
	public ArrayList<DoctorEntity> getStaff_doctorList(){
		return staffdoctorList;
	}
	public void setStaff_doctorList(ArrayList<DoctorEntity> staffdoctorList){
		this.staffdoctorList=staffdoctorList;
	}
	public ArrayList<BloodTypeEntity> getBlood_TypeList(){
		return bloodTypeList;
	}
	public void setBlood_TypeList(ArrayList<BloodTypeEntity> bloodTypeList){
		this.bloodTypeList=bloodTypeList;
	}
	public ArrayList<UserEntity> getUserList(){
		return userList;
	}
	public void setUserList(ArrayList<UserEntity> userList){
		this.userList=userList;
	}

}
