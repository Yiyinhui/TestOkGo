package Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    //Constructor
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DatabaseConstant.DATABASE_NAME, factory, DatabaseConstant.DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    /*
    创建数据库时使用
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //<column definition name> or <table constraint>expected,got 'XXX'. Bug:常数与XXX预留名冲突
        String sql = "create table "+DatabaseConstant.TABLE_NAME+"("+
                DatabaseConstant.ID+" Integer primary key, "+
                DatabaseConstant.TEMPERATURE+" Integer,"+
                DatabaseConstant.HUMIDITY+" Integer,"+
                DatabaseConstant.CO2+" Integer,"+
                DatabaseConstant.LAST_TIME+" varchar(50))";
        sqLiteDatabase.execSQL(sql);

        String sql1 = "create table "+DatabaseConstant.TABLE1_NAME+" ("+
                DatabaseConstant.ID+" Integer primary key ,"+
                DatabaseConstant.CITY+" varchar(20)) ";
        sqLiteDatabase.execSQL(sql1);
    }

    /*
    更新时使用
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
