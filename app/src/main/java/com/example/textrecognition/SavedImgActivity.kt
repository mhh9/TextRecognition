package com.example.textrecognition

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.IOException

class SavedImgActivity : AppCompatActivity() {

    lateinit var selectImg: Button
    lateinit var recognizeImgBtn: ImageView
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    private val Selected_Picture = 200

    lateinit var inputImg: InputImage

    // 문서인식기
    private lateinit var textRecognizer: TextRecognizer


    var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null


    // Create a string that will change which we switch between selected image and recognize text
    private var show_img_or_text = "img"
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_img)


        intentActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                val data = it.data
                val imageUri = data?.data

                convertImagetoText(imageUri)
            }
        )

        imageView = findViewById(R.id.imgView)
        // 이미지 선택기능 추가
        selectImg = findViewById(R.id.selectImg)
        selectImg.setOnClickListener { chooseImg() }

        textRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        textView = findViewById(R.id.textView)

        // 텍스트뷰는 숨겨짐
        textView?.setVisibility(View.GONE)

        // 텍스트를 보여준다
        recognizeImgBtn = findViewById(R.id.recognizeImgBtn)
        recognizeImgBtn!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recognizeImgBtn!!.setColorFilter(Color.DKGRAY)
                    return true
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    recognizeImgBtn!!.setColorFilter(Color.WHITE)
                    if (show_img_or_text === "text") {
                        textView!!.setVisibility(View.GONE)
                        imageView!!.visibility = View.VISIBLE
                        show_img_or_text = "img"
                    } else {
                        textView!!.setVisibility(View.VISIBLE)
                        imageView!!.visibility = View.GONE
                        show_img_or_text = "text"
                    }
                    return true
                }
                return false
            }
        })
    }

    private fun convertImagetoText(imageUri: Uri?) {
        try {
            inputImg = InputImage.fromFilePath(applicationContext, imageUri!!)

            val result: Task<Text> = textRecognizer.process(inputImg)
                .addOnSuccessListener {
                    textView.text = it.text
                }.addOnFailureListener {
                    textView.text = "Error : ${it.message}"
                }
        } catch (e: Exception) {

        }
    }

    private fun chooseImg() {
        //  create a new intent to navigate to gallery
        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        // 이미지를 불러올것
        // select image가 경로 url을 리턴해줌
        intentActivityResultLauncher?.launch(i)
    }



        }

