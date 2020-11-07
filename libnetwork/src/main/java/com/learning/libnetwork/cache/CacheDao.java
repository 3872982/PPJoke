package com.learning.libnetwork.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CacheDao {

    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveCache(Cache cache);

    //删   只能传递对象，删除时根据对象中的primary key进行对比
    @Delete
    int deleteCache(Cache cache);

    //改   只能传递对象，更新时根据对象中的primary key进行对比
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateCache(Cache cache);

    /**
     *
     *注意，冒号后面必须紧跟参数名，中间不能有空格。大于小于号和冒号中间是有空格的。
     * select * from cache where【表中列名】 = :【参数名】   ---------------------------->等于
     *                     where【表中列名】 < :【参数名】   ---------------------------->小于
     *                     where【表中列名】 between :【参数名1】 and :【参数2】---------->区间
     *                     where【表中列名】 like :【参数名】 --------------------------->模糊查询
     *                     where【表中列名】 in (:【参数名集合】)---->查询符合集合内指定字段值的记录
     */
    //查   如果查询结果为多个，可以用List<Cache>
    @Query("select * from cache where `key` = :key")
    Cache getCache(String key);
}
