package com.learning.ppjoke.model;

import java.io.Serializable;

public class Feed implements Serializable {

    /**
     * activityIcon : https://sf1-nhcdn-tos.pstatp.com/obj/p1056/88c5ea2b90134313b99cf2f9a87e9ca1
     * activityText : 专业团队
     * author : {"avatar":"https://p1-ppx.byteimg.com/img/tos-cn-i-0000/d9e6dc8d881247388aca4c44a5831426~200x200.jpeg","commentCount":0,"description":"知足上进，真诚无价！ 淡定从容，睿智有趣。","expires_time":0,"favoriteCount":0,"feedCount":0,"followCount":0,"followerCount":8,"hasFollow":false,"historyCount":1,"id":1864,"likeCount":0,"name":"灵魂修养寓言师","score":0,"topCommentCount":0,"userId":2739619955737415}
     * authorId : 2739619955737415
     * cover : https://p1-ppx.byteimg.com/img/mosaic-legacy/308d30000b2f98fed5dae~544x672_q80.jpeg
     * createTime : 1589292717
     * duration : 11.471
     * feeds_text : 这是什么神操作？
     * height : 672
     * id : 1578919905
     * itemId : 6825960242984000000
     * itemType : 2
     * ugc : {"commentCount":1377,"hasDissed":false,"hasFavorite":false,"hasLiked":false,"hasdiss":false,"likeCount":8315,"shareCount":944}
     * url : https://pipijoke.oss-cn-hangzhou.aliyuncs.com/6825960242983999755.mp4
     * width : 544
     */

    public String activityIcon;
    public String activityText;
    public User author;
    public long authorId;
    public String cover;
    public int createTime;
    public double duration;
    public String feeds_text;
    public int height;
    public int id;
    public long itemId;
    public int itemType;
    public Ugc ugc;
    public String url;
    public int width;
    public Comment topComment;


}
