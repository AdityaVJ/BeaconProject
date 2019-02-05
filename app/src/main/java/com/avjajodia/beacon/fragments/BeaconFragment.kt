package com.avjajodia.beacon.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.avjajodia.beacon.R
import com.avjajodia.beacon.adapters.BeaconListAdapter
import com.avjajodia.beacon.models.BeaconData
import kotlinx.android.synthetic.main.beacon_fragment.*
import java.util.function.UnaryOperator

class BeaconFragment : Fragment() {

    companion object {
        fun newInstance() = BeaconFragment()
    }

    private var beaconList: HashMap<String, String> = hashMapOf()
    private lateinit var viewModel: BeaconViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.beacon_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val beaconListAdapter: BeaconListAdapter = BeaconListAdapter(beaconList)
        beacon_list.adapter = beaconListAdapter

        viewModel = ViewModelProviders.of(this).get(BeaconViewModel::class.java)
        viewModel.getData().observe(this, Observer<BeaconData> { beaconData ->
            beaconList[beaconData!!.id1] = beaconData.distance.toString()
            beaconListAdapter.notifyDataSetChanged()

        })

    }

}
