package com.utopia.springboothn.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author hn
 * @date 2018/08/02
 */
@Document(collection = "first")
public class First implements Serializable {

    /**
     * 将MongoDB的"_id" 对应 实体中的uid字段
     */
    @Field("_id")
    private String uid;
    private String title;
    private String description;
    private String by;
    private int likes;
    /**
     *这个属性不是数据库字段，不用映射数据库
     */
    @Transient
    private String memo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
