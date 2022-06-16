package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.GenericDaoImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericService<T> {
    private final GenericDaoImpl<T> genericDao;

    protected GenericService(GenericDaoImpl<T> genericDao) {
        this.genericDao = genericDao;
    }

    public List<T> findAll(){
        return genericDao.findAll();
    }

    public Optional<T> find(UUID id){
        return genericDao.find(id);
    }

    public void update(T instance){
        genericDao.update(instance);
    }
    public void create(T instance){
        genericDao.create(instance);
    }
    public void delete(UUID id){
        genericDao.delete(id);
    }
}
