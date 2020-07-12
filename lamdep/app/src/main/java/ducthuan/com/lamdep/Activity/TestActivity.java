package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.View;


import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.BaiVietAdapter;
import ducthuan.com.lamdep.Model.BaiViet;
import ducthuan.com.lamdep.R;


public class TestActivity extends AppCompatActivity{
    Toolbar toolbar;
    RecyclerView rv1,rv2;
    public static BaiVietAdapter baiVietAdapter;
    public static ArrayList<BaiViet>baiViets;


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




    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rv1 = findViewById(R.id.rv1);
        rv2 = findViewById(R.id.rv2);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        baiViets = new ArrayList<>();
        baiViets.add(new BaiViet(1,R.drawable.ic_tab_skin,"Da đẹp",false));
        baiViets.add(new BaiViet(2,R.drawable.ic_make_up,"Trang điểm",false));
        baiViets.add(new BaiViet(3,R.drawable.ic_tab_hair,"Tóc đẹp",false));
        baiViets.add(new BaiViet(4,R.drawable.ic_tab_style,"Mặc đẹp",false));
        baiViets.add(new BaiViet(5,R.drawable.ic_tab_workout,"Dáng đẹp",false));
        baiViets.add(new BaiViet(6,R.drawable.ic_fitness,"Tập luyện",false));

        rv1.setLayoutManager(new LinearLayoutManager(TestActivity.this));
        rv1.setHasFixedSize(true);
        rv1.setNestedScrollingEnabled(true);

        baiVietAdapter = new BaiVietAdapter(TestActivity.this,baiViets);
        rv1.setAdapter(baiVietAdapter);





    }


}
