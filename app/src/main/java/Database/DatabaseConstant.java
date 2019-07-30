package Database;

/**\
 * 存储数据库的常量
 */
public class DatabaseConstant {
    public static final String DATABASE_NAME = "info.db";  // 数据库名称
    public static final int DATABASE_VERSION = 1;          //数据库版本
    public static final String TABLE_NAME = "TH_Data";     //数据库表名
    /**
     *ID、TEMP、HUMIDITY、CO2、LAST_TIME、LAST_TIME 一下是数据库表中的字段
     */
    public static  final String ID = "id";                //id主键
    public static  final String TEMPERATURE = "temperature";            //温度
    public static final String HUMIDITY = "humidity";     //湿度
    public static final String CO2 = "co2";               //二氧化碳
    public static final String LAST_TIME = "lastUpdateTime";//更新时间
    /**
     *  TP_DATA 第二个表
     */
    public static final String TABLE1_NAME = "TP_DATA";
    public static  final String CITY = "city";

}
