package com.example.loving_essentials.Utility.FooUtility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.R;

import java.util.List;

public class FooAdapter extends BaseAdapter {
    private Context context;

    private int layout;

    private List<Foo> fooList;

    public FooAdapter(Context context, int layout, List<Foo> fooList) {
        this.context = context;
        layout = layout;
        this.fooList = fooList;
    }

    @Override
    public int getCount() {
        return fooList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtFooName);

        Foo foo = fooList.get(position);

        txtName.setText(foo.getFooName());
        return convertView;
    }
}
