package com.lab.serverlab1.model.DAL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Database Logic
 */
public class DBLogic {

    private EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("test");
    
    public DBLogic(){
        System.out.println("Creating db logic");
    }

    /**
     * Adds a given user to the database
     * @param user
     */
    public void addUser(TUserEntity user) {
        EntityManager em = emf.createEntityManager();
     
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Returns a list with all users in the database
     * @return
     */
    public List<TUserEntity> getUsers() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<TUserEntity> result =  em.createNamedQuery("TUserEntity.findAll", TUserEntity.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return result;


    }

    /**
     * Returns a user corresponding to the given username
     * @param userNameToView
     * @return
     */
    public TUserEntity getUserByUsername(String userNameToView) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TUserEntity result =  em.createNamedQuery("TUserEntity.findByUsername", TUserEntity.class).setParameter("username", userNameToView).getSingleResult();
        em.getTransaction().commit();
        em.close();
        return result;

    }

    /**
     * Returns a list of all posts by a given user
     * @param user
     * @return
     */
    public List<TPostEntity> getAllPostsByUser(TUserEntity user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em.createNamedQuery("TPostEntity.findByUserId", TPostEntity.class).setParameter("id", user.getIdTUser()).getResultList();
    }

    /**
     * Adds a new post to the database
     * @param post
     */
    public void createNewPost(TPostEntity post){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(post);
        em.getTransaction().commit();
        em.close();
    }
}
