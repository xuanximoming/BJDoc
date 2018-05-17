package com.android.hospital.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.UpdateDBTask;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.GroupOrderEntity;
import com.android.hospital.entity.UserEntity;
import com.android.hospital.service.MyService;
import com.android.hospital.util.TLDefaultUpdateListener;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;
import com.github.snowdream.android.app.updater.UpdateFormat;
import com.github.snowdream.android.app.updater.UpdateManager;
import com.github.snowdream.android.app.updater.UpdateOptions;
import com.github.snowdream.android.app.updater.UpdatePeriod;
import com.github.snowdream.android.util.Log;

import java.util.ArrayList;

/**
 * @author Ukey zhang yeshentianyue@sina.com
 * @ClassName: LoginActivity
 * @Description: TODO(登录界面)
 * @date 2012-12-14 上午11:37:58
 */
public class LoginActivity extends Activity implements OnClickListener {

    private SharedPreferences sp; //轻型的数据存储方式
    private EditText mUserEditText, mPwdEditText; //用户名、密码
    private CheckBox mCheckBox;    //记住登录状态
    private Button mOkBut;
    private ArrayList<GroupOrderEntity> groupOrderList;//套餐医嘱集合
    private HospitalApp app;
    private ArrayList<UserEntity> userList;//用户实体列表
    private Button mServerIpBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        app = (HospitalApp) getApplication();
        sp = getSharedPreferences("UserInfo", 0);
        initView();
        isAutoLogin(); //是否记住密码
        WebServiceHelper.SERVERURL = "http://" + Util.getServerIP(LoginActivity.this) + "/WebServiceServer/services/GeneralOpSQL";
        new UpdateDBTask(this).execute();//更新任务
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        startService(intent);
    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: initView
     * @Description: TODO(初始化控件)
     */
    private void initView() {
        mUserEditText = (EditText) findViewById(R.id.login_username);
        mPwdEditText = (EditText) findViewById(R.id.login_password);
        mCheckBox = (CheckBox) findViewById(R.id.login_checkBox1);
        mOkBut = (Button) findViewById(R.id.login_ok);
        mServerIpBut = (Button) findViewById(R.id.server_ip);
        mOkBut.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(new MyCheckedChangeListener());
        mServerIpBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
                final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
                final EditText editText = (EditText) textEntryView.findViewById(R.id.edit1);
                editText.setText("" + Util.getServerIP(LoginActivity.this));
                new AlertDialog.Builder(LoginActivity.this).setTitle("提示:").setView(textEntryView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String timeStr = editText.getText().toString();
                                if (TextUtils.isEmpty(timeStr)) {
                                    return;
                                }
                                Util.setServerIP(LoginActivity.this, timeStr);
                            }
                        }
                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).create().show();
            }
        });

        findViewById(R.id.auto_update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateManager manager = new UpdateManager(LoginActivity.this);
                UpdateOptions options = new UpdateOptions.Builder(LoginActivity.this)
                        .checkUrl("http://" + Util.getServerIP(LoginActivity.this) + "/WebServiceServer/download/updateinfo.xml")
                        .updateFormat(UpdateFormat.XML)
                        .updatePeriod(new UpdatePeriod(UpdatePeriod.EACH_TIME))
                        .checkPackageName(true)
                        .build();
                manager.check(LoginActivity.this, options, new TLDefaultUpdateListener());
            }
        });
        Button autoUpdateBut = (Button) findViewById(R.id.auto_update);
        autoUpdateBut.setText("版本更新" + Util.getVersion(this));
    }

    //记住密码监听事件内部类
    private class MyCheckedChangeListener implements OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            //getSharedPreferences（）第一个参数是存储时的名称，第二个参数则是文件的打开方式
            sp = getSharedPreferences("UserInfo", 0);
            //将checkbox选择的结果，放入sp中
            sp.edit().putBoolean("ischoose", isChecked).commit();
        }
    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: isAutoLogin
     * @Description: TODO(是否记住密码)
     */
    private void isAutoLogin() {
        boolean flag = sp.getBoolean("ischoose", false);
        //sp.getBoolean（）第一个为取的参数，第二个是如果第一个参数没有取到或者为null，则用第二个参数
        if (flag) {
            String username = sp.getString("username", "");
            String pwd = sp.getString("password", "");
            mUserEditText.setText(username);
            mPwdEditText.setText(pwd);
            mCheckBox.setChecked(flag);
        }
    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: onClick
     * @Description: TODO(点击事件)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_ok:
                String dbuser = mUserEditText.getText().toString();
                String pwd = mPwdEditText.getText().toString();
                if (dbuser.equals("") || pwd.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    new LoginTask().execute(dbuser,pwd); //执行登录任务
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: login
     * @Description: TODO(登录初始化)
     */
    private void login() {
        if (mCheckBox.isChecked()) {
            //MODE_WORLD_READABLE允许其他应用程序读取本应用创建的文件;
            //MODE_WORLD_WRITEABLE允许其他应用程序写入本应用程序创建的文件，会覆盖原数据。
            sp = getSharedPreferences("UserInfo", Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
            sp.edit().putString("username", mUserEditText.getText().toString()).commit();
            sp.edit().putString("password", mPwdEditText.getText().toString()).commit();
        }
    }

    /**
     * @author Ukey zhang yeshentianyue@sina.com
     * @ClassName: LoginTask
     * @Description: TODO(登录任务)
     * @date 2012-12-18 下午11:06:46
     */
    private class LoginTask extends AsyncTask<String, Void, Object> {

        private MyProssDialog mDialog;
        @Override
        protected void onPreExecute() {
            mDialog = new MyProssDialog(LoginActivity.this, "登录", "正在登录，请稍候...");
            mDialog.setCancelable();
        }

        @Override
        protected Object doInBackground(String... params) {
            // TODO Auto-generated method stub
            String db_user = params[0].trim().toUpperCase();
            String pwd = params[1].trim().toUpperCase();
            Log.e("doInBackgrounddb_user语句--》", db_user + "  " + pwd);
            String result = WebServiceHelper.getUserName(db_user, pwd);
            Log.e("result语句--》", result);
            String usersql = " select a.title,a.surgery_class ,a.name,a.input_code,b.db_user,b.user_dept from staff_dict a,users b"
                    + " where a.name=b.user_name and a.dept_code=b.user_dept and db_user='" + db_user + "'";

            Log.e("sql语句--》", usersql);
            ArrayList<DataEntity> dataList = WebServiceHelper.getWebServiceData(usersql);
            userList = UserEntity.init(dataList);
            app.setLoginName(db_user);
            app.setUserEntity(userList.get(0));//设置登录医生
            startGroupOrderTask(); //执行获取套餐医嘱任务
            return result;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            mDialog.cancel();
            if (null == result || result.equals("空")) {
                Toast.makeText(getApplicationContext(), "登录失败，用户名或密码错误!", Toast.LENGTH_SHORT).show();
            } else {
                //String[] strArr=result.split("<;>");
                //app.setDoctor(strArr[0]);//设置医生名字
                app.setDoctor(userList.get(0).name);//设置医生名字
                app.setUserList(userList);//设置用户信息
                endGroupOrderTask();
                login();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            /*
			if (userList.size()==0) {
				Toast.makeText(getApplicationContext(), "登录失败，用户名或密码错误!", Toast.LENGTH_SHORT).show();
			}else{
				//String[] strArr=result.split("<;>"); //字符串拆分
				app.setDoctor(userList.get(0).name);//设置医生名字
				app.setUserList(userList);//设置用户信息
				endGroupOrderTask(); //结束套餐医嘱任务
				login();//登录初始化
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}*/
        }
    }


    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: startGroupOrderTask
     * @Description: TODO(套餐医嘱任务)
     */
    private void startGroupOrderTask() {
        String sql = "select group_order_master.group_order_id ,group_order_master.title from group_order_master,group_order_selection" +
                " where group_order_master.group_order_id=group_order_selection.group_order_id" +
                " and title is not null and group_order_selection.user_name='" + app.getLoginName() + "'";
        ArrayList<DataEntity> dataList = WebServiceHelper.getWebServiceData(sql);
        groupOrderList = new ArrayList<GroupOrderEntity>();
        for (int i = 0; i < dataList.size(); i++) {
            GroupOrderEntity entity = new GroupOrderEntity();
            entity.group_order_id = dataList.get(i).get("group_order_id");
            entity.title = dataList.get(i).get("title");
            groupOrderList.add(entity);
        }
    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: endGroupOrderTask
     * @Description: TODO(套餐医嘱任务)
     */
    private void endGroupOrderTask() {
        app.setGroupOrderList(groupOrderList);
    }
}
