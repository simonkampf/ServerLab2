package com.lab.serverlab1.model.DAL;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_user")
@NamedQueries({
        @NamedQuery(name = "TUserEntity.findAll", query = "SELECT u FROM TUserEntity u"),
        @NamedQuery(name = "TUserEntity.findByUsername", query = "SELECT u FROM TUserEntity u WHERE u.username = :username"),
})
public class TUserEntity {
    private int idTUser;
    private String tUsername;
    private String tPassword;
    private String tName;
    private int tAge;
    @Id
    @Column(name = "idt_user")
    public int getIdTUser() {
        return idTUser;
    }

    public void setIdTUser(int idTUser) {
        this.idTUser = idTUser;
    }
    
    @Basic
    @Column(name = "t_username")
    public String getUsername() {
        return tUsername;
    }
    @Basic
    @Column(name = "t_password")
    public String getPassword() {
        return tPassword;
    }
    
    @Basic
    @Column(name = "t_name")
    public String getName() {
        return tName;
    }
    
    @Basic
    @Column(name = "t_age")
    public int getAge() {
        return tAge;
    }
    public void setPassword(String tPassword){
        this.tPassword = tPassword;
    }
    
    public void setUsername(String tUsername) {
        this.tUsername = tUsername;
    }
     public void setName(String tName) {
        this.tName = tName;
    }
    
    public void setAge(int tAge){
        this.tAge = tAge;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUserEntity that = (TUserEntity) o;
        return idTUser == that.idTUser &&
                Objects.equals(tUsername, that.tUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTUser, tUsername);
    }
}
