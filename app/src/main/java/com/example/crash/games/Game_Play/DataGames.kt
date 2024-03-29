package com.example.crash.games.Game_Play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crash.games.ClassForGame.Block

open class DataGames: ViewModel(){
    val LifeBaggage: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val LifeBaggageEnemy: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val BlockInBaggage1: MutableLiveData<ArrayList<Block>> by lazy {
        MutableLiveData<ArrayList<Block>>()
    }
    val BlockInBaggageEnemy1: MutableLiveData<ArrayList<Block>> by lazy {
        MutableLiveData<ArrayList<Block>>()
    }

    val BlockInBaggage2: MutableLiveData<ArrayList<Block>> by lazy {
        MutableLiveData<ArrayList<Block>>()
    }

    val BlockInBaggageEnemy2: MutableLiveData<ArrayList<Block>> by lazy {
        MutableLiveData<ArrayList<Block>>()
    }

    val BlockOutBaggage: MutableLiveData<Block> by lazy {
        MutableLiveData<Block>()
    }

    val BlockOutBaggageEnemy: MutableLiveData<Block> by lazy {
        MutableLiveData<Block>()
    }

    val TopOrBottom: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}
