package com.example.techtonic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ArrayAdapter
import com.example.techtonic.Activity.ReportDetailActivity
import com.example.techtonic.Class.Reports


import com.bumptech.glide.Glide

class ReportAdapter(
    private val context: Context,
    private val reportList: List<Reports>
) : ArrayAdapter<Reports>(context, 0, reportList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_report2, parent, false)

        val report = reportList[position]

        val imageView = view.findViewById<ImageView>(R.id.report_image)
        val titleView = view.findViewById<TextView>(R.id.report_title)
        val statusView = view.findViewById<TextView>(R.id.report_status)
        val viewDetailsButton = view.findViewById<Button>(R.id.view_details_button)

        Glide.with(context)
        .load(report.imageUrl)
        .into(imageView)

        titleView.text = report.hazardType
        statusView.text = report.status

        viewDetailsButton.setOnClickListener {
            val intent = Intent(context, ReportDetailActivity::class.java)
            intent.putExtra("hazardType", report.hazardType)
            intent.putExtra("status", report.status)
            intent.putExtra("location", report.location)
            intent.putExtra("description", report.description)
            intent.putExtra("imageUrl", report.imageUrl)

            context.startActivity(intent)
        }

        return view
    }
}


