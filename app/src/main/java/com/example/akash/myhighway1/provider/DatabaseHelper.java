package com.example.akash.myhighway1.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.akash.myhighway1.data.model.Setting;
import com.example.akash.myhighway1.data.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyHighway.db";

    @Inject
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Setting.class);
            //TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

        } catch (Exception e) {
            Timber.e(e, "Database upgrade error : old Version %d, new Version %d", oldVersion, newVersion);
            resetTables();
        }
    }

    public void resetTables() {
        try {
            TableUtils.dropTable(connectionSource, Setting.class, true);
            //TableUtils.dropTable(connectionSource,User.class,true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        onCreate(null, connectionSource);
    }
}
