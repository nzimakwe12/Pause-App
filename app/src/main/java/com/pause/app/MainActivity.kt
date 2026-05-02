package com.pause.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        val titleText = TextView(this).apply {
            text = "Pause App"
            textSize = 24f
        }
        layout.addView(titleText)
        
        val descText = TextView(this).apply {
            text = "Reclaim Your Focus"
            textSize = 16f
            setPadding(0, 16, 0, 16)
        }
        layout.addView(descText)
        
        val statusText = TextView(this).apply {
            text = "Status: Ready"
            textSize = 14f
        }
        layout.addView(statusText)
        
        val toggleButton = Button(this).apply {
            text = "Toggle Pause"
            setOnClickListener {
                statusText.text = "Pause toggled!"
            }
        }
        layout.addView(toggleButton)
        
        setContentView(layout)
    }
}
