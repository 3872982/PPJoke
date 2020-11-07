package com.learning.libnetwork.cache;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class Cache implements Serializable {
    //PrimaryKey 必须要有,且不为空,autoGenerate 主键的值是否由Room自动生成,默认false
    @PrimaryKey
    @NotNull
    public String key;


    public byte[] data;
}
