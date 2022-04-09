package com.example.crash.SigInUp

// some string for test commit from   Sergey [delete this]

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.crash.R
import com.example.crash.SigInUp.Server.APIService
import com.example.crash.basic_menu.PersonalAccount
import com.example.crash.constance.Constance
import com.example.crash.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit


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
        rawJSON()


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

            if (it.resultCode== RESULT_OK) {
                login = it.data?.getStringExtra(Constance.LOGINE).toString()
                password = it.data?.getStringExtra(Constance.PASSWORD).toString()
                name1 = it.data?.getStringExtra(Constance.NAME1).toString()
                name2 = it.data?.getStringExtra(Constance.NAME2).toString()
                name3 = it.data?.getStringExtra(Constance.NAME3).toString()
                avatar_img_id = it.data?.getIntExtra(Constance.AVATAR_ID, R.drawable.boy1)?.toInt()
                    ?: R.drawable.boy1
                Log.d("SigIn", "$login ,$password")


                bindingclass.imgavatar.visibility = View.INVISIBLE
                val textinfo = "Аккаунт успешно создан"
                bindingclass.tvibfo.text = textinfo
            }else{
                bindingclass.imgavatar.visibility = View.INVISIBLE
                val textinfo = "Аккаунт не создан"
                bindingclass.tvibfo.text = textinfo
            }
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

    fun rawJSON() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.149.47.120/index.php/auth/index/")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", "tr")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.createEmployee(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }


}
