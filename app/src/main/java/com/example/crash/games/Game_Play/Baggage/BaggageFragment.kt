package com.example.crash.games.Game_Play.Baggage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.crash.databinding.FragmentBaggageBinding
import com.example.crash.games.ClassForGame.Block
import com.example.crash.games.Game_Play.DataGames

class BaggageFragment : Fragment() {

    lateinit var baggageBinding: FragmentBaggageBinding
    var lastX = 0
    var lastY = 0
    var downX = 0
    var sizeBaggage = 0
    var downY = 0
    private var blocks = arrayListOf<Block>()
    private val dataGames: DataGames by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baggageBinding = FragmentBaggageBinding.inflate(inflater)
        baggageBinding.fragmentAdapter.adapter = BaggagePagerAdapter(activity?.supportFragmentManager!!, this.lifecycle)
        return baggageBinding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

}


    override fun onDestroy() {
        super.onDestroy()
        dataGames.LifeBaggage.value=true
    }




    companion object {
        @JvmStatic
        fun newInstance() = BaggageFragment()
    }
}