//package Database;
//
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import java.util.ArrayList;
//
//public class DatabaseManager {
//
//    private static DatabaseHelper mDatabaseHelper;
//
//    public static MySqliteHelper getIntance(Context context){
//        if (mDatabaseHelper == null){
//            mDatabaseHelper = new MySqliteHelper(context);
//        }
//        return mDatabaseHelper;
//    }
//
//    /**
//     * 查找方法
//     * @param db
//     * @param sql
//     * @param selectionArgs 查询条件占位符
//     * @return 返回一个Cursor对象
//     */
//    public static Cursor selectSQL(SQLiteDatabase db, String sql, String[] selectionArgs){
//        Cursor cursor = null;
//        if (db != null){
//            cursor = db.rawQuery(sql,selectionArgs);
//        }
//        return cursor;
//    }
//
//    /**
//     * 删改数据库
//     * @param db
//     * @param sql
//     */
//    public static void execSQL(SQLiteDatabase db, String sql){
//        if (db!=null) {
//            if (sql != null && !"".equals(sql)) {
//                db.execSQL(sql);
//            }
//        }
//    }
//
//    /**
//     * 将Cursor对象转化为List合集
//     * @param cursor
//     * @return
//     */
//    public static ArrayList<TH_Data> cursorToList(Cursor cursor){
//        ArrayList<TH_Data> list = new ArrayList<>();
//        while (cursor.moveToNext()){   //判断游标是否有下一个字段
//            //getColumnIndext作用是返回给定字符串的下标(指的是int类型)
//            int columnIndex = cursor.getColumnIndex(Constant.ID);
//            //通过下标找到指定value
//            int id = cursor.getInt(columnIndex);  // 获取id
//            int temp = cursor.getInt( cursor.getColumnIndex(Constant.TEMP)); //获取温度
//            int humidity = cursor.getInt(cursor.getColumnIndex(Constant.HUMIDITY)); //获取湿度
//            int co2 = cursor.getInt(cursor.getColumnIndex(Constant.CO2)); //获取二氧化碳
//            String time = cursor.getString(cursor.getColumnIndex(com.example.boybaby.yicetong.SQLiteDatabase.Constant.LAST_TIME)); //获取时间
//            TH_Data th_data = new TH_Data(id,temp,humidity,co2,time);
//            list.add(th_data);
//        }
//        return list;
//    }
//
//    public static ArrayList<TP_Data> cursor1ToList(Cursor cursor){
//        ArrayList<TP_Data> list = new ArrayList<TP_Data>();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex(Constant.ID));
//            String city = cursor.getString(cursor.getColumnIndex(Constant.CITY));
//            TP_Data tp_data = new TP_Data(id, city);
//            list.add(tp_data);
//        }
//        return list;
//    }
//
//
//
//}
