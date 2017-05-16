package com.dgk.myaudiodemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dgk.myaudiodemo.R;
import com.dgk.myaudiodemo.test.PCMTester;
import com.dgk.myaudiodemo.test.SpeexTalkTester;
import com.dgk.myaudiodemo.test.UnEncodeTalkTester;
import com.dgk.myaudiodemo.test.WAVTester;
import com.dgk.myaudiodemo.util.CommUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private PCMTester pcmTester;
    private WAVTester wavTester;
    private UnEncodeTalkTester talkUnEncodeTester;
    private SpeexTalkTester speexTalkTester;

    private boolean isPCMTesting;
    private boolean isWAVTesting;
    private boolean isUnEncodeTalkTesting;
    private boolean isSpeexTalkTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private static int REQUEST_RECORD = 1;
    private static int REQUEST_WRITE_EXTRA_STORAGE = 2;

    /**
     * Android版本大于5.0之后需要手动申请存储和录音权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;//Android API <5.0不需要动态申请权限
        }
        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTRA_STORAGE);
        }
        int permissionRecord = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionRecord != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTRA_STORAGE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        if (requestCode == REQUEST_RECORD && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
    }

    @OnClick({R.id.btn_pcm_recorder_start, R.id.btn_pcm_recorder_end,
            R.id.btn_wav_recorder_start, R.id.btn_wav_recorder_end, R.id.btn_wav_player,
            R.id.btn_8_recorder_start, R.id.btn_8_recorder_end,
            R.id.btn_speex_recorder_start, R.id.btn_speex_recorder_end})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_pcm_recorder_start:   // PCM
                if (!isPCMTesting) {
                    CommUtil.Toast("开始录制PCM");
                    pcmTester = new PCMTester();
                    pcmTester.start();
                    isPCMTesting = true;
                }
                break;
            case R.id.btn_pcm_recorder_end:
                if (isPCMTesting) {
                    CommUtil.Toast("停止录制PCM");
                    pcmTester.stop();
                    isPCMTesting = false;
                }
                break;

            case R.id.btn_wav_recorder_start:   // WAV
                CommUtil.Toast("开始录制WMV");
                if (!isWAVTesting) {
                    CommUtil.Toast("开始录制WAV");
                    wavTester = new WAVTester();
                    wavTester.start();
                    isWAVTesting = true;
                }
                break;
            case R.id.btn_wav_recorder_end:
                if (isWAVTesting) {
                    CommUtil.Toast("停止录制WAV");
                    wavTester.stop();
                    isWAVTesting = false;
                }
                CommUtil.Toast("停止录制WMV");
                break;
            case R.id.btn_wav_player:
                if (!isWAVTesting) {
                    CommUtil.Toast("开始播放WAV");
                    if (wavTester == null) {
                        wavTester = new WAVTester();
                    }
                    wavTester.playback(new WAVTester.onPlayBackListener() {
                        @Override
                        public void onStop() {
                            isWAVTesting = false;
                        }
                    });
                    isWAVTesting = true;
                }
                CommUtil.Toast("开始播放WMV");
                break;

            case R.id.btn_8_recorder_start:   // 8KHz/16bit/单声道
                if (!isUnEncodeTalkTesting) {
                    CommUtil.Toast("开始通话");
                    talkUnEncodeTester = new UnEncodeTalkTester();
                    talkUnEncodeTester.start();
                    isUnEncodeTalkTesting = true;
                }
                break;
            case R.id.btn_8_recorder_end:
                if (isUnEncodeTalkTesting) {
                    CommUtil.Toast("停止通话");
                    talkUnEncodeTester.stop();
                    isUnEncodeTalkTesting = false;
                }
                break;

            case R.id.btn_speex_recorder_start:   // Speex
                if (!isSpeexTalkTesting) {
                    CommUtil.Toast("开始通话");
                    speexTalkTester = new SpeexTalkTester();
                    speexTalkTester.start();
                    isSpeexTalkTesting = true;
                }
                break;
            case R.id.btn_speex_recorder_end:
                if (isSpeexTalkTesting) {
                    CommUtil.Toast("停止通话");
                    speexTalkTester.stop();
                    isSpeexTalkTesting = false;
                }
                break;
        }
    }
}
