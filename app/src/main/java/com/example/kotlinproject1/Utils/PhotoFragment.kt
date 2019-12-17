package com.example.kotlinproject1.Utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.example.kotlinproject1.R


class PhotoFragment : DialogFragment() {


    lateinit var tvGallery:TextView
    lateinit var tvCamera:TextView

    interface onProfilePhotoListener{

        fun getPhotoPath(photoPath:Uri?)
        fun getPhotoBitmap(cameraPath:Bitmap)
    }

    lateinit var mProfilePhotoListener:onProfilePhotoListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var v=inflater.inflate(R.layout.fragment_photo, container, false)

        tvGallery=v.findViewById(R.id.tvGallery)
        tvCamera=v.findViewById(R.id.tvCamera)

        tvGallery.setOnClickListener {
            var intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            startActivityForResult(intent,100)
        }

        tvCamera.setOnClickListener {
            var intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,200)
        }

        return v

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode== Activity.RESULT_OK && data!=null){

            var galleryPath=data.data
            Log.e("Enes","galleryPath")
            mProfilePhotoListener.getPhotoPath(galleryPath)
            dismiss()

        }else if(requestCode==200 && resultCode== Activity.RESULT_OK && data!=null){

            var cameraPath:Bitmap
            cameraPath=data.extras!!.get("data") as Bitmap
            mProfilePhotoListener.getPhotoBitmap(cameraPath)
            dismiss()

        }
    }

    override fun onAttach(context: Context) {
        mProfilePhotoListener=activity as onProfilePhotoListener

        super.onAttach(context)
    }

}
