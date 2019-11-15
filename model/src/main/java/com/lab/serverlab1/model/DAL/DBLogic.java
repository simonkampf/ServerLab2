package com.lab.serverlab1.model.DAL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

public class DBLogic {


    private EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("test");
    
    public DBLogic(){
        System.out.println("Creating db logic");
    }

    public void addUser(TUserEntity user) {
        EntityManager em = emf.createEntityManager();
     
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }


    public List<TUserEntity> getUsers() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<TUserEntity> result =  em.createNamedQuery("TUserEntity.findAll", TUserEntity.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return result;


    }

    public TUserEntity getUserByUsername(String userNameToView) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TUserEntity result =  em.createNamedQuery("TUserEntity.findByUsername", TUserEntity.class).setParameter("username", userNameToView).getSingleResult();
        em.getTransaction().commit();
        em.close();
        return result;

    }

    public List<TPostEntity> getAllPostsByUser(TUserEntity user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em.createNamedQuery("TPostEntity.findByUserId", TPostEntity.class).setParameter("id", user.getIdTUser()).getResultList();
    }


    public void createNewPost(TPostEntity post) throws Exception{
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(post);
        em.getTransaction().commit();
        em.close();
    }
}
