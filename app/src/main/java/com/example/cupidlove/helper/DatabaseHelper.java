package com.example.cupidlove.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cupidlove.model.ChatMessage;
import com.example.cupidlove.model.Friends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

    //Todo Logcat tag
    private static final String LOG = "Chat DataBase";

    //Todo Database Version
    private static final int DATABASE_VERSION = 1;

    //Todo Database Name
    private static final String DATABASE_NAME = "cupid_love_chats";

    // TodoTable Names
    private static final String TABLE_CHAT = "chats";
    private static final String TABLE_FRIENDS = "friends";

    //Todo Chat Common column names
    private static final String KEY_CHAT_ID = "chat_id";
    private static final String KEY_MESSAGE_TYPE = "ismine";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_BODY = "body";
    private static final String KEY_IS_DATE_ID = "is_date_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_READ = "is_read";


    //Todo friend Common column names
    private static final String ID = "id";
    private static final String KEY_FRIEND_ID = "friend_id";
    private static final String KEY_EJJABERED_ID = "ejabbered_id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_PROFILEIMAGE = "profile_image";


    // Table Create Statements
    // Todo : table create statement
    private static final String CREATE_TABLE_CHAT = "CREATE TABLE "
            + TABLE_CHAT + "(" + KEY_CHAT_ID + " INTEGER PRIMARY KEY,"
            + KEY_SENDER + " TEXT," + KEY_RECEIVER + " TEXT ," + KEY_USER_ID + " TEXT,"
            + KEY_BODY + " TEXT," + KEY_DATE + " TEXT," + KEY_IS_DATE_ID + " TEXT," + KEY_IS_READ + " TEXT,"
            + KEY_TIME + " TEXT ," + KEY_MESSAGE_TYPE + " INTEGER" + ")";

    // Table Create Statements
    // Todo : table create statement
    private static final String CREATE_TABLE_FRIEND = "CREATE TABLE "
            + TABLE_FRIENDS + "(" + ID + " INTEGER PRIMARY KEY,"
            + KEY_FRIEND_ID + " TEXT," + KEY_FNAME + " TEXT ," + KEY_LNAME + " TEXT,"
            + KEY_PROFILEIMAGE + " TEXT," + KEY_EJJABERED_ID + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Todo: creating required tables
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_FRIEND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Todo: on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        //Todo : create new tables
        onCreate(db);
    }

    // ------------------------ "todos" table methods ----------------//

    /*
     *  todo: Insert Chat
     */
    public long insertChat(ChatMessage chatMessage) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Add Chat Id ", chatMessage.dateid + " get");

        ContentValues values = new ContentValues();
        values.put(KEY_BODY, chatMessage.body);
        values.put(KEY_SENDER, chatMessage.sender.toLowerCase());
        values.put(KEY_RECEIVER, chatMessage.receiver.toLowerCase());
        values.put(KEY_DATE, chatMessage.mdate);
        values.put(KEY_TIME, chatMessage.mtime);
        values.put(KEY_IS_DATE_ID, chatMessage.dateid + "");
        values.put(KEY_USER_ID, chatMessage.userId + "");
        values.put(KEY_MESSAGE_TYPE, chatMessage.messagetype);
        if (chatMessage.isRead) {
            values.put(KEY_IS_READ, "1");
        } else {
            values.put(KEY_IS_READ, "0");
        }


        // insert row
        long todo_id = db.insert(TABLE_CHAT, null, values);


        return todo_id;
    }


    /*
   * Updating a todo
   */
    public int updateChat(ChatMessage chatMessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BODY, chatMessage.body);

//        Log.e("Update Id is ", chatMessage.dateid + "");

        // updating row
        return db.update(TABLE_CHAT, values, KEY_IS_DATE_ID + " = ?",
                new String[]{String.valueOf(chatMessage.dateid)});
    }

    public int updateChatRead(ChatMessage chatMessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (chatMessage.isRead) {
            values.put(KEY_IS_READ, "1");
        } else {
            values.put(KEY_IS_READ, "0");
        }


//        Log.e("Update Id is ", chatMessage.msgid + "");

        // updating row
        return db.update(TABLE_CHAT, values, KEY_CHAT_ID + " = ?",
                new String[]{String.valueOf(chatMessage.msgid)});
    }

    public void updateAllToReadChat(List<ChatMessage> message) {
        for (int i = 0; i < message.size(); i++) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_IS_READ, "1");
