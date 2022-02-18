package com.example.app3.basic_menu

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.app3.R
import com.example.app3.databinding.FragmentPlayMenuBinding
import com.example.app3.games.Field
import com.example.app3.games.Games

class PlayMenu : Fragment() {

    lateinit var binding:FragmentPlayMenuBinding
    private val dataPlayMenu:DataPlayMenu by activityViewModels()
    var difficult=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            var previewFields=Field(binding.previeFields,10,binding.lnFields.width/2,binding.lnFields.height/2,40)
            difficult=1
        }
        binding.butNormal.setOnClickListener {
            Log.d("Log1","${binding.lnFields.width}")
            binding.previeFields.removeAllViews()
            var previewFields=Field(binding.previeFields,20,binding.lnFields.width/2,binding.lnFields.height/2,40)
            difficult=2
        }
        binding.btHard.setOnClickListener {
            binding.previeFields.removeAllViews()
            var previewFields=Field(binding.previeFields,30,binding.lnFields.width/2,binding.lnFields.height/2,40)
            difficult=3
        }
    }





    companion object {
        @JvmStatic
        fun newInstance() = PlayMenu()
    }
}