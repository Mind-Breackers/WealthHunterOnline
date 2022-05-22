
package com.example.crash.sigInUp.main


//import com.example.crash.SigInUp.Server.APIService


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.crash.R
import com.example.crash.basic_menu.PersonalAccount
import com.example.crash.databinding.SigInUp2Binding
import com.example.crash.sigInUp.Server.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


/*import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit*/


class MainActivity : AppCompatActivity() {

    lateinit var launcer: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    lateinit var authGuest: FirebaseAuth
    private lateinit var bindingclass: SigInUp2Binding
    private var login: String = "0"
    private var password: String = "0"
    private var email: String = "empty"
    private var flagInUp = true
    lateinit var user: User
    var mainuser: User?=null
    var flagGuest=false
    val database = Firebase.database
    private val userRef = database.getReference("user")
    private val sigRef = database.getReference("user")
    private val logRef = database.getReference("user")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = SigInUp2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)
        //rawJSON()
        bindingclass.googlebtn.setOnClickListener {
            auth=Firebase.auth
            sigGoogle()
        }

        listinerRegistration()


        launcer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { itTask ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(itTask.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        firebaseWithGoogle(account.idToken!!)
                    }
                } catch (e: ApiException) {
                    Log.d("SigIn", "${e.statusCode}")
                }
            }
    }

    override fun onStart() {
        super.onStart()
        flagGuest=intent.getBooleanExtra("Guest",false)
        if(intent.getParcelableExtra<User>("MainUser") !=null) {
            mainuser = intent.getParcelableExtra<User>("MainUser")!!
        }

        authGuest=Firebase.auth
        auth=Firebase.auth




        bindingclass.loginInput.text.clear()
        bindingclass.passwordInput.text.clear()

        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("912809805644-ott7anaovkhh4nt4l08nlglop71qb7ja.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun sigGoogle() {
        val client = getClient()
        launcer.launch(client.signInIntent)
    }

    private fun firebaseWithGoogle(idToken: String) {
        val cridential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(cridential).addOnCompleteListener {
            if (it.isSuccessful) {
                val x = auth.currentUser
                user = User(auth.currentUser?.displayName.toString(),uid=x!!.uid.toString())
                userRef.child(x!!.uid).setValue(user)
                goPersonalAcoount(user)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun btnSigIn(view: View) {
        //sigIN
        if (flagInUp) {
            if (checkText()) {
                val e = bindingclass.loginInput.text.toString()
                val p = bindingclass.passwordInput.text.toString()
                verification(sigRef, e, p)
            }
            Log.d("Login","2")
        }else{
            if (checkText()) {
                login = bindingclass.loginInput.text.toString()
                password = bindingclass.passwordInput.text.toString()
                email = bindingclass.emailInput.text.toString()
                ChekLogPasEmail()
                Log.d("Login", "111111111111111111111111111111111111111111")
            }
        }
    }




    fun ChekLogPasEmail() {
        var flag=true
        if (bindingclass.passwordInput.text.toString().length < 6) {
            bindingclass.dopInfoSigin.text = "Пароль слишком легкий"
            bindingclass.dopInfoSigin.visibility = View.VISIBLE
            flag=false
        }
        if (!isEmailValid(bindingclass.emailInput.text.toString())) {
            bindingclass.dopInfoSigin.text = "Email введен не правильно"
            bindingclass.dopInfoSigin.visibility = View.VISIBLE
            flag=false
        }
        if(flag){
        loginSearch(logRef,  bindingclass.loginInput.text.toString())
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun loginSearch(ref: DatabaseReference, login: String) {
        var flagLog=true
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {
                    if (login == it.child("login").value && password !=it.child("password").value) {
                      flagLog=false

                        bindingclass.dopInfoSigin.text = "Логин занят"
                        bindingclass.dopInfoSigin.visibility = View.VISIBLE
                    }
                }
                if(flagLog){
                   creatUser()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    fun creatUser(){
        auth=Firebase.auth
        auth.signOut()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        user = User(login,uid=auth.uid!!)
                        userRef.child(auth.uid!!).setValue(user)
                    }
                }
                        bindingclass.emailInput.visibility = View.GONE
                        bindingclass.emailTxt.visibility = View.GONE
                        bindingclass.dopfunction.visibility = View.VISIBLE

                        bindingclass.loginInput.text=bindingclass.emailInput.text

                        bindingclass.loginTxt.text="Почта"
                        bindingclass.dopInfoSigin.text = "Аккаунта создан"
                        bindingclass.btnSigIn.text = "Войти"
                        bindingclass.helloText.text = getString(R.string.hello_text_sigin)
                        bindingclass.regClick.text = getString(R.string.reg_click_sigin)
                        bindingclass.NoAccTxt.text = getString(R.string.No_acc_sigin)
                        bindingclass.dopInfoSigin.visibility = View.VISIBLE

                        bindingclass.emailInput.text.clear()
                flagInUp=true

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkText(): Boolean {
        var flag = true
        if (flagInUp) {
            if (bindingclass.loginInput.text.toString() == "") {
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHint,
                        theme
                    )
                )
                flag = false
            } else {
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
            if (bindingclass.passwordInput.text.toString() == "") {
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHint,
                        theme
                    )
                )
                flag = false
            } else {
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
        }
        if (!flagInUp) {
            if (bindingclass.loginInput.text.toString() == "") {
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHint,
                        theme
                    )
                )
                flag = false
            } else {
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
            if (bindingclass.passwordInput.text.toString() == "") {
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHint,
                        theme
                    )
                )
                flag = false
            } else {
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
            if (bindingclass.emailInput.text.toString() == "") {
                bindingclass.emailInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHint,
                        theme
                    )
                )
                flag = false
            } else {
                bindingclass.emailInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
        }
        return flag
    }


    private fun verification(ref: DatabaseReference, email: String, password: String) {
        auth=Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if(it.isSuccessful) {
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val login = snapshot.child(auth.uid!!).child("login").value as String
                        val rating = snapshot.child(auth.uid!!).child("rating").value as Long
                        val user = User(login, rating.toInt(), uid = auth.uid!!)
                        goPersonalAcoount(user)
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
            else{
                bindingclass.dopInfoSigin.text = "Аккаунта нет"
                bindingclass.loginInput.text.clear()
                bindingclass.passwordInput.text.clear()
            }
        }
    }


    fun goPersonalAcoount(user: User) {
            val intent = Intent(this, PersonalAccount::class.java)
            intent.putExtra("Guest1", flagGuest)
        if(authGuest.currentUser!=null) {
            if(flagGuest) {
                auth.signOut()
            }
            if(mainuser !=null) {
                if (mainuser?.uid != user.uid) {
                    Log.d("Guest","4")
                    intent.putExtra("User", mainuser)
                    intent.putExtra("Guest", user)
                } else {
                    Log.d("Guest","3")
                    intent.putExtra("User", mainuser)
                    val usernull = User("Guest", 0, null)
                    intent.putExtra("Guest", usernull)
                }
            }else{
                Log.d("Guest","2")
                intent.putExtra("User", user)
                val usernull = User("Guest", 0, null)
                intent.putExtra("Guest", usernull)
            }
        }
        else {
            Log.d("Guest","1")
            val usernull=User("Guest",0,null)
            intent.putExtra("User", user)
            intent.putExtra("Guest", usernull)
        }
            startActivity(intent)
            finish()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun listinerRegistration(){
        bindingclass.regClick.setOnClickListener {
            if (flagInUp) {
                flagInUp = false
                bindingclass.emailInput.visibility = View.VISIBLE
                bindingclass.emailTxt.visibility = View.VISIBLE
                bindingclass.dopfunction.visibility = View.GONE
                bindingclass.dopInfoSigin.visibility = View.GONE
                bindingclass.loginTxt.text="Логин"
                bindingclass.helloText.text = "Регистрация"
                bindingclass.btnSigIn.text = "Зарегистрироваться"
                bindingclass.regClick.text = "Войти"
                bindingclass.NoAccTxt.text = "Уже есть аккаунт?"
                bindingclass.loginInput.text.clear()
                bindingclass.passwordInput.text.clear()
                bindingclass.emailInput.text.clear()
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
                bindingclass.emailInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            } else {
                flagInUp = true
                bindingclass.helloText.text = getString(R.string.hello_text_sigin)
                bindingclass.regClick.text = getString(R.string.reg_click_sigin)
                bindingclass.NoAccTxt.text = getString(R.string.No_acc_sigin)
                bindingclass.emailInput.visibility = View.GONE
                bindingclass.emailTxt.visibility = View.GONE
                bindingclass.dopfunction.visibility = View.VISIBLE
                bindingclass.dopInfoSigin.visibility = View.VISIBLE
                bindingclass.loginTxt.text="Почта"
                bindingclass.btnSigIn.text = "Войти"
                bindingclass.loginInput.text.clear()
                bindingclass.passwordInput.text.clear()
                bindingclass.loginInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
                bindingclass.passwordInput.setHintTextColor(
                    resources.getColor(
                        R.color.textColorHintTrue,
                        theme
                    )
                )
            }
        }
    }


    /*fun rawJSON() {

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
    }*/


}
