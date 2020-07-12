package ducthuan.com.lamdep.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Interface.ILoadMore;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;



public class SanPhamGoiYAdapter extends RecyclerView.Adapter<SanPhamGoiYAdapter.ViewHolder>{

    Context context;
    ArrayList<SanPham>sanPhams;

    public SanPhamGoiYAdapter(Context context, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham_goiy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPham sanPham = sanPhams.get(position);

        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSanPham);

        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        int gsp = Integer.parseInt(sanPham.getGIA());
        holder.txtGia.setText(String.valueOf(decimalFormat.format(gsp))+"đ");

        holder.txtLuotMua.setText("Đã bán "+ sanPham.getLUOTMUA());
        holder.txtTenSanPham.setText(sanPham.getTENSP());

    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSanPham;
        TextView txtTenSanPham, txtGia,txtLuotMua;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHinhSanPham = itemView.findViewById(R.id.imgHinhSanPhamGoiY);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPhamGoiY);
            txtGia = itemView.findViewById(R.id.txtGiaSanPhamGoiY);
            txtLuotMua = itemView.findViewById(R.id.txtLuotMuaSanPhamGoiY);
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
}
