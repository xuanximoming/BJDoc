package com.android.hospital.ui.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DcAdviceAdapter;
import com.android.hospital.adapter.InspectionAdapter;
import com.android.hospital.adapter.PrescriptionAdapter;
import com.android.hospital.asyntask.BaseAsyncTask.AsyncTaskCallback;
import com.android.hospital.asyntask.CheckTask;
import com.android.hospital.asyntask.DcAdviceTask;
import com.android.hospital.asyntask.DepartmentTask;
import com.android.hospital.asyntask.InspectionTask;
import com.android.hospital.asyntask.PrescriptionTask;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.fragment.CheckFragment;
import com.android.hospital.ui.fragment.DoctorAdviceFragment;
import com.android.hospital.ui.fragment.EmrFragment;
import com.android.hospital.ui.fragment.EvaluationFragment;
import com.android.hospital.ui.fragment.InspectionFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.PrescriptionFragment;
import com.android.hospital.ui.fragment.SignsLifeFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Ukey zhang yeshentianyue@sina.com
 * @ClassName: MainActivity
 * @Description: TODO(程序主界面)
 * @date 2012-12-14 上午11:36:57
 */
public class MainActivity extends Activity implements AsyncTaskCallback<PatientEntity> {

    private LeftListFragment leftFm; //左侧病人列表界面
    private Spinner mSpinner; //科室列表
    private TextView titleTev; //病人信息text
    private ActionBar actionBar; //菜单按钮
    private List<AsyncTask> asyncTasks = null;
    private PatientEntity patientEntity; //病人实体
    private HospitalApp app;
    private EvaluationFragment evfragment;
    private Button startBut, endBut; //开始，结束时间按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (HospitalApp) getApplication();
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);//设置可点击
        actionBar.setLogo(R.drawable.logo_new);
        actionBar.setDisplayShowTitleEnabled(false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (leftFm == null) {
            leftFm = new LeftListFragment();
            ft.add(R.id.main_left_panel, leftFm, "leftfm");
        }
        ft.commit();
        asyncTasks = new ArrayList<AsyncTask>();
        mSpinner = (Spinner) findViewById(R.id.main_left_department_spinner);
        titleTev = (TextView) findViewById(R.id.main_patient_info_tev);
        setSpinner();
        addTabFragment();
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: setSpinner
     * @Description: TODO(设置科室spinner)
     */
    public void setSpinner() {
/*		String sql=ServerDao.getQuery("staff_group_dict",
                                       new String[]{"group_name","group_code"},
				                       new String[]{"group_class"}, 
				                       new String[]{"病区医生"})
				    + "order by group_code" ;
        Log.d("MainActivity","sql--->"+sql);
		new DepartmentTask(this, sql).execute();*/

        String sqls = "select  b.dept_name,a.group_code from staff_group_dict a, dept_dict b,staff_vs_group c,staff_dict d " +
                "  where a.group_code=b.dept_code  " +
                "  and  c.emp_no=d.emp_no " +
                "  and a.group_code=c.group_code " +
                "  and a.group_class='病区医生'" +
                "  and d.user_name ='" + app.getUserEntity().db_user + "'";
        Log.e("科室设置---》", sqls);
        new DepartmentTask(this, sqls).execute();
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: setTitleTev
     * @Description: TODO(设置病人基本信息text显示)
     */
    public void setTitleTev(PatientEntity patientEntity) {
        titleTev.setText("ID号：" + patientEntity.patient_id + "  姓名：" + patientEntity.name + "  性别：" + patientEntity.sex
                + "  年龄：" + String.valueOf(Util.userBirthdayGetAge(patientEntity.date_of_birth))
                + " 剩余预交金：" + patientEntity.charge + " 费别：" + patientEntity.charge_type + "  诊断：" + patientEntity.diagnosis);
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: addTabFragment
     * @Description: TODO(设置标签按钮)  设定文件
     */
    public void addTabFragment() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab()
                .setText("医嘱")
                .setTabListener(new TabListener<DoctorAdviceFragment>(
                        this, "dcadvice", DoctorAdviceFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("检查")
                .setTabListener(new TabListener<CheckFragment>(
                        this, "check", CheckFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("检验")
                .setTabListener(new TabListener<InspectionFragment>(
                        this, "inspection", InspectionFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("处方")
                .setTabListener(new TabListener<PrescriptionFragment>(
                        this, "prescription", PrescriptionFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("生命体征")
                .setTabListener(new TabListener<SignsLifeFragment>(
                        this, "signslife", SignsLifeFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("病历")
                .setTabListener(new TabListener<EmrFragment>(
                        this, "emr", EmrFragment.class)));
		/*actionBar.addTab(actionBar.newTab()
			     .setText("住院评估")
			     .setTabListener(new TabListener<EvaluationFragment>(
	                        this, "evaluation", EvaluationFragment.class)));
		/*actionBar.addTab(actionBar.newTab()
			     .setText("护理模块")
			     .setTabListener(new TabListener<NurseFragment>(
	                        this, "nurse", NurseFragment.class)));*/
		/*actionBar.addTab(actionBar.newTab()
			     .setText("每日评估")
			     .setTabListener(new TabListener<DayEstimateFragment>(
	                        this, "dayestimate", DayEstimateFragment.class)));*/
    }

    /**
     * @author Ukey zhang yeshentianyue@sina.com
     * @ClassName: TabListener
     * @Description: TODO(按钮监听)
     * @date 2012-12-14 上午11:36:57
     */
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
            ft.add(R.id.main_right_frame, mFragment, mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                ft.detach(mFragment);
                ft.commit();
            }
        }

        //选中标签项目
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(R.id.main_right_frame, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        //未选中标签项目
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        //重复选中
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: getSingle
     * @Description: TODO(执行各项目任务)
     */
    @Override
    public void getSingle(PatientEntity value) {
        // TODO Auto-generated method stub
        DebugUtil.debug("测试getSingle" + value.name);
        this.patientEntity = value;
        setTitleTev(value);
        cancleAsyncTask();
        putDcAdviceTask(value, "");
        putCheckTask(value, "");
        putInspectionTask(value, "");
        putPrescriptionTask(value, "");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        evfragment = (EvaluationFragment) this.getFragmentManager().findFragmentByTag("evaluation");
        if (actionBar.getSelectedNavigationIndex() == 5) {
            if (evfragment != null) {
                evfragment = new EvaluationFragment();
                transaction.replace(R.id.main_right_frame, evfragment);
                transaction.commit();
            }
        } else {
            Log.e("saldfhlasnha", "aaaaa");
            if (evfragment != null) {
                transaction.remove(evfragment);
                transaction.commit();
            }
        }
    }

    /**
     * @param @param value    设定文件
     * @return void    返回类型
     * @throws
     * @Title: putDcAdviceTask
     * @Description: TODO(医嘱任务)
     */
    public void putDcAdviceTask(PatientEntity value, String query) {
        DebugUtil.debug("测试put");
        DoctorAdviceFragment fragment = (DoctorAdviceFragment) getFragmentManager().findFragmentByTag("dcadvice");
        if (fragment != null) {
            DcAdviceAdapter adapter = (DcAdviceAdapter) fragment.getListAdapter();
            if (null != adapter && adapter.getCount() != 0) {
                adapter.clearAdapter();
                if (fragment.isAdded()) {
                    fragment.setListShown(false);
                    fragment.setEmptyText("");
                }
            }
            String tableName = "orders";
            String[] paramArray1 = new String[]{"repeat_indicator", "TO_CHAR(start_date_time,'yyyy-MM-dd hh24:mi:ss') as start_date_time",
                    "order_text", "dosage", "dosage_units", "administration", "frequency", "perform_schedule",
                    "TO_CHAR(stop_date_time,'yyyy-MM-dd hh24:mi:ss') as stop_date_time", "freq_detail", "doctor",
                    "order_no", "freq_counter", "freq_interval", "freq_interval_unit", "order_status", "ordering_dept",
                    "order_sub_no", "order_code", "order_class", "drug_billing_attr", "is_basic", "antibiotic"};
            String[] whereArr = new String[]{"patient_id", "visit_id"};
            String[] paramArray2 = new String[]{value.patient_id, value.visit_id};
            String sql = ServerDao.getQuery(tableName, paramArray1, whereArr, paramArray2);
            sql = sql + query;
            putAsyncTask(new DcAdviceTask(fragment, sql).execute());
        }

    }

    /**
     * @param @param value    设定文件
     * @return void    返回类型
     * @throws
     * @Title: putCheckTask
     * @Description: TODO(检查任务)
     */
    public void putCheckTask(PatientEntity value, String query) {
        CheckFragment fragment = (CheckFragment) getFragmentManager().findFragmentByTag("check");
        if (fragment != null) {
            fragment.clearAdapter();
            String tableName = "exam_master a,exam_report b,exam_items c";
            String[] paramArray1 = new String[]{"a.result_status", "a.exam_no", "a.patient_id", "a.visit_id", "a.name", "a.sex",
                    "a.date_of_birth", "c.exam_item", "a.exam_class", "a.exam_sub_class",
                    "TO_CHAR(a.req_date_time,'yyyy-MM-dd hh24:mi:ss') as req_date_time",
                    "a.req_physician", "TO_CHAR(a.report_date_time,'yyyy-MM-dd hh24:mi:ss') as report_date_time",
                    "a.reporter", "b.description", "b.impression"};
            String customWhere = "where a.exam_no = b.exam_no(+) and a.exam_no = c.exam_no and a.patient_id = '" + value.patient_id + "' "
                    + "and a.visit_id = '" + value.visit_id + "'  ";
            String sql = ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
            sql = sql + query + "order by a.req_date_time";
            putAsyncTask(new CheckTask(fragment, sql).execute());
        }
    }

    /**
     * @param @param value    设定文件
     * @return void    返回类型
     * @throws
     * @Title: putInspectionTask
     * @Description: TODO(检验任务)
     */
    public void putInspectionTask(PatientEntity value, String query) {
        InspectionFragment fragment = (InspectionFragment) getFragmentManager().findFragmentByTag("inspection");
        if (fragment != null) {
            InspectionAdapter adapter = (InspectionAdapter) fragment.getListAdapter();
            if (null != adapter && adapter.getCount() != 0) {
                adapter.clearAdapter();
                if (fragment.isAdded()) {
                    fragment.setListShown(false);
                }
            }
            String tableName = "LAB_TEST_ITEMS ,LAB_TEST_MASTER ,DEPT_DICT,STAFF_DICT";
            String[] paramArray1 = new String[]{"LAB_TEST_ITEMS.ITEM_NO", "LAB_TEST_ITEMS.ITEM_NAME", "LAB_TEST_MASTER.SPECIMEN", "LAB_TEST_ITEMS.ITEM_CODE", "LAB_TEST_ITEMS.TEST_NO",
                    "DEPT_DICT.DEPT_NAME", "LAB_TEST_MASTER.RESULT_STATUS", "TO_CHAR(LAB_TEST_MASTER.REQUESTED_DATE_TIME,'yyyy-MM-dd hh24:mi:ss') as REQUESTED_DATE_TIME",
                    "LAB_TEST_MASTER.BILLING_INDICATOR", "LAB_TEST_MASTER.PRIORITY_INDICATOR", "LAB_TEST_MASTER.CHARGE_TYPE", "LAB_TEST_MASTER.NOTES_FOR_SPCM",
                    "LAB_TEST_MASTER.PERFORMED_BY", "LAB_TEST_MASTER.RELEVANT_CLINIC_DIAG", "LAB_TEST_MASTER.NAME", "LAB_TEST_MASTER.SEX", "LAB_TEST_MASTER.AGE",
                    "LAB_TEST_MASTER.ORDERING_DEPT", "LAB_TEST_MASTER.PATIENT_ID", "STAFF_DICT.NAME as ORDERING_PROVIDER  "};
            String customWhere = "WHERE (LAB_TEST_MASTER.TEST_NO= LAB_TEST_ITEMS.TEST_NO ) and "
                    + "( LAB_TEST_MASTER.PERFORMED_BY = DEPT_DICT.DEPT_CODE ) and "
                    + "( LAB_TEST_MASTER.PATIENT_ID = '" + value.patient_id + "' ) and "
                    + "(LAB_TEST_MASTER.VISIT_ID = '" + value.visit_id + "' ) and  "
                    + " STAFF_DICT.USER_NAME = LAB_TEST_MASTER.ORDERING_PROVIDER  ";
            String orderby = "order by LAB_TEST_ITEMS.TEST_NO,LAB_TEST_ITEMS.ITEM_NO  ";
            String sql = ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
            sql = sql + query + orderby;
            Log.e("检验语句------->", sql);
            putAsyncTask(new InspectionTask(fragment, sql).execute());
        }
    }

    /**
     * @param @param value    设定文件
     * @return void    返回类型
     * @throws
     * @Title: putPrescriptionTask
     * @Description: TODO(处方的任务)
     */
    public void putPrescriptionTask(PatientEntity value, String query) {
        PrescriptionFragment fragment = (PrescriptionFragment) getFragmentManager().findFragmentByTag("prescription");
        if (fragment != null) {
            PrescriptionAdapter adapter = (PrescriptionAdapter) fragment.getListAdapter();
            if (null != adapter && adapter.getCount() != 0) {
                adapter.clearAdapter();
                if (fragment.isAdded()) {
                    fragment.setListShown(false);
                }
            }
            String tableName = "DOCT_DRUG_PRESC_MASTER, dept_dict";
            String[] paramArray1 = new String[]{"DOCT_DRUG_PRESC_MASTER.PRESC_NO", "TO_CHAR(DOCT_DRUG_PRESC_MASTER.PRESC_DATE,'yyyy-MM-dd hh24:mi:ss') as PRESC_DATE",
                    "DOCT_DRUG_PRESC_MASTER.PRESCRIBED_BY", "DEPT_DICT.DEPT_NAME", "DOCT_DRUG_PRESC_MASTER.PRESC_TYPE", "DOCT_DRUG_PRESC_MASTER.REPETITION",
                    "DOCT_DRUG_PRESC_MASTER.COSTS", "DOCT_DRUG_PRESC_MASTER.PRESC_STATUS"};
            String customWhere = "WHERE DOCT_DRUG_PRESC_MASTER.Dispensary = dept_dict.dept_code  and (PRESC_STATUS not in (2, 3))  AND (DOCT_DRUG_PRESC_MASTER.PATIENT_ID = '" + value.patient_id + "') "
                    + "AND (DOCT_DRUG_PRESC_MASTER.COSTS >= 0) ";
            String orderby = "order by PRESC_DATE";
            String sql = ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
            sql = sql + query + orderby;
            putAsyncTask(new PrescriptionTask(fragment, sql).execute());
        }
    }

    /**
     * @param @param  paramAsyncTask
     * @param @return 设定文件
     * @return AsyncTask    返回类型
     * @throws
     * @Title: putAsyncTask
     * @Description: TODO(异步任务集合)
     */
    protected AsyncTask putAsyncTask(AsyncTask paramAsyncTask) {
        asyncTasks.add(paramAsyncTask);
        return paramAsyncTask;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        cancleAsyncTask();
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: cancleAsyncTask 设定文件
     * @Description: TODO(取消异步任务)
     */
    public void cancleAsyncTask() {
        for (int i = 0; i < asyncTasks.size(); i++) {
            AsyncTask localAsyncTask = (AsyncTask) asyncTasks.get(i);
            if (!(localAsyncTask == null) || (localAsyncTask.isCancelled())) {
                localAsyncTask.cancel(true);
                DebugUtil.debug("取消执行几次-->" + i);
            }
        }
        asyncTasks.clear();
        //另外一种方式
		/*Iterator localIterator=this.asyncTasks.iterator();
		if (!localIterator.hasNext()) {
			return;
		}
		AsyncTask localAsyncTask = (AsyncTask)localIterator.next();
		if ((localAsyncTask == null) || (localAsyncTask.isCancelled()))
	        continue;
	    localAsyncTask.cancel(true);*/
    }

    /**
     * @param keyCode
     * @param event
     * @return void    返回类型
     * @throws
     * @Title: onKeyDown
     * @Description: TODO(点击取消)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**    设定文件
     * @return void    返回类型
     * @throws
     * @Title: logout
     * @Description: TODO(注销系统)
     */
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // .setIcon(R.);
        builder.setMessage("真的要退出系统吗？").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * @param menu 医嘱的（全部、长期、临时）在doctorAdviceFragment中设置（11、12、13）
     * @return boolean    返回类型
     * @throws
     * @Title: onCreateOptionsMenu
     * @Description: TODO(右上角菜单项)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.addSubMenu(1, Menu.FIRST, 1, "新增医嘱").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 4, 1, "套餐医嘱").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 1, 1, "新增检查").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 2, 1, "新增检验").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 3, 1, "新增处方").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 5, 1, "手术查询").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 6, 1, "手术预约").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 7, 1, "用血查询").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, Menu.FIRST + 8, 1, "用血申请").setIcon(android.R.drawable.ic_menu_add);
        menu.addSubMenu(1, 14, 1, "按时间查询");
        menu.addSubMenu(1, Menu.FIRST + 9, 1, "自然信息").setIcon(android.R.drawable.ic_menu_add);
        return true;
    }

    /**
     * @param item 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: onOptionsItemSelected
     * @Description: TODO(右上角菜单项目选择)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (item.getItemId()) {
            case Menu.FIRST:
                intent = new Intent();
                DoctorAdviceFragment fragment = (DoctorAdviceFragment) this.getFragmentManager().findFragmentByTag("dcadvice");
                DcAdviceAdapter adapter = (DcAdviceAdapter) fragment.getListAdapter();
                DcAdviceEntity entity = (DcAdviceEntity) adapter.getItem(adapter.getCount() - 1);
                intent.putExtra("subentity", entity);
                intent.setClass(this, AddDcAdviceActivity.class);
                startActivityForResult(intent, 11);
                break;
            case Menu.FIRST + 1:
                intent = new Intent();
                intent.setClass(this, AddCheckActivity.class);
                startActivityForResult(intent, 12);
                break;
            case Menu.FIRST + 2:
                intent = new Intent();
                intent.setClass(this, AddInspectionActivity.class);
                startActivityForResult(intent, 13);
                break;
            case Menu.FIRST + 3:
                intent = new Intent();
                intent.setClass(this, AddPrescriptionActivity.class);
                startActivityForResult(intent, 14);
                break;
            case Menu.FIRST + 4:
                showGroupDc(); //套餐医嘱选择框
                break;
            case Menu.FIRST + 5:
                intent = new Intent();
                intent.setClass(this, OperationActivity.class);
                startActivity(intent);
                break;
            case Menu.FIRST + 6:
                intent = new Intent();
                intent.setClass(this, AddOperationActivity.class);
                startActivityForResult(intent, 11);
                //Toast.makeText(MainActivity.this, "功能尚未添加!", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 7:
                intent = new Intent();
                intent.setClass(this, BloodActivity.class);
                startActivity(intent);
                break;
            case Menu.FIRST + 8:
                intent = new Intent();
                intent.setClass(this, AddBloodActivity.class);
                startActivityForResult(intent, 11);
                break;
            case Menu.FIRST + 9:
                intent = new Intent();
                intent.setClass(this, PatientInforActivity.class);
                startActivity(intent);
                break;
            case 11:   //全部医嘱
                DoctorAdviceFragment fragmentall = (DoctorAdviceFragment) this.getFragmentManager().findFragmentByTag("dcadvice");
                DcAdviceAdapter adapterall = (DcAdviceAdapter) fragmentall.getListAdapter();
                adapterall.setAllList();
                break;
            case 12:  //长期医嘱
                DoctorAdviceFragment fragmentlong = (DoctorAdviceFragment) this.getFragmentManager().findFragmentByTag("dcadvice");
                DcAdviceAdapter adapterlong = (DcAdviceAdapter) fragmentlong.getListAdapter();
                adapterlong.setLongList();
                break;
            case 13:   //临时医嘱
                DoctorAdviceFragment fragmentshort = (DoctorAdviceFragment) this.getFragmentManager().findFragmentByTag("dcadvice");
                DcAdviceAdapter adaptershort = (DcAdviceAdapter) fragmentshort.getListAdapter();
                adaptershort.setShortList();
                break;
            case 14:  //按时间查询
                showDatePickerDialog();    //时间查询对话框
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * @Title: showGroupDc
     * @Description: TODO(弹出套餐医嘱选择框)
     * @param 设定文件
     * @return void    返回类型
     * @throws
     */
    private int whichButtonChoose = 0;

    private void showGroupDc() {
        new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle("医嘱模板")
                .setSingleChoiceItems(getArrayItem(), 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked on a radio button do some stuff */
                        whichButtonChoose = whichButton;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked Yes so do some stuff */
                        String id = app.getGroupOrderList().get(whichButtonChoose).group_order_id;
                        Intent intent = new Intent();
                        intent.putExtra("id", id);
                        intent.setClass(MainActivity.this, GroupDcAdviceActivity.class);
                        startActivityForResult(intent, 11);
                        whichButtonChoose = 0;//点击后，重置为零
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked No so do some stuff */
                    }
                })
                .create().show();
    }

    /**  设定文件
     * @return void    返回类型
     * @throws
     * @Title: getArrayItem
     * @Description: TODO(得到模板数组)
     */
    public String[] getArrayItem() {
        int size = app.getGroupOrderList().size();
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = app.getGroupOrderList().get(i).title;
        }
        return arr;
    }

    /**  设定文件
     * @return void    返回类型
     * @throws
     * @Title: showDatePickerDialog
     * @Description: TODO(显示时间查询对话框)
     */
    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.common_query_timedialog, null);
        startBut = (Button) textEntryView.findViewById(R.id.start_time_but);
        endBut = (Button) textEntryView.findViewById(R.id.end_time_but);
        startBut.setText(new StringBuilder().append(mYear).append("-")
                // Month is 0 based so add 1
                .append(Util.toQueryTime(mMonth + 1)).append("-")
                .append(Util.toQueryTime(mDay)));
        endBut.setText(new StringBuilder().append(mYear).append("-")
                // Month is 0 based so add 1
                .append(Util.toQueryTime(mMonth + 1)).append("-")
                .append(Util.toQueryTime(mDay)));
        startBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, mStartDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        endBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, mEndDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle("按时间查询")
                .setView(textEntryView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked OK so do some stuff */
                        timeQueryTask();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked cancel so do some stuff */
                    }
                }).create().show();
    }

    private DatePickerDialog.OnDateSetListener mStartDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    startBut.setText(
                            new StringBuilder().append(year).append("-")
                                    // Month is 0 based so add 1
                                    .append(Util.toQueryTime(monthOfYear + 1)).append("-")
                                    .append(Util.toQueryTime(dayOfMonth))
                    );
                }
            };
    private DatePickerDialog.OnDateSetListener mEndDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    endBut.setText(
                            new StringBuilder().append(year).append("-")
                                    // Month is 0 based so add 1
                                    .append(Util.toQueryTime(monthOfYear + 1)).append("-")
                                    .append(Util.toQueryTime(dayOfMonth))
                    );
                }
            };

    /**  设定文件
     * @return void    返回类型
     * @throws
     * @Title: timeQueryTask
     * @Description: TODO(根据时间查询)
     */
    private void timeQueryTask() {

        this.patientEntity = app.getPatientEntity();

        //医嘱----开始执行日期
        String dcAdviceSql = " and TO_CHAR(start_date_time,'yyyy-MM-dd')>='"
                + startBut.getText().toString()
                + "' and TO_CHAR(start_date_time,'yyyy-MM-dd')<='"
                + endBut.getText().toString() + "' ";
        //检查 ----项目申请日期
        String checkSql = " and TO_CHAR(req_date_time,'yyyy-MM-dd')>='"
                + startBut.getText().toString()
                + "' and TO_CHAR(req_date_time,'yyyy-MM-dd')<='"
                + endBut.getText().toString() + "' ";
        //检验----检验日期
        String inspectionSql = " and TO_CHAR(requested_date_time,'yyyy-MM-dd')>='"
                + startBut.getText().toString()
                + "' and TO_CHAR(requested_date_time,'yyyy-MM-dd')<='"
                + endBut.getText().toString() + "' ";
        //处方----处方日期
        String prescriptionSql = " and TO_CHAR(presc_date,'yyyy-MM-dd')>='"
                + startBut.getText().toString()
                + "' and TO_CHAR(presc_date,'yyyy-MM-dd')<='"
                + endBut.getText().toString() + "' ";

        switch (getActionBar().getSelectedNavigationIndex()) {
            case 0:
                putDcAdviceTask(patientEntity, dcAdviceSql);
                DebugUtil.debug("actionbar--->id00000");
                break;
            case 1:
                putCheckTask(patientEntity, checkSql);
                DebugUtil.debug("actionbar--->id111111");
                break;
            case 2:
                putInspectionTask(patientEntity, inspectionSql);
                DebugUtil.debug("actionbar--->id222222");
                break;
            case 3:
                putPrescriptionTask(patientEntity, prescriptionSql);
                DebugUtil.debug("actionbar--->id333333");
                break;
            default:
                break;
        }
    }
    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @return void    返回类型
     * @throws
     * @Title: onActivityResult  startActivityForResult(intent,11)
     * @Description: TODO(实现activity间传值)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (resultCode) {
            case 11:
                putDcAdviceTask(app.getPatientEntity(), "");
                break;
            case 12:
                putCheckTask(app.getPatientEntity(), "");
                putDcAdviceTask(app.getPatientEntity(), "");
                break;
            case 13:
                putInspectionTask(app.getPatientEntity(), "");
                putDcAdviceTask(app.getPatientEntity(), "");
                break;
            case 14:
                putPrescriptionTask(app.getPatientEntity(), "");
                putDcAdviceTask(app.getPatientEntity(), "");
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * getdepartpoistion()获得选中科室的位置
     * 在住院评估中，保存时使用科室的名称
     * <p>
     * (注意：现在科室里面，加了“自己的病人”，在使用时，会报错
     * 废弃不用
     */
    public int getdepartposition() {
        int position = mSpinner.getSelectedItemPosition();
        return position;
    }

    @Override
    public void getList(ArrayList<PatientEntity> values) {
        // TODO Auto-generated method stub

    }
}
