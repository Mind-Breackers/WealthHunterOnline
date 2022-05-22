package com.example.crash.basic_menu

import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crash.sigInUp.Server.User

open class DataPlayMenu:ViewModel() {
    val nameP: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val nameGuest: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val flagGUest:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val activeMenu:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val activeTrain:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val managerSound:MutableLiveData<AudioManager> by lazy {
        MutableLiveData<AudioManager>()
    }

    val SeekBar:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}