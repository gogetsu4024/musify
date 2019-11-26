package com.chengtao.pianoview.entity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/*
 * 钢琴键模型
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public class PianoKey {
  private Piano.PianoKeyType type;
  private Piano.PianoVoice voice;
  private int group;
  private int positionOfGroup;
  private Drawable keyDrawable;
  private int voiceId;
  private boolean isPressed;
  private Rect[] areaOfKey;
  private String letterName;
  private int fingerID = -1;

  public PianoKey() {
  }

  public Piano.PianoKeyType getType() {
    return this.type;
  }

  public void setType(Piano.PianoKeyType type) {
    this.type = type;
  }

  public Piano.PianoVoice getVoice() {
    return this.voice;
  }

  public void setVoice(Piano.PianoVoice voice) {
    this.voice = voice;
  }

  public int getGroup() {
    return this.group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public int getPositionOfGroup() {
    return this.positionOfGroup;
  }

  public void setPositionOfGroup(int positionOfGroup) {
    this.positionOfGroup = positionOfGroup;
  }

  public Drawable getKeyDrawable() {
    return this.keyDrawable;
  }

  public void setKeyDrawable(Drawable keyDrawable) {
    this.keyDrawable = keyDrawable;
  }

  public int getVoiceId() {
    return this.voiceId;
  }

  public void setVoiceId(int voiceId) {
    this.voiceId = voiceId;
  }

  public boolean isPressed() {
    return this.isPressed;
  }

  public void setPressed(boolean pressed) {
    this.isPressed = pressed;
  }

  public Rect[] getAreaOfKey() {
    return this.areaOfKey;
  }

  public void setAreaOfKey(Rect[] areaOfKey) {
    this.areaOfKey = areaOfKey;
  }

  public String getLetterName() {
    return this.letterName;
  }

  public void setLetterName(String letterName) {
    this.letterName = letterName;
  }

  public boolean contains(int x, int y) {
    boolean isContain = false;
    Rect[] areas = this.getAreaOfKey();
    int length = this.getAreaOfKey().length;

    for(int i = 0; i < length; ++i) {
      if (areas[i] != null && areas[i].contains(x, y)) {
        isContain = true;
        break;
      }
    }

    return isContain;
  }

  public void resetFingerID() {
    this.fingerID = -1;
  }

  public void setFingerID(int fingerIndex) {
    this.fingerID = fingerIndex;
  }

  public int getFingerID() {
    return this.fingerID;
  }
}