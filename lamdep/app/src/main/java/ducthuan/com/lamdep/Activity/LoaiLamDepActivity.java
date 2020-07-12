package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.LoaiLamDepAdapter;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoaiLamDepActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvLoaiLamDep;
    Intent intent;
    String tenloaild = "";
    String loaild = "";
    LoaiLamDepAdapter loaiLamDepAdapter;
    ArrayList<LoaiLamDep>loaiLamDeps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_lam_dep);
        intent = getIntent();
        if(intent.hasExtra("tenloailamdep") && intent.hasExtra("loailamdep")){
            tenloaild = intent.getStringExtra("tenloailamdep");
            loaild = intent.getStringExtra("loailamdep");
            addControls();
            getDataLoaiLamDep();
            addEvents();

        }

    }

    private void getDataLoaiLamDep() {
        DataService dataService = APIService.getService();
        Call<List<LoaiLamDep>>callback = dataService.getLoaiLamDep(loaild);
        callback.enqueue(new Callback<List<LoaiLamDep>>() {
            @Override
            public void onResponse(Call<List<LoaiLamDep>> call, Response<List<LoaiLamDep>> response) {
                loaiLamDeps = (ArrayList<LoaiLamDep>) response.body();
                if(loaiLamDeps.size()>0){
                    loaiLamDepAdapter = new LoaiLamDepAdapter(LoaiLamDepActivity.this,loaiLamDeps);
                    rvLoaiLamDep.setLayoutManager(new LinearLayoutManager(LoaiLamDepActivity.this));
                    rvLoaiLamDep.setNestedScrollingEnabled(true);
                    rvLoaiLamDep.setHasFixedSize(true);
                    rvLoaiLamDep.setAdapter(loaiLamDepAdapter);
                    loaiLamDepAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<LoaiLamDep>> call, Throwable t) {

            }
        });
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
        rvLoaiLamDep = findViewById(R.id.rvLoaiLamDep);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        getSupportActionBar().setTitle(tenloaild);



    }
}
