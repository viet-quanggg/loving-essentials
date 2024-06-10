package com.example.loving_essentials.Domain.Entity;

public class Foo {
    private String FooName ;

    public Foo() {
    }

    public Foo(String fooName) {
        FooName = fooName;
    }

    public String getFooName() {
        return FooName;
    }

    public void setFooName(String fooName) {
        FooName = fooName;
    }
}
