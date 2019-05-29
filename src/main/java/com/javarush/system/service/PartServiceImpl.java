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
        int idIfExists = dao.getIdByName(part.getName().trim());
        if (idIfExists != -1) {
            throw new IllegalModificationException("Trying to add an existing part. The one with name '" + part.getName()
                    + "' is already exist.");
        } else {
            part.setName(part.getName().trim());
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
    @Override
    public int partsCount() {
        return dao.partsCount();
    }

    @Override
    public void setOrdering(String ordering) {
        dao.setOrdering(ordering);
    }

    /**
     * @param partName name of the searching part
     * @param currentPage current client's page
     * @param itemsOnPage how many items located on one page
     * @return page num on which the part located, -1 if the part doesn't exist and 0 if it is located in the current page
     */
    @Override
    public int searchPartPage(String partName, int currentPage, int itemsOnPage) {
        int partsCount = dao.partsCount();
        if (partsCount < itemsOnPage || dao.getIdByName(partName) == -1) return -1;
        List<Part> list = dao.getAllParts(1, partsCount);
        for (Part p : list) {
            if (p.getName().equalsIgnoreCase(partName)) {
                int page =  (list.indexOf(p) / itemsOnPage) + 1;
                return currentPage == page ? 0 : page;
            }
        }
        return -1;
    }

    @Override
    public int checkPage(int page, int resultsCount) {
        if (page > 1) {
            return (dao.partsCount() - ((page-1)*resultsCount)) == 0  ? page - 1 : page;
        }
        return page;
    }

    @Override
    public void setFilterAttr(String filterAttr) {
        dao.setFilterAttr(filterAttr);
    }

    @Override
    public String getFilterAttr() {
        return dao.getFilterAttr();
    }
}

