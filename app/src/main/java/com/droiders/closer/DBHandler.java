package com.droiders.closer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.droiders.closer.Users.Cover;
import com.droiders.closer.Users.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler sInstance;

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "myinfo";


    private static final String TABLE_NAME = "myprofile";


    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COVER = "cover";
    private static final String COLUMN_VERIFIED="verified";
    private static final String COLUMN_LINK="link";
    private static final String COLUMN_GENDER="gender";
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_MOBILE="mobile";
    private static final String COLUMN_ADDRESS="address";
    private static final String COLUMN_DOB="dob";
    private static final String COLUMN_PROFESSION="profession";
    private static final String COLUMN_SKILLS="skills";

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHandler getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_COVER + " TEXT,"
                + COLUMN_VERIFIED + " INTEGER,"
                + COLUMN_LINK + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_MOBILE + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_PROFESSION + " TEXT,"
                + COLUMN_SKILLS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

// Create tables again
        onCreate(db);
    }


    public void addme(UserInfo me) {
        SQLiteDatabase db = this.getWritableDatabase();
        int verified=me.isVerified()?1:0;

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,me.getId());
        values.put(COLUMN_NAME, me.getName());
        if(me.getCover()!=null)
            values.put(COLUMN_COVER,me.getCover().getSource());
        values.put(COLUMN_VERIFIED,verified);
        values.put(COLUMN_LINK, me.getLink());
        values.put(COLUMN_GENDER, me.getGender());
        values.put(COLUMN_EMAIL, me.getEmail());
        values.put(COLUMN_MOBILE,me.getMobile());
        values.put(COLUMN_ADDRESS,me.getAddress());
        values.put(COLUMN_DOB,me.getDob());
        values.put(COLUMN_PROFESSION,me.getProfession());
        values.put(COLUMN_SKILLS,me.getSkillset());

// Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public List<UserInfo> getAllMe() {
        List<UserInfo> allMe=new ArrayList<UserInfo>();
        UserInfo me = null;
        String selectQuery = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                me=new UserInfo();
                me.setId(cursor.getString(0));
                me.setName(cursor.getString(1));
                Cover c=new Cover();
                c.setSource(cursor.getString(2));
                me.setCover(c);
                boolean verified= (Integer.parseInt(cursor.getString(3))==0)?false:true;
                me.setVerified(verified);
                me.setLink(cursor.getString(4));
                me.setGender(cursor.getString(5));
                me.setEmail(cursor.getString(6));
                me.setMobile(cursor.getString(7));
                me.setAddress(cursor.getString(8));
                me.setDob(cursor.getString(9));
                me.setProfession(cursor.getString(10));
                me.setSkillset(cursor.getString(11));

                allMe.add(me);
            } while (cursor.moveToNext());
        }

        return allMe;

    }


    public int editMe(UserInfo me) {
        SQLiteDatabase db = this.getWritableDatabase();
        int verified=me.isVerified()?1:0;

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,me.getId());
        values.put(COLUMN_NAME, me.getName());
        if(me.getCover()!=null)
            values.put(COLUMN_COVER,me.getCover().getSource());
        values.put(COLUMN_VERIFIED,verified);
        values.put(COLUMN_LINK, me.getLink());
        values.put(COLUMN_GENDER, me.getGender());
        values.put(COLUMN_EMAIL, me.getEmail());
        values.put(COLUMN_MOBILE,me.getMobile());
        values.put(COLUMN_ADDRESS,me.getAddress());
        values.put(COLUMN_DOB,me.getDob());
        values.put(COLUMN_PROFESSION,me.getProfession());
        values.put(COLUMN_SKILLS,me.getSkillset());

// updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { me.getId() });

    }

    public UserInfo getMe(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserInfo me=null;

        if(id!=null){
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_COVER, COLUMN_VERIFIED, COLUMN_LINK,
                            COLUMN_GENDER, COLUMN_EMAIL,COLUMN_MOBILE ,COLUMN_ADDRESS ,COLUMN_DOB ,COLUMN_PROFESSION ,COLUMN_SKILLS },
                    COLUMN_ID + "=?",
                    new String[] { id }, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                boolean verified = (Integer.parseInt(cursor.getString(3)) == 0) ? false : true;
                me=new UserInfo();
                me.setId(cursor.getString(0));
                me.setName(cursor.getString(1));
                Cover c=new Cover();
                c.setSource(cursor.getString(2));
                me.setCover(c);
                me.setVerified(verified);
                me.setLink(cursor.getString(4));
                me.setGender(cursor.getString(5));
                me.setEmail(cursor.getString(6));
                me.setMobile(cursor.getString(7));
                me.setAddress(cursor.getString(8));
                me.setDob(cursor.getString(9));
                me.setProfession(cursor.getString(10));
                me.setSkillset(cursor.getString(11));

            }

            return me;
        }

        else return null;
    }


   /* public void removePerson(Hitlist person) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(person.getId()) });
        db.execSQL("UPDATE "+TABLE_NAME+" set "+COLUMN_ID+" = ("+COLUMN_ID+" - 1) WHERE "+COLUMN_ID+" > "+person.getId());
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = (seq-1) WHERE name = "+TABLE_NAME );
    }

    public void removeAllPerson(List<Hitlist> persons) {
        SQLiteDatabase db = this.getWritableDatabase();

        Hitlist person;

        for(int i=0;i<persons.size();i++){
            person=persons.get(i);
            db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(person.getId()) });
        }

        List<Hitlist> reorderId=sInstance.getAllPersons();
        for(int i=0;i<reorderId.size();i++){
            Hitlist p=reorderId.get(i);
            p.setId(i+1);
            editPerson(p);
        }
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_NAME+"';");
    }


    public int getPersonsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();


        return count;
    }
    */
}

