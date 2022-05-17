package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import com.example.crash.R
import com.example.crash.games.Game_Play.DataGames

@SuppressLint("ClickableViewAccessibility")
abstract class AbstractPlayField(
    private val parentGame: RelativeLayout,
    displayheight: Int,
    displaywidth: Int,
    parentPool:RelativeLayout,
    parentField1: RelativeLayout,
    parentField2: RelativeLayout,
    protected  val baggageBtn: ImageButton,
    dataPlayMenu: DataGames,
    size:Int,
    protected  val musorka: ImageView,
    val blocks:ArrayList<Block>,
    val blocksPool:PoolAbstract
) {
    val baggaeBlock= arrayListOf<Block>()
    val baggaeBlockEnemy= arrayListOf<Block>()
    val newblockInBaggage=arrayListOf<Block>()
    val newblockInBaggageEnemy=arrayListOf<Block>()

    val cellsSize=blocksPool.cellSize
    val fieldheight = (displayheight - blocksPool.height) / 2 + 50
    val fieldwidth = displaywidth - (displaywidth / 6) * 2
    val field1Centerheight = displayheight-blocksPool.posY/2
    val field1Centerwidth = displaywidth / 2
    val field2Centerheight = displayheight / 4
    protected val field2Centerwidth = displaywidth / 2
    protected  var downX = 0
    protected  var downY = 0
    protected  var lastX = 0
    lateinit var  playField: Field   //сделать приватной
     lateinit var enemyField: Field
    protected var capturedBlock: Block? = null
    protected  var lastY = 0
    protected  var action=3
    val actionPlay: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


    init {
    }



    protected  fun checkBaggageBtn(baggageSize:Int, capturedBlock: Block?){
        if(baggageSize>0){
            if(capturedBlock==null){
                baggageBtn.setImageResource(R.drawable.ic_zakryty_sunduk)
            }else{
                baggageBtn.setImageResource(R.drawable.ic_polny_ryukzak)
            }
        }
        else{
            if(capturedBlock==null){
                baggageBtn.setImageResource(R.drawable.ic_zakryty_sunduk)
            }else{
                baggageBtn.setImageResource(R.drawable.ic_otkryty_sunduk)
            }
        }
    }
}