package org.fiodorov.model;

import java.time.Instant;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Generic super class for Question, answer
 * Created by Roman Fiodorov on 12.11.2015.
 */
@MappedSuperclass
public class GenericModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name="created_date")
    private Instant createdDate;

    @Column(name="edited_date")
    private LocalDateTime editedDate;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "created_by", referencedColumnName="id")
    private User createdBy;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "edited_by", referencedColumnName="id")
    private User editedBy;

    @Column(name="content",length = 2047)
    private String content;

    @Column(name="deleted")
    private Boolean deleted = false;

    @Column(name="rank")
    private int rank = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(LocalDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}
