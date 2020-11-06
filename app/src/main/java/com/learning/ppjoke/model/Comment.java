package com.learning.ppjoke.model;

import java.io.Serializable;

/**
 * 神评
 */
public class Comment implements Serializable {

    /**
     * author : {"avatar":"http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100","commentCount":339,"description":"小朋友,你是否有很多问号","expires_time":1596726031266,"favoriteCount":16,"feedCount":10,"followCount":107,"followerCount":30,"hasFollow":false,"historyCount":2606,"id":1755,"likeCount":2,"name":"、蓅哖╰伊人为谁笑","qqOpenId":"FE41683AD4ECF91B7736CA9DB8104A5C","score":1000,"topCommentCount":10,"userId":1578919786}
     * commentCount : 24
     * commentId : 6811005208387131000
     * commentText : 先帝创业未半而去当特种兵
     * commentType : 1
     * createTime : 1585810726
     * hasLiked : false
     * height : 0
     * id : 2022
     * itemId : 6810585660219480000
     * likeCount : 29064
     * ugc : {"commentCount":0,"hasDissed":false,"hasFavorite":false,"hasLiked":true,"hasdiss":false,"likeCount":14,"shareCount":0}
     * userId : 0
     * width : 0
     */

    public User author;
    public int commentCount;
    public long commentId;
    public String commentText;
    public int commentType;
    public int createTime;
    public boolean hasLiked;
    public int height;
    public int id;
    public long itemId;
    public int likeCount;
    public Ugc ugc;
    public int userId;
    public int width;
    public String imageUrl;
    public String videoUrl;
}
