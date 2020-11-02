package com.saglamorhan.artbook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayOutputStream

class MainActivity2 : AppCompatActivity() {

    var selectedPicture :  Uri?  = null
    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


    }

    fun selectImage(view : View){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //İzin verilmemisse calisir
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else{
            // İzin verilmisse calisir
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery,2)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1){

            if (grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery,2)
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Galerideyken yapılanlar burada

        if (requestCode== 2 && resultCode == RESULT_OK && data != null){

            selectedPicture = data.data

            if (selectedPicture != null){

                if (Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(this.contentResolver,selectedPicture!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(selectedBitmap)

                }else{

                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedPicture)
                    imageView.setImageBitmap(selectedBitmap)

                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun save(view : View){

        val artName = etArtText.text.toString()
        val artistName = etArtistName.text.toString()
        val year = etYear.text.toString().toIntOrNull()

        if (selectedBitmap != null){

            val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap?.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

        }


    }

    fun makeSmallerBitmap(image : Bitmap, maximumSize : Int) : Bitmap{

        var width = image.width
        var height = image.height

        //resim yatay mi yoksa dikey bir resim mi onu anlamaya calisiyoruz
        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1){

            //Resim yataydir.
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()

        }else{

            //Resim dikeydir.
            height = maximumSize
            val scaledHeight = height * bitmapRatio
            width = scaledHeight.toInt()

        }

        return Bitmap.createScaledBitmap(image,width,height,true)

    }


}