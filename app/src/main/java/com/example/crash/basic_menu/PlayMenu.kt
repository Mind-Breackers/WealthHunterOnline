package com.example.crash.basic_menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.crash.databinding.FragmentPlayMenuBinding
import com.example.crash.games.Game_Play.Games

class PlayMenu : Fragment() {

    lateinit var binding:FragmentPlayMenuBinding
    private val dataPlayMenu:DataPlayMenu by activityViewModels()
    var difficult=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayMenuBinding.inflate(inflater)
        dataPlayMenu.imId.value?.let { binding.Avatar.setImageResource(it) }
        binding.Name.text = dataPlayMenu.nameP.value


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bplay.setOnClickListener {
            val intentGames= Intent(activity , Games::class.java)
            intentGames.putExtra("difficult",difficult)
            startActivity(intentGames);
        }

        binding.butEazy.setOnClickListener {
            binding.previeFields.removeAllViews()
            Log.d("Log1","${binding.lnFields.width}")
          /*  var previewFields=Field(binding.previeFields,10,40,binding.lnFields.width,
                (binding.lnFields.height*1.1).toInt()
            )*/
            difficult=1
        }
        binding.butNormal.setOnClickListener {
            Log.d("Log1","${binding.lnFields.width}")
            binding.previeFields.removeAllViews()
            /*var previewFields=Field(binding.previeFields,20,40,binding.lnFields.width, (binding.lnFields.height*1.1).toInt())*/
            difficult=2
        }
        binding.btHard.setOnClickListener {
            binding.previeFields.removeAllViews()
          // var previewFields=Field(binding.previeFields,30,40,binding.lnFields.width, (binding.lnFields.height*1.1).toInt())
            difficult=3
        }
    }





    companion object {
        @JvmStatic
        fun newInstance() = PlayMenu()
    }
}