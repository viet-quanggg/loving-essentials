package com.example.loving_essentials.Utility.FooUtility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.R;

import java.util.List;

public class ShippingAdapter extends BaseAdapter {

    private Context context;
    private int layout;

    private List<AddressResponseDto> addressResponseDtos;

    public ShippingAdapter(Context context, int layout, List<AddressResponseDto> addressResponseDtos) {
        this.context = context;
        this.layout = layout;
        this.addressResponseDtos = addressResponseDtos;
    }

    @Override
    public int getCount() {
        return addressResponseDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return addressResponseDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView txtUsername = convertView.findViewById(R.id.txtUsername);
        TextView txtPhoneNumber = convertView.findViewById(R.id.txtPhone);
        TextView txtAddress = convertView.findViewById(R.id.txtAddress);
        TextView txtEmail = convertView.findViewById(R.id.txtEmail);

        AddressResponseDto addressResponseDto = addressResponseDtos.get(position);

        txtUsername.setText(addressResponseDto.getUserInfo().getName());
        txtPhoneNumber.setText(addressResponseDto.getUserInfo().getPhoneNumber());

        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(addressResponseDto.getHouseNumber()).append(" ")
                .append(addressResponseDto.getStreet()).append(", ")
                .append(addressResponseDto.getWard());

        if (addressResponseDto.getDistrict() != null && !addressResponseDto.getDistrict().isEmpty()) {
            addressBuilder.append(", ").append(addressResponseDto.getDistrict());
        }

        addressBuilder.append(", ").append(addressResponseDto.getCity());

        txtAddress.setText(addressBuilder.toString());
        txtEmail.setText(addressResponseDto.getUserInfo().getEmail());

        return convertView;
    }
}
