package com.bassemgharbi.androidpiano.recording.view.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bassemgharbi.androidpiano.recording.services.repository.InstrumentPlayerRepository;
import com.bassemgharbi.androidpiano.recording.services.repository.InstrumentRecordRepository;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyCustomView extends View implements View.OnClickListener {
    private Paint paint;
    private Canvas canvas;
    private RectF square;
    private Context context;
    private int layoutWidth;
    private float scale;
    private boolean isInitFinish;
    private int minRange;
    private long timeInMs;
    private boolean selected=false;
    private String test="";
    private Rect rectangle;
    private MidiTrack midiTrack;
    private String midiPath;
    private InstrumentRecordRepository instrumentRecordRepository;
    private InstrumentPlayerRepository instrumentPlayerRepository;
    private boolean muted=false;

    public MyCustomView(Context context) {
        this(context, (AttributeSet)null);
    }
    public MyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        this.setOnLongClickListener(vLong);
        this.setOnCreateContextMenuListener(vC);
        this.layoutWidth = 0;
        this.scale = 1.0F;
        this.isInitFinish = false;
        this.minRange = 0;

        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.square = new RectF();
        timeInMs = 1000;
        rectangle = new Rect(0, 0, (int)timeInMs, (int)timeInMs);

        // create a rectangle that we'll draw later


        // create the Paint and set its color


    }

    public void setVariables(long timeInMs, MidiTrack midiTrack,String path) throws IOException {
        this.timeInMs=timeInMs;
        rectangle = new Rect(0, 0, (int)timeInMs, (int)timeInMs);
        this.midiTrack = midiTrack;
        this.midiPath = path;
        instrumentRecordRepository = InstrumentRecordRepository.getInstance(path);
        instrumentPlayerRepository = InstrumentPlayerRepository.getInstance(path);
    }

    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawRect(rectangle,paint);
        if (selected){
            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(50, 40, 80));
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setStrokeWidth(10);
             this.canvas.drawRect(rectangle,myPaint);

        }
        if (muted)
        {
            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(180, 40, 130));
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setStrokeWidth(10);
            this.canvas.drawRect(rectangle,myPaint);

        }
        // canvas.drawColor();


    }
    private View.OnLongClickListener vLong = new View.OnLongClickListener() {
        public boolean onLongClick(View view) {
            showContextMenu();
            return true;
        }
    };
    private View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose what To do !");

            menu.add(0, 0, 0, "Mute")
                    .setOnMenuItemClickListener(mMenuItemClickListener);
            menu.add(0, 1, 0, "Delete")
                    .setOnMenuItemClickListener(mMenuItemClickListener);
            menu.add(0, 2, 0, "translate")
                    .setOnMenuItemClickListener(mMenuItemClickListener);


        }
    };

    private MenuItem.OnMenuItemClickListener mMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case 0:{
                    try {

                      instrumentRecordRepository.muteTrack(1,0);
                        muted=!muted;
                        invalidate();
                        instrumentPlayerRepository.resetPlayer();
                        instrumentPlayerRepository.getmPlayer().prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case 1:
                    // do "Map"
                    return true;

                case 2:
                    // do "Market"
                    return true;
            }

            return false;
        }
    };




    @Override
    public void onClick(View v) {
        selected = !selected;
        invalidate();
        Log.d("sssssss","ssssssssssssssssssssssssssssssssssssssss");
    }


}
