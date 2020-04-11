package com.easypdfreader

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.android.synthetic.main.activity_pdf_view.*


class PdfViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {

    var pageNumber = 0
    var pdfFileName: String? = null
    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        position = intent.getIntExtra("position", -1);
        displayFromSdcard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayFromSdcard() {
        pdfFileName = MainListActivity.fileList[position].name

        pdfView.fromFile(MainListActivity.fileList.get(position))
            .defaultPage(pageNumber)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        title = String.format("%s %s / %s", pdfFileName, page + 1, pageCount);
    }

    override fun loadComplete(nbPages: Int) {
        pdfView.documentMeta
    }

}
