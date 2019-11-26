package com.chengtao.pianoview.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bassemgharbi.androidpiano.MidiSoundController;
import com.bassemgharbi.androidpiano.R;
import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.entity.PianoKey;
import com.chengtao.pianoview.listener.OnLoadAudioListener;
import com.chengtao.pianoview.listener.OnPianoListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * 钢琴自定义视图
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public class PianoView extends View {
  private static final String TAG = "PianoView";
  private Piano piano;
  private ArrayList<PianoKey[]> whitePianoKeys;
  private ArrayList<PianoKey[]> blackPianoKeys;
  private CopyOnWriteArrayList<PianoKey> pressedKeys;
  private Paint paint;
  private RectF square;
  private String[] pianoColors;
  private Context context;
  private int layoutWidth;
  private float scale;
  private OnLoadAudioListener loadAudioListener;
  private OnPianoListener pianoListener;
  private int progress;
  private boolean canPress;
  private boolean isAutoPlaying;
  private boolean isInitFinish;
  private int minRange;
  private int maxRange;
  private Handler autoPlayHandler;
  private static final int HANDLE_AUTO_PLAY_START = 0;
  private static final int HANDLE_AUTO_PLAY_END = 1;
  private static final int HANDLE_AUTO_PLAY_BLACK_DOWN = 2;
  private static final int HANDLE_AUTO_PLAY_WHITE_DOWN = 3;
  private static final int HANDLE_AUTO_PLAY_KEY_UP = 4;
  private MidiSoundController midiSoundController;

  public PianoView(Context context) {
    this(context, (AttributeSet)null);
  }

  public PianoView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PianoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.midiSoundController = new MidiSoundController(getContext());
    this.piano = null;
    this.pressedKeys = new CopyOnWriteArrayList();
    this.pianoColors = new String[]{"#C0C0C0", "#A52A2A", "#FF8C00", "#FFFF00", "#00FA9A", "#00CED1", "#4169E1", "#FFB6C1", "#FFEBCD"};
    this.layoutWidth = 0;
    this.scale = 1.0F;
    this.progress = 0;
    this.canPress = true;
    this.isAutoPlaying = false;
    this.isInitFinish = false;
    this.minRange = 0;
    this.maxRange = 0;

    this.context = context;
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.paint.setStyle(Paint.Style.FILL);
    this.square = new RectF();

    //

    LocalBroadcastManager.getInstance(context).registerReceiver(instrumentChangedReceiver,
            new IntentFilter("Instrument change"));
    LocalBroadcastManager.getInstance(context).registerReceiver(volumeChangedReceiver,
            new IntentFilter("volume midi changed"));
    LocalBroadcastManager.getInstance(context).registerReceiver(RecordStateReceiver,
            new IntentFilter("Record Start"));
  }
  public BroadcastReceiver instrumentChangedReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      int midiHex = intent.getIntExtra("midiHex",0);
      Toast.makeText(context,""+midiHex ,Toast.LENGTH_SHORT).show();
      midiSoundController.changeMidiInstrument(midiHex);
    }
  };
  public BroadcastReceiver volumeChangedReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      int volumeVelocityHex = intent.getIntExtra("midiVelocity",0);
      Toast.makeText(context,""+volumeVelocityHex ,Toast.LENGTH_SHORT).show();
      midiSoundController.setVOLUME(volumeVelocityHex);
    }
  };
  public BroadcastReceiver RecordStateReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      boolean record = intent.getBooleanExtra("Record",false);
      Toast.makeText(context,"Recording"+record ,Toast.LENGTH_SHORT).show();
      if (record)
        midiSoundController.startRecording();
      else {
        try {
          float length =midiSoundController.stopRecording();
          Toast.makeText(context,"length "+length ,Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  };
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    Log.e("PianoView", "onMeasure");
    Drawable whiteKeyDrawable = ContextCompat.getDrawable(this.context, R.drawable.white_piano_key);
    int whiteKeyHeight = whiteKeyDrawable.getIntrinsicHeight();
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    switch(heightMode) {
      case -2147483648:
        height = Math.min(height, whiteKeyHeight);
        break;
      case 0:
        height = whiteKeyHeight;
    }

    this.scale = (float)(height - this.getPaddingTop() - this.getPaddingBottom()) / (float)whiteKeyHeight;
    this.layoutWidth = width - this.getPaddingLeft() - this.getPaddingRight();
    this.setMeasuredDimension(width, height);
  }

  protected void onDraw(Canvas canvas) {
    if (this.piano == null) {
      this.minRange = 0;
      this.maxRange = this.layoutWidth;
      this.piano = new Piano(this.context, this.scale);
      this.whitePianoKeys = this.piano.getWhitePianoKeys();
      this.blackPianoKeys = this.piano.getBlackPianoKeys();
    }

    int i;
    PianoKey[] var3;
    int var4;
    int var5;
    PianoKey key;
    if (this.whitePianoKeys != null) {
      for(i = 0; i < this.whitePianoKeys.size(); ++i) {
        var3 = (PianoKey[])this.whitePianoKeys.get(i);
        var4 = var3.length;

        for(var5 = 0; var5 < var4; ++var5) {
          key = var3[var5];
          this.paint.setColor(Color.parseColor(this.pianoColors[i]));
          key.getKeyDrawable().draw(canvas);
          Rect r = key.getKeyDrawable().getBounds();
          int sideLength = (r.right - r.left) / 2;
          int left = r.left + sideLength / 2;
          int top = r.bottom - sideLength - sideLength / 3;
          int right = r.right - sideLength / 2;
          int bottom = r.bottom - sideLength / 3;
          this.square.set((float)left, (float)top, (float)right, (float)bottom);
          canvas.drawRoundRect(this.square, 6.0F, 6.0F, this.paint);
          this.paint.setColor(-16777216);
          this.paint.setTextSize((float)sideLength / 1.8F);
          Paint.FontMetricsInt fontMetrics = this.paint.getFontMetricsInt();
          int baseline = (int)((this.square.bottom + this.square.top - (float)fontMetrics.bottom - (float)fontMetrics.top) / 2.0F);
          this.paint.setTextAlign(Paint.Align.CENTER);
          canvas.drawText(key.getLetterName(), this.square.centerX(), (float)baseline, this.paint);
        }
      }
    }

    if (this.blackPianoKeys != null) {
      for(i = 0; i < this.blackPianoKeys.size(); ++i) {
        var3 = (PianoKey[])this.blackPianoKeys.get(i);
        var4 = var3.length;

        for(var5 = 0; var5 < var4; ++var5) {
          key = var3[var5];
          key.getKeyDrawable().draw(canvas);
        }
      }
    }

    if (!this.isInitFinish && this.piano != null && this.pianoListener != null) {
      this.isInitFinish = true;
      this.pianoListener.onPianoInitFinish();
    }

  }

  public boolean onTouchEvent(MotionEvent event) {
    if (!this.canPress) {
      return false;
    } else {
      switch(event.getAction()) {
        case 0:
          this.handleDown(event.getActionIndex(), event);
          break;
        case 1:
          this.handleUp();
          this.performClick();
          break;
        case 2:
          int i;
          for(i = 0; i < event.getPointerCount(); ++i) {
            this.handleMove(i, event);
          }

          for(i = 0; i < event.getPointerCount(); ++i) {
            this.handleDown(i, event);
          }
        case 3:
        case 4:
        default:
          break;
        case 5:
          this.handleDown(event.getActionIndex(), event);
          break;
        case 6:
          this.handlePointerUp(event.getPointerId(event.getActionIndex()));
      }

      return true;
    }
  }

  public boolean performClick() {
    return super.performClick();
  }

  private void handleDown(int which, MotionEvent event) {
    int x = (int)event.getX(which) + this.getScrollX();
    int y = (int)event.getY(which);

    int i;
    PianoKey[] var6;
    int var7;
    int var8;
    PianoKey key;
    for(i = 0; i < this.whitePianoKeys.size(); ++i) {
      var6 = (PianoKey[])this.whitePianoKeys.get(i);
      var7 = var6.length;

      for(var8 = 0; var8 < var7; ++var8) {
        key = var6[var8];
        if (!key.isPressed() && key.contains(x, y)) {
          this.handleWhiteKeyDown(which, event, key);
        }
      }
    }

    for(i = 0; i < this.blackPianoKeys.size(); ++i) {
      var6 = (PianoKey[])this.blackPianoKeys.get(i);
      var7 = var6.length;

      for(var8 = 0; var8 < var7; ++var8) {
        key = var6[var8];
        if (!key.isPressed() && key.contains(x, y)) {
          this.handleBlackKeyDown(which, event, key);
        }
      }
    }

  }

  private void handleWhiteKeyDown(int which, MotionEvent event, PianoKey key) {
    key.getKeyDrawable().setState(new int[]{16842919});
    key.setPressed(true);
    if (event != null) {
      key.setFingerID(event.getPointerId(which));
    }

    this.pressedKeys.add(key);
    this.invalidate(key.getKeyDrawable().getBounds());
    //this.utils.playMusic(key);
    if (this.pianoListener != null) {
        //Log.d("azdqsd",key.getGroup()+"");
        midiSoundController.playNote2(key.getVoice().toString(),key.getGroup(),0);
      this.pianoListener.onPianoClick(key.getType(), key.getVoice(), key.getGroup(), key.getPositionOfGroup());
    }

  }

  private void handleBlackKeyDown(int which, MotionEvent event, PianoKey key) {
    key.getKeyDrawable().setState(new int[]{16842919});
    key.setPressed(true);
    if (event != null) {
      key.setFingerID(event.getPointerId(which));
    }

    this.pressedKeys.add(key);
    this.invalidate(key.getKeyDrawable().getBounds());
    //this.utils.playMusic(key);
    if (this.pianoListener != null) {
        midiSoundController.playNote2(key.getVoice().toString(),key.getGroup(),1);
        this.pianoListener.onPianoClick(key.getType(), key.getVoice(), key.getGroup(), key.getPositionOfGroup());
    }

  }

  private void handleMove(int which, MotionEvent event) {
    int x = (int)event.getX(which) + this.getScrollX();
    int y = (int)event.getY(which);
    Iterator var5 = this.pressedKeys.iterator();

    while(var5.hasNext()) {
      PianoKey key = (PianoKey)var5.next();
      if (key.getFingerID() == event.getPointerId(which) && !key.contains(x, y)) {
        key.getKeyDrawable().setState(new int[]{-16842919});
        this.invalidate(key.getKeyDrawable().getBounds());
        key.setPressed(false);
        key.resetFingerID();
          if (key.getType().getValue()==1)
              midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),0);
          else
              midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),1);

          this.pressedKeys.remove(key);
      }
    }

  }

  private void handlePointerUp(int pointerId) {
    Iterator var2 = this.pressedKeys.iterator();

    while(var2.hasNext()) {
      PianoKey key = (PianoKey)var2.next();
      if (key.getFingerID() == pointerId) {
        key.setPressed(false);
        key.resetFingerID();
        key.getKeyDrawable().setState(new int[]{-16842919});
        this.invalidate(key.getKeyDrawable().getBounds());
          if (key.getType().getValue()==1)
              midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),0);
          else
              midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),1);

          this.pressedKeys.remove(key);
        break;
      }
    }

  }

  private void handleUp() {
    if (this.pressedKeys.size() > 0) {
      Iterator var1 = this.pressedKeys.iterator();

      while(var1.hasNext()) {
        PianoKey key = (PianoKey)var1.next();
        key.getKeyDrawable().setState(new int[]{-16842919});
        key.setPressed(false);
          Log.d(key.getType().getValue()+"","test test");
          if (key.getType().getValue()==1)
          midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),0);
          else
          midiSoundController.stopNote2(key.getVoice().toString(),key.getGroup(),1);

          this.invalidate(key.getKeyDrawable().getBounds());
      }

      this.pressedKeys.clear();
    }

  }


  public int getPianoWidth() {
    return this.piano != null ? this.piano.getPianoWith() : 0;
  }

  public int getLayoutWidth() {
    return this.layoutWidth;
  }

  public void setPianoColors(String[] pianoColors) {
    if (pianoColors.length == 9) {
      this.pianoColors = pianoColors;
    }

  }

  public void setCanPress(boolean canPress) {
    this.canPress = canPress;
  }

  public void scroll(int progress) {
    int x;
    switch(progress) {
      case 0:
        x = 0;
        break;
      case 100:
        x = this.getPianoWidth() - this.getLayoutWidth();
        break;
      default:
        x = (int)((float)progress / 100.0F * (float)(this.getPianoWidth() - this.getLayoutWidth()));
    }

    this.minRange = x;
    this.maxRange = x + this.getLayoutWidth();
    this.scrollTo(x, 0);
    this.progress = progress;
  }


  public void setPianoListener(OnPianoListener pianoListener) {
    this.pianoListener = pianoListener;
  }

  public void setLoadAudioListener(OnLoadAudioListener loadAudioListener) {
    this.loadAudioListener = loadAudioListener;
  }


  private int dpToPx(int dp) {
    DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
    return Math.round((float)dp * (displayMetrics.xdpi / 160.0F));
  }

  private void handleAutoPlay(Message msg) {
    PianoKey key;
    switch(msg.what) {
      case 0:
        break;
      case 1:
        this.isAutoPlaying = false;
        this.setCanPress(true);

        break;
      case 2:
        if (msg.obj != null) {
          try {
            key = (PianoKey)msg.obj;
            this.autoScroll(key);
            this.handleBlackKeyDown(-1, (MotionEvent)null, key);
          } catch (Exception var4) {
            Log.e("TAG", "黑键对象有问题:" + var4.getMessage());
          }
        }
        break;
      case 3:
        if (msg.obj != null) {
          try {
            key = (PianoKey)msg.obj;
            this.autoScroll(key);
            this.handleWhiteKeyDown(-1, (MotionEvent)null, key);
          } catch (Exception var3) {
            Log.e("TAG", "白键对象有问题:" + var3.getMessage());
          }
        }
        break;
      case 4:
        this.handleUp();
    }

  }

  private void autoScroll(PianoKey key) {
    if (this.isAutoPlaying && key != null) {
      Rect[] areas = key.getAreaOfKey();
      if (areas != null && areas.length > 0 && areas[0] != null) {
        int left = areas[0].left;
        int right = key.getAreaOfKey()[0].right;

        int i;
        for(i = 1; i < areas.length; ++i) {
          if (areas[i] != null) {
            if (areas[i].left < left) {
              left = areas[i].left;
            }

            if (areas[i].right > right) {
              right = areas[i].right;
            }
          }
        }

        if (left < this.minRange || right > this.maxRange) {
          i = (int)((float)left * 100.0F / (float)this.getPianoWidth());
          this.scroll(i);
        }
      }
    }

  }

  protected void onRestoreInstanceState(Parcelable state) {
    super.onRestoreInstanceState(state);
    this.postDelayed(() -> {
      this.scroll(this.progress);
    }, 200L);
  }
}
