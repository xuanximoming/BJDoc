package com.android.hospital.ui.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ListFragment;

public abstract class BaseFragment extends Fragment{

	public abstract void init();
			
	public abstract boolean validate();
	
	public abstract void onOtherFragmentClick();
	
	public abstract void insert();
}
