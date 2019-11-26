package com.chengtao.pianoview.listener;

/*
 * 钢琴绘制完成接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

import com.chengtao.pianoview.entity.Piano;

public interface OnPianoListener {
  void onPianoInitFinish();

  void onPianoClick(Piano.PianoKeyType var1, Piano.PianoVoice var2, int var3, int var4);
}
