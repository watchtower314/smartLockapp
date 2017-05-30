package niravitalzohar.smartlock.smartlock;

/**
 * Created by zohar on 23/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;



public class SQLiteHandler extends SQLiteOpenHelper {

    public static permission_type CURRENT_PERMISSION_TYPE;
    public static String CURRENT_LOCKID;
    public static String CURRENT_USERNAME;


    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "SmartLock";

    // Login table name
    private static final String TABLE_USER = "SM_users";
    public String gettable(){return TABLE_USER; }

    private static final String TABLE_PERMISSION = "permission";

    public String getTablePermission(){return TABLE_PERMISSION; }

    private static final String TABLE_LOCKS = "locks";
    public String getTableLocks(){return TABLE_LOCKS; }





    // Login Table Columns names
   // private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "uphone";
    private static final String KEY_PASSWORD = "upassword";

    //common colums
    private static final String KEY_UID = "uid";
    private static final String LOCK_ID = "lockid";




    //permission Table Columns names


    private static final String PERMISSION_TYPE = "permission_type";
    private static final String DURATION = "duration";
    private static final String PYSICALID = "pysicalId";


    //locks Table Columns name
    private static final String IP = "ip";
    private static final String PORT = "port";
    private static final String LOCK_STATUS = "lockstatus";
    private static final String MANGER_CODE = "mangercode";
    private static final String MEMBER_CODE = "membercode";




    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);*/
         String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
          //       + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_PERMISSION = "CREATE TABLE " + TABLE_PERMISSION + "("
                + PERMISSION_TYPE + " INTEGER,"
                + LOCK_ID + " TEXT," + KEY_UID + " TEXT,"
        + DURATION + " TEXT," + PYSICALID + " INTEGER" +")";

       // db.execSQL(CREATE_TABLE);
       db.execSQL(CREATE_TABLE_PERMISSION);


        String CREATE_TABLE_LOCKS = "CREATE TABLE " + TABLE_LOCKS + "("
                + LOCK_ID + " TEXT,"
                + IP + " TEXT," + PORT + " TEXT,"
                + LOCK_STATUS + " TEXT," + MANGER_CODE + " TEXT," + MEMBER_CODE + " TEXT" +")";

        // db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_LOCKS);


        Log.d(TAG, "Database tables created");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("jhj","upgrading");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCKS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
   // public void addUser(String name, String email, String uid, String uphone, String upassword) {
        public void addUser( String email, String uid, String uphone, String upassword) {

            SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
     //   values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); //
        values.put(KEY_PHONE, uphone); //
        values.put(KEY_PASSWORD,upassword);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       // values.put(KEY_NAME, user.getUserName()); // Name
        values.put(KEY_EMAIL, user.getEmail()); // Email
        values.put(KEY_UID, user.getUserid()); //
        values.put(KEY_PHONE, user.getPhonenumber()); //
        values.put(KEY_PASSWORD,user.getPassword());

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

   public void addpermission(Permission permission) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PERMISSION_TYPE, String.valueOf(permission.getPertyp()));
        values.put(LOCK_ID,permission.getLockid());
        values.put(KEY_UID,permission.getUserid());
        values.put(DURATION,permission.getDuration());
        values.put(PYSICALID,permission.getPysicalId());


        long id = db.insert(TABLE_PERMISSION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New permission inserted into sqlite: " + id);


    }


    public void addlock(Lock lock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCK_ID,lock.getLockid());
        values.put(IP,lock.getIp());
        values.put(PORT,lock.getPort());
        values.put(LOCK_STATUS,lock.getLockStatus());
        values.put(MANGER_CODE,lock.getMangerCode());
        values.put(MEMBER_CODE,lock.getMemberCode());


        long id = db.insert(TABLE_PERMISSION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New permission inserted into sqlite: " + id);


    }


        /**
         * Getting user data from database
         * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("uphone", cursor.getString(4));
            user.put("upassword", cursor.getString(5));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public String getSinlgeEntry(String Email)
    {

        SQLiteDatabase db = this.getWritableDatabase();
       // String[] result_columns=new String[]{"name","email","uid","uphone","upassword"};
        String[] result_columns=new String[]{"email","uid","uphone","upassword"};

        Cursor cursor=db.query(TABLE_USER, result_columns, " email=?", new String[]{Email}, null, null,null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("upassword"));
        cursor.close();
        return password;
    }


    public Lock getSingleLock(String ip){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LOCKS + " WHERE "
                + IP + " = " + ip;

        Log.e("LOG", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        Lock lock=new Lock();
        lock.setLockid((c.getString(c.getColumnIndex(LOCK_ID))));
        lock.setIp((c.getString(c.getColumnIndex(IP))));
        lock.setPort((c.getString(c.getColumnIndex(PORT))));
        lock.setLockStatus((c.getString(c.getColumnIndex(LOCK_STATUS))));
        lock.setMangerCode((c.getString(c.getColumnIndex(MANGER_CODE))));
        lock.setMemberCode((c.getString(c.getColumnIndex(MEMBER_CODE))));

       return lock;

    }


}