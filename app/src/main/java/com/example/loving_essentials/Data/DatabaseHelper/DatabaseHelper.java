package com.example.loving_essentials.Data.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context,@Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    //Query no return data from database (insert,update,delete)
    public void Query(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }
    //Query return data from database (select)
    public Cursor GetData(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //User
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "Email TEXT," +
                "Phone TEXT," +
                "Role INTEGER," +
                "Password TEXT);");
        //Address
        db.execSQL("CREATE TABLE IF NOT EXISTS Address (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserId INTEGER," +
                "Street TEXT," +
                "City TEXT," +
                "State TEXT," +
                "ZipCode TEXT," +
                "FOREIGN KEY(UserId) REFERENCES User(Id));");
        //Category
        db.execSQL("CREATE TABLE IF NOT EXISTS Category (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT);");
        //Brand
        db.execSQL("CREATE TABLE IF NOT EXISTS Brand (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT);");
        //Product
        db.execSQL("CREATE TABLE IF NOT EXISTS Product (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "Price REAL," +
                "Description TEXT," +
                "Image TEXT," +
                "CategoryId INTEGER," +
                "BrandId INTEGER," +
                "FOREIGN KEY(CategoryId) REFERENCES Category(Id)," +
                "FOREIGN KEY(BrandId) REFERENCES Brand(Id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Transactions (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Status BOOLEAN, " +
                "CreatedAt DATETIME," +
                "GrossAmount REAL);");
        // Create Order table
        db.execSQL("CREATE TABLE IF NOT EXISTS OrderTable (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date DATETIME," +
                "TotalAmount REAL," +
                "DeliveryDate DATETIME," +
                "UserId INTEGER," +
                "TransactionsId INTEGER," +
                "FOREIGN KEY(UserId) REFERENCES User(Id)," +
                "FOREIGN KEY(TransactionsId) REFERENCES Transactions(Id));");
        // Create OrderDetail table
        db.execSQL("CREATE TABLE IF NOT EXISTS OrderDetail (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "OrderId INTEGER," +
                "ProductId INTEGER," +
                "Quantity INTEGER," +
                "FOREIGN KEY(OrderId) REFERENCES OrderTable(Id)," +
                "FOREIGN KEY(ProductId) REFERENCES Product(Id));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
