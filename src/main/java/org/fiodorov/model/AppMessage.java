package org.fiodorov.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fiodorov.utils.Defaults;
import org.hibernate.annotations.Type;

/**
 * Entity model for system messages about karma changing
 * Created by Roman Fiodorov on 22.03.2016.
 */
@Entity
@Table(name="messages", schema= Defaults.SCHEMA)
public class AppMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="message",length = 2047)
    private String message;

    @Column(name="changingKarma")
    private Integer changingKarma;

    @Column(name="createdDate")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime createdDate;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender", referencedColumnName="id")
    private User sender;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "receiver", referencedColumnName="id")
    private User receiver;

    @Column(name="link")
    private String linkToPage;

    @Column(name="isRead", columnDefinition = "boolean not null default false")
    private boolean read = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Integer getChangingKarma() {
        return changingKarma;
    }

    public void setChangingKarma(Integer changingKarma) {
        this.changingKarma = changingKarma;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLinkToPage() {
        return linkToPage;
    }

    public void setLinkToPage(String linkToPage) {
        this.linkToPage = linkToPage;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "AppMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", changingKarma=" + changingKarma +
                ", createdDate=" + createdDate +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", linkToPage='" + linkToPage + '\'' +
                '}';
    }
}
