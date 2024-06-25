package com.example.loving_essentials.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.loving_essentials.Domain.Entity.ProductDetail;
import com.example.loving_essentials.R;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtName, txtPrice,txtQuantity, txtDes, txtBrandname, txtCateName;
    ImageView imgPro;
    Button btnaddtocart, btnOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtDes = findViewById(R.id.txtDes);
        txtBrandname = findViewById(R.id.txtBrand);
        txtCateName = findViewById(R.id.txtCate);
        imgPro = findViewById(R.id.imgProduct);
        btnaddtocart = findViewById(R.id.btnAddToCartDetail);
        btnOrder = findViewById(R.id.btnBuyNow);

        Intent intent = getIntent();

        ProductDetail productDetail = (ProductDetail) intent.getSerializableExtra("product");

        txtName.setText(productDetail.getName());
        txtPrice.setText("$ " + productDetail.getPrice());
        txtQuantity.setText("Items Avalable: " + productDetail.getQuantity());
        txtDes.setText(productDetail.getDescription());
        txtBrandname.setText(productDetail.getBrandName());
        txtCateName.setText(productDetail.getCategoryName());
        Glide.with(this)
                .load(productDetail.getImageURL())
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDE_TAG", "Image Load Error", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgPro);
    }



}