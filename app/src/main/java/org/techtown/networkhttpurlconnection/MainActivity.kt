package org.techtown.networkhttpurlconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.networkhttpurlconnection.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonRequest.setOnClickListener { 
            CoroutineScope(Dispatchers.IO).launch{
                try{
                var urlText = binding.editUrl.text.toString()
                if(!urlText.startsWith("https")){ //주소 입력필드에서 입력된 주소를 가져와 https로 시작하지 않으면
                    //  https://를 앞에 붙여줍니다
                    urlText = "https://${urlText}"
                }
                val url = URL(urlText)
                val urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                if(urlConnection.responseCode == HttpURLConnection.HTTP_OK){
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)
                    val content = StringBuilder()
                    while(true){
                        val line = buffered.readLine()?:break
                        content.append(line)
                    }
                    buffered.close()
                    urlConnection.disconnect()
                    launch(Dispatchers.Main){
                        binding.textContent.text = content.toString()
                    }
                }
            }catch(e:Exception){
                e.printStackTrace()
                }
            }
        }
    }
}