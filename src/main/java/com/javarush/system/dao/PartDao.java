package com.javarush.system.dao;

import com.javarush.system.model.Part;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class PartDao {

    private SessionFactory sessionFactory;
    private String ordering = "id";
    private Boolean filterAttr;

    public Boolean getFilterAttr() {
        return filterAttr;
    }

    public void setFilterAttr(Boolean filterAttr) {
        this.filterAttr = filterAttr;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public List<Part> getAllParts(int page, int resultsCount) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Part p ORDER BY p." + ordering).setFirstResult(10 * (page - 1)).setMaxResults(resultsCount).list();
    }

    public List<Part> getAllPartsWithFilter(int page, int resultsCount) {
        Session session = sessionFactory.getCurrentSession();
        Query<Part> query = session.createQuery("from Part p WHERE p.essential = :essentialValue ORDER BY p." + ordering);
        query.setParameter("essentialValue", filterAttr);
        return query.setFirstResult(10 * (page - 1)).setMaxResults(resultsCount).list();
    }

    @SuppressWarnings("unchecked")
    public int partsCount() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select count(*) from Part", Number.class).getSingleResult().intValue();
    }

    @SuppressWarnings("unchecked")
    public void add(Part part) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(part);
    }
    @SuppressWarnings("unchecked")
    public void remove(Part part) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(part);
    }

    @SuppressWarnings("unchecked")
    public void update(Part part) {
        Session session = sessionFactory.getCurrentSession();
        session.update(part);
    }
    @SuppressWarnings("unchecked")
    public Part getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Part.class, id);
    }
    @SuppressWarnings("unchecked")
    public int getIdByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        String sql = "select p.id from Part p where p.name = :name";
        Query<Integer> query = session.createQuery(sql);
        query.setParameter("name", name);
        Integer result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            return -1;
        }
        return result;
    }
}
