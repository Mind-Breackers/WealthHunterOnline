package com.example.crash.basic_menu.Achievements

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.databinding.FragmentAchievementsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Achievements : Fragment(), SeekBar.OnSeekBarChangeListener {
    lateinit var binding: FragmentAchievementsBinding
    lateinit var auth: FirebaseAuth
    private val dataPlayMenu: DataPlayMenu by activityViewModels()
    private val imgAchivementList = listOf(
        R.drawable.cup1,
        R.drawable.cup2,
        R.drawable.cup3
    )
    private var index = 0
    private var countAchievements=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentAchievementsBinding.inflate(inflater)
        binding.login.text=dataPlayMenu.nameP.value
        val currentVolume: Int? =  dataPlayMenu.managerSound.value?.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (currentVolume != null) {
            binding.seekBar.progress=(currentVolume.toDouble()/ dataPlayMenu.managerSound.value?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)!!.toDouble()*binding.seekBar.max.toDouble()).toInt()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentVolume: Int? =  dataPlayMenu.managerSound.value?.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (currentVolume != null) {
            binding.seekBar.progress=(currentVolume.toDouble()/ dataPlayMenu.managerSound.value?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)!!.toDouble()*binding.seekBar.max.toDouble()).toInt()
        }
        auth = Firebase.auth
        binding.bOut.setOnClickListener {
            auth.signOut()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)
            dataPlayMenu.activeMenu.value=false
        }

        dataPlayMenu.SeekBar.observe(activity as LifecycleOwner,{
            when(it){
                1->{
                    binding.seekBar.progress = if (binding.seekBar.progress + 1 > binding.seekBar.max) binding.seekBar.max else binding.seekBar.getProgress() + 1
                }
                -1->{
                    binding.seekBar.progress = if (binding.seekBar.progress - 1 < 0) 0 else binding.seekBar.progress - 1
                }
                0->{
                    binding.seekBar.progress=0
                }
            }
        })


        binding.seekBar.setOnSeekBarChangeListener(this)
    }



    companion object {
        @JvmStatic
        fun newInstance() = Achievements()
    }

    override fun onStart() {
        super.onStart()
        val currentVolume: Int? =  dataPlayMenu.managerSound.value?.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (currentVolume != null) {
            binding.seekBar.progress=(currentVolume.toDouble()/ dataPlayMenu.managerSound.value?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)!!.toDouble()*binding.seekBar.max.toDouble()).toInt()
            upgradeImgSound(binding.seekBar.progress)
        }
    }


    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

        val maxVolume: Int? =  dataPlayMenu.managerSound.value?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val percent: Double = (p1.toDouble())/(100.0)
        val seventyVolume = (maxVolume!! * percent).toInt()
        dataPlayMenu.managerSound.value?.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, AudioManager.FLAG_SHOW_UI)
        upgradeImgSound(p1)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }


    fun upgradeImgSound(sound:Int){
        if(sound<10){
            binding.sound.setImageResource(R.drawable.ic_sound_mute)
        }
        if(sound in 11..50){
            binding.sound.setImageResource(R.drawable.ic_sound_min)
        }
        if(sound in 51..100){
            binding.sound.setImageResource(R.drawable.ic_sound_max)
        }
    }
}