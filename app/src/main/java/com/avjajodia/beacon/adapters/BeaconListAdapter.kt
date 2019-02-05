package com.avjajodia.beacon.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avjajodia.beacon.R
import com.avjajodia.beacon.models.BeaconData
import kotlinx.android.synthetic.main.beacon_list_item.view.*


/**
 * Created by Aditya V Jajodia on 05-02-2019.
 */
class BeaconListAdapter(private val list: HashMap<String, String>) :
    RecyclerView.Adapter<BeaconListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.beacon_list_item, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val keys = list.keys
        val singleItem = list.values

        holder.itemView.beacon_id.text = keys.elementAt(position)
        holder.itemView.distance.text = singleItem.elementAt(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}