package com.jikexueyuan.mymusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MediaPlayer mediaPlayer;
    private TextView tvLrcShow;

    private LrcProcess mLrcProcess;
    private List<LrcContent> lrcList = new ArrayList<LrcContent>(); //存放歌词列表对象
    private int index = 0;
    private String lrcShow;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String myMsg = (String) msg.obj;
            tvLrcShow.setText(myMsg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        mLrcProcess = new LrcProcess();

        mLrcProcess.readLRC(this, R.raw.lyrics);

        lrcList = mLrcProcess.getLrcList();

        mediaPlayer.start();

        new Thread() {

            @Override
            public void run() {
                super.run();
                tvLrcShow = (TextView) findViewById(R.id.tvLrcShow);
                boolean running = true;
                for (int i = 0; running; i++) {
                    lrcIndex();
                    lrcShow = lrcList.get(index).getLrcStr();
//                    System.out.println(lrcShow);

                    Message msg = new Message();
                    msg.obj = lrcShow;
                    handler.sendMessage(msg);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


    public int lrcIndex() {

            if (mediaPlayer.isPlaying()) {
                int currentTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (currentTime < duration) {
                    for (int i = 0; i < lrcList.size(); i++) {
                        if (i < lrcList.size() - 1) {
                            if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                                index = i;
                            }
                            if (currentTime > lrcList.get(i).getLrcTime()
                                    && currentTime < lrcList.get(i + 1).getLrcTime()) {
                                index = i;
                            }
                        }
                        if (i == lrcList.size() - 1
                                && currentTime > lrcList.get(i).getLrcTime()) {
                            index = i;
                        }
                    }
                }
            }

        return index;
    }
}
