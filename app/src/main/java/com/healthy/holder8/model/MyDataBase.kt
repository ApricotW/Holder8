package com.healthy.holder8.model

import android.database.Cursor
import com.healthy.holder8.extension.toVarargArray
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MyDataBase {

    companion object {
        val databaseOpenHelper = MyDatabaseOpenHelper()
        val instance = MyDataBase()
        val datas:Array<String> = arrayOf(Account(mutableMapOf()).ID.toString())
    }
    fun saveAccount(account: Account){
        databaseOpenHelper.use {
            //SQLiteDatabase扩展方法
            insert(AccountTable.NAME,*account.map.toVarargArray())
        }

    }
    fun getAccount(username:String):Cursor{
        return databaseOpenHelper.readableDatabase.query(AccountTable.NAME, datas,username,null,null,null,null)
    }
    fun getAllAccount():List<Account>{
        return databaseOpenHelper.use{
            select(AccountTable.NAME).parseList(object : MapRowParser<Account>{
                override fun parseRow(columns: Map<String, Any?>): Account = Account(columns.toMutableMap())
            })
        }
    }
    fun deleteAccount(account:String){
        databaseOpenHelper.use {
            delete(AccountTable.NAME,account,null)
        }
    }
}