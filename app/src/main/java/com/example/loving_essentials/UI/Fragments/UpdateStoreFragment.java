package com.example.loving_essentials.UI.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.loving_essentials.Domain.Entity.Store;
import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IStoreService;
import com.example.loving_essentials.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStoreFragment extends DialogFragment {
    private EditText editSName, editSAddress, editSPhone, editSOpen, editSClose;
//    private EditText editLatitude, editLongitude;
    private Button btnUpdateStore;
    private Store store;
    private OnStoreUpdatedListener listener;
    private Geocoder geocoder;

    public UpdateStoreFragment(Store store) {
        this.store = store;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_store_admin, container, false);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        editSName = view.findViewById(R.id.editSName);
        editSAddress = view.findViewById(R.id.editSAddress);
        editSPhone = view.findViewById(R.id.editSPhone);
        editSOpen = view.findViewById(R.id.editSOpen);
        editSClose = view.findViewById(R.id.editSClose);
//        editLatitude = view.findViewById(R.id.editSLT);
//        editLongitude = view.findViewById(R.id.editSLG);
        btnUpdateStore = view.findViewById(R.id.btnUpdateStore);

        // Hiển thị thông tin cửa hàng hiện tại nếu có
        if (store != null) {
            editSName.setText(store.getName());
            editSAddress.setText(store.getAddress());
            editSPhone.setText(store.getPhone());
            editSOpen.setText(store.getOpenHours());
            editSClose.setText(store.getCloseHours());
//            editLatitude.setText(String.valueOf(store.getLatitude()));
//            editLongitude.setText(String.valueOf(store.getLongitude()));
        }

        btnUpdateStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStore();
            }
        });

        return view;
    }

    private void updateStore() {
        // Lấy dữ liệu từ EditText
        String name = editSName.getText().toString();
        String address = editSAddress.getText().toString();
        String phone = editSPhone.getText().toString();
        String openHours = editSOpen.getText().toString();
        String closeHours = editSClose.getText().toString();
//        double latitude = TextUtils.isEmpty(editLatitude.getText().toString()) ? 0.0 : Double.parseDouble(editLatitude.getText().toString());
//        double longitude = TextUtils.isEmpty(editLongitude.getText().toString()) ? 0.0 : Double.parseDouble(editLongitude.getText().toString());
        double latitude = 0.0;
        double longitude = 0.0;

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                Toast.makeText(getContext(), "Unable to find location for the provided address", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Geocoder failed", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin cửa hàng
        store.setName(name);
        store.setAddress(address);
        store.setPhone(phone);
        store.setOpenHours(openHours);
        store.setCloseHours(closeHours);
        store.setLatitude(latitude);
        store.setLongitude(longitude);

        // Gọi API để cập nhật cửa hàng
        Call<Void> call = APIClient.getClient().create(IStoreService.class).UpdateStore(store);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Gọi callback để thông báo cửa hàng đã được cập nhật thành công
                    if (listener != null) {
                        listener.onStoreUpdated(store);
                    }
                    dismiss(); // Đóng dialog sau khi cập nhật thành công
                } else {
                    Toast.makeText(getContext(), "Failed to update store", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UpdateStoreDialog", "Failed to update store", t);
                Toast.makeText(getContext(), "Failed to update store", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Interface để thông báo khi cửa hàng được cập nhật thành công
    public interface OnStoreUpdatedListener {
        void onStoreUpdated(Store updatedStore);
    }

    // Phương thức để thiết lập listener
    public void setOnStoreUpdatedListener(OnStoreUpdatedListener listener) {
        this.listener = listener;
    }
}