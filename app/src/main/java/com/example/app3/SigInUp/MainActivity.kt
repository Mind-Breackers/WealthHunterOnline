package com.example.app3.SigInUp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.app3.R
import com.example.app3.basic_menu.PersonalAccount
import com.example.app3.constance.Constance
import com.example.app3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var bindingclass: ActivityMainBinding
    private var login: String = "0"
    private var password: String = "0"
    private var name1: String = "rmpty"
    private var name2: String = "rmpty"
    private var name3: String = "rmpty"
    private var avatar_img_id: Int = 0
    private var sigIn: ActivityResultLauncher<Intent>? = null
    private var sigUp: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingclass.root)

        sigIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val l = it.data?.getStringExtra(Constance.LOGINE)
            val p = it.data?.getStringExtra(Constance.PASSWORD)
            if (login == l && password == p) {
                bindingclass.imgavatar.visibility = View.INVISIBLE
                bindingclass.tvibfo.text = ""
                intent = Intent(this, PersonalAccount::class.java)
                intent.putExtra("Img", avatar_img_id)
                intent.putExtra("Name", name1)
                startActivity(intent)
            } else {
                bindingclass.tvibfo.text = "Аккаунта нет"
                bindingclass.imgavatar.visibility = View.VISIBLE
                bindingclass.imgavatar.setImageResource(R.drawable.notaccount)
            }
        }

        sigUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            login = it.data?.getStringExtra(Constance.LOGINE)!!
            password = it.data?.getStringExtra(Constance.PASSWORD)!!
            name1 = it.data?.getStringExtra(Constance.NAME1)!!
            name2 = it.data?.getStringExtra(Constance.NAME2)!!
            name3 = it.data?.getStringExtra(Constance.NAME3)!!
            avatar_img_id = it.data?.getIntExtra(Constance.AVATAR_ID, R.drawable.boy1)!!

            bindingclass.imgavatar.visibility = View.INVISIBLE
            val textinfo = "Аккаунт успешно создан"
            bindingclass.tvibfo.text = textinfo
        }
    }


    fun onClicksigin(view: View) {
        val intent = Intent(this, SignInUp::class.java)
        intent.putExtra(Constance.SIGN_STATE, Constance.SIG_IN_STATE)
        sigIn?.launch(intent)

    }

    fun onClicksigup(view: View) {
        val intent = Intent(this, SignInUp::class.java)
        intent.putExtra(Constance.SIGN_STATE, Constance.SIG_UP_STATE)
        sigUp?.launch(intent)
    }


}
