package org.wit.memo.models

interface MemoStore {
    fun findAll(): List<MemoModel>
    fun create(memo: MemoModel)
    fun update(memo: MemoModel)
    fun delete(memo: MemoModel)
}