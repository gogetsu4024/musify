package com.bassemgharbi.androidpiano.ChooseMidiToRecord;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.bassemgharbi.androidpiano.R;
import com.google.common.collect.Lists;

import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class ChooseMidiToRecordActivity extends AppCompatActivity {
    ScrollerViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_midi_to_record);

        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(MidiFragment.class, getBgRes2(), getTitles2());
        manager.addCommonFragment(MidiFragment.class,getBgRes(),getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        // just set viewPager
        springIndicator.setViewPager(viewPager);

    }

    private List<String> getTitles(){
        return Lists.newArrayList("Midi");
    }
    private List<String> getTitles2(){
        return Lists.newArrayList("Mp3");
    }

    private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1);
    }
    private List<Integer> getBgRes2(){
        return Lists.newArrayList(R.drawable.bg1);
    }





}
