package com.javarush.system.service;


import com.javarush.system.dao.PartDao;
import com.javarush.system.exceptions.IllegalModificationException;
import com.javarush.system.model.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartServiceImpl implements PartService {
    private PartDao dao;

    @Autowired
    public void setDao(PartDao dao) {
        this.dao = dao;
    }


    @Override
    public List<Part> getAllParts(int page, int resultsCount) {
        return dao.getAllParts(page, resultsCount);
    }

    @Override
    public void add(@NonNull Part part) throws IllegalModificationException {
        int idIfExists = dao.getIdByName(part.getName());
        if (idIfExists != -1) {
            throw new IllegalModificationException("Trying to add an existing part. The one with name '" + part.getName()
                    + "' already exist.");
        } else {
            dao.add(part);
        }
    }

    @Override
    public void remove(@NonNull Part part) {
       dao.remove(part);
    }


    @Override
    public void update(int id, @NonNull Part part) throws IllegalModificationException {
        int idIfExists = dao.getIdByName(part.getName());
        if (idIfExists != -1 && idIfExists != part.getId()) {
            throw new IllegalModificationException("Trying to set an existing part name. Part with name '" + part.getName()
                    + "' is already exist.");
        } else {
            checkType(part);
            dao.update(part);
        }
    }

    @Override
    public @NonNull Part getById(int id) {
        return dao.getById(id);
    }


    @Override
    public void addOne(int id) {
        Part part = dao.getById(id);
        part.setCount(part.getCount() + 1);
        dao.update(part);
    }

    @Override
    public void removeOne(int id) {
        Part part = dao.getById(id);
        if (part.getCount() == 0) {
            dao.remove(part);
        } else {
            part.setCount(part.getCount() - 1);
            dao.update(part);
        }
    }

    @Override
    public int calculateSetups() {
        int[] essentialsCount = new int[Part.EssentialType.values().length];
        List<Part> parts = dao.getAllParts(1, dao.partsCount());
        for (Part part : parts) {
            if (part.getCount() == 0) continue;
            Part.EssentialType type = part.getType();
            if (part.isEssential()) {
                essentialsCount[type.ordinal()] = essentialsCount[type.ordinal()] + part.getCount();
            }
        }
        int result = essentialsCount[0];
        for (int count : essentialsCount) {
            if (count < result) result = count;
        }
        return result;
    }


    private static void checkType(Part part) {
        if (part.getType() != null) {
            part.setEssential(true);
        }
    }

    public int partsCount() {
        return dao.partsCount();
    }

    @Override
    public void setOrdering(String ordering) {
        dao.setOrdering(ordering);
    }

    @Override
    public String getOrdering() {
        return dao.getOrdering();
    }

    @Override
    public int getIdByName(String name) {
        return dao.getIdByName(name);
    }
}

