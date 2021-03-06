package com.learning.libnetwork.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learning.libcommon.globals.AppGlobal;

@Database(entities = {Cache.class},version = 1,exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    private static CacheDatabase sCacheDatabase;

    public static CacheDatabase getInstance(){
        if(sCacheDatabase == null){
            synchronized (CacheDatabase.class){
                if(sCacheDatabase == null){
                    //创建一个内存数据库
                    //但是这种数据库的数据只存在于内存中，也就是进程被杀之后，数据随之丢失
                    //Room.inMemoryDatabaseBuilder()

                    sCacheDatabase = Room.databaseBuilder(AppGlobal.getApplication(), CacheDatabase.class, "ppjoke_cache")
                            //是否允许在主线程进行查询
                            .allowMainThreadQueries()
                            //数据库创建和打开后的回调
                            //.addCallback()
                            //设置查询的线程池
                            //.setQueryExecutor()
                            //.openHelperFactory()
                            //room的日志模式
                            //.setJournalMode()
                            //数据库升级异常之后的回滚
                            //.fallbackToDestructiveMigration()
                            //数据库升级异常后根据指定版本进行回滚
                            //.fallbackToDestructiveMigrationFrom()
                            // .addMigrations(CacheDatabase.sMigration)
                            .build();
                }
            }
        }

        return sCacheDatabase;
    }

    public abstract CacheDao getCacheDao();
}
