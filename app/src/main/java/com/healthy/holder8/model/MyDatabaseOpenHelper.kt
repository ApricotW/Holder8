package com.healthy.holder8.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.healthy.holder8.ui.MainActivity
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context = MainActivity.instance): ManagedSQLiteOpenHelper(ctx,"MyDatabase",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(AccountTable.NAME,true,
            AccountTable.ID to TEXT + PRIMARY_KEY + AUTOINCREMENT,
            AccountTable.password to TEXT,AccountTable.beat to INTEGER,
            AccountTable.bpressure to INTEGER,AccountTable.btemperature to INTEGER,
            AccountTable.boxy to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(AccountTable.NAME,true)
        onCreate(db)
    }

}