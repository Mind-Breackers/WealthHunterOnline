package com.example.crash.basic_menu

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crash.basic_menu.Achievements.Achivement_Item

open class DataPlayMenu:ViewModel() {
    val nameP: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val imId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val Cup:MutableLiveData<Achivement_Item> by lazy {
        MutableLiveData<Achivement_Item>()
    }

    val AchivementScrinshot:MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }

    val activeMenu:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val active:MutableLiveData<View> by lazy {
        MutableLiveData<View>()
    }

    val authResult:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

}