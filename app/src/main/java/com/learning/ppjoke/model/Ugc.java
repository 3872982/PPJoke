package com.learning.ppjoke.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ugc ugc = (Ugc) o;
        return commentCount == ugc.commentCount &&
                hasDissed == ugc.hasDissed &&
                hasFavorite == ugc.hasFavorite &&
                hasLiked == ugc.hasLiked &&
                hasdiss == ugc.hasdiss &&
                likeCount == ugc.likeCount &&
                shareCount == ugc.shareCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentCount, hasDissed, hasFavorite, hasLiked, hasdiss, likeCount, shareCount);
    }
}
