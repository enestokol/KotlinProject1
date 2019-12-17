package com.example.kotlinproject1.Utils

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.MainActivity
import com.example.kotlinproject1.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_photo.*
import java.io.ByteArrayOutputStream

class ProfilePhoto : AppCompatActivity(),PhotoFragment.onProfilePhotoListener {

    var permission:Boolean=false
    var gallerySource: Uri?=null
    var cameraSource: Bitmap?=null




    fun progressvisible(){
        progressBar1.visibility= View.VISIBLE
    }

    fun progressinvisible(){
        progressBar1.visibility= View.INVISIBLE
    }


    override fun getPhotoPath(photoPath: Uri?) {
        gallerySource=photoPath
        Picasso.with(this).load(gallerySource).resize(220,200).into(updateimg1)
    }

    override fun getPhotoBitmap(cameraPath: Bitmap) {
        cameraSource=cameraPath
        updateimg1.setImageBitmap(cameraPath)
        //Picasso.with(activity).load(cameraSource).into(updateimg)
    }

    private fun askPermission() {
        var permissionList= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA)

        if(ContextCompat.checkSelfPermission(this,permissionList[0])== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,permissionList[1])== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,permissionList[2])== PackageManager.PERMISSION_GRANTED){
            permission=true
        }
        else{
            ActivityCompat.requestPermissions(this,permissionList,150)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==150){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED &&
                grantResults[1]== PackageManager.PERMISSION_GRANTED &&
                grantResults[2]== PackageManager.PERMISSION_GRANTED){
                var dialog=PhotoFragment()
                dialog.show(supportFragmentManager,"tag1")
            }
            else{
                Toast.makeText(this,"You must grant all permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getResourceId(id: Int?) = when (id) {
        0 -> R.mipmap.ic_first_avatar
        1 -> R.mipmap.ic_second_avatar
        2 -> R.mipmap.ic_third_avatar
        3 -> R.mipmap.ic_fourth_avatar
        4 -> R.mipmap.ic_fifth_avatar
        5 -> R.mipmap.ic_sixth_avatar
        else -> R.mipmap.ic_first_avatar
    }

    private fun readFBUser() {
        var reference = FirebaseDatabase.getInstance().reference
        var fbuser = FirebaseAuth.getInstance().currentUser

        var query = reference.child("User")
            .orderByKey()
            .equalTo(fbuser?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (singleSnapshot in p0.children) {
                    var user = singleSnapshot.getValue(User::class.java)
                    if(user?.imageup!=null){
                        Picasso.with(this@ProfilePhoto).load(user?.imageup).into(updateimg1)
                    }
                    else{
                        updateimg1.setImageResource(getResourceId(user?.image))
                    }
                    //updateimg1.setImageResource(getResourceId(user?.image))
                }

            }

        })
    }



    inner class BackgroundImageCompress:AsyncTask<Uri,Void,ByteArray?>{


        var myBitmap:Bitmap?=null



        constructor(){}

        constructor(bm:Bitmap){
            if(bm!=null){
                myBitmap=bm
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Uri?): ByteArray? {
           if(myBitmap==null){
               myBitmap=MediaStore.Images.Media.getBitmap(this@ProfilePhoto.contentResolver,p0[0])

           }

            var imageBytes: ByteArray? = null

            for(i in 1..5){
                imageBytes=convertBitmaptoByte(myBitmap,100/i)
                publishProgress()

            }

            return imageBytes

        }

        private fun convertBitmaptoByte(myBitmap: Bitmap?, i: Int): ByteArray? {
            var stream=ByteArrayOutputStream()
            myBitmap?.compress(Bitmap.CompressFormat.JPEG,i,stream)
            return stream.toByteArray()

        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadImagegoFirebase(result)
        }

    }

    private fun uploadImagegoFirebase(result: ByteArray?) {


        progressvisible()

        var storageReference=FirebaseStorage.getInstance().getReference()
        var imageUploadedPath=storageReference.child("images/users"+FirebaseAuth.getInstance().currentUser?.uid+"/profile_image")

        var uploadTo=imageUploadedPath.putBytes(result!!)






        uploadTo.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageUploadedPath.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Toast.makeText(this@ProfilePhoto,"Path"+downloadUri.toString(), Toast.LENGTH_SHORT).show()

                FirebaseDatabase.getInstance().reference
                    .child("User")
                    .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .child("imageup")
                    .setValue(downloadUri.toString())
                progressinvisible()
            } else {
                // Handle failures
                // ...
            }
        }

//        uploadTo.addOnSuccessListener (object :OnSuccessListener<UploadTask.TaskSnapshot>{
//
//
//            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
//
//
//                var firebaseURL=uploadTo.getResult().uploadSessionUri
//
//                Toast.makeText(this@ProfilePhoto,"Path"+p0.toString(), Toast.LENGTH_SHORT).show()
//
//                FirebaseDatabase.getInstance().reference
//                    .child("User")
//                    .child(FirebaseAuth.getInstance().currentUser?.uid!!)
//                    .child("imageup")
//                    .setValue(firebaseURL.toString())
//                progressinvisible()
//
//            }
//        })
//
    }

    private fun ImageCompressed(gallerySource:Uri){
        var compressed=BackgroundImageCompress()
        compressed.execute(gallerySource)

    }

    private fun CameraCompressed(cameraSource:Bitmap){
        var compressed=BackgroundImageCompress(cameraSource)
        var uri: Uri? = null
        compressed.execute(uri)

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readFBUser()
        setContentView(R.layout.activity_profile_photo)


        updateimg1.setOnClickListener {

            if(permission){

                var dialog=PhotoFragment()
                dialog.show(supportFragmentManager,"tag1")
            }
            else{
                askPermission()
            }
        }

        returnhomepage.setOnClickListener {
            val intent1= Intent(this@ProfilePhoto, MainActivity::class.java)
            startActivity(intent1)
            finish()
        }

        updateImgBtn1.setOnClickListener {
            if(gallerySource!=null){

                ImageCompressed(gallerySource!!)

            }else if(cameraSource!=null){

                CameraCompressed(cameraSource!!)

            }
        }
    }
}
