package org.wit.memo.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class MemoMemStore : MemoStore, AnkoLogger {

    val memos = ArrayList<MemoModel>()

    override fun findAll(): List<MemoModel> {
        return memos
    }

    override fun create(memo: MemoModel) {
        memo.id = getId()
        memos.add(memo)
        logAll()
    }

    override fun update(memo: MemoModel) {
        var foundMemo: MemoModel? = memos.find { p -> p.id == memo.id }
        if (foundMemo != null) {
            foundMemo.title = memo.title
            foundMemo.description = memo.description
            logAll();
        }
    }

    override fun delete(memo: MemoModel) {
        memos.remove(memo)
    }

    fun logAll() {
        memos.forEach { info("${it}") }
    }
}