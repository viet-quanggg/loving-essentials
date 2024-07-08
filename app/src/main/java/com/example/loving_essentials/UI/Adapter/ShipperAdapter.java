package com.example.loving_essentials.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.Shipper;
import com.example.loving_essentials.R;

import java.util.ArrayList;
import java.util.List;

public class ShipperAdapter extends RecyclerView.Adapter<ShipperAdapter.ShipperViewHolder> {
    private List<Shipper> shippers;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
//    private Shipper selectedShipper;
//    private RecyclerView recyclerView;

    public ShipperAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.shippers = new ArrayList<>();
//        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ShipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.shipper_item, parent, false);
        return new ShipperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipperViewHolder holder, int position) {
        Shipper shipper = shippers.get(position);
        holder.bind(shipper);
    }

    @Override
    public int getItemCount() {
        return shippers.size();
    }

    public void setShippers(List<Shipper> shippers) {
        this.shippers = shippers;
        notifyDataSetChanged();
    }

//    public Shipper getSelectedShipper() {
//        return selectedShipper;
//    }

    public interface OnShipperSelectedListener {
        void onShipperSelected(int userId);
    }

    private OnShipperSelectedListener listener;

    // Add a method to set the listener
    public void setOnShipperSelectedListener(OnShipperSelectedListener listener) {
        this.listener = listener;
    }

    class ShipperViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkboxShipper;
        private ImageView imgShipper;
        private TextView txtShipperName;
        private TextView txtShipperEmail;
        private TextView txtShipperPhone;

        public ShipperViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxShipper = itemView.findViewById(R.id.checkboxShipper);
            imgShipper = itemView.findViewById(R.id.imgShipper);
            txtShipperName = itemView.findViewById(R.id.txtShipperName);
            txtShipperEmail = itemView.findViewById(R.id.txtShipperEmail);
            txtShipperPhone = itemView.findViewById(R.id.txtShipperPhone);

            checkboxShipper.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedPosition != getAdapterPosition()) {
                        notifyItemChanged(selectedPosition);
                        selectedPosition = getAdapterPosition();
                    }
                    if (listener != null) {
                        int userId = shippers.get(getAdapterPosition()).getId();
                        listener.onShipperSelected(userId);
                    }
                } else if (selectedPosition == getAdapterPosition()) {
                    selectedPosition = -1;
                }
            });
        }

        public void bind(Shipper shipper) {
            txtShipperName.setText(shipper.getName());
            txtShipperEmail.setText(shipper.getEmail());
            txtShipperPhone.setText(shipper.getPhoneNumber());
            checkboxShipper.setChecked(selectedPosition == getAdapterPosition());
//            checkboxShipper.setChecked(shipper == selectedShipper);
        }
    }
}