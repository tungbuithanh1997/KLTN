package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;

public class HienThiSanPhamTheoDanhMucAdapter extends RecyclerView.Adapter<HienThiSanPhamTheoDanhMucAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham>sanPhams;
    int layoutManager;

    public HienThiSanPhamTheoDanhMucAdapter(Context context, int layoutManager, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
        this.layoutManager = layoutManager;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgHinhSPTheoDM;
        TextView txtPhanTramKMTheoDM,txtTenSPTheoDM,txtGiaSPTheoDM,txtGiaSPTheoDMChuaKM,txtLuotMuaSPTheoDM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPTheoDM = itemView.findViewById(R.id.imgHinhSPTheoDM);
            txtPhanTramKMTheoDM = itemView.findViewById(R.id.txtPhanTramKMTheoDM);
            txtTenSPTheoDM = itemView.findViewById(R.id.txtTenSPTheoDM);
            txtGiaSPTheoDM = itemView.findViewById(R.id.txtGiaSPTheoDM);
            txtGiaSPTheoDMChuaKM = itemView.findViewById(R.id.txtGiaSPTheoDMChuaKM);
            txtLuotMuaSPTheoDM = itemView.findViewById(R.id.txtLuotMuaSPTheoDM);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("itemsp",sanPhams.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public HienThiSanPhamTheoDanhMucAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(layoutManager == 1){
            view = LayoutInflater.from(context).inflate(R.layout.dong_grid_sanpham_theodanhmuc, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.dong_list_sanpham_theodanhmuc, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HienThiSanPhamTheoDanhMucAdapter.ViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPTheoDM);
        holder.txtTenSPTheoDM.setText(sanPham.getTENSP());
        holder.txtLuotMuaSPTheoDM.setText("Đã bán "+sanPham.getLUOTMUA());

        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int gkm = Integer.parseInt(sanPham.getGIA());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        if(km == 0){
            holder.txtPhanTramKMTheoDM.setVisibility(View.GONE);
            holder.txtGiaSPTheoDMChuaKM.setVisibility(View.GONE);
            holder.txtGiaSPTheoDM.setText(String.valueOf(decimalFormat.format(gkm))+"đ");

        }else if(km > 0){
            int giagiam = (gkm/100)* (100-km);
            holder.txtGiaSPTheoDM.setText(String.valueOf(decimalFormat.format(giagiam)+"đ"));
            holder.txtPhanTramKMTheoDM.setText("-"+sanPham.getKHUYENMAI()+"%");
            holder.txtGiaSPTheoDMChuaKM.setText(String.valueOf(decimalFormat.format((gkm))+"đ"));
            holder.txtGiaSPTheoDMChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }


}
