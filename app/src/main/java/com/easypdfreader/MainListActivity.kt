package com.easypdfreader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainListActivity : AppCompatActivity(), PdfListAdapter.ClickListener {

    private lateinit var pdfListAdapter: PdfListAdapter

    companion object {
        var fileList: ArrayList<File> = ArrayList()
    }

    private var REQUEST_PERMISSIONS = 1
    private var permission = false
    private lateinit var dir: File
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfListAdapter = PdfListAdapter(this)
        layoutManager = LinearLayoutManager(this)
        rvPdf.layoutManager = layoutManager
        rvPdf.adapter = pdfListAdapter
        rvPdf.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        dir = File(Environment.getExternalStorageDirectory().toString())
    }

    override fun onStart() {
        super.onStart()
        isPermissionAvailable()
    }

    override fun itemClicked(position: Int) {
        var intent = Intent(this, PdfViewActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)

    }

    private fun isPermissionAvailable() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainListActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this@MainListActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS
                )
            }
        } else {
            permission = true
            getPdfFile(dir)
            pdfListAdapter.setPdfsToList(fileList)

        }
    }

    private fun getPdfFile(dir: File): ArrayList<File> {
        val listFile = dir.listFiles()
        if (listFile != null && listFile.isNotEmpty()) {
            for (i in listFile.indices) {
                if (listFile[i].isDirectory) {
                    getPdfFile(listFile[i])
                } else {
                    var booleanpdf = false
                    if (listFile[i].name.endsWith(".pdf")) {
                        for (j in fileList.indices) {
                            if (fileList[j].name == listFile[i].name) {
                                booleanpdf = true
                            } else {
                            }
                        }
                        if (booleanpdf) {
                            booleanpdf = false
                        } else {
                            fileList.add(listFile[i])
                        }
                    }
                }
            }
        }
        return fileList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission = true
                getPdfFile(dir!!)
                pdfListAdapter.setPdfsToList(fileList)

            } else {
                Toast.makeText(applicationContext, "Please allow the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
