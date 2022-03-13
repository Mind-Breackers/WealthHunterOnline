package com.example.crash.basic_menu.Achievements

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crash.databinding.FigursBinding

class AchivementAdapter(private val onClickListiner: AchivmentClickListiner) : RecyclerView.Adapter<AchivementAdapter.AchivementHolder>(),View.OnClickListener {

    lateinit var binding: FigursBinding
    val figursList = ArrayList<Achivement_Item>()

    class AchivementHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val binding =FigursBinding.bind(itemView)
            fun bind(achievement: Achivement_Item)= with(binding){
                    imfigure.setImageResource(achievement.img)
                    namesScore.text="${achievement.name}"
            }

    }


    override fun onClick(v: View) {
        val cup = v.tag as Achivement_Item
        Log.d("Field1"," $cup")
        onClickListiner.onClick(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchivementHolder {
        val view=LayoutInflater.from(parent.context)
        binding=FigursBinding.inflate(view,parent,false)

        binding.achivmentlr.setOnClickListener(this)

        return AchivementHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AchivementHolder, position: Int) {
       val cup=figursList[position]
        holder.binding.achivmentlr.tag=cup


        holder.bind(figursList[position])
    }

    override fun getItemCount(): Int {
        return figursList.size
    }


    fun addAchivement(achievement: Achivement_Item){
        figursList.add(achievement)
        notifyDataSetChanged()
    }

}