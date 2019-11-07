package com.lab.serverlab1.model.DAL;

import javax.persistence.*;

@Entity
@Table(name = "t_userpost", schema = "forum")
@NamedQueries({
        @NamedQuery(name = "TUserpostEntity.findById", query = "SELECT up FROM TUserpostEntity up WHERE up.idTUserPost = :id"),
})
public class TUserpostEntity {
    private int idTUserPost;

    @Id
    @Column(name = "idT_UserPost")
    public int getIdTUserPost() {
        return idTUserPost;
    }

    public void setIdTUserPost(int idTUserPost) {
        this.idTUserPost = idTUserPost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TUserpostEntity that = (TUserpostEntity) o;

        if (idTUserPost != that.idTUserPost) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return idTUserPost;
    }
}
