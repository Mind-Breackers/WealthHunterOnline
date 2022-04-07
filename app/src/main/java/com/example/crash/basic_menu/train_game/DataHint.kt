package com.example.crash.basic_menu.train_game

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataHint: ViewModel() {
    val TrainScrinshot: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }

    val activeHints: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val RightLeft: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val HintString:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}