package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ducthuan.com.lamdep.R;


public class TestActivity extends AppCompatActivity{
    Toolbar toolbar;

    ImageView imgHinh;
    Button btnBatDau;

    boolean batdau = true;

    ArrayList<Integer>dsHinh;
    int vitri = 0;

    CountDownTimer countDownTimer;

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        addControls();
        addEvents();

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(batdau){
                    btnBatDau.setText("Dừng lại");
                    batdau = false;
                    mediaPlayer.start();
                    countDownTimer.start();


                }else {
                    btnBatDau.setText("Bắt đầu");
                    countDownTimer.cancel();
                    batdau = true;
                }
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgHinh = findViewById(R.id.imgHinh);
        btnBatDau = findViewById(R.id.btnBatDau);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);



        dsHinh = new ArrayList<>();
        dsHinh.add(R.drawable.dt1);
        dsHinh.add(R.drawable.dt2);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.coi);
        countDownTimer = new CountDownTimer(20000,500) {
            @Override
            public void onTick(long l) {

                imgHinh.setImageResource(dsHinh.get(vitri));
                vitri = vitri + 1;

                if(vitri == dsHinh.size()){
                    vitri = 0;
                }
            }

            @Override
            public void onFinish() {
                countDownTimer.start();
            }
        };


    }
}
