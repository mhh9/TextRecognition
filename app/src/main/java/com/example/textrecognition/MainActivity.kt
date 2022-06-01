package com.example.textrecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var savedImgButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            savedImgButton = findViewById(R.id.savedImg_btn)
            savedImgButton!!.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SavedImgActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }

