package com.example.loving_essentials.Data.IRepository;

import com.example.loving_essentials.Domain.Entity.Foo;

import java.util.List;

public interface IFooRepository {
    List<Foo> Get();
}
