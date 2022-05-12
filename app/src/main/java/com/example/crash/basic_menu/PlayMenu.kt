package com.example.crash.basic_menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.crash.basic_menu.train_game.Train_game
import com.example.crash.databinding.FragmentPlayMenuBinding
import com.example.crash.games.ClassForGame.Field
import com.example.crash.games.ClassForGame.FieldPreview
import com.example.crash.games.Game_Play.Games
import com.example.crash.sigInUp.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PlayMenu : Fragment() {

    lateinit var binding:FragmentPlayMenuBinding
    private val dataPlayMenu:DataPlayMenu by activityViewModels()
    var difficult=1
    var flag_game_train=true
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayMenuBinding.inflate(inflater)
        binding.previeFields.post {
            val previewFields = FieldPreview(
                binding.previeFields,
                30,
                60,
                binding.previeFields.height / 2,
                binding.previeFields.width / 2,
                binding.previeFields.height,
                binding.previeFields.width
            )
            binding.bplay.width=binding.previeFields.width
        }
        binding.txtmenu.text="Mind Brakers"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding.bplay.setOnClickListener {
            if(flag_game_train) {
                val intentGames = Intent(activity, Games::class.java)
                intentGames.putExtra("difficult", difficult)
                startActivity(intentGames)
            }else{
               dataPlayMenu.activeTrain.value=true
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)
            }
        }

        binding.backbutton.visibility=View.INVISIBLE
        binding.bquestion.setOnClickListener{
            binding.textView.text="Пройти обучающий уровень"
            flag_game_train=false
            binding.txtmenu.text="Обучающий уровень"
            binding.backbutton.visibility=View.VISIBLE
            it.visibility=View.INVISIBLE
        }

        binding.backbutton.setOnClickListener{
            it.visibility=View.INVISIBLE
            binding.bquestion.visibility=View.VISIBLE
            binding.textView.text="Пошли в шахту?"
            binding.txtmenu.text="Mind Brakers"
            flag_game_train=true
        }

    }





    companion object {
        @JvmStatic
        fun newInstance() = PlayMenu()
    }
}