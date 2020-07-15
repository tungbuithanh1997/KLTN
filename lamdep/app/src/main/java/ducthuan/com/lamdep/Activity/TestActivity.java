package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


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


public class TestActivity extends AppCompatActivity implements TabHost.TabContentFactory {
    Toolbar toolbar;
    TabHost tabHost;

    ArrayList<LoaiLamDep>loaiLamDeps;
    LoaiLamDepAdapter loaiLamDepAdapter;
    RecyclerView rvLoaiLamDep;




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
        tabHost = findViewById(R.id.tabHost);


        tabHost.setup();

        Resources res = getResources();
        Configuration cfg = res.getConfiguration();
        boolean hor = cfg.orientation == Configuration.ORIENTATION_PORTRAIT;

        if (hor) {
            TabWidget tw = tabHost.getTabWidget();
            tw.setOrientation(LinearLayout.VERTICAL);
        }

        tabHost.addTab(tabHost.newTabSpec("1")
                .setIndicator(createIndicatorView(tabHost, "Da đẹp", getResources().getDrawable(R.drawable.ic_tab_skin)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("2")
                .setIndicator(createIndicatorView(tabHost, "Trang điểm", getResources().getDrawable(R.drawable.ic_make_up)))
                .setContent(this));

        tabHost.addTab(tabHost.newTabSpec("3")
                .setIndicator(createIndicatorView(tabHost, "Tóc đẹp", getResources().getDrawable(R.drawable.ic_tab_hair)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("4")
                .setIndicator(createIndicatorView(tabHost, "Mặc đẹp", getResources().getDrawable(R.drawable.ic_tab_style)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("5")
                .setIndicator(createIndicatorView(tabHost, "Dáng đẹp", getResources().getDrawable(R.drawable.ic_tab_workout)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("6")
                .setIndicator(createIndicatorView(tabHost, "Tập luyện", getResources().getDrawable(R.drawable.ic_fitness)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("6")
                .setIndicator(createIndicatorView(tabHost, "Tập luyện", getResources().getDrawable(R.drawable.ic_fitness)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("6")
                .setIndicator(createIndicatorView(tabHost, "Tập luyện", getResources().getDrawable(R.drawable.ic_fitness)))
                .setContent(this));

    }

    private View createIndicatorView(TabHost tabHost, CharSequence label, Drawable icon) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View tabIndicator = inflater.inflate(R.layout.tab_indicator,
                tabHost.getTabWidget(), // tab widget is the parent
                false); // no inflate params

        final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
        tv.setText(label);

        final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
        iconView.setImageDrawable(icon);

        return tabIndicator;
    }


    @Override

    public View createTabContent(String tag) {

        loaiLamDeps = new ArrayList<>();
        rvLoaiLamDep = new RecyclerView(this);
        rvLoaiLamDep.setNestedScrollingEnabled(true);
        rvLoaiLamDep.setHasFixedSize(true);
        rvLoaiLamDep.setLayoutManager(new LinearLayoutManager(this));


        DataService dataService = APIService.getService();
        Call<List<LoaiLamDep>>call = dataService.getLoaiLamDep(tag);
        call.enqueue(new Callback<List<LoaiLamDep>>() {
            @Override
            public void onResponse(Call<List<LoaiLamDep>> call, Response<List<LoaiLamDep>> response) {
                loaiLamDeps = (ArrayList<LoaiLamDep>) response.body();
                if(loaiLamDeps.size()>0){
                    loaiLamDepAdapter = new LoaiLamDepAdapter(TestActivity.this,loaiLamDeps);
                    rvLoaiLamDep.setAdapter(loaiLamDepAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<LoaiLamDep>> call, Throwable t) {

            }
        });


        return rvLoaiLamDep;
    }


}
