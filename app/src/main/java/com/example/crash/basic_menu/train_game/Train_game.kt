package com.example.crash.basic_menu.train_game

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.constance.Constance
import com.example.crash.databinding.FragmentTrainGameBinding
import com.example.crash.games.ClassForGame.Block
import com.example.crash.games.ClassForGame.Field
import com.example.crash.games.Game_Play.Baggage.BaggageFragment
import com.example.crash.games.Game_Play.DataGames
import com.example.crash.games.ClassForGame.Pool


class Train_game : Fragment() {
    lateinit var binding: FragmentTrainGameBinding
    private val dataGames: DataGames by activityViewModels()
    private val dataPlayMenu: DataPlayMenu by activityViewModels()
    private val DataHint: DataHint by activityViewModels()
    var displayheight: Int = 0
    var displaywidth: Int = 0

    lateinit var trainfield: TrainClass

    lateinit var textRules:TextView

    var action: Int = 1
    val flagAction= arrayListOf<Boolean>(true,true,true,true,true,true,true,true,true)

    var downX = 0
    private val hintsString = listOf(
        Constance.HINT_1,
        Constance.HINT_2,
        Constance.HINT_3,
        Constance.HINT_4,
        Constance.HINT_5,
        Constance.HINT_6,
        Constance.HINT_7,
        Constance.HINT_8
    )

    private val hintsRulsString = listOf(
        Constance.HINT_RULS_1,
        Constance.HINT_RULS_2,
        Constance.HINT_RULS_3,
        Constance.HINT_RULS_4,
        Constance.HINT_RULS_5,
        Constance.HINT_RULS_6,
        "Удачи в дальнейшей игре"
    )
    var downY = 0
    private var sizeBaggage = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainGameBinding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val displaymetrics = resources.displayMetrics
        displayheight = displaymetrics.heightPixels
        displaywidth = displaymetrics.widthPixels
        val field1Centerheight = displayheight / 2 + displayheight / 3
        val field1Centerwidth = displaywidth / 2
        binding.gameHint.setAutoSizeTextTypeUniformWithPresetSizes(
            intArrayOf(
                10,
                12,
                14,
                16,
                20,
                22
            ), TypedValue.COMPLEX_UNIT_SP
        )
        sendHint(0)


        var block = arrayListOf<Block>()
        var tempPool = Pool(binding.firstScreenTrain, block, 0, displayheight / 2, displaywidth)
        tempPool.clearBack()
        binding.firstScreenTrain.setOnClickListener {
            it.visibility = View.GONE
        }

