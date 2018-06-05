package bit.basemodule.database.dbhelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.List;

import bit.basemodule.database.dao.UserDao;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "MyDb.db";
    private static final int DATABASE_VERSION = 1;
    private RuntimeExceptionDao<UserDao, Integer> userDao = null;
    private static DatabaseHelper databaseHelperContext;

    synchronized public static DatabaseHelper getInstance(Context context) {
        if (databaseHelperContext == null) {
            databaseHelperContext = new DatabaseHelper(context);
        }
        return databaseHelperContext;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {

            TableUtils.createTableIfNotExists(connectionSource, UserDao.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, UserDao.class, true);
            onCreate(db, connectionSource);

        } catch (Exception e) {
            onCreate(db, connectionSource);
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<UserDao, Integer> getUserDao() {
        if (userDao == null) userDao =
                getRuntimeExceptionDao(UserDao.class);
        return userDao;
    }

    public void saveUser(UserDao userDao) {
        try {
            getUserDao().create(userDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserDao(UserDao userDao) {
        try {

            DeleteBuilder<UserDao, Integer> deleteBuilder = getUserDao().deleteBuilder();
            deleteBuilder.where().eq("id", userDao.getId());
            deleteBuilder.delete();
            saveUser(userDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<UserDao> getUserList() {
        try {
            return getUserDao().queryBuilder().orderBy("id", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

