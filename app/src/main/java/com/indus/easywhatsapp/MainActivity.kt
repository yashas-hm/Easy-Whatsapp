package com.indus.easywhatsapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var buttonGo: FloatingActionButton
    private lateinit var editText: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup(){
        initUI()
        listener()
    }

    private fun initUI(){
        buttonGo = findViewById(R.id.add)
        editText = findViewById(R.id.numberAdder)
    }

    private fun listener(){
        buttonGo.setOnClickListener{
            if(editText.isFocused){
                if(!editText.text.isNullOrEmpty()){
                    if(editText.text!!.length<10){
                        editText.error = "Number should be 10 digits"
                    }else{
                        openWhatsAppChat(editText.text.toString())
                    }
                }
            }else{
                editText.requestFocus()
            }
        }
    }

    private fun openWhatsAppChat(@NonNull phoneNumber: String) {
        if (phoneNumber.isEmpty()) return

        if(isAppInstalled()) {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.action = Intent.ACTION_VIEW
            sendIntent.setPackage("com.whatsapp")
            val url =
                    "https://api.whatsapp.com/send?phone=$phoneNumber&text= "
            sendIntent.data = Uri.parse(url)
            startActivity(sendIntent)
        } else {
            Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_LONG).show()
        }
    }

    private fun isAppInstalled(): Boolean {
        val pm: PackageManager = packageManager
        val isInstalled: Boolean
        isInstalled = try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return isInstalled
    }
}