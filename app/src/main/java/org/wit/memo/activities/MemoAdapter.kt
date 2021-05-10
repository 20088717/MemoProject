package org.wit.memo.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_memo.view.*
import org.wit.memo.R
import org.wit.memo.helpers.readImageFromPath
import org.wit.memo.models.MemoModel

interface MemoListener {
    fun onMemoClick(memo: MemoModel)
}

class MemoAdapter constructor(private var memos: List<MemoModel>,
                                   private val listener: MemoListener) :
        RecyclerView.Adapter<MemoAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
                LayoutInflater.from(parent?.context).inflate(
                        R.layout.card_memo, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val memo = memos[holder.adapterPosition]
        holder.bind(memo, listener)
    }

    override fun getItemCount(): Int = memos.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(memo: MemoModel,  listener : MemoListener) {
            itemView.memoTitle.text = memo.title
            itemView.description.text = memo.description
            itemView.dateadded.text = memo.personDate
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, memo.image))
            itemView.setOnClickListener { listener.onMemoClick(memo) }
        }
    }
}