package com.example.app3.basic_menu.Achievements

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app3.R
import com.example.app3.basic_menu.DataPlayMenu
import com.example.app3.databinding.FragmentAchievementsBinding
import kotlin.random.Random


class Achievements : Fragment() {
    lateinit var binding: FragmentAchievementsBinding

    private val dataPlayMenu: DataPlayMenu by activityViewModels()
    private lateinit var adapter : AchivementAdapter
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
        adapter = AchivementAdapter(object : AchivmentClickListiner {
            override fun onClick(v: View) {
                binding.infoCup.removeAllViews()
                v.visibility=View.INVISIBLE
                dataPlayMenu.AchivementScrinshot.value= screenShot(binding.root)
                v.visibility=View.VISIBLE
                dataPlayMenu.Cup.value=v.tag as Achivement_Item
                activity?.supportFragmentManager?.beginTransaction()?.replace(binding.infoCup.id,
                    BlankFragment.newInstance()
                )?.commit()
                binding.infoCup.visibility = View.VISIBLE
                dataPlayMenu.active.value=binding.rcAchivment
            }})
        rcInit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.infoCup.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(BlankFragment.newInstance())
                ?.commit()

            binding.infoCup.visibility=View.GONE
            binding.rcAchivment.visibility=View.VISIBLE

        }
    }

    fun screenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun rcInit() {
        binding.rcAchivment.layoutManager = GridLayoutManager(activity, 4)
        binding.rcAchivment.adapter = adapter
        while(countAchievements<40){
            if (index > 2) {
                index = 0
            }
            val achivement = Achivement_Item(
                imgAchivementList[index],
                "Figurs ${countAchievements + 1}",
                Random.nextInt(100,1000)
            )
            adapter.addAchivement(achivement)
            index++
            countAchievements++
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() = Achievements()
    }
}