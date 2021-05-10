package org.wit.memo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_memo.*
import kotlinx.android.synthetic.main.card_memo.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.memo.R
import org.wit.memo.helpers.readImage
import org.wit.memo.helpers.readImageFromPath
import org.wit.memo.helpers.showImagePicker
//import org.wit.placemark.helpers.readImage
//import org.wit.placemark.helpers.readImageFromPath
//import org.wit.placemark.helpers.showImagePicker
import org.wit.memo.main.MainApp
//import org.wit.placemark.models.Location
import org.wit.memo.models.MemoModel
class MemoActivity : AppCompatActivity(), AnkoLogger {

    var memo = MemoModel()
    lateinit var app : MainApp
    var edit = false
    val IMAGE_REQUEST = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        app = application as MainApp

        if (intent.hasExtra("memo_edit")) {
            edit = true
            memo = intent.extras?.getParcelable<MemoModel>("memo_edit")!!
            memoTitleAdd.setText(memo.title)
           memoDescriptionAdd.setText(memo.description)
            memoAddressAdd.setText(memo.address)
            memoImage.setImageBitmap(readImageFromPath(this, memo.image))
            if (memo.image != null) {
                chooseImage.setText(R.string.change_memo_image)
            }
            btnAdd.setText(R.string.save_memo)
        }


        btnAdd.setOnClickListener() {
            memo.title = memoTitleAdd.text.toString()
            memo.description = memoDescriptionAdd.text.toString()
            memo.address = memoAddressAdd.text.toString()
            if (memo.title.isEmpty()) {
                toast(R.string.enter_memo_title)
            } else {
                if (edit) {
                    app.memos.update(memo.copy())
                } else {
                    app.memos.create(memo.copy())
                }
            }
            info("add Button Pressed: $memoTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_memo, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.memos.delete(memo)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    memo.image = data.getData().toString()
                    memoImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_memo_image)
                }
            }
        }
    }

}