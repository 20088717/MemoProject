package org.wit.memo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
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
import java.util.*

class MemoActivity : AppCompatActivity(), AnkoLogger {

    var memo = MemoModel()
    lateinit var app : MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    var dateToBeStored : String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        val datePicker = findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            dateToBeStored = "$day/$month/$year"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        app = application as MainApp

        if (intent.hasExtra("memo_edit")) {
//          datePicker.isVisible = false
            datePicker.isVisible = true
            edit = true
            memo = intent.extras?.getParcelable<MemoModel>("memo_edit")!!
            memoTitleAdd.setText(memo.title)
           memoDescriptionAdd.setText(memo.description)
            memoAddressAdd.setText(memo.address)
            memoImage.setImageBitmap(readImageFromPath(this, memo.image))
            if (memo.image != null) {
                chooseImage.setText(R.string.change_memo_image)
            }

            val string: String = memo.personDate
            val dateArray: List<String> = string.split("/")
            datePicker.updateDate(dateArray[2].toInt(), dateArray[1].toInt()-1, dateArray[0].toInt())

            btnAdd.setText(R.string.save_memo)
        }


        btnAdd.setOnClickListener() {
            memo.title = memoTitleAdd.text.toString()
            memo.description = memoDescriptionAdd.text.toString()
            memo.address = memoAddressAdd.text.toString()
            memo.personDate = dateToBeStored
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