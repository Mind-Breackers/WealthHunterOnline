package com.example.crash.games.Game_Play

import android.R.attr.button
import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.Window
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.Block
import com.example.crash.games.Field
import com.example.crash.games.Pool


class Games : AppCompatActivity() {
    lateinit var bindingclass: Games2Binding
    lateinit var root: RelativeLayout
    lateinit var FieldPlayer1: Field
    private val dataPlayMenu: DataPlayMenu by viewModels()
    val blocks = ArrayList<Block>()
    var downX = 0
    private var size = 0
    private var sizeBaggage = 0

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingclass = Games2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)

        val displaymetrics = resources.displayMetrics
        val displayheight = displaymetrics.heightPixels
        val displaywidth = displaymetrics.widthPixels
        root = bindingclass.rlPool
        size = intent.getIntExtra("difficult", 0) * 10*4

        val blocksPool = Pool(root, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth)
        val fieldheight = (displayheight - blocksPool.height) / 2 + 50
        val fieldwidth = displaywidth - (displaywidth / 6) * 2
        val game = GameClass(bindingclass.rlGames, displayheight, displaywidth,bindingclass.rlPool,bindingclass.Player1,bindingclass.Player2,size)




        bindingclass.Player1.post {
            val params = bindingclass.skillsLeftPlayer1.layoutParams as LinearLayout.LayoutParams
            params.width = (displaywidth - fieldwidth) / 2
            params.height = fieldheight
            bindingclass.skillsLeftPlayer1.layoutParams = params

            val params1 = bindingclass.skillsRight.layoutParams as LinearLayout.LayoutParams
            params1.width = (displaywidth - fieldwidth) / 2
            params1.height = fieldheight - bindingclass.baggageBtn.height
            bindingclass.skillsRight.layoutParams = params1


            val params2 = bindingclass.field.layoutParams as LinearLayout.LayoutParams
            params2.width = fieldwidth
            params2.height = fieldheight
            bindingclass.field.layoutParams = params2
        }


        sizeBaggage = blocksPool.height / 2
        bindingclass.Player1.post {
            bindingclass.baggage.layoutParams.height = sizeBaggage
        }

        bindingclass.baggageBtn.setOnClickListener {
            openFrag(Baggage_Fragment.newInstance(), bindingclass.baggage.id)
            bindingclass.baggage.visibility = View.VISIBLE
            it.isEnabled = false
        }

        dataPlayMenu.LifeBaggage.observe(this, {
            bindingclass.baggage.visibility = View.GONE
            bindingclass.baggageBtn.isEnabled = true
        })

    }

    private fun openFrag(f: Fragment, idHolder: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out).add(idHolder, f).commit()
        if (f.isVisible()) {
            supportFragmentManager.beginTransaction().replace(idHolder, f).commit()
        } else {

        }

    }
}



