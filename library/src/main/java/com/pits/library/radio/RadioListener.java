package com.pits.library.radio;

public interface RadioListener {

  void onRadioLoading();

  void onRadioConnected();

  void onRadioStarted();

  void onRadioStopped();

  void onMetaDataReceived(String s, String s2);

  void onError();

  void songInfo(String title);
}
