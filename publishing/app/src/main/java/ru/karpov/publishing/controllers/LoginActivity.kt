package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivityLoginBinding
import com.example.publishinghousekotlin.http.requests.LoginRequest
import com.example.publishinghousekotlin.http.responses.JwtResponse
import com.example.publishinghousekotlin.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



/**
 * Activity для авторизации пользователя
 * @author Денис
 * @since 1.0.0
 */
class LoginActivity: AppCompatActivity() {


    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var activityLoginBinding: ActivityLoginBinding


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        goToMainScreen()

        setListeners()
        activityLoginBinding.loginBtn.setOnClickListener {
            login()
        }

        activityLoginBinding.registerTextView.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners(){
        val listener = Listener()

        listener.emailListener(activityLoginBinding.emailText, activityLoginBinding.emailContainer)
        listener.passwordListener(activityLoginBinding.passwordText, activityLoginBinding.passwordContainer)
    }


    /**
     * Метод авторизации пользователя
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun login(){
        val message = Messages()

        if(activityLoginBinding.emailContainer.helperText == null && activityLoginBinding.passwordContainer.helperText == null){

            val email = activityLoginBinding.emailText.text.toString().trim()
            val password = activityLoginBinding.passwordText.text.toString().trim()

            val loginRequest = LoginRequest(email, password)
            var jwtResponse: JwtResponse?
            var unauthorizedAccess = true

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    delay(1000)
                    withContext(Dispatchers.Main){
                        activityLoginBinding.progressBar.visibility = View.VISIBLE
                    }

                    jwtResponse = UserRepository().login(loginRequest)

                    if(jwtResponse != null){
                        JwtResponse.saveToMemory(applicationContext, jwtResponse!!, applicationContext.resources.getString(R.string.keyForJwtResponse))
                    }

                } catch(e: Exception){
                    message.showError("Ошибка авторизации. Повторите попытку",activityLoginBinding.root)
                    unauthorizedAccess = false
                }
                runOnUiThread{
                    activityLoginBinding.progressBar.visibility = View.GONE
                }

                goToMainScreen()

                if(unauthorizedAccess){
                    message.showError("Ошибка авторизации. Неверный логин или пароль",activityLoginBinding.root)
                }
            }

        } else{
            message.showError("Некорректный ввод. Повторите попытку.",activityLoginBinding.root)
        }
    }



    /**
     * Метод перехода на главный экран приложения
     */
    private fun goToMainScreen(){
        if(JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(R.string.keyForJwtResponse)) != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

}