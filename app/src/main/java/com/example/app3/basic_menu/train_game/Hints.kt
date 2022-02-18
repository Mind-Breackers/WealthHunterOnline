package com.example.app3.basic_menu.train_game

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.app3.basic_menu.DataPlayMenu
import com.example.app3.databinding.FragmentHintsBinding


class Hints : Fragment() {
    lateinit var binding:FragmentHintsBinding
    val dataPlayMenu : DataPlayMenu by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHintsBinding.inflate(inflater)
        binding.blurLa.blurColor(Color.BLACK)
        binding.blurLa.blurOpacity(0.3f)
        dataPlayMenu.TrainScrinshot.value?.let { binding.blurLa.setBitmapBlur(it,1,10) }
        binding.txtHint1.movementMethod = ScrollingMovementMethod();
        binding.txtHint2.movementMethod = ScrollingMovementMethod();
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.hint1.setOnClickListener {
            it.visibility=View.GONE
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        binding.hint2.setOnClickListener {
            it.visibility=View.GONE
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        dataPlayMenu.HintString.observe(activity as LifecycleOwner,{
            if(dataPlayMenu.RightLeft.value==0){
                binding.txtHint1.text=it
                binding.hint1.visibility=View.VISIBLE

            }
            if(dataPlayMenu.RightLeft.value==1){
                binding.txtHint2.text=it
                binding.hint2.visibility=View.VISIBLE
            }
            if(dataPlayMenu.RightLeft.value==2){
                binding.winTxt.text=it
                binding.winLn.visibility=View.VISIBLE
                binding.winBtn.setOnClickListener {
                    activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        dataPlayMenu.activeHints.value=true
    }

    companion object {
        @JvmStatic
        fun newInstance() = Hints()
    }
}