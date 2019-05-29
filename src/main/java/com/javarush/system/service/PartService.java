package com.javarush.system.service;

import com.javarush.system.exceptions.IllegalModificationException;
import com.javarush.system.model.Part;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface PartService {
    @Transactional
    List<Part> getAllParts(int page, int resultsCount);
    @Transactional
    void add(Part part) throws IllegalModificationException;
    @Transactional
    void remove(Part part);
    @Transactional
    void update(int id, Part part) throws IllegalModificationException;
    @Transactional
    Part getById(int id);
    @Transactional
    void addOne(int id);
    @Transactional
    void removeOne(int id);
    @Transactional
    int calculateSetups();
    @Transactional
    int partsCount();
    void setOrdering(String ordering);
    @Transactional
    int searchPartPage(String partName, int currentPage, int itemsOnPage);
    @Transactional
    int checkPage(int page, int resultsCount);
    @Transactional
    void setFilterAttr(String filterAttr);
    @Transactional
    String getFilterAttr();
}
