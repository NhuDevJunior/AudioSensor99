package com.example.audiosensor.ui.home;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.audiosensor.DBHelper;
import com.example.audiosensor.MainActivity;
import com.example.audiosensor.R;
import com.example.audiosensor.RecordingItem;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeFragment extends Fragment {

    private Chronometer chronometer;
    private Button startbtn, stopbtn, playbtn, stopplay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    private static String filename = null;
    DBHelper dbHelper;
    long mStartingTimeMilis=0;
    long mElapsedMilis=0;
    /*public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper=new DBHelper(getContext());
        chronometer=root.findViewById(R.id.chronometer);
        startbtn = root.findViewById(R.id.btnRecord);
        stopbtn =  root.findViewById(R.id.btnStop);
        playbtn = root.findViewById(R.id.btnPlay);
        stopplay = root.findViewById(R.id.btnStopPlay);
        stopbtn.setEnabled(false);
        playbtn.setEnabled(false);
        stopplay.setEnabled(false);
        filename="";
        long tsLong=System.currentTimeMillis()/1000;
        String ts=String.valueOf(tsLong);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording_"+ts+".3gp";
        filename += "/AudioRecording_"+ts;

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                long tsLong=System.currentTimeMillis()/100000;
        String ts=String.valueOf(tsLong);
        filename="";
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording_"+ts+".3gp";
        filename += "/AudioRecording_"+ts;*/
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    mStartingTimeMilis=System.currentTimeMillis();
                    chronometer.start();
                    stopbtn.setEnabled(true);
                    startbtn.setEnabled(false);
                    playbtn.setEnabled(false);
                    stopplay.setEnabled(false);
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(mFileName);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    mRecorder.start();
                    Toast.makeText(getContext(), "Recording Started", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), mFileName, Toast.LENGTH_LONG).show();

            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                mElapsedMilis=System.currentTimeMillis()-mStartingTimeMilis;
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                playbtn.setEnabled(true);
                stopplay.setEnabled(true);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;

                // add database
                RecordingItem recordingItem=new RecordingItem(filename,mFileName,
                        mElapsedMilis,System.currentTimeMillis());
                dbHelper.addRecording(recordingItem);
                mStartingTimeMilis=0;
                Toast.makeText(getContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
            }
        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                playbtn.setEnabled(false);
                stopplay.setEnabled(true);
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(mFileName);
                    mPlayer.prepare();
                    mPlayer.start();
                    Toast.makeText(getContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
        });
        stopplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.release();
                mPlayer = null;
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                playbtn.setEnabled(true);
                stopplay.setEnabled(false);
                Toast.makeText(getContext(),"Playing Audio Stopped", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}