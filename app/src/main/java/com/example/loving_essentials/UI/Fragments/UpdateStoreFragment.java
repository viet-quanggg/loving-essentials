package com.example.loving_essentials.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loving_essentials.Domain.Entity.DTOs.StoreDTO;
import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IStoreService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStoreFragment extends Fragment {
    private EditText editStoreName;
    private EditText editStoreAddress;
    private EditText editStorePhone;
    private EditText editStoreOpenHours;
    private EditText editStoreCloseHours;
    private Button saveStoreButton;
    private IStoreService storeService;
    private static final String TAG = "UpdateStoreFragment";
    private StoreDTO store;

    public UpdateStoreFragment(StoreDTO store) {
        this.store = store;
    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.update_store_admin, container, false);
//
//        editStoreName = view.findViewById(R.id.editSName);
//        editStoreAddress = view.findViewById(R.id.editSAddress);
//        editStorePhone = view.findViewById(R.id.editSPhone);
//        editStoreOpenHours = view.findViewById(R.id.editSOpen);
//        editStoreCloseHours = view.findViewById(R.id.edtiSClose);
//        saveStoreButton = view.findViewById(R.id.btnCreateStore);
//
//        storeService = APIClient.getClient().create(IStoreService.class);
//
//        if (store != null) {
//            editStoreName.setText(store.getName());
//            editStoreAddress.setText(store.getAddress());
//            editStorePhone.setText(store.getPhone());
//            editStoreOpenHours.setText(store.getOpenHours());
//            editStoreCloseHours.setText(store.getCloseHours());
//        }
//
//        saveStoreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                store.setName(editStoreName.getText().toString());
//                store.setAddress(editStoreAddress.getText().toString());
//                store.setPhone(editStorePhone.getText().toString());
//                store.setOpenHours(editStoreOpenHours.getText().toString());
//                store.setCloseHours(editStoreCloseHours.getText().toString());
//
//                updateStore(store);
//            }
//        });
//
//        return view;
//    }
//
//    private void updateStore(StoreDTO store) {
//        Call<Void> call = storeService.UpdateStore(store);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Store updated successfully", Toast.LENGTH_SHORT).show();
//                    getFragmentManager().popBackStack();  // Quay lại fragment trước
//                } else {
//                    Log.e(TAG, "Update failed: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e(TAG, "Update failed", t);
//            }
//        });
//    }
}
