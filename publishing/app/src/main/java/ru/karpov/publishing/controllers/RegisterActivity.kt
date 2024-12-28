package com.example.publishinghousekotlin.controllers


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivityRegisterBinding
import com.example.publishinghousekotlin.http.requests.RegisterRequest
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity для регистрации пользователя
 * @author Денис
 * @since 1.0.0
 */
class RegisterActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var activityRegisterBinding: ActivityRegisterBinding


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setListeners()

        activityRegisterBinding.registerBtn.setOnClickListener {
            register()
        }

    }

    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners(){
        val listener = Listener()

        listener.nameListener(activityRegisterBinding.nameText, activityRegisterBinding.nameContainer)
        listener.phoneListener(activityRegisterBinding.phoneText, activityRegisterBinding.phoneContainer)
        listener.emailListener(activityRegisterBinding.emailText, activityRegisterBinding.emailContainer)
        listener.passwordAndConfirmPasswordListener(activityRegisterBinding.passwordText, activityRegisterBinding.confirmPasswordText, activityRegisterBinding.passwordContainer, activityRegisterBinding.confirmPasswordContainer)
        listener.confirmPasswordListener(activityRegisterBinding.passwordText,activityRegisterBinding.confirmPasswordText, activityRegisterBinding.confirmPasswordContainer)
    }


    /**
     * Переход в прошлую активность
     * @return завершать ли текущую активность
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Метод регистрации пользователя
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun register(){
        val message = Messages()

        if(activityRegisterBinding.nameContainer.helperText == null && activityRegisterBinding.phoneContainer.helperText == null && activityRegisterBinding.emailContainer.helperText == null &&
            activityRegisterBinding.passwordContainer.helperText == null && activityRegisterBinding.confirmPasswordContainer.helperText == null){

            val name = activityRegisterBinding.nameText.text.toString().trim()
            val phone = activityRegisterBinding.phoneText.text.toString().trim()
            val email = activityRegisterBinding.emailText.text.toString().trim()
            val password = activityRegisterBinding.passwordText.text.toString().trim()

            val registerRequest = RegisterRequest(name, phone, email,password, "CUSTOMER")
            var messageResponse: MessageResponse? = null

            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    delay(1000)
                    withContext(Dispatchers.Main){
                        activityRegisterBinding.progressBar.visibility = View.VISIBLE
                    }

                    messageResponse = UserRepository().register(registerRequest)

                } catch (e:Exception){
                    message.showError("Ошибка регистрации. Повторите попытку",activityRegisterBinding.root)
                }
                runOnUiThread{
                    activityRegisterBinding.progressBar.visibility = View.INVISIBLE
                }

                if(messageResponse != null){
                    if(messageResponse!!.code == 400){
                        message.showError(messageResponse!!.message, activityRegisterBinding.root)
                    }else{
                        message.showSuccess(messageResponse!!.message, activityRegisterBinding.root)

                        delay(1000)
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }else{
            message.showError("Некорректный ввод. Повторите попытку.", activityRegisterBinding.root)
        }
    }

}