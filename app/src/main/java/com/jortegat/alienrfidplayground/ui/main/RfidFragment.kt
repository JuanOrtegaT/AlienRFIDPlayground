package com.jortegat.alienrfidplayground.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jortegat.alienrfidplayground.R

class RfidFragment : Fragment() {

    companion object {
        fun newInstance() = RfidFragment()
        private const val HOST_IP = "192.168.1.58"
        private const val ALIEN_IP = "192.168.1.61"
        private const val TELNET_PORT = 23
        private const val MESSAGES_PORT = 3988
        private const val USERNAME = "alien"
        private const val PASSWORD = "password"
    }

    private val viewModel: RfidViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.rfid_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonListeners()
        observers()
    }

    private fun observers() {
        viewModel.connectionResponse.observe(viewLifecycleOwner, Observer(::validateConnection))
        viewModel.lastTagResponse.observe(viewLifecycleOwner, Observer(::printTag))
        viewModel.onNewTagTagResponse.observe(viewLifecycleOwner, Observer(::onNewTag))
    }

    private fun validateConnection(connected: Boolean) {
        if (connected) {
            Toast.makeText(
                requireContext(),
                "is connection open? = ${viewModel.reader.isOpen}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun printTag(tag: String) {
        Toast.makeText(requireContext(), "Last Tag = $tag", Toast.LENGTH_SHORT).show()
    }

    private fun onNewTag(tag: String) {
        Toast.makeText(requireContext(), "New Tag = $tag", Toast.LENGTH_SHORT).show()
    }

    private fun buttonListeners() {
        activity?.findViewById<Button>(R.id.buttonOpenCon)?.setOnClickListener {
            viewModel.startReader()
        }

        activity?.findViewById<Button>(R.id.buttonTag)?.setOnClickListener {
            viewModel.getLastTag()
        }

        activity?.findViewById<Button>(R.id.buttonListener)?.setOnClickListener {
            viewModel.setupNotification()
        }
    }
}