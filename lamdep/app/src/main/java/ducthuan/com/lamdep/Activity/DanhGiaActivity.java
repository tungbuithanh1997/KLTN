package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.DanhGiaAdapter;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.R;

public class DanhGiaActivity extends AppCompatActivity {

    ArrayList<DanhGia>danhGias;
    DanhGiaAdapter danhGiaAdapter;
    ArrayList<DanhGia>temps = new ArrayList<>();

    Toolbar toolbar;
    TabLayout tabLayout;
    RecyclerView rvDanhGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        GetIntent();
        addControls();
        addEvents();
    }

    private void addEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: temps.clear();temps.addAll(danhGias); danhGiaAdapter.notifyDataSetChanged();break;
                    case 1: temps.clear();
                    for(int i = 0; i < danhGias.size(); i++){
                        if(Integer.parseInt(danhGias.get(i).getSOSAO()) == 5){
                            temps.add(danhGias.get(i));
                        }
                    }
                    danhGiaAdapter.notifyDataSetChanged();
                    break;
                    case 2: temps.clear();
                        for(int i = 0; i < danhGias.size(); i++){
                            if(Integer.parseInt(danhGias.get(i).getSOSAO()) == 4){
                                temps.add(danhGias.get(i));
                            }
                        }
                        danhGiaAdapter.notifyDataSetChanged();
                        break;
                    case 3: temps.clear();
                        for(int i = 0; i < danhGias.size(); i++){
                            if(Integer.parseInt(danhGias.get(i).getSOSAO()) == 3){
                                temps.add(danhGias.get(i));
                            }
                        }
                        danhGiaAdapter.notifyDataSetChanged();
                        break;
                    case 4: temps.clear();
                        for(int i = 0; i < danhGias.size(); i++){
                            if(Integer.parseInt(danhGias.get(i).getSOSAO()) == 2){
                                temps.add(danhGias.get(i));
                            }
                        }
                        danhGiaAdapter.notifyDataSetChanged();
                        break;
                    case 5: temps.clear();
                        for(int i = 0; i < danhGias.size(); i++){
                            if(Integer.parseInt(danhGias.get(i).getSOSAO()) == 1){
                                temps.add(danhGias.get(i));
                            }
                        }
                        danhGiaAdapter.notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        rvDanhGia = findViewById(R.id.rvDanhGia);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.getTabAt(0).setText("Tất cả ("+danhGias.size()+")");
        danhGiaAdapter = new DanhGiaAdapter(DanhGiaActivity.this, temps);
        rvDanhGia.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Divider mặc định
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        /*//Custom divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDanhGia.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.custom_divider_danhgia);
        dividerItemDecoration.setDrawable(drawable);*/
        rvDanhGia.addItemDecoration(dividerItemDecoration);
        rvDanhGia.setHasFixedSize(true);
        rvDanhGia.setNestedScrollingEnabled(true);
        rvDanhGia.setAdapter(danhGiaAdapter);

    }



    private void GetIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("danhgias")){
            danhGias = intent.getParcelableArrayListExtra("danhgias");
            temps.addAll(danhGias);
        }
    }
}
