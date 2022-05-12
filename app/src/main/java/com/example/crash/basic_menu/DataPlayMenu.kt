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
    val nameP: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val imId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val activeMenu:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val activeTrain:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val active:MutableLiveData<View> by lazy {
        MutableLiveData<View>()
    }

    val authResult:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }



    val managerSound:MutableLiveData<AudioManager> by lazy {
        MutableLiveData<AudioManager>()
    }

    val SeekBar:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


}