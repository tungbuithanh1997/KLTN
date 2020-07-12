package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.TestActivity;
import ducthuan.com.lamdep.Model.BaiViet;
import ducthuan.com.lamdep.R;

public class BaiVietAdapter extends RecyclerView.Adapter<BaiVietAdapter.ViewHolder> {

    Context context;
    ArrayList<BaiViet>baiViets;

    public BaiVietAdapter(Context context, ArrayList<BaiViet> baiViets) {
        this.context = context;
        this.baiViets = baiViets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_loaibaiviet, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final BaiViet baiViet = baiViets.get(position);
        holder.imgHinh.setImageResource(baiViet.getHinh());
        holder.txtTen.setText(baiViet.getTen());

        if(baiViet.isBg() == false){
            holder.viewBg.setVisibility(View.VISIBLE);
        }else {
            holder.viewBg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < TestActivity.baiViets.size(); i++) {
                    if(i==position){
                        TestActivity.baiViets.get(i).setBg(true);
                    }else {
                        TestActivity.baiViets.get(i).setBg(false);
                    }
                }
                TestActivity.baiVietAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return baiViets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView txtTen;
        View viewBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            txtTen = itemView.findViewById(R.id.txtTen);
            viewBg = itemView.findViewById(R.id.viewBg);
        }
    }

}
