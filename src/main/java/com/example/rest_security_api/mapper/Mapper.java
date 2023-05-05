package com.example.rest_security_api.mapper;

public interface Mapper<F, T> {
    T map(F entity);
}
