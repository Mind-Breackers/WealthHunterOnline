package com.example.crash.SigInUp


import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.crash.R
import com.example.crash.constance.Constance
import com.example.crash.databinding.SignInUpBinding


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


    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickDone(view: View) {
            if (signstate == Constance.SIG_UP_STATE) {
                if(bindingclass.edLogin.text.toString() =="" || bindingclass.edPassword.text.toString()==""
                    ||  bindingclass.edName1.text.toString()=="" || bindingclass.edName2.text.toString()=="" || bindingclass.edName3.text.toString()=="") {
                    txtEmpty()
                }
                else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(Constance.LOGINE, bindingclass.edLogin.text?.toString())
                    intent.putExtra(Constance.PASSWORD, bindingclass.edPassword.text?.toString())
                    intent.putExtra(Constance.NAME1, bindingclass.edName1.text?.toString())
                    intent.putExtra(Constance.NAME2, bindingclass.edName2.text?.toString())
                    intent.putExtra(Constance.NAME3, bindingclass.edName3.text?.toString())
                    if (bindingclass.imageView.isVisible)
                        intent.putExtra(Constance.AVATAR_ID, imgList[indexImg])
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else if (signstate == Constance.SIG_IN_STATE) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constance.LOGINE, bindingclass.edLogin.text.toString())
                intent.putExtra(Constance.PASSWORD, bindingclass.edPassword.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(bindingclass.edLogin.text ==null || bindingclass.edPassword.text==null ||  bindingclass.edName1.text==null || bindingclass.edName2.text==null || bindingclass.edName3.text==null){
                setResult(RESULT_CANCELED,intent)
        }
    }


    fun onClickAvatar(view: View) {
        if(indexImg==2){
            indexImg=-1
        }
        bindingclass.imageView.setImageResource(imgList[++indexImg])
        bindingclass.imageView.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun txtEmpty(){
        if(bindingclass.edLogin.text.toString() ==""){
            bindingclass.edLogin.setHintTextColor(resources.getColor(R.color.textColorHint,theme))
        }
        if(bindingclass.edPassword.text.toString() ==""){
            bindingclass.edPassword.setHintTextColor(resources.getColor(R.color.textColorHint,theme))
        }
        if(bindingclass.edName1.text.toString() ==""){
            bindingclass.edName1.setHintTextColor(resources.getColor(R.color.textColorHint,theme))
        }
        if(bindingclass.edName2.text.toString() ==""){
            bindingclass.edName2.setHintTextColor(resources.getColor(R.color.textColorHint,theme))
        }
        if(bindingclass.edName3.text.toString() ==""){
            bindingclass.edName3.setHintTextColor(resources.getColor(R.color.textColorHint,theme))
        }
    }
}