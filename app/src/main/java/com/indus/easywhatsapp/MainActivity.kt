package com.indus.easywhatsapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var buttonGo: FloatingActionButton
    private lateinit var editText: AppCompatEditText
    private lateinit var countryText: AppCompatEditText

    val countryAreaCodes = arrayOf("93", "355", "213",
        "376", "244", "672", "54", "374", "297", "61", "43", "994", "973",
        "880", "375", "32", "501", "229", "975", "591", "387", "267", "55",
        "673", "359", "226", "95", "257", "855", "237", "1", "238", "236",
        "235", "56", "86", "61", "61", "57", "269", "242", "682", "506",
        "385", "53", "357", "420", "45", "253", "670", "593", "20", "503",
        "240", "291", "372", "251", "500", "298", "679", "358", "33",
        "689", "241", "220", "995", "49", "233", "350", "30", "299", "502",
        "224", "245", "592", "509", "504", "852", "36", "91", "62", "98",
        "964", "353", "44", "972", "39", "225", "1876", "81", "962", "7",
        "254", "686", "965", "996", "856", "371", "961", "266", "231",
        "218", "423", "370", "352", "853", "389", "261", "265", "60",
        "960", "223", "356", "692", "222", "230", "262", "52", "691",
        "373", "377", "976", "382", "212", "258", "264", "674", "977",
        "31", "687", "64", "505", "227", "234", "683", "850", "47", "968",
        "92", "680", "507", "675", "595", "51", "63", "870", "48", "351",
        "1", "974", "40", "7", "250", "590", "685", "378", "239", "966",
        "221", "381", "248", "232", "65", "421", "386", "677", "252", "27",
        "82", "34", "94", "290", "508", "249", "597", "268", "46", "41",
        "963", "886", "992", "255", "66", "228", "690", "676", "216", "90",
        "993", "688", "971", "256", "44", "380", "598", "1", "998", "678",
        "39", "58", "84", "681", "967", "260", "263")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup(){
        initUI()
        listener()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(){
        buttonGo = findViewById(R.id.add)
        editText = findViewById(R.id.numberAdder)
        countryText = findViewById(R.id.countryAdder)
        val code = getCountryZipCode()
        countryText.setText("+$code")
    }

    private fun getCountryZipCode(): String {
        val countryID :String
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        countryID = manager.simCountryIso.toUpperCase(Locale.getDefault())
        val countryName = resources.getStringArray(R.array.CountryCodes)
        for (i in countryName.indices) {
            val countryCode = countryName[i].split(",".toRegex()).toTypedArray()
            if (countryCode[1].trim() == countryID.trim()) {
                return countryCode[0]
            }
        }
        return '1'.toString()
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun listener(){
        buttonGo.setOnClickListener{
            if(editText.isFocused){
                if(!editText.text.isNullOrEmpty()) {
                    if (editText.text!!.length < 10) {
                        editText.error = "Number should be 10 digits"
                        showMessage(this, "Number should be 10 digits")
                    } else if (!countryAreaCodes.contains(countryText.text.toString().drop(1))){
                        showMessage(this, "Invalid Country Code")
                    }
                    else{
                        val str = countryText.text.toString()
                        if(str[0]=='+'){
                            openWhatsAppChat(editText.text.toString(), str.drop(1))
                        }else{
                            openWhatsAppChat(editText.text.toString(), str)
                        }
                    }
                }
            }else{
                editText.requestFocus()
            }
        }
    }

    private fun openWhatsAppChat(@NonNull phoneNumber: String, @NonNull countryCode: String) {
        if (phoneNumber.isEmpty()) return
        if (countryCode.isEmpty()) return

        if(isAppInstalled()) {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.action = Intent.ACTION_VIEW
            sendIntent.setPackage("com.whatsapp")
            val url =
                    "https://api.whatsapp.com/send?phone=$countryCode$phoneNumber&text= "
            sendIntent.data = Uri.parse(url)
            startActivity(sendIntent)
        } else {
            Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_LONG).show()
        }
    }

    private fun isAppInstalled(): Boolean {
        val pm: PackageManager = packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}