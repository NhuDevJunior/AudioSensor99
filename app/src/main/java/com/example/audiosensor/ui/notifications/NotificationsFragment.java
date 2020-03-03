package com.example.audiosensor.ui.notifications;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiosensor.ContactAdapterDemo;
import com.example.audiosensor.DBHelper;
import com.example.audiosensor.IonClickContact;
import com.example.audiosensor.R;
import com.example.audiosensor.RecordingItem;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    ArrayList<RecordingItem> arrayList;
    DBHelper dbHelper;
    ContactAdapterDemo contactAdapterDemo;
    RecyclerView recyclerView;
    Dialog dialog;
    Button button1,button2;
    String path;
    long time;
    TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        dbHelper=new DBHelper(getContext());
        arrayList=new ArrayList<>();
        arrayList=dbHelper.getAllAudio();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        contactAdapterDemo=new ContactAdapterDemo(arrayList);
        recyclerView=root.findViewById(R.id.recycler);
        contactAdapterDemo.setIonClickContact(new IonClickContact() {
            @Override
            public void onClickName(RecordingItem recordingItem) {
                path=recordingItem.getPath();
                time=recordingItem.getLength();
                showDialog();
            }

            @Override
            public void onClickImage(RecordingItem recordingItem) {
                showDialog();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapterDemo);

        return root;
    }
    public void showDialog() {
        dialog = new Dialog(getContext());
        dialog.setTitle("Thangcode.com");
        dialog.setContentView(R.layout.dialog_play_audio);
        dialog.show();
        button1= dialog.findViewById(R.id.startdialog);
        button2= dialog.findViewById(R.id.stopdialog);
        textView=dialog.findViewById(R.id.timecount);
        final CountDownTimer countDownTimer=new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                textView.setText("done!");
            }
        };
        final MediaPlayer mp=new MediaPlayer();
        try{
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(path);

            mp.prepare();
        }catch(Exception e){e.printStackTrace();}
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
               countDownTimer.start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                countDownTimer.cancel();
                Toast.makeText(getContext(),"video da dung",Toast.LENGTH_LONG).show();
            }
        });

    }

}