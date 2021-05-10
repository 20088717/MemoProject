package org.wit.memo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemoModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var address: String = "",
                     var image: String = "",
                     var personDate: String = ""
) : Parcelable