        trainfield = TrainClass(
            binding.gameTrain,
            displayheight,
            displaywidth,
            binding.rlPoolTrain,
            binding.Player1Train,
            binding.Player1Train,
            binding.baggageBtnTrain,
            dataGames,
            10,
            DataHint,
            binding.musorkaSvg
        )
        binding.Player1Train.post {
            val paramsbtn = binding.baggageBtnTrain.layoutParams as RelativeLayout.LayoutParams
            paramsbtn.leftMargin =
                (displaywidth - (displaywidth - trainfield.fieldwidth) / 2) + (displaywidth - trainfield.fieldwidth) / 16
            paramsbtn.topMargin = displayheight - binding.baggageBtnTrain.height
            binding.baggageBtnTrain.layoutParams = paramsbtn

            binding.baggageBtnTrain.post {
                val paramsMusorka = binding.musorkaSvg.layoutParams as RelativeLayout.LayoutParams
                paramsMusorka.rightMargin = (displaywidth - (displaywidth - trainfield.fieldwidth) / 2) + (displaywidth - trainfield.fieldwidth) / 16
                paramsMusorka.leftMargin = (displaywidth - trainfield.fieldwidth) / 16
                paramsMusorka.topMargin = displayheight - binding.baggageBtnTrain.height
                binding.musorkaSvg.layoutParams = paramsMusorka
            }

            val paramslnBtn = binding.lnBaggageTrain.layoutParams as LinearLayout.LayoutParams
            paramslnBtn.height = binding.baggageBtnTrain.height
            binding.lnBaggageTrain.layoutParams = paramslnBtn


            val params = binding.skillsLeftPlayer1Train.layoutParams as LinearLayout.LayoutParams
            params.width = (displaywidth - trainfield.fieldwidth) / 2
            params.height = trainfield.fieldheight
            binding.skillsLeftPlayer1Train.layoutParams = params

            val params1 = binding.skillsRightTrain.layoutParams as LinearLayout.LayoutParams
            params1.width = (displaywidth - trainfield.fieldwidth) / 2
            params1.height = trainfield.fieldheight - binding.baggageBtnTrain.height
            binding.skillsRightTrain.layoutParams = params1


            val params2 = binding.fieldTrain.layoutParams as LinearLayout.LayoutParams
            params2.width = trainfield.fieldwidth
            params2.height = trainfield.fieldheight
            binding.fieldTrain.layoutParams = params2


            dataGames.BlockOutBaggage.observe(activity as LifecycleOwner, {
                trainfield.baggaeBlock.removeLast()
                val bl = Block(binding.rlPoolTrain, field1Centerwidth, field1Centerheight, 50, it)
                trainfield.blocks.add(bl)
            })

            binding.baggageBtnTrain.setOnClickListener {
                sendHint(8)
                trainfield.baggaeBlock.clear()
                trainfield.baggaeBlock.addAll(trainfield.newblockInBaggage)
                trainfield.newblockInBaggage.clear()
                if (dataGames.BlockInBaggage1.value != null) {
                    trainfield.baggaeBlock.addAll(dataGames.BlockInBaggage1.value!!)
                    if (dataGames.BlockInBaggage2.value != null) {
                        trainfield.baggaeBlock.addAll(dataGames.BlockInBaggage2.value!!)
                    }
                }
                if (trainfield.baggaeBlock.size < 7) {
                    if (trainfield.baggaeBlock.size - 3 > 0) {
                        var i = 0
                        val baggage1 = arrayListOf<Block>()
                        trainfield.baggaeBlock.forEach {
                            if (i < 3) {
                                baggage1.add(it)
                                i++
                            }
                        }
                        val baggage2 = arrayListOf<Block>()
                        for (index in 3 until trainfield.baggaeBlock.size) {
                            baggage2.add(trainfield.baggaeBlock[index])
                        }
                        dataGames.BlockInBaggage1.value = baggage1
                        dataGames.BlockInBaggage2.value = baggage2
                    } else {
                        val baggage3 = arrayListOf<Block>()
                        baggage3.addAll(trainfield.baggaeBlock)
                        dataGames.BlockInBaggage1.value = baggage3
                        dataGames.BlockInBaggage2.value = null
                    }
                }
                openFrag(BaggageFragment.newInstance(), binding.baggageTrain.id)
                binding.baggageTrain.visibility = View.VISIBLE
                it.isEnabled = false
            }




            sizeBaggage = trainfield.blocksPool.height / 2
            binding.baggageTrain.layoutParams.height = sizeBaggage


            dataGames.LifeBaggage.observe(activity as LifecycleOwner, {
                sendHint(6)
                binding.baggageTrain.visibility = View.GONE
                binding.baggageBtnTrain.isEnabled = true
            })
        }


