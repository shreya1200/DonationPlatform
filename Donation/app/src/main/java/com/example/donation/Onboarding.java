package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Onboarding extends AppCompatActivity {
    private SliderAdapter sliderAdapter;
    private TextView[]mdots;
    private LinearLayout mdotsLayout;
    private ViewPager mViewPager;
    private Button nextBtn,finishBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mViewPager=findViewById(R.id.mViewPager);
        mdotsLayout=findViewById(R.id.mdotsLayout);
        nextBtn=(Button)findViewById(R.id.nextbtn);
        finishBtn=(Button)findViewById(R.id.finish);
        sliderAdapter=new SliderAdapter(this);
        mViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mViewPager.addOnPageChangeListener(viewlistener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCurrentPage+1);
                if(mCurrentPage==3){
                    Intent intent=new Intent(Onboarding.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void skip(View view){
        startActivity(new Intent(this,homepage.class));
    }
    public void finish(View view){
        startActivity(new Intent(this,homepage.class));
    }



    public void addDotsIndicator(int position){
        mdots=new TextView[3];
        mdotsLayout.removeAllViews();
        int i;
        for ( i=0;i<mdots.length;i++){
            mdots[i]=new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mdotsLayout.addView(mdots[i]);
        }
        if(mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.red));
        }
    }

    ViewPager.OnPageChangeListener viewlistener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {


            addDotsIndicator(i);
            mCurrentPage=i;
            if(i==0){
                nextBtn.setEnabled(true);
                finishBtn.setVisibility(View.INVISIBLE);

            }else if (i==mdots.length-1){
                nextBtn.setEnabled(true);
                finishBtn.setVisibility(View.INVISIBLE);
                finishBtn.setVisibility(View.VISIBLE);
                finishBtn.setEnabled(true);
            }else{
                nextBtn.setEnabled(true);




            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}