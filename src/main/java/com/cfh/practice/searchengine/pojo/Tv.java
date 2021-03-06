package com.cfh.practice.searchengine.pojo;

import java.util.Date;

public class Tv {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.title
     *
     * @mbggenerated
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.director
     *
     * @mbggenerated
     */
    private String director;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.broadcast_time
     *
     * @mbggenerated
     */
    private Date broadcastTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.gmt_create
     *
     * @mbggenerated
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.gmt_modified
     *
     * @mbggenerated
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.is_update
     *
     * @mbggenerated
     */
    private Integer isUpdate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tv.description
     *
     * @mbggenerated
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.id
     *
     * @return the value of tv.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.id
     *
     * @param id the value for tv.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.title
     *
     * @return the value of tv.title
     *
     * @mbggenerated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.title
     *
     * @param title the value for tv.title
     *
     * @mbggenerated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.director
     *
     * @return the value of tv.director
     *
     * @mbggenerated
     */
    public String getDirector() {
        return director;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.director
     *
     * @param director the value for tv.director
     *
     * @mbggenerated
     */
    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.broadcast_time
     *
     * @return the value of tv.broadcast_time
     *
     * @mbggenerated
     */
    public Date getBroadcastTime() {
        return broadcastTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.broadcast_time
     *
     * @param broadcastTime the value for tv.broadcast_time
     *
     * @mbggenerated
     */
    public void setBroadcastTime(Date broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.gmt_create
     *
     * @return the value of tv.gmt_create
     *
     * @mbggenerated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.gmt_create
     *
     * @param gmtCreate the value for tv.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.gmt_modified
     *
     * @return the value of tv.gmt_modified
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.gmt_modified
     *
     * @param gmtModified the value for tv.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.is_update
     *
     * @return the value of tv.is_update
     *
     * @mbggenerated
     */
    public Integer getIsUpdate() {
        return isUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.is_update
     *
     * @param isUpdate the value for tv.is_update
     *
     * @mbggenerated
     */
    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tv.description
     *
     * @return the value of tv.description
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tv.description
     *
     * @param description the value for tv.description
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}