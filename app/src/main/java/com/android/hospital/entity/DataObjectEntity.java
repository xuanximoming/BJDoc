package com.android.hospital.entity;

import java.io.Serializable;

public class DataObjectEntity implements Serializable{

	private static final long serialVersionUID = -6342770665544341374L;

	private String key;
	private String value;
	
	public DataObjectEntity(){
		
	}
	
	public DataObjectEntity(String key,String value){
		this.key=key;
		this.value=value;
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
    /**
     * 
    * @Title: setValue 
    * @Description: TODO(如果从服务器获得的某个字段为空或者==null，则赋值为空字符串); 
    * @param @param value    设定文件 
    * @return void    返回类型 
    * @throws
     */
	public void setValue(String value) {
		value=value.trim();
		if (value==null||value.equals("null")) {
			value="";
		}
		this.value = value;
	}
	
	
}
