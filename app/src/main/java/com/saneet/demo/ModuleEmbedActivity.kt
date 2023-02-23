package com.saneet.demo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.saneet.demo.databinding.ActivityModuleEmbedBinding
import kotlin.reflect.full.createInstance

class ModuleEmbedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModuleEmbedBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModuleEmbedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentName = intent.getStringExtra("FRAGMENT")
        if (fragmentName == null) {
            throw RuntimeException("No name in intent")
        } else {
            val fragment = Class.forName(fragmentName).kotlin.createInstance()
            if (fragment == null) {
                throw RuntimeException("No fragment found by name: $fragmentName")
            } else {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment as Fragment)
                        .commitNow()
            }
        }
    }
}