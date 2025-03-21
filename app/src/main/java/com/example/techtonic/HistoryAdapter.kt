package com.example.techtonic.Activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.techtonic.Class.Reports
import com.example.techtonic.R

class HistoryAdapter(
    private val context: Context,
    private val historyMap: Map<String, List<Reports>>
) : BaseExpandableListAdapter() {

    private val dates = historyMap.keys.toList()

    override fun getGroupCount(): Int = dates.size

    override fun getChildrenCount(groupPosition: Int): Int =
        historyMap[dates[groupPosition]]?.size ?: 0

    override fun getGroup(groupPosition: Int): Any = dates[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any =
        historyMap[dates[groupPosition]]?.get(childPosition) ?: Reports("", "", "", 0, "", "")

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_date, parent, false)
        val dateTextView = view.findViewById<TextView>(R.id.date_text_view)
        dateTextView.text = dates[groupPosition]
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_report, parent, false)
        val report = getChild(groupPosition, childPosition) as Reports

        val titleTextView = view.findViewById<TextView>(R.id.report_title)
        val locationTextView = view.findViewById<TextView>(R.id.report_location)
        titleTextView.text = report.hazardType
        locationTextView.text = report.location

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}
