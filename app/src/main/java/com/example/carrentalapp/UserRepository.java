package com.example.carrentalapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
public class UserRepository {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";
    private Context context;

    public void logoutUser() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }

    public UserRepository(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void saveUserIdToPreferences(int userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addUser(User user) {
        // Check if the email is already registered
        if (isEmailRegistered(user.getEmail())) {
            return -1; // Return a special value indicating email already registered
        }

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(UserContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(UserContract.UserEntry.COLUMN_PHONE, user.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword()); // Assuming User has a getPassword method

        return database.insert(UserContract.UserEntry.TABLE_NAME, null, values);
    }

    private boolean isEmailRegistered(String email) {
        String[] projection = {UserContract.UserEntry._ID};
        String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = database.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean emailRegistered = cursor.moveToFirst();
        cursor.close();

        return emailRegistered;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("phone")),
                        cursor.getString(cursor.getColumnIndex("password"))
                );
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                UserContract.UserEntry._ID
        };

        // Define the selection criteria
        String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ? AND " +
                UserContract.UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};  // Note: Password comparison should be secure (e.g., hashed)

        // Query the database
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if the cursor has any rows (user found)
        boolean userExists = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return userExists;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int userId = -1; // Default value, indicating user not found

        String[] projection = {
                UserContract.UserEntry._ID
        };

        String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(UserContract.UserEntry._ID));
        }

        cursor.close();
        db.close();

        return userId;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] projection = {
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_EMAIL,
                UserContract.UserEntry.COLUMN_PHONE,
                UserContract.UserEntry.COLUMN_PASSWORD
        };

        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD))
                    // Add other columns if needed
            );
        }

        cursor.close();
        db.close();

        return user;
    }


    public String getUserEmailById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserContract.UserEntry.COLUMN_EMAIL};

        // Define the selection criteria
        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        // Query the database
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userEmail = null;

        if (cursor.moveToFirst()) {
            userEmail = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return userEmail;
    }

    public String getUserPhoneById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserContract.UserEntry.COLUMN_PHONE};

        // Define the selection criteria
        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        // Query the database
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userPhone = null;

        if (cursor.moveToFirst()) {
            userPhone = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHONE));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return userPhone;
    }

    public String getUserPasswordById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserContract.UserEntry.COLUMN_PASSWORD};

        // Define the selection criteria
        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        // Query the database
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userPassword = null;

        if (cursor.moveToFirst()) {
            userPassword = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return userPassword;
    }

    public String getUserNameById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserContract.UserEntry.COLUMN_NAME};

        // Define the selection criteria
        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        // Query the database
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userName = null;

        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return userName;
    }

    public boolean updateUserDetails(int userId, String updatedName, String updatedEmail, String updatedPhone, String updatedPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME, updatedName);
        values.put(UserContract.UserEntry.COLUMN_EMAIL, updatedEmail);
        values.put(UserContract.UserEntry.COLUMN_PHONE, updatedPhone);
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, updatedPassword);

        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        int rowsUpdated = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        return rowsUpdated > 0;
    }
}
