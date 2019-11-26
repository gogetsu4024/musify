package com.chengtao.pianoview.listener;

/*
 * 加载音频消息接口
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public interface LoadAudioMessage {
  void sendStartMessage();

  void sendFinishMessage();

  void sendErrorMessage(Exception var1);

  void sendProgressMessage(int var1);
}
