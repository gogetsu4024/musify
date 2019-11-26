package com.chengtao.pianoview.entity;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import androidx.core.content.ContextCompat;


import com.bassemgharbi.androidpiano.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*
 * 钢琴模型
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public class Piano {
  public static final int PIANO_NUMS = 88;
  private static final int BLACK_PIANO_KEY_GROUPS = 8;
  private static final int WHITE_PIANO_KEY_GROUPS = 9;
  private ArrayList<PianoKey[]> blackPianoKeys = new ArrayList(8);
  private ArrayList<PianoKey[]> whitePianoKeys = new ArrayList(9);
  private int blackKeyWidth;
  private int blackKeyHeight;
  private int whiteKeyWidth;
  private int whiteKeyHeight;
  private int pianoWith = 0;
  private float scale = 0.0F;
  private Context context;

  public Piano(Context context, float scale) {
    this.context = context;
    this.scale = scale;
    this.initPiano();
  }

  private void initPiano() {
    if (this.scale > 0.0F) {
      Drawable blackDrawable = ContextCompat.getDrawable(this.context,R.drawable.black_piano_key);
      Drawable whiteDrawable = ContextCompat.getDrawable(this.context, R.drawable.white_piano_key);
      this.blackKeyWidth = blackDrawable.getIntrinsicWidth();
      this.blackKeyHeight = (int)((float)blackDrawable.getIntrinsicHeight() * this.scale);
      this.whiteKeyWidth = whiteDrawable.getIntrinsicWidth();
      this.whiteKeyHeight = (int)((float)whiteDrawable.getIntrinsicHeight() * this.scale);

      int i;
      PianoKey[] mKeys;
      int j;
      Rect[] areaOfKey;
      for(i = 0; i < 8; ++i) {
        switch(i) {
          case 0:
            mKeys = new PianoKey[1];
            break;
          default:
            mKeys = new PianoKey[5];
        }

        for(j = 0; j < mKeys.length; ++j) {
          mKeys[j] = new PianoKey();
          areaOfKey = new Rect[1];
          mKeys[j].setType(com.chengtao.pianoview.entity.Piano.PianoKeyType.BLACK);
          mKeys[j].setGroup(i);
          mKeys[j].setPositionOfGroup(j);
          mKeys[j].setVoiceId(this.getVoiceFromResources("b" + i + j));
          mKeys[j].setPressed(false);
          mKeys[j].setKeyDrawable((new ScaleDrawable(ContextCompat.getDrawable(this.context, R.drawable.black_piano_key), 0, 1.0F, this.scale)).getDrawable());
          this.setBlackKeyDrawableBounds(i, j, mKeys[j].getKeyDrawable());
          areaOfKey[0] = mKeys[j].getKeyDrawable().getBounds();
          mKeys[j].setAreaOfKey(areaOfKey);
          if (i == 0) {
            mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.LA);
            break;
          }

          switch(j) {
            case 0:
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.DO);
              break;
            case 1:
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.RE);
              break;
            case 2:
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.FA);
              break;
            case 3:
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.SO);
              break;
            case 4:
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.LA);
          }
        }

        this.blackPianoKeys.add(mKeys);
      }

      for(i = 0; i < 9; ++i) {
        switch(i) {
          case 0:
            mKeys = new PianoKey[2];
            break;
          case 8:
            mKeys = new PianoKey[1];
            break;
          default:
            mKeys = new PianoKey[7];
        }

        for(j = 0; j < mKeys.length; ++j) {
          mKeys[j] = new PianoKey();
          mKeys[j].setType(com.chengtao.pianoview.entity.Piano.PianoKeyType.WHITE);
          mKeys[j].setGroup(i);
          mKeys[j].setPositionOfGroup(j);
          mKeys[j].setVoiceId(this.getVoiceFromResources("w" + i + j));
          mKeys[j].setPressed(false);
          mKeys[j].setKeyDrawable((new ScaleDrawable(ContextCompat.getDrawable(this.context, R.drawable.white_piano_key), 0, 1.0F, this.scale)).getDrawable());
          this.setWhiteKeyDrawableBounds(i, j, mKeys[j].getKeyDrawable());
          this.pianoWith += this.whiteKeyWidth;
          if (i == 0) {
            switch(j) {
              case 0:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.LA);
                mKeys[j].setLetterName("A0");
                break;
              case 1:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.SI);
                mKeys[j].setLetterName("B0");
            }
          } else {
            if (i == 8) {
              areaOfKey = new Rect[]{mKeys[j].getKeyDrawable().getBounds()};
              mKeys[j].setAreaOfKey(areaOfKey);
              mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.DO);
              mKeys[j].setLetterName("C8");
              break;
            }

            switch(j) {
              case 0:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.DO);
                mKeys[j].setLetterName("C" + i);
                break;
              case 1:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT_RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.RE);
                mKeys[j].setLetterName("D" + i);
                break;
              case 2:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.MI);
                mKeys[j].setLetterName("E" + i);
                break;
              case 3:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.FA);
                mKeys[j].setLetterName("F" + i);
                break;
              case 4:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT_RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.SO);
                mKeys[j].setLetterName("G" + i);
                break;
              case 5:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT_RIGHT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.LA);
                mKeys[j].setLetterName("A" + i);
                break;
              case 6:
                mKeys[j].setAreaOfKey(this.getWhitePianoKeyArea(i, j, com.chengtao.pianoview.entity.Piano.BlackKeyPosition.LEFT));
                mKeys[j].setVoice(com.chengtao.pianoview.entity.Piano.PianoVoice.SI);
                mKeys[j].setLetterName("B" + i);
            }
          }
        }

        this.whitePianoKeys.add(mKeys);
      }
    }

  }

  private int getVoiceFromResources(String voiceName) {
    return this.context.getResources().getIdentifier(voiceName, "raw", this.context.getPackageName());
  }

  private Rect[] getWhitePianoKeyArea(int group, int positionOfGroup, com.chengtao.pianoview.entity.Piano.BlackKeyPosition blackKeyPosition) {
    int offset = 0;
    if (group == 0) {
      offset = 5;
    }

    switch(blackKeyPosition) {
      case LEFT:
        Rect[] left = new Rect[]{new Rect((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth, this.blackKeyHeight, (7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth + this.blackKeyWidth / 2, this.whiteKeyHeight), new Rect((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth + this.blackKeyWidth / 2, 0, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth, this.whiteKeyHeight)};
        return left;
      case LEFT_RIGHT:
        Rect[] leftRight = new Rect[]{new Rect((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth, this.blackKeyHeight, (7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth + this.blackKeyWidth / 2, this.whiteKeyHeight), new Rect((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth + this.blackKeyWidth / 2, 0, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth - this.blackKeyWidth / 2, this.whiteKeyHeight), new Rect((7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth - this.blackKeyWidth / 2, this.blackKeyHeight, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth, this.whiteKeyHeight)};
        return leftRight;
      case RIGHT:
        Rect[] right = new Rect[]{new Rect((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth, 0, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth - this.blackKeyWidth / 2, this.whiteKeyHeight), new Rect((7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth - this.blackKeyWidth / 2, this.blackKeyHeight, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth, this.whiteKeyHeight)};
        return right;
      default:
        return null;
    }
  }

  private void setWhiteKeyDrawableBounds(int group, int positionOfGroup, Drawable drawable) {
    int offset = 0;
    if (group == 0) {
      offset = 5;
    }

    drawable.setBounds((7 * group - 5 + offset + positionOfGroup) * this.whiteKeyWidth, 0, (7 * group - 4 + offset + positionOfGroup) * this.whiteKeyWidth, this.whiteKeyHeight);
  }

  private void setBlackKeyDrawableBounds(int group, int positionOfGroup, Drawable drawable) {
    int whiteOffset = 0;
    int blackOffset = 0;
    if (group == 0) {
      whiteOffset = 5;
    }

    if (positionOfGroup == 2 || positionOfGroup == 3 || positionOfGroup == 4) {
      blackOffset = 1;
    }

    drawable.setBounds((7 * group - 4 + whiteOffset + blackOffset + positionOfGroup) * this.whiteKeyWidth - this.blackKeyWidth / 2, 0, (7 * group - 4 + whiteOffset + blackOffset + positionOfGroup) * this.whiteKeyWidth + this.blackKeyWidth / 2, this.blackKeyHeight);
  }

  public ArrayList<PianoKey[]> getWhitePianoKeys() {
    return this.whitePianoKeys;
  }

  public ArrayList<PianoKey[]> getBlackPianoKeys() {
    return this.blackPianoKeys;
  }

  public int getPianoWith() {
    return this.pianoWith;
  }

  private static enum BlackKeyPosition {
    LEFT,
    LEFT_RIGHT,
    RIGHT;

    private BlackKeyPosition() {
    }
  }

  public static enum PianoKeyType {
    @SerializedName("0")
    BLACK(0),
    @SerializedName("1")
    WHITE(1);

    private int value;

    private PianoKeyType(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }

    public String toString() {
      return "PianoKeyType{value=" + this.value + '}';
    }
  }

  public static enum PianoVoice {
    DO,
    RE,
    MI,
    FA,
    SO,
    LA,
    SI;

    private PianoVoice() {
    }
  }
}
