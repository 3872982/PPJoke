package com.learning.ppjoke.model;

import java.io.Serializable;

/**
 * 点赞 喜欢 不喜欢 评论数
 */
public class Ugc implements Serializable {

    /**
     * commentCount : 1377
     * hasDissed : false
     * hasFavorite : false
     * hasLiked : false
     * hasdiss : false
     * likeCount : 8315
     * shareCount : 944
     */

    public int commentCount;
    public boolean hasDissed;
    public boolean hasFavorite;
    public boolean hasLiked;
    public boolean hasdiss;
    public int likeCount;
    public int shareCount;
}
