package org.wit.memo.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.memo.models.MemoJSONStore
import org.wit.memo.models.MemoStore

class MainApp : Application(), AnkoLogger {

    lateinit var memos: MemoStore

    override fun onCreate() {
        super.onCreate()
        memos = MemoJSONStore(applicationContext)
        info("Memo started")
    }
}