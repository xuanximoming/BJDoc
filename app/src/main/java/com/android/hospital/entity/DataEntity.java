package com.android.hospital.entity;

import java.io.Serializable;

public class DataEntity implements Serializable{

	private static final long serialVersionUID = 1410861311566301519L;
	private int arraySize =  0 ;
    private DataObjectEntity[] objArr=null;
    
    public DataEntity(){
    	
    }
    
    public DataEntity(int arraySize){
    	this.arraySize=arraySize;
    	this.objArr=new DataObjectEntity[arraySize];
    }
    
    public boolean add(int index,String key,String value) {
    	DataObjectEntity objectEntity=new DataObjectEntity();
    	objectEntity.setKey(key);
    	objectEntity.setValue(value);
    	if(index >= 0 && index <= arraySize){
    		objArr[index] = objectEntity;
			return true;
		}
		return false;
    }
    
    public String get(String key){
		for(int i=0;i<arraySize;i++){
			DataObjectEntity obj = objArr[i]; 
			if(obj.getKey()!=null){
				if(obj.getKey().equals(key)){
					return obj.getValue();
				}
			}
		}
		return "";
	}
}
