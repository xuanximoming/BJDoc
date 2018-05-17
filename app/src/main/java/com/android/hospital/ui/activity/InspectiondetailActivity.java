package com.android.hospital.ui.activity;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.InspectionDetailAdapter;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionEntity;
import com.android.hospital.entity.InspectiondetailEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @ClassName: InspectionInfoActivity
 * @Description: TODO(检验明细显示)
 * @author Ukey zhang yeshentianyue@sina.com
 * @date 2012-12-19 下午3:27:47
 *
 */
public class InspectiondetailActivity extends Activity{

    private ListView mListView;
    private View mProcessView; //显示明细信息的view
    private TextView mEmptyView; //当数据为空，显示
    private TextView tev1,tev2,tev3;//基本信息显示
    private InspectionEntity inspectionEntity;//检验实体
    private HospitalApp app;
    private PatientEntity patientEntity;//病人实体
    private ArrayList<InspectiondetailEntity> arrayList;//检验明细信息列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inspection_list_detail);
        Intent intent=getIntent();
        inspectionEntity=(InspectionEntity) intent.getSerializableExtra("inspection");
        app=(HospitalApp) getApplication();
        this.patientEntity=app.getPatientEntity();
        initView();
        new InspectiondetailTask().execute();//执行检验明细任务
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                InspectiondetailEntity itemEntity=arrayList.get(arg2);
                String value=itemEntity.report_item_name;

                String valueend=value+ '('+getItemName();

                Intent intent=new Intent(InspectiondetailActivity.this,InspectionChartActivity.class);
                intent.putExtra("value", valueend);
                startActivity(intent);
            }
        });
    }

    /**
     *
     * @Title: initView
     * @Description: TODO(初始化控件)
     * @param     设定文件
     * @return void    返回类型
     * @throws
     */
    private void initView() {
        mListView=(ListView) findViewById(R.id.inspection_detail_listview);
        mProcessView=findViewById(R.id.progressContainer);
        mEmptyView=(TextView) findViewById(R.id.inspection_empty);
        tev1=(TextView) findViewById(R.id.patient_information_item1);
        tev2=(TextView) findViewById(R.id.patient_information_item2);
        tev3=(TextView) findViewById(R.id.patient_information_item3);

        tev1.setText("床号："+patientEntity.bed_no+"   姓名："+patientEntity.name+"  性别："+patientEntity.sex
                +"   病人ID："+patientEntity.patient_id+"   剩余预交金："+patientEntity.prepayments+"   费别："+patientEntity.charge_type);
        tev2.setText("临床诊断："+patientEntity.diagnosis+"   检验科室："+inspectionEntity.dept_name+"   标本："+inspectionEntity.specimen);
        tev3.setText(inspectionEntity.item_name);
        mListView.setVisibility(View.INVISIBLE);//隐藏
        mProcessView.setVisibility(View.VISIBLE);//可见
        mEmptyView.setVisibility(View.INVISIBLE);//隐藏

        findViewById(R.id.text_layout).setVisibility(View.GONE);
    }

    /**
     *
     * @ClassName: InspectiondetailTask
     * @Description: TODO(获取明细任务)
     * @author Ukey zhang yeshentianyue@sina.com
     * @date 2012-12-19 下午4:06:59
     *
     */
    private class InspectiondetailTask extends AsyncTask<Void, Void, Object>{

        @Override
        protected Object doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(getSql());
            arrayList=InspectiondetailEntity.init(dataList);
            return arrayList;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            mProcessView.setVisibility(View.GONE);//隐藏
            if (arrayList.size()!=0) {
                mListView.setVisibility(View.VISIBLE); //可见
                InspectionDetailAdapter adapter=new InspectionDetailAdapter(InspectiondetailActivity.this, arrayList);
                mListView.setAdapter(adapter);
            }else {
                mEmptyView.setVisibility(View.VISIBLE);//如果没有数据，显示“没有数据”
            }
        }
    }

    /**
     *
     * @Title: getSql
     * @Description: TODO(得到获取明细sql语句)
     * @param     设定文件
     * @return void    返回类型
     * @throws
     */
    private String getSql(){
        String tableName="LAB_RESULT";
        String[] paramArray1=new String[]{"TO_CHAR(LAB_RESULT.RESULT_DATE_TIME,'yyyy-MM-dd hh24:mi:ss') as RESULT_DATE_TIME","LAB_RESULT.REPORT_ITEM_NAME",
                "LAB_RESULT.RESULT","LAB_RESULT.UNITS","LAB_RESULT.PRINT_CONTEXT","LAB_RESULT.ABNORMAL_INDICATOR"};
        String[] whereArr=new String[]{"LAB_RESULT.TEST_NO"};
        String[] paramArray2=new String[]{inspectionEntity.test_no};
        String sql=ServerDao.getQuery(tableName, paramArray1, whereArr, paramArray2);
        return sql;
    }
    /**
     *
     * @Title: getItemname
     * @Description: TODO(获取项目的语句,检验曲线图使用)
     * @param     设定文件
     * @return void    返回类型
     * @throws
     */
    public String getItemName(){
        String  str=inspectionEntity.item_name_temp;
        String str_temp="";
        String[] arrStrings=str.split("\\)");
        for (int i=0;i<arrStrings.length-1;i++){
            str_temp+="'"+arrStrings[i]+"',";
        }
        str_temp+="'"+arrStrings[arrStrings.length-1]+"'";
        return str_temp;
    }

}
