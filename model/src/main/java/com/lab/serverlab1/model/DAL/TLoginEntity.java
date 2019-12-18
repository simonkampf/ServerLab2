package com.lab.serverlab1.model.DAL;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "t_login", schema = "forum", catalog = "")

@NamedQueries({
        @NamedQuery(name = "TLoginEntity.findAllByUser", query = "SELECT l FROM TLoginEntity l WHERE l.tUserId = :userId"),
})
public class TLoginEntity {
    private int idtLogin;
    private int tUserId;
    private Timestamp tDate;

    @Id
    @Column(name = "idt_login")
    public int getIdtLogin() {
        return idtLogin;
    }

    public void setIdtLogin(int idtLogin) {
        this.idtLogin = idtLogin;
    }

    @Basic
    @Column(name = "t_userId")
    public int gettUserId() {
        return tUserId;
    }

    public void settUserId(int tUserId) {
        this.tUserId = tUserId;
    }

    @Basic
    @Column(name = "t_date")
    public Timestamp gettDate() {
        return tDate;
    }

    public void settDate(Timestamp tDate) {
        this.tDate = tDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLoginEntity that = (TLoginEntity) o;

        if (idtLogin != that.idtLogin) return false;
        if (tUserId != that.tUserId) return false;
        if (tDate != null ? !tDate.equals(that.tDate) : that.tDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idtLogin;
        result = 31 * result + tUserId;
        result = 31 * result + (tDate != null ? tDate.hashCode() : 0);
        return result;
    }
}
