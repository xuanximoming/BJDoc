package com.android.hospital.entity;
/**
 * 
* @ClassName: GroupDcAdviceEntity 
* @Description: TODO(套餐医嘱) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2013-1-1 下午4:07:58 
*
 */
public class GroupOrderEntity {
	
	public String group_order_id;//医嘱模板标识（唯一）	
	public String title;//模板名称	
	public String dept_code;//所属科室	
	public String creator_id;//建立者代码	
	public String last_mondify_date_time;//最后修改日期	
	public String permission;//级别	
	public String input_code;//输入码
	
}
