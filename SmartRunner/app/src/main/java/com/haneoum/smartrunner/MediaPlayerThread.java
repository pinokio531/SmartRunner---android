package com.haneoum.smartrunner;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

public class MediaPlayerThread extends Thread {

    MediaPlayer mediaPlayer;

    public MediaPlayerThread(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void run() {
        while (StaticValueX.warningrun = true){
            try {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
                Thread.sleep(10);
            }catch (Exception e){

            }
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            warningSound();
        }
    };

    public void warningSound(){
        if(StaticValueX.x >= 10){
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
        else if(StaticValueX.x <= 0.1){
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }
}
