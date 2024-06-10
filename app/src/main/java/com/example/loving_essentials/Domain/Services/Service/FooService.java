package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Data.IRepository.IFooRepository;
import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.Domain.Services.IService.IFooService;

import java.util.ArrayList;
import java.util.List;

public class FooService implements IFooService {
    private IFooRepository iFooRepository;

    public FooService() {
    }

    public FooService(IFooRepository iFooRepository) {
        this.iFooRepository = iFooRepository;
    }

    @Override
    public List<Foo> GetFooList() {
        Foo foo = new Foo("QuangFoo");
        Foo foo2 = new Foo("ThangFoo");

        List<Foo> fooList = new ArrayList<>();

        fooList.add(foo);
        fooList.add(foo2);

        return fooList;
    }
}
