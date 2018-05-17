package com.android.hospital.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hospital.util.PacsApi;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

/**
 * Created by WHL on 2015/3/4.
 */
public class PacsImageSliderActivity extends FragmentActivity{

    private int[] imagesIndex;

    private String seriesUid;
    private String studyUid;
    
    private SliderLayout mSliderLayout;
    
    EditText editText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacs_image_slider);
        
        mSliderLayout= (SliderLayout) findViewById(R.id.slider);
        
        editText= (EditText) findViewById(R.id.text);

        imagesIndex=getIntent().getIntArrayExtra("images");
        seriesUid=getIntent().getStringExtra("seriesUid");
        studyUid=getIntent().getStringExtra("studyUid");
        

        init();
    }

    private void init() {
        for (int i = 0; i < imagesIndex.length; i++) {
            TextSliderView textSliderView=new TextSliderView(this);
            String imageUrl= PacsApi.getImage(studyUid, seriesUid, String.valueOf(i));
            textSliderView.image(imageUrl)
                    .description(String.valueOf(i+1));
            editText.setText(imageUrl);
            mSliderLayout.addSlider(textSliderView);
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
    }
}
