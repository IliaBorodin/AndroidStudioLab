package com.example.android.bookdiary

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PathPermission
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerPermission()
        checkPermission()

    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_GRANTED->{

                    }

            else->{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


            }
        }

    }
    private fun registerPermission(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this, "Its ok", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Its not ok", Toast.LENGTH_SHORT).show()
            }
        }
    }

}