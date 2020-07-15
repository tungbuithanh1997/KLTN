package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.SanPhamGoiYAdapter;
import ducthuan.com.lamdep.Interface.ILoadMore;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SanPhamGoiY extends Fragment {

    RecyclerView rvSanPhamGoiY;
    GridLayoutManager gridLayoutManager;
    ArrayList<SanPham>sanPhams;
    SanPhamGoiYAdapter sanPhamGoiYAdapter;
    int item_andautien = 0;
    int tong_item = 0;
    int item_loadtruoc = 6;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goi_y_hom_nay, container,false);
        rvSanPhamGoiY = view.findViewById(R.id.rvSanPhamGoiY);

        getDataSanPhamGoiY();

        return view;
    }



    private void getDataSanPhamGoiY() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamGoiY();
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    gridLayoutManager = new GridLayoutManager(getActivity(),2);
                    rvSanPhamGoiY.setLayoutManager(gridLayoutManager);
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(getActivity(), sanPhams);
                    rvSanPhamGoiY.setHasFixedSize(true);
                    //Khắc phục croll chậm
                    rvSanPhamGoiY.setNestedScrollingEnabled(true);
                    rvSanPhamGoiY.setAdapter(sanPhamGoiYAdapter);
                }

                /*rvSanPhamGoiY.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        RecyclerView.LayoutManager layoutManager = gridLayoutManager;
                        item_andautien = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                        tong_item = gridLayoutManager.getItemCount();
                        Log.d("tongitem", tong_item+"-"+item_andautien);
                    }
                });*/

                //sanPhamGoiYAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

}
