package com.lab.serverlab1.model.DAL;

import org.hibernate.type.descriptor.sql.SmallIntTypeDescriptor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "t_post", schema = "forum", catalog = "")
@NamedQueries({
        @NamedQuery(name = "TPostEntity.findById", query = "SELECT p FROM TPostEntity p WHERE p.id = :id"),
        @NamedQuery(name = "TPostEntity.findByUserId", query = "SELECT p FROM TPostEntity p WHERE p.receiverId = :id OR p.senderId = :id"),
})
public class TPostEntity {
    private int idTPost;
    private String tMessage;
    private Timestamp tTime;
    private int tSenderId;
    private int tReceiverId;
    private byte tPrivate;


    @Basic
    @Column(name = "t_private")
    public byte getPrivate() {
        return tPrivate;
    }

    public void setPrivate(byte tPrivate) {
        this.tPrivate = tPrivate;
    }

    @Id
    @Column(name = "idT_Post")
    public int getIdTPost() {
        return idTPost;
    }

    public void setIdTPost(int idTPost) {
        this.idTPost = idTPost;
    }

    @Basic
    @Column(name = "T_Message")
    public String getMessage() {
        return tMessage;
    }

    public void setMessage(String tMessage) {
        this.tMessage = tMessage;
    }

    @Basic
    @Column(name = "T_Time")
    public Timestamp getTime() {
        return tTime;
    }

    public void setTime(Timestamp tTime) {
        this.tTime = tTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPostEntity that = (TPostEntity) o;

        if (idTPost != that.idTPost) return false;
        if (tMessage != null ? !tMessage.equals(that.tMessage) : that.tMessage != null) return false;
        if (tTime != null ? !tTime.equals(that.tTime) : that.tTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idTPost;
        result = 31 * result + (tMessage != null ? tMessage.hashCode() : 0);
        result = 31 * result + (tTime != null ? tTime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "T_SenderId")
    public int getSenderId() {
        return tSenderId;
    }

    public void setSenderId(int tSenderId) {
        this.tSenderId = tSenderId;
    }

    @Basic
    @Column(name = "t_receiverId")
    public int getReceiverId() {
        return tReceiverId;
    }

    public void setReceiverId(int tReceiverId) {
        this.tReceiverId = tReceiverId;
    }


}
