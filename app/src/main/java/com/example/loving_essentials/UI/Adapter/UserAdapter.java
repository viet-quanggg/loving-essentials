package com.example.loving_essentials.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserDTO;
import com.example.loving_essentials.Domain.Entity.User;
import com.example.loving_essentials.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<UserDTO> users;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    public UserAdapter(Context context, List<UserDTO> users, OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
        this.context = context;
        this.users = users;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        UserDTO user = users.get(position);
        holder.id.setText(String.valueOf(user.getId()));
        holder.username.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhoneNumber());
        holder.role.setText(getRoleName(user.getRole()));
        holder.status.setText(getStatusName(user.getStatus()));

        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, user);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (onLongClickListener != null) {
                onLongClickListener.onLongClick(position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private String getRoleName(int role) {
        switch (role) {
            case 1:
                return "User";
            case 2:
                return "Shipper";
            default:
                return "Admin";
        }
    }

    private String getStatusName(Byte status) {
        if (status == 1) {
            return "Active";
        } else {
            return "Inactive";
        }
    }

    // Interfaces for click and long-click listeners
    public interface OnClickListener {
        void onClick(int position, UserDTO user);
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, username, email, phone, role, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvId);
            username = itemView.findViewById(R.id.tvName);
            email = itemView.findViewById(R.id.tvEmail);
            phone = itemView.findViewById(R.id.tvPhoneNumber);
            role = itemView.findViewById(R.id.tvRole);
            status = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition(), users.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(view -> {
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick(getAdapterPosition());
                    return true;
                }
                return false;
            });
        }
    }
}