        trainfield.actionPlay.observe(activity as LifecycleOwner, {
            sendHint(it)
        })
        trainfield.blocksPool.actionPool.observe(activity as LifecycleOwner, {
            if (it == 2) {
                sendHint(1)
            }
            if (it == 1) {
                sendHint(2)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.fraimTrain.removeAllViews()
        dataPlayMenu.activeMenu.value = true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendHint(number: Int) {
        //сделать action и чекать какая подсказка должна быть в будущем ну еще бы как-то оограничить движения в обучении

        var txtSize = 0
            when (number) {
                1 -> {
                    if (action == 1 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=2
                        binding.gameHint.text = hintsString[0]
                        txtSize = hintsString[0].length
                    }
                }
                2 -> {
                    if (action == 2 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=3
                        binding.gameHint.text = hintsString[1]
                        txtSize = hintsString[1].length
                    }
                }
                3 -> {
                    if (action == 3 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=4
                        binding.gameHint.text = hintsString[2]
                        txtSize = hintsString[2].length
                    }
                }
                4 -> {
                    if( action==4 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=5
                        binding.gameHint.text = hintsString[3]
                        txtSize = hintsString[3].length
                    }
                }
                5 -> {
                    if( action==5 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=8
                        binding.gameHint.text = hintsString[4]
                        txtSize = hintsString[4].length
                    }
                }
                6 -> {
                    if( action==6 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=9
                        binding.gameHint.text = hintsString[5]
                        txtSize = hintsString[5].length
                    }
                }
                7 -> {
                    if( action==7 && flagAction[number-1]) {
                        ///победа
                        flagAction[number-1]=(false)
                        action=1
                        //binding.gameHint.visibility=View.GONE


                        //неплохое решение тут отрисовка поля от пула идет можно везде так сделать
                        var fieldEnemy= Field(binding.gameTrain,10,trainfield.cellsSize,displaywidth / 2,trainfield.blocksPool.posY/2,trainfield.blocksPool.posY,trainfield.fieldwidth)
                        trainfield.blocksPool.destroy()

                        binding.gameHint.text=""
                        val params1=binding.gameHint.layoutParams as RelativeLayout.LayoutParams
                        params1.topMargin=trainfield.blocksPool.posY
                        params1.width=displaywidth
                        params1.height=trainfield.blocksPool.height
                        binding.gameHint.layoutParams=params1
                        binding.gameHint.text=Constance.HINT_RULS_1
                        hintsRules()
                        return
                    }
                }
                8 -> {
                    if( action==8 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=6
                        binding.gameHint.text = hintsString[7]
                        txtSize = hintsString[7].length
                    }
                }
                9 -> {
                    if( action==9 && flagAction[number-1]) {
                        flagAction[number-1]=(false)
                        action=7
                        binding.gameHint.text = hintsString[6]
                        txtSize = hintsString[6].length
                    }
                }
                else -> txtSize = 50
            }

        val params = binding.gameHint.layoutParams as RelativeLayout.LayoutParams
            if (txtSize >= 40) {
                params.height = displayheight / 5
                params.width = displaywidth / 1.5.toInt()
            } else {
                params.height = displayheight / 4
                params.width = displaywidth / 2
            }

        //логи для понимамания будущего  и прошлого действия
        Log.d("Block","$action  + $number")
        binding.gameHint.layoutParams = params
    }

    private fun openFrag(f: Fragment, idHolder: Int) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)?.add(idHolder, f)?.commit()
        if (f.isVisible()) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(idHolder, f)?.commit()
        } else {

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hintsRules(){
        visibleSkills()
        binding.baggageBtnTrain.setOnClickListener(null)
        binding.gameTrain.setOnTouchListener(null)
        binding.gameTrain.setOnClickListener {
            if(action==hintsRulsString.size){
                onDestroy()
            }
            else {
                binding.gameHint.text = hintsRulsString[action]
                action++
            }
        }
    }

    private fun visibleSkills(){
        binding.treeTrain.visibility=View.VISIBLE
        binding.treeTrain2.visibility=View.VISIBLE
        binding.waterTrain.visibility=View.VISIBLE
        binding.waterTrain2.visibility=View.VISIBLE
        binding.fireTrain.visibility=View.VISIBLE
        binding.fireTrain2.visibility=View.VISIBLE
        binding.tumanTrain.visibility=View.VISIBLE
        binding.tumanTrain2.visibility=View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() = Train_game()
    }

}