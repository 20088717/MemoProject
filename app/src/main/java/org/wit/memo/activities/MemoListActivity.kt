package org.wit.memo.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_memo_list.*
import org.jetbrains.anko.intentFor
import org.wit.memo.R
import org.wit.memo.main.MainApp
import org.jetbrains.anko.startActivityForResult
import org.wit.memo.models.MemoModel

class MemoListActivity : AppCompatActivity(), MemoListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        app = application as MainApp

        //layout and populate for display
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager   //recyclerView is a widget in activity_memo_list.xml
        loadMemos()

        //enable action bar and set title
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> startActivityForResult<MemoActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMemoClick(memo: MemoModel) {
        startActivityForResult(intentFor<MemoActivity>().putExtra("memo_edit", memo), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadMemos()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadMemos() {
        showMemos(app.memos.findAll())
    }

    fun showMemos (memos: List<MemoModel>) {
        recyclerView.adapter = MemoAdapter(memos, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}

