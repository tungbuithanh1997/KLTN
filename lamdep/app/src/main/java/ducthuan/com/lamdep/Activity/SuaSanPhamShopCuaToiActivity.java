package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.HinhSanPhamAdapter;
import ducthuan.com.lamdep.Adapter.HinhSanPhamSuaAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuaSanPhamShopCuaToiActivity extends AppCompatActivity {

    private static final int PERMISSTION_CODE = 1000;

    public final static int PICK_IMAGE_REQUEST = 1;
    Toolbar toolbar;
    ImageView imgAnhBia;
    Uri uriAnhBia;

    SanPham sanPham;

    SharedPreferences sharedPreferences;

    RecyclerView rvHinhNho;
    Button btnThemHinhAnh,btnDeleteProduct;
    EditText txtTenSP,txtMoTaSP,txtGiaSP,txtSoLuongSP,txtKhuyenMai;
    TextView txtDanhMuc;

    public static ArrayList<Uri> uriHinhSP;
    public static HinhSanPhamSuaAdapter hinhSanPhamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham_shop_cua_toi);
        Intent intent = getIntent();
        if (intent.hasExtra("itemsp")) {
            sanPham = intent.getParcelableExtra("itemsp");
            addControls();
            addEvents();
        }

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnThemHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSTION_CODE);

                    } else {
                        // Gọi intent của hệ thống để chọn ảnh nhé.
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        // Thêm dòng này để có thể select nhiều ảnh trong 1 lần nhé các bạn
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Files to Upload"),
                                PICK_IMAGE_REQUEST);
                    }
                }
            }
        });

        txtDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaSanPhamShopCuaToiActivity.this);
                builder.setTitle("Chọn danh mục");
                builder.setView(R.layout.custom_dialog_chondanhmuc);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                final RadioGroup rgDanhMuc = dialog.findViewById(R.id.rgDanhMuc);
                Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
                Button btnHuy = dialog.findViewById(R.id.btnHuy);

                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rgDanhMuc.getCheckedRadioButtonId()==-1){
                            Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Vui lòng chọn danh mục !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int vtDanhMuc = rgDanhMuc.getCheckedRadioButtonId();
                        RadioButton radioButton = dialog.findViewById(vtDanhMuc);
                        txtDanhMuc.setText(radioButton.getText().toString());
                        dialog.dismiss();
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaSanPhamShopCuaToiActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataService dataService = APIService.getService();
                        Call<String>call = dataService.deleteProduct(sanPham.getMASP());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Xóa sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Xóa sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSTION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Gọi intent của hệ thống để chọn ảnh nhé.
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    // Thêm dòng này để có thể select nhiều ảnh trong 1 lần nhé các bạn
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Files to Upload"),
                            PICK_IMAGE_REQUEST);
                } else {
                    Toast.makeText(this, "Từ chối cấp quyền...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri item = data.getClipData().getItemAt(i).getUri();
                    uriHinhSP.add(item);
                }

            }else if(data.getData()!=null){
                Uri uri = data.getData();
                uriHinhSP.add(uri);
            }
            hinhSanPhamAdapter.notifyDataSetChanged();
        }
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgAnhBia = findViewById(R.id.imgAnhBia);
        rvHinhNho = findViewById(R.id.rvHinhNho);
        btnThemHinhAnh = findViewById(R.id.btnThemHinhAnh);

        txtTenSP = findViewById(R.id.txtTenSP);
        txtMoTaSP = findViewById(R.id.txtMoTaSP);
        txtGiaSP = findViewById(R.id.txtGiaSP);
        txtSoLuongSP = findViewById(R.id.txtSoLuongSP);
        txtKhuyenMai = findViewById(R.id.txtKhuyenMai);
        txtDanhMuc = findViewById(R.id.txtDanhMuc);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        uriHinhSP = new ArrayList<>();

        hinhSanPhamAdapter = new HinhSanPhamSuaAdapter(SuaSanPhamShopCuaToiActivity.this,uriHinhSP);
        rvHinhNho.setLayoutManager(new LinearLayoutManager(SuaSanPhamShopCuaToiActivity.this, RecyclerView.HORIZONTAL,false));
        rvHinhNho.setHasFixedSize(true);
        rvHinhNho.setAdapter(hinhSanPhamAdapter);
        hinhSanPhamAdapter.notifyDataSetChanged();

        Picasso.with(SuaSanPhamShopCuaToiActivity.this).load(TrangChuActivity.base_url+sanPham.getANHLON()).into(imgAnhBia);
        if(!sanPham.getANHNHO().equals("") && sanPham.getANHNHO()!=null){
            String[]anhnhos = sanPham.getANHNHO().split(",");
            for (int i = 0; i < anhnhos.length; i++) {
                uriHinhSP.add(Uri.parse(TrangChuActivity.base_url+anhnhos[i]));
            }
            hinhSanPhamAdapter.notifyDataSetChanged();
        }

        txtTenSP.setText(sanPham.getTENSP());
        txtMoTaSP.setText(sanPham.getTHONGTIN());
        txtGiaSP.setText(sanPham.getGIA());
        txtSoLuongSP.setText(sanPham.getSOLUONG());
        txtKhuyenMai.setText(sanPham.getKHUYENMAI());

        int maloaisp = Integer.parseInt(sanPham.getMALOAISP());
        if(maloaisp==1){
            txtDanhMuc.setText("Thời trang nữ");
        }else if(maloaisp==2){
            txtDanhMuc.setText("Thời trang nam");
        }else if(maloaisp==3){
            txtDanhMuc.setText("Sức khỏe Làm đẹp");
        }else if(maloaisp==4){
            txtDanhMuc.setText("Đồng hồ Trang sức");
        }else if(maloaisp==5){
            txtDanhMuc.setText("Thời trang thương hiệu");
        }else if(maloaisp==6){
            txtDanhMuc.setText("Thể thao Tập luyện");
        }else if(maloaisp==7){
            txtDanhMuc.setText("Phụ kiện thời trang");
        }else if(maloaisp==8){
            txtDanhMuc.setText("Sản phẩm khác");
        }




        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sua_san_pham_shopcuatoi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itXong:
                suaSanPham();
                break;
        }

        return true;
    }

    private void suaSanPham() {

        String masp = sanPham.getMASP();
        String tensp =txtTenSP.getText().toString();
        String motasp =txtMoTaSP.getText().toString();
        String giasp =txtGiaSP.getText().toString();
        String soluong = txtSoLuongSP.getText().toString();
        String khuyenmai =txtKhuyenMai.getText().toString();
        String danhmuc = txtDanhMuc.getText().toString();
        int maloaisp = 1;
        if(danhmuc.equals("Thời trang nữ")){
            maloaisp = 1;
        }else if(danhmuc.equals("Thời trang nam")){
            maloaisp = 2;
        }else if(danhmuc.equals("Sức khỏe Làm đẹp")){
            maloaisp = 3;
        }else if(danhmuc.equals("Đồng hồ Trang sức")){
            maloaisp = 4;
        }else if(danhmuc.equals("Thời trang thương hiệu")){
            maloaisp = 5;
        }else if(danhmuc.equals("Thể thao Tập luyện")){
            maloaisp = 6;
        }else if(danhmuc.equals("Phụ kiện thời trang")){
            maloaisp = 7;
        }else if(danhmuc.equals("Sản phẩm khác")){
            maloaisp = 8;
        }

        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv","");
        if(tensp.equals("")||motasp.equals("")||giasp.equals("")||soluong.equals("")||khuyenmai.equals("")){
            Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Vui lòng nhập đủ thông tin !", Toast.LENGTH_SHORT).show();
        }else {
            DataService dataService1 = APIService.getService();
            Call<String>call1 = dataService1.editProduct(masp,tensp,giasp,khuyenmai,motasp,soluong,String.valueOf(maloaisp));
            call1.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if(kq.equals("OK")){
                        Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Sửa sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Sửa sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }

        /*List<MultipartBody.Part> parts = new ArrayList<>();
        ArrayList<Uri>uris = new ArrayList<>();
        uris.add(uriAnhBia);
        for (int i = 0; i < uriHinhSP.size(); i++) {
            uris.add(uriHinhSP.get(i));

        }
        for (int i = 0; i < uris.size(); i++) {
            parts.add(prepareFilePart("image"+i,uris.get(i)));
        }

        RequestBody description = createPartFromString("uploadhinhsp");
        RequestBody size = createPartFromString(""+parts.size());
        DataService dataService = APIService.getService();
        Call<String> call = dataService.uploadMultipleFilesDynamic(description,size,parts);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.length()>0){
                    String[]hinhsp = kq.split(",");
                    String anhlon = "hinhanh/hinhsanpham/"+hinhsp[0];
                    String anhnho = "";
                    if(hinhsp.length>=1){
                        for (int i = 1; i < hinhsp.length; i++) {
                            if(i==hinhsp.length-1){
                                anhnho += "hinhanh/hinhsanpham/"+hinhsp[i];
                            }else {
                                anhnho += "hinhanh/hinhsanpham/"+hinhsp[i]+",";
                            }
                        }
                    }

                    Log.d("kiemtra",anhlon+"-"+anhnho);


                    String tensp =txtTenSP.getText().toString();
                    String motasp =txtMoTaSP.getText().toString();
                    String giasp =txtGiaSP.getText().toString();
                    String soluong = txtSoLuongSP.getText().toString();
                    String khuyenmai =txtKhuyenMai.getText().toString();
                    String slsp =txtSoLuongSP.getText().toString();
                    String danhmuc = txtDanhMuc.getText().toString();
                    int maloaisp = 1;
                    if(danhmuc.equals("Thời trang nữ")){
                        maloaisp = 1;
                    }else if(danhmuc.equals("Thời trang nam")){
                        maloaisp = 2;
                    }else if(danhmuc.equals("Sức khỏe Làm đẹp")){
                        maloaisp = 3;
                    }else if(danhmuc.equals("Đồng hồ Trang sức")){
                        maloaisp = 4;
                    }else if(danhmuc.equals("Thời trang thương hiệu")){
                        maloaisp = 5;
                    }else if(danhmuc.equals("Thể thao Tập luyện")){
                        maloaisp = 6;
                    }else if(danhmuc.equals("Phụ kiện thời trang")){
                        maloaisp = 7;
                    }else if(danhmuc.equals("Sản phẩm khác")){
                        maloaisp = 8;
                    }

                    sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
                    String manv = sharedPreferences.getString("manv","");
                    if(tensp.equals("")||motasp.equals("")||giasp.equals("")||soluong.equals("")||khuyenmai.equals("")){
                        Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Vui lòng nhập đủ thông tin !", Toast.LENGTH_SHORT).show();
                    }else {
                        DataService dataService1 = APIService.getService();
                        Call<String>call1 = dataService1.addProduct(tensp,giasp,khuyenmai,anhlon,anhnho,motasp,soluong,String.valueOf(maloaisp),manv);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Thêm sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Thêm sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }



                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SuaSanPhamShopCuaToiActivity.this, "Upload thất bại !", Toast.LENGTH_SHORT).show();
            }
        });*/




    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create (MediaType.parse(FileUtils.MIME_TYPE_IMAGE), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
