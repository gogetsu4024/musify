package com.bassemgharbi.androidpiano.Menu.view.ui;

import android.animation.ArgbEvaluator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bassemgharbi.androidpiano.Menu.services.model.Model;
import com.bassemgharbi.androidpiano.Menu.view.adapters.Adapter;
import com.bassemgharbi.androidpiano.Menu.viewModel.MenuViewModel;
import com.bassemgharbi.androidpiano.R;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    MenuViewModel menuViewModel;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        constraintLayout = findViewById(R.id.musify);
        viewPager = findViewById(R.id.viewPager);
        if (getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_LANDSCAPE)
            constraintLayout.setVisibility(View.INVISIBLE);
        menuViewModel = ViewModelProviders.of(this)
                .get(MenuViewModel.class);
        menuViewModel.init();
        menuViewModel.getData().observe(this, new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
                adapter.notifyDataSetChanged();
            }
        });

        initViewPager();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public void initViewPager(){
        adapter = new Adapter(menuViewModel.getData().getValue(), this);


        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.blue_grey_900),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            constraintLayout.setVisibility(View.VISIBLE);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            constraintLayout.setVisibility(View.INVISIBLE);
        else
            Log.w("tag", "other: " + orientation);

    }
}
