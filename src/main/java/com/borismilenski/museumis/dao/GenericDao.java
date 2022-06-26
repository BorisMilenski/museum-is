package com.borismilenski.museumis.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericDao<T> {
    T create(T t);
    void delete(UUID id);
    Optional<T> find(UUID id);
    List<T> findAll();
    int update(T t);
}
