package com.example.loving_essentials.UI.FooView;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.Domain.Services.IService.IFooService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.Utility.FooUtility.FooAdapter;

import java.util.ArrayList;
import java.util.List;

public class FooList extends AppCompatActivity {

//    public IFooService iFooService;
    private List<Foo> fooArrayList;
    private ListView fooLv;

    public FooList(){

    }
//    public FooList(IFooService iFooService) {
//        this.iFooService = iFooService;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foo_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fooList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        fooArrayList = iFooService.GetFooList();
        fooArrayList = new ArrayList<>();
        fooLv = (ListView) findViewById(R.id.fooListView);

        FooAdapter fooAdapter = new FooAdapter(this,R.layout.foo_custom_lv, fooArrayList);

        fooLv.setAdapter(fooAdapter);

    }





}