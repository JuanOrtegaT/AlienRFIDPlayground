package com.jortegat.alienrfidplayground.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alien.enterpriseRFID.reader.AlienClass1Reader
import com.jortegat.alienrfidplayground.TagManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RfidViewModel : ViewModel() {

    var reader: AlienClass1Reader = AlienClass1Reader().apply {
        setConnection(ALIEN_IP, TELNET_PORT)
        username = USERNAME
        password = PASSWORD
    }

    private val tagManager: TagManager by lazy {
        TagManager()
    }

    init {
        startMessageService()
        tagManager.onMessageArrived = {
            onNewTagMediator.postValue(it)
        }
    }

    private fun startMessageService() {
        viewModelScope.launch(Dispatchers.IO) {
            tagManager.startMessageService()
        }
    }

    private val connectionOpenMediator = MediatorLiveData<Boolean>()
    val connectionResponse: LiveData<Boolean> get() = connectionOpenMediator

    private val lastTagMediator = MediatorLiveData<String>()
    val lastTagResponse: LiveData<String> get() = lastTagMediator

    private val onNewTagMediator = MediatorLiveData<String>()
    val onNewTagTagResponse: LiveData<String> get() = onNewTagMediator

    fun startReader() {
        viewModelScope.launch(Dispatchers.IO) {
            startRfidReader()
        }
    }

    fun getLastTag() {
        viewModelScope.launch(Dispatchers.IO) {
            getLastTagFromReader()
        }
    }

    fun setupNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            reader.setNotifyAddress(HOST_IP, MESSAGES_PORT)
            reader.notifyFormat = AlienClass1Reader.XML_FORMAT
            reader.notifyTrigger = "True"
            reader.notifyMode = AlienClass1Reader.ON
            reader.autoModeReset()
            reader.autoMode = AlienClass1Reader.ON
        }
    }

    private fun startRfidReader() {
        if (!reader.isOpen) {
            try {
                reader.open()
                connectionOpenMediator.postValue(true)
            } catch (e: Exception) {
                connectionOpenMediator.postValue(false)
            }
        }
    }

    private fun getLastTagFromReader() {
        if (reader.isOpen) {
            try {
                lastTagMediator.postValue(reader.tagList[0].tagID)
            } catch (e: Exception) {
                lastTagMediator.postValue("Error")
            }
        }
    }

    companion object {
        private const val HOST_IP = "192.168.1.58"
        private const val ALIEN_IP = "192.168.1.61"
        private const val TELNET_PORT = 23
        private const val MESSAGES_PORT = 3988
        private const val USERNAME = "alien"
        private const val PASSWORD = "password"
    }

}