package org.wit.memo.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.memo.helpers.*
import java.util.*

val JSON_FILE = "memos.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<MemoModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class MemoJSONStore : MemoStore, AnkoLogger {

    val context: Context
    var memos = mutableListOf<MemoModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<MemoModel> {
        return memos
    }

    override fun create(memo: MemoModel) {
        memo.id = generateRandomId()
        memos.add(memo)
        serialize()
    }

    override fun update(memo: MemoModel) {
        val memosList = findAll() as ArrayList<MemoModel>
        var foundMemo: MemoModel? =memosList.find { p -> p.id == memo.id }
        if (foundMemo != null) {
            foundMemo.title = memo.title
            foundMemo.description = memo.description
            foundMemo.address = memo.address
            foundMemo.image = memo.image
            foundMemo.personDate = memo.personDate

        }
        serialize()
    }

    override fun delete(memo: MemoModel) {
        memos.remove(memo)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(memos, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        memos = Gson().fromJson(jsonString, listType)
    }
}