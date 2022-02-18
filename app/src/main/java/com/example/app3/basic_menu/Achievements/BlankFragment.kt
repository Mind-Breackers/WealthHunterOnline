package com.example.app3.basic_menu.Achievements

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.app3.basic_menu.DataPlayMenu
import com.example.app3.databinding.AchivmentInfoBinding

class BlankFragment : Fragment() {
    lateinit var binding: AchivmentInfoBinding
    private val dataPlayMenu: DataPlayMenu by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= AchivmentInfoBinding.inflate(inflater)
        binding.blurLayout.blurColor(Color.BLACK)
        binding.blurLayout.blurOpacity(0.7f)
        dataPlayMenu.AchivementScrinshot.value?.let { binding.blurLayout.setBitmapBlur(it,20,100) }
        dataPlayMenu.Cup.value?.let { binding.imgAchiv.setImageResource(it.img) }
        binding.nameAchiv.text=dataPlayMenu.Cup.value?.name
        binding.infoAchiv.text=dataPlayMenu.Cup.value?.score.toString()
        dataPlayMenu.active.value?.visibility=View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment()
    }
}