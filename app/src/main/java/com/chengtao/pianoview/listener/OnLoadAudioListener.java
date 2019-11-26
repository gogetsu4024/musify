package com.chengtao.pianoview.listener;


/*
 * 加载音频接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public interface OnLoadAudioListener {
  void loadPianoAudioStart();

  void loadPianoAudioFinish();

  void loadPianoAudioError(Exception var1);

  void loadPianoAudioProgress(int var1);
}
