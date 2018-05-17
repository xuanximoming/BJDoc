package com.android.hospital.ui.fragment;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.android.hospital.HospitalApp;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.EvaluationInhospitalEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.activity.AddPrescriptionActivity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.activity.OperationActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.activity.R.id;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: EvaluationFragment 
* @Description: TODO(住院评估界面) 
* @author lll
* @date 2013-01-17 
*
 */
public class EvaluationFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	private View view;
	private HospitalApp app;
	private MainActivity mActivity; 
	private TextView patient_info; //病人基本信息显示
	private String patientInfo; //病人基本信息
	private String age;//记录病人的年龄
	private String departname;//病人的科室名称
	private ArrayList<EvaluationInhospitalEntity> mArrayList; //存放评估信息
	private Button mCancleBut,mOkBut,mClearBut; //取消、保存、清空按钮
	private int flag;//标识
	private int yctotal=0,zctotal=0;//压疮总分，坠床总分
	private String ycchengdu="",zcchengdu="",ttchengdu="";//压疮，坠床，疼痛程度
	private EvaluationInhospitalEntity entity;

	//////////////////////////////////////////////////////////////
	private CheckBox cbRybingqing_1,cbRybingqing_2,cbmethod_1,cbmethod_2,cbmethod_3; //入院病情，入院方式
	private EditText ethistory,ettemperature,etmaibo,etbreath,etxueya;//病史，体温，脉搏，呼吸，血压
	private EditText etjingshen_qita,etyishi_qita,eteyeproblem_qita,etearproblem_qita,etlanguage_qita,etactive_qita,etwowei_qita,etysqk_qita;//各种其他和描述
	private RadioButton rbjingshen_1,rbjingshen_2,rbjingshen_3,rbjingshen_4,rbjingshen_5;//精神状态
	private RadioButton rbyishi_1,rbyishi_2,rbyishi_3,rbyishi_4,rbyishi_5;//意识情况
	private RadioButton rbeyeproblem_1,rbeyeproblem_2,rbearproblem_1,rbearproblem_2;//视力障碍,听力障碍
	private RadioButton rblanguage_1,rblanguage_2,rblanguage_3,rblanguage_4,rbactive_1,rbactive_2,rbactive_3,rbactive_4;//语言能力，活动形态
	private EditText worksEditText;//职业
	private Spinner spinner1,spinner2,spinner3;//文化，婚姻，子女
	private RadioButton rbxiyan_1,rbxiyan_2,rbyinjiu_1,rbyinjiu_2,rbyinshi_1,rbyinshi_2,rbshuimian_1,rbshuimian_2;//吸烟，饮酒，饮食，睡眠
	private CheckBox cbhulijibie_1,cbhulijibie_2,cbhulijibie_3,cbhulijibie_4;//护理级别
	private RadioButton rbwowei_1,rbwowei_2, rbwowei_3,rbwowei_4;//卧位
	private RadioButton rbysqk_1,rbysqk_2,rbysqk_3,rbysqk_4,rbysqk_5,rbysqk_6;//饮食
	private RadioButton rbdbian_1,rbdbian_2,rbdbian_3,rbdbian_4,rbxbian_1,rbxbian_2,rbxbian_3,rbxbian_4,rbxbian_5;//大便，小便	
	private RadioButton rbpfqk_1,rbpfqk_2,rbpfqk_3,rbpfqk_4,rbpfqk_5,rbpfqk_6,rbpfqk_7,rbpfqk_8,rbpfqk_9;//皮肤情况
	private EditText etpfqk_buwei;//皮肤情况的部位
	private RadioButton rbruchuang_1,rbruchuang_2,rbtengtong_1,rbtengtong_2;//褥疮，疼痛
	private EditText etrc_buwei,etrc_chengdu,etrc_mianji,ettt_buwei,ettt_chengdu,ettt_xingzhi,ettt_time;//褥疮，疼痛的部位，程度，面积，性质
	private RadioButton rbdiedao_1,rbdiedao_2;//坠床跌倒风险
	private RadioButton rbjbrs_1,rbjbrs_2,rbjbrs_3,rbzyxz_1,rbzyxz_2,rbryxj_1,rbryxj_2;//对疾病的认识,住院须知，入院宣教
	private RadioButton rbganjue_1,rbganjue_2,rbganjue_3,rbganjue_4,rbchaoshi_1,rbchaoshi_2,rbchaoshi_3,rbchaoshi_4;//感觉，潮湿
    private RadioButton rbhdong_1,rbhdong_2,rbhdong_3,rbhdong_4,rbydong_1,rbydong_2,rbydong_3,rbydong_4;//活动,移动
    private RadioButton rbyy_1,rbyy_2,rbyy_3,rbyy_4,rbmcjq_1,rbmcjq_2,rbmcjq_3;//营养，摩擦和剪切力
    private EditText etyctotal,etycdanger,etzctotal,etzcdanger,etttdanger;//压疮，坠床，疼痛总分，危险程度
    private CheckBox cbzc_1,cbzc_2,cbzc_3,cbzc_4,cbzc_5,cbzc_6,cbzc_7,cbzc_8,cbzc_9,cbzc_10,cbzc_11,cbzc_12;//坠床跌倒评分
    private CheckBox cbtt_0,cbtt_1,cbtt_2,cbtt_3,cbtt_4,cbtt_5,cbtt_6,cbtt_7,cbtt_8,cbtt_9,cbtt_10;//疼痛评分
    private RadioButton rbyaowu_1,rbyaowu_2,rbshiwu_1,rbshiwu_2,rbqita;//过敏史
    private EditText etguominqita;//过敏其他
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	app=(HospitalApp) getActivity().getApplication();
		mActivity=(MainActivity) getActivity();
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().findViewById(R.id.listview_common_titlebar).setVisibility(View.GONE);
	    view=inflater.inflate(R.layout.fragment_add_evaluation, null);
	    patientInfo=patientInfo();
		init();
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	private void init() {
		// TODO Auto-generated method stub
		mCancleBut=(Button) view.findViewById(R.id.add_evaluation_but_cancle); 
		mOkBut=(Button) view.findViewById(R.id.add_evaluation_but_ok);
		mClearBut=(Button)view.findViewById(R.id.add_evaluation_but_clear);
		patient_info=(TextView) view.findViewById(R.id.patient_infprmation);
		patient_info.setText(patientInfo);
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		mClearBut.setOnClickListener(this);
		
		cbRybingqing_1=(CheckBox) view.findViewById(R.id.inhospital_checkbox_rybingqing_1);
		cbRybingqing_2=(CheckBox) view.findViewById(R.id.inhospital_checkbox_rybingqing_2);
		cbmethod_1=(CheckBox) view.findViewById(R.id.inhospital_checkbox_method_1);
		cbmethod_2=(CheckBox)view.findViewById(R.id.inhospital_checkbox_method_2);
		cbmethod_3=(CheckBox)view.findViewById(R.id.inhospital_checkbox_method_3);
		ethistory=(EditText) view.findViewById(R.id.inhospital_et_history);
		ettemperature=(EditText)view.findViewById(R.id.inhospital_et_temperature);
		etmaibo=(EditText)view.findViewById(R.id.inhospital_et_maibo);
		etbreath=(EditText)view.findViewById(R.id.inhospital_et_breath);
		etxueya=(EditText)view.findViewById(R.id.inhospital_et_xueya);
		rbjingshen_1=(RadioButton)view.findViewById(R.id.inhospital_rbutton_jingshen_1);
		rbjingshen_2=(RadioButton)view.findViewById(R.id.inhospital_rbutton_jingshen_2);
		rbjingshen_3=(RadioButton)view.findViewById(R.id.inhospital_rbutton_jingshen_3);
		rbjingshen_4=(RadioButton)view.findViewById(R.id.inhospital_rbutton_jingshen_4);
		rbjingshen_5=(RadioButton)view.findViewById(R.id.inhospital_rbutton_jingshen_5);
		etjingshen_qita=(EditText) view.findViewById(R.id.inhospital_et_jingshen_qita);
		rbyishi_1=(RadioButton)view.findViewById(R.id.inhospital_rbutton_yishi_1);
		rbyishi_2=(RadioButton)view.findViewById(R.id.inhospital_rbutton_yishi_2);
		rbyishi_3=(RadioButton)view.findViewById(R.id.inhospital_rbutton_yishi_3);
		rbyishi_4=(RadioButton)view.findViewById(R.id.inhospital_rbutton_yishi_4);
		rbyishi_5=(RadioButton)view.findViewById(R.id.inhospital_rbutton_yishi_5);
		etyishi_qita=(EditText) view.findViewById(R.id.inhospital_et_yishi_qita);
		rbeyeproblem_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_eyeproblem_1);
		rbeyeproblem_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_eyeproblem_2);
		eteyeproblem_qita=(EditText) view.findViewById(R.id.inhospital_et_eyeproblem_qita);
		rbearproblem_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_earproblem_1);
		rbearproblem_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_earproblem_2);
		etearproblem_qita=(EditText) view.findViewById(R.id.inhospital_et_earproblem_qita);
		rblanguage_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_language_1);
		rblanguage_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_language_2);
		rblanguage_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_language_3);
		rblanguage_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_language_4);
		etlanguage_qita=(EditText) view.findViewById(R.id.inhospital_et_language_qita);
		rbactive_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_active_1);
		rbactive_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_active_2);
		rbactive_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_active_3);
		rbactive_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_active_4);
		etactive_qita=(EditText) view.findViewById(R.id.inhospital_et_active_qita);
		worksEditText=(EditText) view.findViewById(R.id.inhospital_et_works);
		spinner1=(Spinner) view.findViewById(R.id.inhospital_spinner_cultural);
		spinner2=(Spinner) view.findViewById(R.id.inhospital_spinner_hunyin);
		spinner3=(Spinner) view.findViewById(R.id.inhospital_spinner_children);
		rbxiyan_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiyan_1);
		rbxiyan_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiyan_2);
		rbyinjiu_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinjiu_1);
		rbyinjiu_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinjiu_2);
		rbyinshi_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshi_1);
		rbyinshi_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshi_2);
		rbshuimian_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_shuimian_1);
		rbshuimian_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_shuimian_2);
		cbhulijibie_1=(CheckBox) view.findViewById(R.id.inhospital_checkbox_hulijibie_1);
		cbhulijibie_2=(CheckBox) view.findViewById(R.id.inhospital_checkbox_hulijibie_2);
		cbhulijibie_3=(CheckBox) view.findViewById(R.id.inhospital_checkbox_hulijibie_3);
		cbhulijibie_4=(CheckBox) view.findViewById(R.id.inhospital_checkbox_hulijibie_4);
		rbwowei_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_wowei_1);
		rbwowei_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_wowei_2);
		rbwowei_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_wowei_3);
		rbwowei_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_wowei_4);
		etwowei_qita=(EditText) view.findViewById(R.id.inhospital_et_wowei_qita);
		rbysqk_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_1);
		rbysqk_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_2);
		rbysqk_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_3);
		rbysqk_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_4);
		rbysqk_5=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_5);
		rbysqk_6=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yinshiqingkuang_6);
		etysqk_qita=(EditText) view.findViewById(R.id.inhospital_et_yinshiqingkuang_qita);
		rbdbian_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_dabian_1);
		rbdbian_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_dabian_2);
		rbdbian_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_dabian_3);
		rbdbian_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_dabian_4);
		rbxbian_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiaobian_1);
		rbxbian_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiaobian_2);
		rbxbian_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiaobian_3);
		rbxbian_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiaobian_4);
		rbxbian_5=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xiaobian_5);
		rbpfqk_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_1);
		rbpfqk_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_2);
		rbpfqk_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_3);
		rbpfqk_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_4);
		rbpfqk_5=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_5);
		rbpfqk_6=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_6);
		rbpfqk_7=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_7);
		rbpfqk_8=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_8);
		rbpfqk_9=(RadioButton) view.findViewById(R.id.inhospital_rbutton_pifuqingkuang_9);
		etpfqk_buwei=(EditText) view.findViewById(R.id.inhospital_et_pifuqingkuang_qita);
		rbruchuang_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ruchuang_1);
		rbruchuang_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ruchuang_2);
		etrc_buwei=(EditText) view.findViewById(R.id.inhospital_et_ruchuang_buwei);
		etrc_chengdu=(EditText) view.findViewById(R.id.inhospital_et_ruchuang_chengdu);
		etrc_mianji=(EditText) view.findViewById(R.id.inhospital_et_ruchuang_mianji);
		rbtengtong_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_tengtong_1);
		rbtengtong_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_tengtong_2);
		ettt_buwei=(EditText) view.findViewById(R.id.inhospital_et_tengtong_buwei);
		ettt_chengdu=(EditText) view.findViewById(R.id.inhospital_et_tengtong_chengdu);
		ettt_time=(EditText) view.findViewById(R.id.inhospital_et_tengtong_time);
		ettt_xingzhi=(EditText) view.findViewById(R.id.inhospital_et_tengtong_xingzhi);
		rbdiedao_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_diedao_1);
		rbdiedao_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_diedao_2);
		rbjbrs_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_jibingrenshi_1);
		rbjbrs_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_jibingrenshi_2);
		rbjbrs_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_jibingrenshi_3);
		rbzyxz_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_zhuyuanxuzhi_1);
		rbzyxz_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_zhuyuanxuzhi_2);
		rbryxj_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xuanjiao_1);
		rbryxj_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_xuanjiao_2);
		rbganjue_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ganjue_1);
		rbganjue_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ganjue_2);
		rbganjue_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ganjue_3);
		rbganjue_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_ganjue_4);
		rbchaoshi_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_chaoshi_1);
		rbchaoshi_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_chaoshi_2);
		rbchaoshi_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_chaoshi_3);
		rbchaoshi_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_chaoshi_4);
		rbhdong_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_hongdong_1);
		rbhdong_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_hongdong_2);
		rbhdong_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_hongdong_3);
		rbhdong_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_hongdong_4);
		rbydong_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yidong_1);
		rbydong_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yidong_2);
		rbydong_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yidong_3);
		rbydong_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yidong_4);
		rbyy_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yingyang_1);
		rbyy_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yingyang_2);
		rbyy_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yingyang_3);
		rbyy_4=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yingyang_4);
		rbmcjq_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_mochajianqie_1);
		rbmcjq_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_mochajianqie_2);
		rbmcjq_3=(RadioButton) view.findViewById(R.id.inhospital_rbutton_mochajianqie_3);
		etyctotal=(EditText) view.findViewById(R.id.inhospital_et_ycwx_zongfen);
		etycdanger=(EditText) view.findViewById(R.id.inhospital_et_ycwx_wxcd);
		cbzc_1=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_1);
		cbzc_2=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_2);
		cbzc_3=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_3);
		cbzc_4=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_4);
		cbzc_5=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_5);
		cbzc_6=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_6);
		cbzc_7=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_7);
		cbzc_8=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_8);
		cbzc_9=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_9);
		cbzc_10=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_10);
		cbzc_11=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_11);
		cbzc_12=(CheckBox) view.findViewById(R.id.inhospital_checkbox_zhuichuang_12);
		etzctotal=(EditText) view.findViewById(R.id.inhospital_et_zcddpf_zongfen);
		etzcdanger=(EditText) view.findViewById(R.id.inhospital_et_zcddpf_wxcd);
		cbtt_0=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_0);
		cbtt_1=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_1);
		cbtt_2=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_2);
		cbtt_3=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_3);
		cbtt_4=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_4);
		cbtt_5=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_5);
		cbtt_6=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_6);
		cbtt_7=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_7);
		cbtt_8=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_8);
		cbtt_9=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_9);
		cbtt_10=(CheckBox) view.findViewById(R.id.inhospital_checkbox_tengtong_10);
		etttdanger=(EditText) view.findViewById(R.id.inhospital_et_ttpf_zongfen);
		rbyaowu_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yaowu_1);
		rbyaowu_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_yaowu_2);
		rbshiwu_1=(RadioButton) view.findViewById(R.id.inhospital_rbutton_shiwu_1);
		rbshiwu_2=(RadioButton) view.findViewById(R.id.inhospital_rbutton_shiwu_2);
		rbqita=(RadioButton) view.findViewById(R.id.inhospital_rbutton_qita_1);
		etguominqita= (EditText) view.findViewById(R.id.inhospital_et_qita_qita);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		cbRybingqing_1.setOnClickListener(this);
		cbRybingqing_2.setOnClickListener(this);
		cbmethod_1.setOnClickListener(this);
		cbmethod_2.setOnClickListener(this);
		cbmethod_3.setOnClickListener(this);
		rbjingshen_1.setOnClickListener(this);
		rbjingshen_2.setOnClickListener(this);
		rbjingshen_3.setOnClickListener(this);
		rbjingshen_4.setOnClickListener(this);
		rbjingshen_5.setOnClickListener(this);
		rbyishi_1.setOnClickListener(this);
		rbyishi_2.setOnClickListener(this);
		rbyishi_3.setOnClickListener(this);
		rbyishi_4.setOnClickListener(this);
		rbyishi_5.setOnClickListener(this);
		rbeyeproblem_1.setOnClickListener(this);
		rbeyeproblem_2.setOnClickListener(this);
		rbearproblem_1.setOnClickListener(this);
		rbearproblem_2.setOnClickListener(this);
		rblanguage_1.setOnClickListener(this);
		rblanguage_2.setOnClickListener(this);
		rblanguage_3.setOnClickListener(this);
		rblanguage_4.setOnClickListener(this);
		rbactive_1.setOnClickListener(this);
		rbactive_2.setOnClickListener(this);
		rbactive_3.setOnClickListener(this);
		rbactive_4.setOnClickListener(this);
		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);
		spinner3.setOnItemSelectedListener(this);
		rbxiyan_1.setOnClickListener(this);
		rbxiyan_2.setOnClickListener(this);
		rbyinjiu_1.setOnClickListener(this);
		rbyinjiu_2.setOnClickListener(this);
		rbyinshi_1.setOnClickListener(this);
		rbyinshi_2.setOnClickListener(this);
		rbshuimian_1.setOnClickListener(this);
		rbshuimian_2.setOnClickListener(this);
		cbhulijibie_1.setOnClickListener(this);
		cbhulijibie_2.setOnClickListener(this);
		cbhulijibie_3.setOnClickListener(this);
		cbhulijibie_4.setOnClickListener(this);
		rbwowei_1.setOnClickListener(this);
		rbwowei_2.setOnClickListener(this);
		rbwowei_3.setOnClickListener(this);
		rbwowei_4.setOnClickListener(this);
		rbysqk_1.setOnClickListener(this);
		rbysqk_2.setOnClickListener(this);
		rbysqk_3.setOnClickListener(this);
		rbysqk_4.setOnClickListener(this);
		rbysqk_5.setOnClickListener(this);
		rbysqk_6.setOnClickListener(this);
		rbdbian_1.setOnClickListener(this);
		rbdbian_2.setOnClickListener(this);
		rbdbian_3.setOnClickListener(this);
		rbdbian_4.setOnClickListener(this);
		rbxbian_1.setOnClickListener(this);
		rbxbian_2.setOnClickListener(this);
		rbxbian_3.setOnClickListener(this);
		rbxbian_4.setOnClickListener(this);
		rbxbian_5.setOnClickListener(this);
		rbpfqk_1.setOnClickListener(this);
		rbpfqk_2.setOnClickListener(this);
		rbpfqk_3.setOnClickListener(this);
		rbpfqk_4.setOnClickListener(this);
		rbpfqk_5.setOnClickListener(this);
		rbpfqk_6.setOnClickListener(this);
		rbpfqk_7.setOnClickListener(this);
		rbpfqk_8.setOnClickListener(this);
		rbpfqk_9.setOnClickListener(this);
		rbruchuang_1.setOnClickListener(this);
		rbruchuang_2.setOnClickListener(this);
		rbtengtong_1.setOnClickListener(this);
		rbtengtong_2.setOnClickListener(this);
		rbdiedao_1.setOnClickListener(this);
		rbdiedao_2.setOnClickListener(this);
		rbjbrs_1.setOnClickListener(this);
		rbjbrs_2.setOnClickListener(this);
		rbjbrs_3.setOnClickListener(this);
		rbzyxz_1.setOnClickListener(this);
		rbzyxz_2.setOnClickListener(this);
		rbryxj_1.setOnClickListener(this);
		rbryxj_2.setOnClickListener(this);
		rbganjue_1.setOnClickListener(this);
		rbganjue_2.setOnClickListener(this);
		rbganjue_3.setOnClickListener(this);
		rbganjue_4.setOnClickListener(this);
		rbchaoshi_1.setOnClickListener(this);
		rbchaoshi_2.setOnClickListener(this);
		rbchaoshi_3.setOnClickListener(this);
		rbchaoshi_4.setOnClickListener(this);
		rbhdong_1.setOnClickListener(this);
		rbhdong_2.setOnClickListener(this);
		rbhdong_3.setOnClickListener(this);
		rbhdong_4.setOnClickListener(this);
		rbydong_1.setOnClickListener(this);
		rbydong_2.setOnClickListener(this);
		rbydong_3.setOnClickListener(this);
		rbydong_4.setOnClickListener(this);
		rbyy_1.setOnClickListener(this);
		rbyy_2.setOnClickListener(this);
		rbyy_3.setOnClickListener(this);
		rbyy_4.setOnClickListener(this);
		rbmcjq_1.setOnClickListener(this);
		rbmcjq_2.setOnClickListener(this);
		rbmcjq_3.setOnClickListener(this);
		cbzc_1.setOnClickListener(this);
		cbzc_2.setOnClickListener(this);
		cbzc_3.setOnClickListener(this);
		cbzc_4.setOnClickListener(this);
		cbzc_5.setOnClickListener(this);
		cbzc_6.setOnClickListener(this);
		cbzc_7.setOnClickListener(this);
		cbzc_8.setOnClickListener(this);
		cbzc_9.setOnClickListener(this);
		cbzc_10.setOnClickListener(this);
		cbzc_11.setOnClickListener(this);
		cbzc_12.setOnClickListener(this);
		cbtt_0.setOnClickListener(this);
		cbtt_1.setOnClickListener(this);
		cbtt_2.setOnClickListener(this);
		cbtt_3.setOnClickListener(this);
		cbtt_4.setOnClickListener(this);
		cbtt_5.setOnClickListener(this);
		cbtt_6.setOnClickListener(this);
		cbtt_7.setOnClickListener(this);
		cbtt_8.setOnClickListener(this);
		cbtt_9.setOnClickListener(this);
		cbtt_10.setOnClickListener(this);
		rbyaowu_1.setOnClickListener(this);
		rbyaowu_2.setOnClickListener(this);
		rbshiwu_1.setOnClickListener(this);
		rbshiwu_2.setOnClickListener(this);
		rbqita.setOnClickListener(this);
	}
	/**
	 * String patientInfo()
	 * 获得病人的基本信息，返回一个String 
	 * @return
	 */
	public String patientInfo(){
		String patientInfoString="";
	    age=String.valueOf(Util.userBirthdayGetAge(app.getPatientEntity().date_of_birth));
		int location=mActivity.getdepartposition();
		departname=app.getDepartnameList().get(location);//获得科室名称
		patientInfoString="姓名："+app.getPatientEntity().name+"    性别："+app.getPatientEntity().sex+"    年龄："+age
						+"    科室："+departname+"    床位："+ app.getPatientEntity().bed_no+"    住院号："+app.getPatientEntity().inp_no
						+"    诊断："+app.getPatientEntity().diagnosis+"    入院时间："+app.getPatientEntity().admission_date_time;	
		return patientInfoString;
	}
	/**
	 * patientData
	 * 暂存病人的基本信息，如果评估保存，先插入这些数据
	 */
	public void patientData(){
		mArrayList=new ArrayList<EvaluationInhospitalEntity>();
		int k=1;
		for (int i = 0; i < 8; i++,k++) {
		    entity=new EvaluationInhospitalEntity();
			entity.item_id = "010"+k;
			mArrayList.add(entity);
			//mArrayList.get(i).item_id = "010"+k;
		}
		mArrayList.get(0).item_value=app.getPatientEntity().name;
		mArrayList.get(1).item_value=app.getPatientEntity().sex;
		mArrayList.get(2).item_value=age;
		mArrayList.get(3).item_value=departname;
		mArrayList.get(4).item_value=app.getPatientEntity().bed_no;
		mArrayList.get(5).item_value=app.getPatientEntity().inp_no;
		mArrayList.get(6).item_value=app.getPatientEntity().diagnosis;
		mArrayList.get(7).item_value=app.getPatientEntity().admission_date_time;
	}
	/**
	 * 
	* @Title: getAddData 
	* @Description: TODO(得到) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void getAddData(){
		//entity=new EvaluationInhospitalEntity();
		String timeString=Util.toSimpleDate();
		if(cbRybingqing_1.isChecked()||cbRybingqing_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0109";
			entity.item_name="入院病情";
			mArrayList.add(entity);
		}
		if(cbRybingqing_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="010901";
			entity.item_name="危重";
			mArrayList.add(entity);
		}
		if(cbRybingqing_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="010902";
			entity.item_name="一般";
			mArrayList.add(entity);
		}
		if(cbmethod_1.isChecked()||cbmethod_2.isChecked()||cbmethod_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0111";
			entity.item_name="入院方式";
			mArrayList.add(entity);
		}
		if(cbmethod_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011101";
			entity.item_name="步行";
			mArrayList.add(entity);
		}
		if(cbmethod_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011102";
			entity.item_name="轮椅";
			mArrayList.add(entity);
		}
		if(cbmethod_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011103";
			entity.item_name="平车";
			mArrayList.add(entity);
		}
		entity=new EvaluationInhospitalEntity();
		entity.item_id="0112";
		entity.item_name="病史与主诉";
		entity.item_value=ethistory.getText().toString();
		mArrayList.add(entity);
		entity=new EvaluationInhospitalEntity();
		entity.item_id="0113";
		entity.item_value=ettemperature.getText().toString();
		mArrayList.add(entity);
		entity=new EvaluationInhospitalEntity();
		entity.item_id="0114";
		entity.item_value=etmaibo.getText().toString();
		mArrayList.add(entity);
		entity=new EvaluationInhospitalEntity();
		entity.item_id="0115";
		entity.item_value=etbreath.getText().toString();
		mArrayList.add(entity);
		entity=new EvaluationInhospitalEntity();
		entity.item_id="0116";
		entity.item_value=etxueya.getText().toString();
		mArrayList.add(entity);
		if(rbjingshen_1.isChecked()||rbjingshen_2.isChecked()||rbjingshen_3.isChecked()||rbjingshen_4.isChecked()||rbjingshen_5.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0117";
			entity.item_name="精神状态";
			mArrayList.add(entity);
		}
		if(rbjingshen_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011701";
			entity.item_name="正常";
			mArrayList.add(entity);
		}
		if(rbjingshen_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011702";
			entity.item_name="焦虑";
			mArrayList.add(entity);
		}
		if(rbjingshen_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011703";
			entity.item_name="紧张";
			mArrayList.add(entity);
		}
		if(rbjingshen_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011704";
			entity.item_name="恐惧";
			mArrayList.add(entity);
		}
		if(rbjingshen_5.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011705";
			entity.item_name="其他";
			entity.item_value=etjingshen_qita.getText().toString();
			mArrayList.add(entity);
		}	
		if (rbyishi_1.isChecked()||rbyishi_2.isChecked()||rbyishi_3.isChecked()||rbyishi_4.isChecked()||rbyishi_5.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0118";
			entity.item_name="意识情况";
			mArrayList.add(entity);
		}
		if (rbyishi_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011801";
			entity.item_name="清醒";
			mArrayList.add(entity);		
		}
		if (rbyishi_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011802";
			entity.item_name="嗜睡";
			mArrayList.add(entity);		
		}
		if (rbyishi_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011803";
			entity.item_name="昏睡";
			mArrayList.add(entity);		
		}
		if (rbyishi_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011804";
			entity.item_name="昏迷";
			mArrayList.add(entity);		
		}
		if (rbyishi_5.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011805";
			entity.item_name="其他";
			entity.item_value=etyishi_qita.getText().toString();
			mArrayList.add(entity);		
		}
		if (rbeyeproblem_1.isChecked()||rbeyeproblem_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0119";
			entity.item_name="视力障碍";
			mArrayList.add(entity);		
		}
		if (rbeyeproblem_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011901";
			entity.item_name="无";
			mArrayList.add(entity);	
		}
		if (rbeyeproblem_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011902";
			entity.item_name="有";
			mArrayList.add(entity);	
		}
		if(eteyeproblem_qita!=null&&rbeyeproblem_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="011903";
			entity.item_name="描述";
			entity.item_value=eteyeproblem_qita.getText().toString();
			mArrayList.add(entity);	
		}
		if(rbearproblem_1.isChecked()||rbearproblem_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0120";
			entity.item_name="听力障碍";
			mArrayList.add(entity);
		}
		if (rbearproblem_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012001";
			entity.item_name="无";
			mArrayList.add(entity);	
		}
		if (rbearproblem_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012002";
			entity.item_name="有";
			mArrayList.add(entity);	
		}
		if(etearproblem_qita!=null&&rbearproblem_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012003";
			entity.item_name="描述";
			entity.item_value=etearproblem_qita.getText().toString();
			mArrayList.add(entity);	
		}
		if (rblanguage_1.isChecked()||rblanguage_2.isChecked()||rblanguage_3.isChecked()||rblanguage_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0121";
			entity.item_name="语言能力";
			mArrayList.add(entity);
		}
		if(rblanguage_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012101";
			entity.item_name="正常";
			mArrayList.add(entity);
		}
		if(rblanguage_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012102";
			entity.item_name="含糊不清";
			mArrayList.add(entity);
		}
		if(rblanguage_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012103";
			entity.item_name="失语";
			mArrayList.add(entity);
		}
		if(rblanguage_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012104";
			entity.item_name="其他";
			entity.item_value=etlanguage_qita.getText().toString();
			mArrayList.add(entity);
		}
		if (rbactive_1.isChecked()||rbactive_2.isChecked()||rbactive_3.isChecked()||rbactive_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0122";
			entity.item_name="活动形态";
			mArrayList.add(entity);
		}
		if(rbactive_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012201";
			entity.item_name="完全自理";
			mArrayList.add(entity);
		}
		if(rbactive_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012202";
			entity.item_name="部分自理";
			mArrayList.add(entity);
		}
		if(rbactive_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012203";
			entity.item_name="卧床";
			mArrayList.add(entity);
		}
		if(rbactive_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012204";
			entity.item_name="其他";
			entity.item_value=etactive_qita.getText().toString();
			mArrayList.add(entity);
		}
		if (worksEditText.getText().toString()!=null) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0124";
			entity.item_name="职业";
			entity.item_value=worksEditText.getText().toString();
			mArrayList.add(entity);
		}
		if (spinner1.getSelectedItemPosition()!=0) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0125";
			entity.item_name="文化程度";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01250"+spinner1.getSelectedItemPosition();
			entity.item_name=spinner1.getSelectedItem().toString();
			mArrayList.add(entity);
		}
		if (spinner2.getSelectedItemPosition()!=0) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0126";
			entity.item_name="婚姻状况";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01260"+spinner2.getSelectedItemPosition();
			entity.item_name=spinner2.getSelectedItem().toString();
			mArrayList.add(entity);
		}
		if (spinner3.getSelectedItemPosition()!=0) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0127";
			entity.item_name="子女";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01270"+spinner3.getSelectedItemPosition();
			entity.item_name=spinner3.getSelectedItem().toString();
			mArrayList.add(entity);
		}	
		if (rbxiyan_1.isChecked()||rbxiyan_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0128";
			entity.item_name="吸烟";
			mArrayList.add(entity);	
		}
		if (rbxiyan_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012801";
			entity.item_name="无";
			mArrayList.add(entity);	
		}
		if (rbxiyan_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012802";
			entity.item_name="有";
			mArrayList.add(entity);	
		}
		if (rbyinjiu_1.isChecked()||rbyinjiu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0129";
			entity.item_name="饮酒";
			mArrayList.add(entity);	
		}
		if (rbyinjiu_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012901";
			entity.item_name="无";
			mArrayList.add(entity);	
		}
		if (rbyinjiu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="012902";
			entity.item_name="有";
			mArrayList.add(entity);	
		}
		if (rbyinshi_1.isChecked()||rbyinshi_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0130";
			entity.item_name="饮食";
			mArrayList.add(entity);	
		}
		if (rbyinshi_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013001";
			entity.item_name="正常";
			mArrayList.add(entity);	
		}
		if (rbyinshi_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013002";
			entity.item_name="不正常";
			mArrayList.add(entity);	
		}
		if (rbshuimian_1.isChecked()||rbshuimian_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0131";
			entity.item_name="睡眠";
			mArrayList.add(entity);	
		}
		if (rbshuimian_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013101";
			entity.item_name="自然入睡";
			mArrayList.add(entity);	
		}
		if (rbshuimian_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013102";
			entity.item_name="药物辅助";
			mArrayList.add(entity);	
		}
		if (rbyaowu_1.isChecked()||rbyaowu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="015002";
			entity.item_name="药物";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbyaowu_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01500201";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbyaowu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01500202";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (rbshiwu_1.isChecked()||rbshiwu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="015001";
			entity.item_name="食物";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbshiwu_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01500101";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbshiwu_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01500102";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (rbqita.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="015003";
			entity.item_name="其他";
			entity.item_value=etguominqita.getText().toString();
			mArrayList.add(entity);
			flag=1;
		}	
		if (flag==1) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0150";
			entity.item_name="过敏史";
			mArrayList.add(entity);
			flag=0;
		}
		if (cbhulijibie_1.isChecked()||cbhulijibie_2.isChecked()||cbhulijibie_3.isChecked()||cbhulijibie_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0134";
			entity.item_name="护理级别";
			mArrayList.add(entity);	
		}
		if(cbhulijibie_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013404";
			entity.item_name="特级护理";
			mArrayList.add(entity);
		}
		if(cbhulijibie_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013401";
			entity.item_name="一级护理";
			mArrayList.add(entity);
		}
		if(cbhulijibie_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013402";
			entity.item_name="二级护理";
			mArrayList.add(entity);
		}
		if(cbhulijibie_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013403";
			entity.item_name="三级护理";
			mArrayList.add(entity);
		}
		if (rbwowei_1.isChecked()||rbwowei_2.isChecked()||rbwowei_3.isChecked()||rbwowei_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0135";
			entity.item_name="卧位";
			mArrayList.add(entity);	
		}
		if(rbwowei_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013501";
			entity.item_name="平卧位";
			mArrayList.add(entity);	
		}
		if(rbwowei_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013502";
			entity.item_name="半卧位";
			mArrayList.add(entity);	
		}
		if(rbwowei_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013504";
			entity.item_name="自主体位";
			mArrayList.add(entity);	
		}
		if(rbwowei_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013503";
			entity.item_name="其他";
			entity.item_value=etwowei_qita.getText().toString();
			mArrayList.add(entity);	
		}
		if(rbysqk_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013601";
			entity.item_name="普食";
			mArrayList.add(entity);	
			flag=1;
		}
		if(rbysqk_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013602";
			entity.item_name="半流食";
			mArrayList.add(entity);	
			flag=1;
		}
		if(rbysqk_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013603";
			entity.item_name="流食";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbysqk_6.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013604";
			entity.item_name="其他";
			entity.item_value=etysqk_qita.getText().toString();
			mArrayList.add(entity);	
			flag=1;
		}
		if(rbysqk_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013606";
			entity.item_name="禁食";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbysqk_5.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013605";
			entity.item_name="低盐低脂普通饮食";
			mArrayList.add(entity);
			flag=1;
		}
		if(flag==1){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0136";
			entity.item_name="饮食";
			mArrayList.add(entity);
			flag=0;
		}
		if (rbdbian_1.isChecked()||rbdbian_2.isChecked()||rbdbian_3.isChecked()||rbdbian_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0137";
			entity.item_name="大便";
			mArrayList.add(entity);
		}
		if (rbdbian_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013701";
			entity.item_name="正常";
			mArrayList.add(entity);
		}
		if (rbdbian_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013702";
			entity.item_name="便秘";
			mArrayList.add(entity);
		}
		if (rbdbian_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013703";
			entity.item_name="腹泻";
			mArrayList.add(entity);
		}
		if (rbdbian_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013704";
			entity.item_name="造瘘";
			mArrayList.add(entity);
		}
		if(rbxbian_1.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013801";
			entity.item_name="正常";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbxbian_2.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013802";
			entity.item_name="失禁";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbxbian_3.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013803";
			entity.item_name="排尿困难";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbxbian_4.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013804";
			entity.item_name="留置导管";
			mArrayList.add(entity);
			flag=1;
		}
		if(rbxbian_5.isChecked()){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013805";
			entity.item_name="异常";
			mArrayList.add(entity);
			flag=1;
		}
		if (flag==1) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0138";
			entity.item_name="小便";
			mArrayList.add(entity);
			flag=0;
		}
		if (rbpfqk_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013901";
			entity.item_name="正常";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013902";
			entity.item_name="苍白";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013903";
			entity.item_name="紫绀";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013904";
			entity.item_name="黄杂";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_5.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013905";
			entity.item_name="青紫";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_6.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013907";
			entity.item_name="水肿";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_7.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013908";
			entity.item_name="脱皮";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_8.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013909";
			entity.item_name="皮疹";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbpfqk_9.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="013910";
			entity.item_name="部位";
			entity.item_value=etpfqk_buwei.getText().toString();
			mArrayList.add(entity);
			flag=1;
		}
		if (flag==1) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0139";
			entity.item_name="皮肤情况";
			mArrayList.add(entity);
			flag=0;
		}
		if (rbruchuang_1.isChecked()||rbruchuang_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0140";
			entity.item_name="褥疮";
			mArrayList.add(entity);
		}
		if (rbruchuang_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014001";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbruchuang_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014002";
			entity.item_name="有";
			mArrayList.add(entity);
			if (etrc_buwei!=null&&!etrc_buwei.equals("")) {
				entity=new EvaluationInhospitalEntity();
				entity.item_id="01400201";
				entity.item_name="部位";
				entity.item_value=etrc_buwei.getText().toString();
				mArrayList.add(entity);
			}
			if (etrc_chengdu!=null&&!etrc_chengdu.equals("")) {
				entity=new EvaluationInhospitalEntity();
				entity.item_id="01400202";
				entity.item_name="程度";
				entity.item_value=etrc_chengdu.getText().toString();
				mArrayList.add(entity);
			}
			if (etrc_mianji!=null&&!etrc_mianji.equals("")) {
				entity=new EvaluationInhospitalEntity();
				entity.item_id="01400203";
				entity.item_name="面积";
				entity.item_value=etrc_mianji.getText().toString();
				mArrayList.add(entity);
			}
		}
		if (rbtengtong_1.isChecked()||rbtengtong_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0144";
			entity.item_name="疼痛";
			mArrayList.add(entity);
		}
		if (rbtengtong_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014401";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbtengtong_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014402";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (ettt_chengdu!=null&&!ettt_chengdu.equals("")) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014403";
			entity.item_name="程度";
			entity.item_value=ettt_chengdu.getText().toString();
			mArrayList.add(entity);
		}
		if (ettt_buwei!=null&&!ettt_buwei.equals("")) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014404";
			entity.item_name="部位";
			entity.item_value=ettt_buwei.getText().toString();
			mArrayList.add(entity);
		}
		if (ettt_xingzhi!=null&&!ettt_xingzhi.equals("")) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014405";
			entity.item_name="性质";
			entity.item_value=ettt_xingzhi.getText().toString();
			mArrayList.add(entity);
		}
		if (ettt_time!=null&&!ettt_time.equals("")) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014406";
			entity.item_name="时间";
			entity.item_value=ettt_time.getText().toString();
			mArrayList.add(entity);
		}
		if (rbdiedao_1.isChecked()||rbdiedao_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014407";
			entity.item_name="坠床跌倒风险";
			mArrayList.add(entity);
		}
		if (rbdiedao_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01440701";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbdiedao_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01440702";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (rbjbrs_1.isChecked()||rbjbrs_2.isChecked()||rbjbrs_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0141";
			entity.item_name="对疾病的认识";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014104";
			entity.item_name="护士签名";
			entity.item_value=app.getDoctor(); //获取护士的登陆名
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014105";
			entity.item_name="记录时间";
			entity.item_value=timeString;
			mArrayList.add(entity);
		}
		if (rbjbrs_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014101";
			entity.item_name="完全了解";
			mArrayList.add(entity);
		}
		if (rbjbrs_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014102";
			entity.item_name="部分了解";
			mArrayList.add(entity);
		}
		if (rbjbrs_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014103";
			entity.item_name="不了解";
			mArrayList.add(entity);
		}
		if (rbzyxz_1.isChecked()||rbzyxz_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0142";
			entity.item_name="告知患者或家属住院须知";
			mArrayList.add(entity);
		}
		if (rbzyxz_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014201";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbzyxz_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014202";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (rbryxj_1.isChecked()||rbryxj_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0143";
			entity.item_name="做入院宣教";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014303";
			entity.item_name="护士签名";
			entity.item_value=app.getDoctor(); //获取护士的登陆名
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014304";
			entity.item_name="记录时间";
			entity.item_value=timeString;
			mArrayList.add(entity);
		}
		if (rbryxj_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014301";
			entity.item_name="无";
			mArrayList.add(entity);
		}
		if (rbryxj_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014302";
			entity.item_name="有";
			mArrayList.add(entity);
		}
		if (rbganjue_1.isChecked()||rbganjue_2.isChecked()||rbganjue_3.isChecked()||rbganjue_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014501";
			entity.item_name="感觉";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbganjue_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450101";
			entity.item_name="1完全丧失";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbganjue_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450102";
			entity.item_name="2非常丧失";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbganjue_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450103";
			entity.item_name="3轻度丧失";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if (rbganjue_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450104";
			entity.item_name="4未受损害";
			mArrayList.add(entity);
			yctotal+=4;
		}
		if (rbchaoshi_1.isChecked()||rbchaoshi_2.isChecked()||rbchaoshi_3.isChecked()||rbchaoshi_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014502";
			entity.item_name="潮湿";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbchaoshi_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450201";
			entity.item_name="1持久潮湿";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbchaoshi_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450202";
			entity.item_name="2非常潮湿";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbchaoshi_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450203";
			entity.item_name="3偶尔潮湿";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if (rbchaoshi_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450204";
			entity.item_name="4很少潮湿";
			mArrayList.add(entity);
			yctotal+=4;
		}
		if (rbhdong_1.isChecked()||rbhdong_2.isChecked()||rbhdong_3.isChecked()||rbhdong_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014503";
			entity.item_name="活动";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbhdong_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450301";
			entity.item_name="1卧床不起";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbhdong_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450302";
			entity.item_name="2局限于床";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbhdong_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450303";
			entity.item_name="3偶尔步行";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if (rbhdong_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450304";
			entity.item_name="4经常步行";
			mArrayList.add(entity);
			yctotal+=4;
		}
		if (rbydong_1.isChecked()||rbydong_2.isChecked()||rbydong_3.isChecked()||rbydong_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014504";
			entity.item_name="移动";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbydong_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450401";
			entity.item_name="1完全不能";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbydong_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450402";
			entity.item_name="2严重受限";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbydong_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450403";
			entity.item_name="3轻度受限";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if (rbydong_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450404";
			entity.item_name="4不受限";
			mArrayList.add(entity);
			yctotal+=4;
		}
		if (rbyy_1.isChecked()||rbyy_2.isChecked()||rbyy_3.isChecked()||rbyy_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014505";
			entity.item_name="营养";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbyy_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450501";
			entity.item_name="1非常差";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbyy_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450502";
			entity.item_name="2可能不足";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbyy_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450503";
			entity.item_name="3适当";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if (rbyy_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450504";
			entity.item_name="4良好";
			mArrayList.add(entity);
			yctotal+=4;
		}
		if (rbmcjq_1.isChecked()||rbmcjq_2.isChecked()||rbmcjq_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014506";
			entity.item_name="摩擦和剪切力";
			mArrayList.add(entity);
			flag=1;
		}
		if (rbmcjq_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450601";
			entity.item_name="1有问题";
			mArrayList.add(entity);
			yctotal+=1;
		}
		if (rbmcjq_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450602";
			entity.item_name="2有潜在危险";
			mArrayList.add(entity);
			yctotal+=2;
		}
		if (rbmcjq_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="01450603";
			entity.item_name="3无明显问题";
			mArrayList.add(entity);
			yctotal+=3;
		}
		if(flag==1){
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014507";
			entity.item_value=""+yctotal; //总分
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014508";
			ycchengdu=dangerous(yctotal, 1);
			entity.item_value=ycchengdu; //危险程度
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014509";
			entity.item_value=app.getDoctor();//护士签名
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014510";
			entity.item_value=timeString; // 时间
			mArrayList.add(entity);
			flag=0;
		}
		if (cbzc_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014601";
			entity.item_name="2意识模糊无定向力";
			mArrayList.add(entity);
			zctotal+=2;
		}
		if (cbzc_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014602";
			entity.item_name="3近3个月内有3次或以上坠床跌倒史";
			mArrayList.add(entity);
			zctotal+=3;
		}
		if (cbzc_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014603";
			entity.item_name="2站立不稳";
			mArrayList.add(entity);
			zctotal+=2;
		}
		if (cbzc_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014616";
			entity.item_name="3头晕";
			mArrayList.add(entity);
			zctotal+=3;
		}
		if (cbzc_5.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014604";
			entity.item_name="1视物模糊";
			mArrayList.add(entity);
			zctotal+=1;
		}
		if (cbzc_6.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014605";
			entity.item_name="2镇静期间";
			mArrayList.add(entity);
			zctotal+=2;
		}
		if (cbzc_7.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014606";
			entity.item_name="1体能虚弱";
			mArrayList.add(entity);
			zctotal+=1;
		}
		if (cbzc_8.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014607";
			entity.item_name="2诊断为体位性低血压";
			mArrayList.add(entity);
			zctotal+=2;
		}
		if (cbzc_9.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014608";
			entity.item_name="2近期有意识丧失癫痫史";
			mArrayList.add(entity);
			zctotal+=2;
		}
		if (cbzc_10.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014609";
			entity.item_name="1>65岁";
			mArrayList.add(entity);
			zctotal+=1;
		}
		if (cbzc_11.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014610";
			entity.item_name="1吸毒或酗酒";
			mArrayList.add(entity);
			zctotal+=1;
		}
		if (cbzc_12.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014611";
			entity.item_name="1使用抗高血压药物";
			mArrayList.add(entity);
			zctotal+=1;
		}
		if (zctotal>0) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0146";
			entity.item_name="坠床跌倒评分";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014612";
			entity.item_value=zctotal+""; //总分值
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014613";
			zcchengdu=dangerous(zctotal, 2);
			entity.item_value=zcchengdu; //危险程度
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014614";
			entity.item_value=app.getDoctor(); //护士签名
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014615";
			entity.item_value=timeString; //时间
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0144070202";
			entity.item_value=zcchengdu; //坠床跌倒风险 
			mArrayList.add(entity);
		}
		if (cbtt_0.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014701";
			entity.item_name="不痛0";
			ttchengdu="无";
			mArrayList.add(entity);
		}
		if (cbtt_1.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014702";
			entity.item_name="疼痛1";
			ttchengdu="轻度";
			mArrayList.add(entity);
		}
		if (cbtt_2.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014703";
			entity.item_name="疼痛2";
			ttchengdu="轻度";
			mArrayList.add(entity);
		}
		if (cbtt_3.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014704";
			entity.item_name="疼痛3";
			ttchengdu="轻度";
			mArrayList.add(entity);
		}
		if (cbtt_4.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014705";
			entity.item_name="疼痛4";
			ttchengdu="轻度";
			mArrayList.add(entity);
		}
		if (cbtt_5.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014706";
			entity.item_name="疼痛5";
			ttchengdu="中度";
			mArrayList.add(entity);
		}
		if (cbtt_6.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014707";
			entity.item_name="疼痛6";
			ttchengdu="中度";
			mArrayList.add(entity);
		}
		if (cbtt_7.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014708";
			entity.item_name="疼痛7";
			ttchengdu="重度";
			mArrayList.add(entity);
		}
		if (cbtt_8.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014709";
			entity.item_name="疼痛8";
			ttchengdu="重度";
			mArrayList.add(entity);
		}
		if (cbtt_9.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014710";
			entity.item_name="疼痛9";
			ttchengdu="重度";
			mArrayList.add(entity);
		}
		if (cbtt_10.isChecked()) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014711";
			entity.item_name="很痛10";
			ttchengdu="重度";
			mArrayList.add(entity);
		}
		if (!"".equals(ttchengdu)) {
			entity=new EvaluationInhospitalEntity();
			entity.item_id="0147";
			entity.item_name="疼痛评分";
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014712";
			entity.item_value=ttchengdu; //疼痛程度
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014613";
			entity.item_value=app.getDoctor(); //护士签名
			mArrayList.add(entity);
			entity=new EvaluationInhospitalEntity();
			entity.item_id="014614";
			entity.item_value=timeString; //时间
			mArrayList.add(entity);
		}
		
	}
	
	
	/**
	 * onClick(View v)
	 * 实现保存、清空、取消按钮
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.add_evaluation_but_ok:
			//执行保存任务
			patientData();//保存病人的基本信息
			getAddData();
			
			new AlertDialog.Builder(mActivity)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("是否确认提交？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                	new InsertEvaluationTask().execute();
                	Toast.makeText(mActivity, "保存成功!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create().show();
			//回显总分和危险程度
			etyctotal.setText(""+yctotal);
			etycdanger.setText(ycchengdu);
			etzctotal.setText(""+zctotal);
			etzcdanger.setText(zcchengdu);
			etttdanger.setText(ttchengdu);
			yctotal=0;
			zctotal=0;
			break;
		case R.id.add_evaluation_but_clear:
			//清空
//			EvaluationFragment evfragment=new EvaluationFragment();
//			FragmentTransaction ft=getFragmentManager().beginTransaction();
//			if (evfragment!=null) {
//				//evfragment=new EvaluationFragment();		
//				ft.add(R.id.main_right_frame, evfragment);
//			}
//			ft.commit();
			EvaluationFragment evfragment=new EvaluationFragment();
			FragmentManager fragmentManager = getFragmentManager(); 
			FragmentTransaction transaction=fragmentManager.beginTransaction();
			transaction.replace(R.id.main_right_frame,evfragment );
			transaction.commit();
			//mActivity.evaluationclear();

			Toast.makeText(mActivity, "清空数据!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.add_evaluation_but_cancle:
			//暂时不用
			break;
		default:
			break;
		}	
	}
	
	/**
	 * 
	* @Title: evaluationInsertSql 
	* @Description: TODO(住院评估插入方法) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void evaluationInsertSql(){
		String record_time=Util.toSimpleDate();
		String patient_id=app.getPatientEntity().patient_id.toString();
		String visit_id=app.getPatientEntity().visit_id.toString();
		String dept_code=app.getPatientEntity().dept_code.toString();
		int mm=mArrayList.size();
		Log.e("数据的长度----------->",mm+"" );
		for (int i = 0; i < mArrayList.size(); i++){
				EvaluationInhospitalEntity eventity=mArrayList.get(i);
				if(null==eventity.item_name){
					eventity.item_name="";
				}
				if(null==eventity.item_value){
					eventity.item_value="";
				}
				StringBuffer buffer=new StringBuffer(); 
				buffer.append("insert into pat_evaluation_m" + " (patient_id,"+ 
						"visit_id," + 
						"dict_id," + 
						"item_id," + 
						"item_name, " + 
						"item_value,"+
						"dept_code,"+
						"record_date )"+
						"values(");
				buffer.append("'"+patient_id).append("',");
				buffer.append("'"+visit_id).append("',");
				buffer.append("'"+"02").append("',");
				buffer.append("'"+eventity.item_id).append("',");
				buffer.append("'"+eventity.item_name).append("',");
				buffer.append("'"+eventity.item_value).append("',");
				buffer.append("'"+dept_code+"H").append("',");
				buffer.append("TO_DATE('"+record_time).append("','yyyy-MM-dd hh24:mi:ss') )");
		        WebServiceHelper.insertWebServiceData(buffer.toString());
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub		
	}
	/**
	 * 
	* @ClassName: InsertEvaluationTask
	* @Description: TODO(处方插入任务 ) 
	* @author lll
	* @date 2013-01-28
	*
	 */
	private class InsertEvaluationTask extends AsyncTask<Void, Void, String>{

        private MyProssDialog mDialog;
		
		@Override
		protected void onPreExecute() {
	        mDialog=new MyProssDialog(mActivity, "提交", "正在提交请求，请稍候...");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			evaluationInsertSql();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mDialog.cancel();
		}
	}
	/**
	 * 根据统计值，计算危险程度
	 * @return
	 */
	public String dangerous(int total,int categorys){
		String result="";
		if(categorys==1){
			//根据压疮规定判断
			result="";
		}else{
			//根据坠床跌倒判断
			result="";
		}
		return result;
	}
	/**
	 * 清空函数，即刷新界面
	 */
	public void clear(){
		
	}
}
