package com.example.loving_essentials.UI.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.StringUtils;
import com.example.loving_essentials.Domain.Entity.Brand;
import com.example.loving_essentials.Domain.Entity.Category;
import com.example.loving_essentials.Domain.Entity.DTOs.Admin.ProductManagement.CreateProduct;
import com.example.loving_essentials.Domain.Entity.DTOs.Admin.ProductManagement.ProductDetailAdmin;
import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserDTO;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Services.IService.IBrandService;
import com.example.loving_essentials.Domain.Services.IService.ICategoryService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;
import com.example.loving_essentials.Domain.Services.Service.BrandService;
import com.example.loving_essentials.Domain.Services.Service.CategoryService;
import com.example.loving_essentials.Domain.Services.Service.ProductService;
import com.example.loving_essentials.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagementFragment extends Fragment {
    List<ProductDTO> products;
    List<Category> categories;
    List<Brand> brands;
    IProductService productService;
    ICategoryService categoryService;
    IBrandService brandService;
    ArrayAdapter categoryAdapter, brandAdapter;
    int selectedCreateBrand = 0;
    int selectedCreateCategory = 0;
    int selectedUpdateCategory = 0;
    int selectedUpdateBrand = 0;
    private TableLayout tableLayout;
    Button btnStartCreate;
    Uri imagePath;
    Map config = new HashMap();
    ImageView imgCreatePreview;
    ImageView imgUpdatePreview;
    private static final String TAG = "Upload ###";

    private static int IMAGE_REQ=1;

    public ProductManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_management, container, false);
        categoryService = CategoryService.getCategoryService();
        categories = new ArrayList<>();
        brandService = BrandService.getBrandService();
        brands = new ArrayList<>();
        tableLayout = root.findViewById(R.id.tbl_product);
        productService = ProductService.getProductService();
        products = new ArrayList<>();
        fetchProducts();
        btnStartCreate = root.findViewById(R.id.btnStartCreate);
        btnStartCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateProductDialog();
            }
        });
        initCongif();
        return root;
    }
    private void fetchProducts() {
        Call<ProductDTO[]> call = productService.getProductsAdmin();
        Log.d("Information: ", "Enter success response");
        call.enqueue(new Callback<ProductDTO[]>() {
            @Override
            public void onResponse(Call<ProductDTO[]> call, Response<ProductDTO[]> response) {
                ProductDTO[] productList = response.body();
                Log.d("Information: ", "Enter success response");
                Log.d("Information: ", "List length: " + productList.length);
                if (productList != null) {
                    products.clear();
                    products.addAll(Arrays.asList(productList));
                    populateTable();
                } else {
                    Toast.makeText(getContext(), "Error in getting products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDTO[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in getting products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateTable() {
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }
        for (int i = 0; i < products.size(); i++) {
            ProductDTO item = products.get(i);
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundResource(R.drawable.product_table_row);

            TextView columnId = new TextView(getContext());
            columnId.setLayoutParams(new TableRow.LayoutParams(
                    0, 150, 1));
            columnId.setGravity(Gravity.CENTER);
            columnId.setPadding(8, 8, 8, 8);
            columnId.setText(String.valueOf(item.getId()));
            columnId.setBackgroundResource(R.drawable.product_table_row_border);

            TextView columnName = new TextView(getContext());
            columnName.setLayoutParams(new TableRow.LayoutParams(
                    0, 150, 1));
            columnName.setGravity(Gravity.CENTER);
            columnName.setPadding(8, 8, 8, 8);
            columnName.setText(truncateString(item.getName()));
            columnName.setBackgroundResource(R.drawable.product_table_row_border);

            TextView columnPrice = new TextView(getContext());
            columnPrice.setLayoutParams(new TableRow.LayoutParams(
                    0, 150, 1));
            columnPrice.setGravity(Gravity.CENTER);
            columnPrice.setPadding(8, 8, 8, 8);
            columnPrice.setText(String.valueOf(item.getPrice()));
            columnPrice.setBackgroundResource(R.drawable.product_table_row_border);

            TextView columnStatus = new TextView(getContext());
            String statusString = "Unavailable";
            if (item.getStatus() == 1) {
                statusString = "Available";
            }
            columnStatus.setLayoutParams(new TableRow.LayoutParams(
                    0, 150, 1));
            columnStatus.setGravity(Gravity.CENTER);
            columnStatus.setPadding(8, 8, 8, 8);
            columnStatus.setText(statusString);
            columnStatus.setBackgroundResource(R.drawable.product_table_row_border);

            tableRow.addView(columnId);
            tableRow.addView(columnName);
            tableRow.addView(columnPrice);
            tableRow.addView(columnStatus);

            // Add click listener to the row
            final int position = i; // Use a final variable to access inside the listener
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductDTO clickedItem = products.get(position);
                    showUpdateProductDialog(clickedItem);
                }
            });

            tableLayout.addView(tableRow);
        }
    }
    private void showUpdateProductDialog(ProductDTO productDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_product_admin, null);
        builder.setView(dialogView);

        EditText edtUpdateProductName = dialogView.findViewById(R.id.edtUpdateProductName);
        EditText edtUpdateProductDescription = dialogView.findViewById(R.id.edtUpdateProductDescription);
        EditText edtPrice = dialogView.findViewById(R.id.edtUpdateProductPrice);
        EditText edtQuantity = dialogView.findViewById(R.id.edtUpdateProductQuantity);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtUpdateProductImageUrl);
        AutoCompleteTextView selectedCategory = (AutoCompleteTextView)dialogView.findViewById(R.id.selectedCategory);
        AutoCompleteTextView selectedBrand = (AutoCompleteTextView) dialogView.findViewById(R.id.selectedBrand);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerUpdateProductStatus);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdateProduct);
        Button btnUploadImage = dialogView.findViewById(R.id.btnUploadImage);
        imgUpdatePreview = dialogView.findViewById(R.id.ivProductPreview);

        imgUpdatePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUpdateImage();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath == null) {
                    Toast.makeText(getContext(), "Please choose an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d(TAG, "onStart: "+"started");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.d(TAG, "onStart: "+"uploading");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Log.d(TAG, "onStart: "+"success");
                        if (resultData.get("url") != null) {
                            String url = resultData.get("url").toString();
                            edtImageUrl.setText(url);
                        }

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.d(TAG, "onStart: "+error);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.d(TAG, "onStart: "+error);
                    }
                }).dispatch();

            }
        });

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, categories);
        selectedCategory.setAdapter(categoryAdapter);
        Call<Category[]> callCategory = categoryService.getCategories();
        callCategory.enqueue(new Callback<Category[]>() {
            @Override
            public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                Category[] categoryResult = response.body();
                if (categoryResult == null) {
                    return;
                }

                categories.addAll(Arrays.asList(categoryResult));
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Category[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in getting categories", Toast.LENGTH_SHORT).show();
            }
        });
        selectedCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                selectedUpdateCategory = selectedCategory.getId();
            }
        });
        brands = new ArrayList<>();
        brandAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, brands);
        selectedBrand.setAdapter(brandAdapter);
        Call<Brand[]> callBrand = brandService.getBrands();
        callBrand.enqueue(new Callback<Brand[]>() {
            @Override
            public void onResponse(Call<Brand[]> call, Response<Brand[]> response) {
                Brand[] brandResult = response.body();
                if (brandResult == null) {
                    return;
                }
                brands.addAll(Arrays.asList(brandResult));
                brandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Brand[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in getting brands", Toast.LENGTH_SHORT).show();
            }
        });
        selectedBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                selectedUpdateBrand = selectedBrand.getId();
            }
        });

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Set existing user data
        edtUpdateProductName.setText(productDTO.getName());
        edtUpdateProductDescription.setText(productDTO.getDescription());
        edtImageUrl.setText(productDTO.getimageURL());
        edtPrice.setText(String.valueOf(productDTO.getPrice()));
        edtQuantity.setText(String.valueOf(productDTO.getQuantity()));
        spinnerStatus.setSelection(productDTO.getStatus());

        Glide.with(this).load(productDTO.getimageURL())
                .into(imgUpdatePreview);

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtUpdateProductName.getText().toString())) {
                edtUpdateProductName.setError("Required");
            } else if (TextUtils.isEmpty(edtUpdateProductDescription.getText().toString())) {
                edtUpdateProductDescription.setError("Required");
            } else if (TextUtils.isEmpty(edtImageUrl.getText().toString())) {
                edtImageUrl.setError("Required");
            } else if (TextUtils.isEmpty(edtPrice.getText().toString())) {
                edtPrice.setError("Required");
            } else if (TextUtils.isEmpty(edtQuantity.getText().toString())) {
                edtQuantity.setError("Required");
            } else if (selectedUpdateBrand == 0 && selectedUpdateCategory == 0) {
                Toast.makeText(getContext(), "Please select a category or brand", Toast.LENGTH_SHORT).show();
            } else {
                int id = productDTO.getId();
                String name = edtUpdateProductName.getText().toString();
                String description = edtUpdateProductDescription.getText().toString();
                String imageURL = edtImageUrl.getText().toString();
                double price = Double.parseDouble(edtPrice.getText().toString());
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                int categoryId = selectedUpdateCategory;
                int brandId = selectedUpdateBrand;
                byte status = (byte) spinnerStatus.getSelectedItemPosition();

                ProductDetailAdmin productDetailAdmin = new ProductDetailAdmin(id, name, price, description, imageURL, quantity, categoryId, brandId, status);

                updateProduct(productDetailAdmin);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showCreateProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_product_admin, null);
        builder.setView(dialogView);

        EditText edtCreateProductName = dialogView.findViewById(R.id.edtCreateProductName);
        EditText edtCreateProductDescription = dialogView.findViewById(R.id.edtCreateProductDescription);
        EditText edtPrice = dialogView.findViewById(R.id.edtCreateProductPrice);
        EditText edtQuantity = dialogView.findViewById(R.id.edtCreateProductQuantity);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtCreateProductImageUrl);
        AutoCompleteTextView selectedCategory = (AutoCompleteTextView)dialogView.findViewById(R.id.selectedCategory);
        AutoCompleteTextView selectedBrand = (AutoCompleteTextView) dialogView.findViewById(R.id.selectedBrand);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerCreateProductStatus);
        Button btnCreate = dialogView.findViewById(R.id.btnCreateProduct);
        Button btnUploadImage = dialogView.findViewById(R.id.btnUploadImage);
        imgCreatePreview = dialogView.findViewById(R.id.ivProductPreview);

        imgCreatePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath == null) {
                    Toast.makeText(getContext(), "Please choose an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d(TAG, "onStart: "+"started");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.d(TAG, "onStart: "+"uploading");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Log.d(TAG, "onStart: "+"success");
                        if (resultData.get("url") != null) {
                            String url = resultData.get("url").toString();
                            edtImageUrl.setText(url);
                        }

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.d(TAG, "onStart: "+error);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.d(TAG, "onStart: "+error);
                    }
                }).dispatch();

            }
        });

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, categories);
        selectedCategory.setAdapter(categoryAdapter);
        Call<Category[]> callCategory = categoryService.getCategories();
        callCategory.enqueue(new Callback<Category[]>() {
            @Override
            public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                Category[] categoryResult = response.body();
                if (categoryResult == null) {
                    return;
                }

                categories.addAll(Arrays.asList(categoryResult));
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Category[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in getting categories", Toast.LENGTH_SHORT).show();
            }
        });
        selectedCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                selectedCreateCategory = selectedCategory.getId();
            }
        });
        brands = new ArrayList<>();
        brandAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, brands);
        selectedBrand.setAdapter(brandAdapter);
        Call<Brand[]> callBrand = brandService.getBrands();
        callBrand.enqueue(new Callback<Brand[]>() {
            @Override
            public void onResponse(Call<Brand[]> call, Response<Brand[]> response) {
                Brand[] brandResult = response.body();
                if (brandResult == null) {
                    return;
                }
                brands.addAll(Arrays.asList(brandResult));
                brandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Brand[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in getting brands", Toast.LENGTH_SHORT).show();
            }
        });
        selectedBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                selectedCreateBrand = selectedBrand.getId();
            }
        });
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
        AlertDialog dialog = builder.create();

        btnCreate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtCreateProductName.getText().toString())) {
                edtCreateProductName.setError("Required");
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(edtCreateProductDescription.getText().toString())) {
                edtCreateProductDescription.setError("Required");
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(edtImageUrl.getText().toString())) {
                edtImageUrl.setError("Required");
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(edtPrice.getText().toString())) {
                edtPrice.setError("Required");
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(edtQuantity.getText().toString())) {
                edtQuantity.setError("Required");
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (selectedCreateCategory == 0 && selectedCreateCategory == 0) {
                Toast.makeText(getContext(), "Please select a category or brand", Toast.LENGTH_SHORT).show();
            } else {
                String name = edtCreateProductName.getText().toString();
                String description = edtCreateProductDescription.getText().toString();
                String imageUrl = edtImageUrl.getText().toString();
                double price = Double.parseDouble(edtPrice.getText().toString());
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                int categoryId = selectedCreateCategory;
                int brandId = selectedCreateBrand;
                byte status = (byte) spinnerStatus.getSelectedItemPosition();

                CreateProduct createProduct = new CreateProduct(name, price, description, imageUrl, quantity, categoryId, brandId, status);

                addProduct(createProduct);



                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addProduct(CreateProduct product) {
        productService.createProduct(product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    fetchProducts();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Product added failed", Toast.LENGTH_SHORT).show();
                fetchProducts();
            }
        });
    }
    private void updateProduct(ProductDetailAdmin product) {
        productService.editProduct(product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                    fetchProducts();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Product updated failed", Toast.LENGTH_SHORT).show();
                fetchProducts();
            }
        });
    }
    private void selectImage() {
        Log.d("Inside select image", "Inside select image");
        Intent intent=new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);

    }
    private void selectUpdateImage() {
        Log.d("Inside select image", "Inside select image");
        Intent intent=new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        updateActivityResultLauncher.launch(intent);

    }
    ActivityResultLauncher<Intent> updateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath=data.getData();
                        imgUpdatePreview.setImageURI(imagePath);

                    }
                }
            });
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath=data.getData();
                        imgCreatePreview.setImageURI(imagePath);

                    }
                }
            });
    private String truncateString(String str) {
        String[] words = str.split("\\s+");
        if (words.length > 3) {
            return String.join(" ", Arrays.copyOfRange(words, 0, 3)) + "...";
        } else {
            return str;
        }
    }
    private void initCongif() {
        config.put("cloud_name", "dpmjibfhc");
        config.put("api_key","432497326611892");
        config.put("api_secret","dkTA4AvWQtblmhcUECE0eo9V090");
        MediaManager.init(getContext(), config);
    }
}