package com.javarush.system.dao;

import com.javarush.system.model.Part;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PartDao {

    private SessionFactory sessionFactory;
    private String ordering = "id";
    private String filterAttr;

    public String getFilterAttr() {
        return filterAttr;
    }

    public void setFilterAttr(String filterAttr) {
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
        if (filterAttr.equals("all")) {
            return session.createQuery("from Part p ORDER BY p." + ordering).setFirstResult(10 * (page - 1)).setMaxResults(resultsCount).list();
        }
        return session.createQuery("from Part p WHERE p.essential = " + filterAttr + " ORDER BY p." + ordering).setFirstResult(10 * (page - 1)).setMaxResults(resultsCount).list();
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
        String sql = "select p.id from Part p where LOWER(p.name) = LOWER(:name)";
        TypedQuery<Integer> query = session.createQuery(sql, Integer.class);
        query.setParameter("name", name);
        Integer result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("EXCEPTION! Involved - " + name);
            return -1;
        }
        return result;
    }
}
