package com.easypdfreader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_item_pdf.view.*
import java.io.File

class PdfListAdapter(private val context: Context) :
    RecyclerView.Adapter<PdfListAdapter.PdfViewHolder>() {

    private var clickListener: ClickListener = context as MainListActivity

    private var pdfList: ArrayList<File> = arrayListOf()

    inner class PdfViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val card = view.card!!
        val tvPdfName = view.tvPdfName!!

        init {
            card.setOnClickListener { clickListener.itemClicked(adapterPosition) }
        }
    }

    fun setPdfsToList(pdfList: ArrayList<File>) {
        this.pdfList = pdfList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.row_item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.tvPdfName.text = pdfList[position].name
    }

    interface ClickListener {
        fun itemClicked(position: Int)
    }
}