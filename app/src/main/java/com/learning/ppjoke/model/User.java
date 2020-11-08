package com.learning.ppjoke.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {


    /**
     * avatar : https://p1-ppx.byteimg.com/img/tos-cn-i-0000/d9e6dc8d881247388aca4c44a5831426~200x200.jpeg
     * commentCount : 0
     * description : 知足上进，真诚无价！ 淡定从容，睿智有趣。
     * expires_time : 0
     * favoriteCount : 0
     * feedCount : 0
     * followCount : 0
     * followerCount : 8
     * hasFollow : false
     * historyCount : 1
     * id : 1864
     * likeCount : 0
     * name : 灵魂修养寓言师
     * score : 0
     * topCommentCount : 0
     * userId : 2739619955737415
     */

    public String avatar;
    public int commentCount;
    public String description;
    public long expires_time;
    public int favoriteCount;
    public int feedCount;
    public int followCount;
    public int followerCount;
    public boolean hasFollow;
    public int historyCount;
    public int id;
    public int likeCount;
    public String name;
    public int score;
    public int topCommentCount;
    public long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return commentCount == user.commentCount &&
                expires_time == user.expires_time &&
                favoriteCount == user.favoriteCount &&
                feedCount == user.feedCount &&
                followCount == user.followCount &&
                followerCount == user.followerCount &&
                hasFollow == user.hasFollow &&
                historyCount == user.historyCount &&
                id == user.id &&
                likeCount == user.likeCount &&
                score == user.score &&
                topCommentCount == user.topCommentCount &&
                userId == user.userId &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(description, user.description) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avatar, commentCount, description, expires_time, favoriteCount, feedCount, followCount, followerCount, hasFollow, historyCount, id, likeCount, name, score, topCommentCount, userId);
    }
}
