package com.example.app3.SigInUp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.app3.R
import com.example.app3.constance.Constance
import com.example.app3.databinding.SignInUpBinding


class SignInUp : AppCompatActivity() {
    private var indexImg=-1
    private lateinit var bindingclass: SignInUpBinding
    private var signstate = ""
    private val imgList= arrayOf(R.drawable.boy1, R.drawable.boy2, R.drawable.girl)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = SignInUpBinding.inflate(layoutInflater)
        setContentView(bindingclass.root)
        
        signstate = intent.getStringExtra(Constance.SIGN_STATE)!!

        if (signstate == Constance.SIG_IN_STATE) {
            bindingclass.edName1.visibility = View.GONE
            bindingclass.edName2.visibility = View.GONE
            bindingclass.edName3.visibility = View.GONE
            bindingclass.btavatar.visibility = View.INVISIBLE
        }
    }


    fun onClickDone(view: View) {
        if (signstate == Constance.SIG_UP_STATE) {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constance.LOGINE, bindingclass.edLogin.text.toString())
            intent.putExtra(Constance.PASSWORD, bindingclass.edPassword.text.toString())
            intent.putExtra(Constance.NAME1, bindingclass.edName1.text.toString())
            intent.putExtra(Constance.NAME2, bindingclass.edName2.text.toString())
            intent.putExtra(Constance.NAME3, bindingclass.edName3.text.toString())
            if (bindingclass.imageView.isVisible)
                intent.putExtra(Constance.AVATAR_ID, imgList[indexImg])
            setResult(RESULT_OK, intent)
            finish()

        } else if (signstate == Constance.SIG_IN_STATE) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constance.LOGINE, bindingclass.edLogin.text.toString())
            intent.putExtra(Constance.PASSWORD, bindingclass.edPassword.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    fun onClickAvatar(view: View) {
        if(indexImg==2){
            indexImg=-1
        }
        bindingclass.imageView.setImageResource(imgList[++indexImg])
        bindingclass.imageView.visibility = View.VISIBLE
    }


}