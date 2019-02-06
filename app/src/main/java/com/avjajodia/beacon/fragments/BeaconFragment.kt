package com.avjajodia.beacon.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.avjajodia.beacon.R
import com.avjajodia.beacon.adapters.BeaconListAdapter
import com.avjajodia.beacon.application.BeaconApplication
import com.avjajodia.beacon.models.BeaconData
import com.avjajodia.beacon.viewmodels.BeaconViewModel
import io.socket.client.Socket
import kotlinx.android.synthetic.main.beacon_fragment.*
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class BeaconFragment : Fragment() {

    companion object {
        fun newInstance() = BeaconFragment()
    }

    private var beaconList: HashMap<String, String> = hashMapOf()
    private lateinit var viewModel: BeaconViewModel
    private val btAdapter = BluetoothAdapter.getDefaultAdapter()

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

        BeaconApplication.getSocket().on(Socket.EVENT_CONNECT) {
            BeaconApplication.getSocket().emit("add user", "Beacon App")
        }

        viewModel = ViewModelProviders.of(this).get(BeaconViewModel::class.java)
        viewModel.getData().observe(this, Observer<BeaconData> { beaconData ->
            beaconList[beaconData!!.id1] = beaconData.distance.toString()
            beaconListAdapter.notifyDataSetChanged()

        })

    }

}