//            Log.e("Update Id is ", message.get(i).msgid + "");

            // updating row
            long update = db.update(TABLE_CHAT, values, KEY_CHAT_ID + " = ?",
                    new String[]{String.valueOf(message.get(i).msgid)});
        }

    }


    /**
     * todo: Get All Chat Message
     */
    public List<ChatMessage> getAllChatMessage(String sender, String receiver) {

        List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

        if (sender == null) {
            return chatMessages;
        } else {
            sender = sender.toLowerCase();
        }
        if (receiver == null) {
            return chatMessages;
        } else {
            receiver = receiver.toLowerCase();
        }

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " where(" + KEY_SENDER + "=" + "'" + sender + "'" + " and " + KEY_RECEIVER + "='" + receiver + "')"
                + " or (" + KEY_SENDER + "='" + receiver + "' and " + KEY_RECEIVER + "='" + sender + "')";
//        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ChatMessage message = new ChatMessage();
                message.msgid = (c.getInt((c.getColumnIndex(KEY_CHAT_ID))));
                message.receiver = (c.getString(c.getColumnIndex(KEY_RECEIVER)));
                message.sender = (c.getString(c.getColumnIndex(KEY_SENDER)));
                message.messagetype = (c.getInt(c.getColumnIndex(KEY_MESSAGE_TYPE)));
                message.mtime = (c.getString(c.getColumnIndex(KEY_TIME)));
                message.body = (c.getString(c.getColumnIndex(KEY_BODY)));
                message.mdate = (c.getString(c.getColumnIndex(KEY_DATE)));
                message.dateid = (c.getString(c.getColumnIndex(KEY_IS_DATE_ID)));
                if (c.getString(c.getColumnIndex(KEY_IS_READ)).equals("0")) {
                    message.isRead = false;
                } else {
                    message.isRead = true;
                }

                // adding to todo list
                chatMessages.add(message);
            } while (c.moveToNext());
        }
        return chatMessages;
    }


    /**
     * todo: Get Last Chat Message
     */
    public String getLastchatMessage(String sender, String receiver, String userId) {
        sender = sender.toLowerCase();
        receiver = receiver.toLowerCase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " where(" + KEY_SENDER + "=" + "'" + sender + "'" + " and " + KEY_RECEIVER + "='" + receiver + "')"
                + " or (" + KEY_SENDER + "='" + receiver + "' and " + KEY_RECEIVER + "='" + sender + "') ORDER BY " + KEY_CHAT_ID + " DESC LIMIT 1";

//        String selectQuery="SELECT  * FROM " + TABLE_CHAT ;
//        Log.e(LOG, selectQuery);


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        if (c.moveToFirst()) {
            ChatMessage message = new ChatMessage();
            message.msgid = (c.getInt((c.getColumnIndex(KEY_CHAT_ID))));
            message.receiver = (c.getString(c.getColumnIndex(KEY_RECEIVER)));
            message.sender = (c.getString(c.getColumnIndex(KEY_SENDER)));
            message.messagetype = (c.getInt(c.getColumnIndex(KEY_MESSAGE_TYPE)));
            message.mtime = (c.getString(c.getColumnIndex(KEY_TIME)));
            message.body = (c.getString(c.getColumnIndex(KEY_BODY)));
            message.mdate = (c.getString(c.getColumnIndex(KEY_DATE)));
            message.dateid = (c.getString(c.getColumnIndex(KEY_IS_DATE_ID)));

            if (message.messagetype == 3 || message.messagetype == 4) {
                return "Date Request";
            } else if (message.body != null) {
                return message.body;
            } else {
                return null;
            }
        } else {
            getAllChatMessage(sender, receiver);
            return null;
        }


    }

    public String getUnReadMessage(String sender, String receiver) {
        List<Integer> countList = new ArrayList<>();

        sender = sender.toLowerCase();
        receiver = receiver.toLowerCase();


        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " where(" + KEY_SENDER + "=" + "'" + sender + "'" + " and " + KEY_RECEIVER + "='" + receiver + "')"
                + " or (" + KEY_SENDER + "='" + receiver + "' and " + KEY_RECEIVER + "='" + sender + "')" + " and " + KEY_IS_READ + "='0'";
//        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            int chat = 0;
            do {
                if ((c.getString((c.getColumnIndex(KEY_IS_READ)))).equals("0")) {
                    countList.add(0);
                }

//                Log.e("Chat Is", countList.size() + "");

            } while (c.moveToNext());

            return countList.size() + "";
        }
        return "0";
    }


    /*
     * todo: Delete Chat
     */
    public void deleteChat() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CHAT);
    }


    // Todo : closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * Todo : get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    /*
   *  todo: Insert friend
   */
    public long insertFriend(Friends friends) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friends.getFriend_id());
        values.put(KEY_FNAME, friends.getFname());
        values.put(KEY_LNAME, friends.getLname());
        values.put(KEY_EJJABERED_ID, friends.getEjaberedid());
        values.put(KEY_PROFILEIMAGE, friends.getProfileImage());

        // insert row
        long todo_id = db.insert(TABLE_FRIENDS, null, values);
//        Log.e("Friend  Insert", todo_id + "");

        return todo_id;
    }

    /*
   * todo: Delete friend
   */
    public void deleteFriend() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FRIENDS);
    }


    /**
     * todo: Get Friend
     */
    public Friends getFriendDetail(String ejjebredId) {

        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS + " where(" + KEY_EJJABERED_ID + "=" + "'" + ejjebredId + "')";
//        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Friends friend = new Friends();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                friend.setEjaberedid(c.getString(c.getColumnIndex(KEY_EJJABERED_ID)));
                friend.setFname(c.getString(c.getColumnIndex(KEY_FNAME)));
                friend.setLname(c.getString(c.getColumnIndex(KEY_LNAME)));
                friend.setProfileImage(c.getString(c.getColumnIndex(KEY_PROFILEIMAGE)));
                friend.setFriend_id(c.getString(c.getColumnIndex(KEY_FRIEND_ID)));
                friend.setId(c.getInt(c.getColumnIndex(ID)) + "");


            } while (c.moveToNext());
            return friend;
        } else {
            return null;
        }

    }

    /**
     * todo: Get All Chat Message
     */
    public String getFriend() {

        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;
//        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            Log.e("id is",(c.getString(c.getColumnIndex(KEY_EJJABERED_ID))));
          return "friend";
        }else {
            return null;
        }
    }


}
