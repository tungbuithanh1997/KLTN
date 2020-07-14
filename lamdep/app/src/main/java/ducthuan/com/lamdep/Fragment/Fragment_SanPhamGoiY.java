package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.SanPhamGoiYAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SanPhamGoiY  extends Fragment {

    RecyclerView rvSanPhamGoiY;
    GridLayoutManager gridLayoutManager;
    ArrayList<SanPham> sanPhams;
    SanPhamGoiYAdapter sanPhamGoiYAdapter;
    SharedPreferences sharedPreferences;
    String manv = "";
    View view;
    /*
    GetRecommendAsyncTask asyncTask = new GetRecommendAsyncTask(new GetRecommendAsyncTask.AsyncResponse(){
        @Override
        public void processFinish(List<SanPham> output) {
                            output = (ArrayList<SanPham>) output;
                         if(output != null){
                             for (SanPham sp : output)
                                 sanPhams.add(sp);

                             sanPhamGoiYAdapter.notifyDataSetChanged();


                    }

        }
    });*/
    int item_andautien = 0;
    int tong_item = 0;
    int item_loadtruoc = 6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goi_y_hom_nay, container,false);
        addControls();
        return view;
    }

    private void addControls() {
        rvSanPhamGoiY = view.findViewById(R.id.rvSanPhamGoiY);
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        manv= sharedPreferences.getString("manv","");
        sanPhams = new ArrayList<>();
        rvSanPhamGoiY.setLayoutManager(new GridLayoutManager(getContext(),2));
        rvSanPhamGoiY.setHasFixedSize(true);
        rvSanPhamGoiY.setNestedScrollingEnabled(true);
        if(manv.equals("")){
            getDSSPChuaDangNhap();
        }else {
            ABC();
        }

    }


    private void getDSSPChuaDangNhap(){
        DataService dataService = APIService.getService();
        Call<List<SanPham>>call = dataService.layDanhSachSanPhamGoiY();
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams.clear();
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(getActivity(), sanPhams);
                    rvSanPhamGoiY.setAdapter(sanPhamGoiYAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void ABC()
    {
        DataService dataService = APIService.getRSService();
        Call<List<SanPham>>call = dataService.getSPGoiYRS(manv, "20");
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams.clear();
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(getContext(),sanPhams);
                    rvSanPhamGoiY.setAdapter(sanPhamGoiYAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                Log.d("sss",t.toString());
            }
        });
    }

}
