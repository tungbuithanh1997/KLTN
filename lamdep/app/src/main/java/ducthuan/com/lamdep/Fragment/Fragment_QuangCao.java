package ducthuan.com.lamdep.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.QuangCaoAdapter;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_QuangCao extends Fragment {

    ViewPager viewPagerQuangCao;
    CircleIndicator indicatorQuangCao;
    QuangCaoAdapter quangCaoAdapter;
    ArrayList<QuangCao>quangCaos;

    Runnable runnable;
    Handler handler;

    int currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quang_cao, container, false);

        viewPagerQuangCao = view.findViewById(R.id.viewPagerQuangCao);
        indicatorQuangCao = view.findViewById(R.id.indicatorQuangCao);
        getDataQuangCao();


        return view;
    }

    private void getDataQuangCao() {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>>callback = dataService.layQuangCaoTheoNgay();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                quangCaos = (ArrayList<QuangCao>) response.body();
                if(quangCaos.size()>0){
                    quangCaoAdapter = new QuangCaoAdapter(getActivity(), quangCaos);
                    viewPagerQuangCao.setAdapter(quangCaoAdapter);
                    indicatorQuangCao.setViewPager(viewPagerQuangCao);

                    //Tu dong chay quang cao
                    handler = new Handler();
                    //thuc hien hanh dong khi handler goi
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            currentItem = viewPagerQuangCao.getCurrentItem();
                            currentItem++;
                            if(currentItem>=viewPagerQuangCao.getAdapter().getCount()){
                                currentItem=0;
                            }
                            viewPagerQuangCao.setCurrentItem(currentItem, true);
                            handler.postDelayed(runnable, 4500);
                        }
                    };
                    handler.postDelayed(runnable, 4500);
                }



            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {

            }
        });
    }
}
