package com.example.whatsupcloneinandorid.activiteis

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import com.example.whatsupcloneinandorid.MainActivity
import com.example.whatsupcloneinandorid.R
import com.example.whatsupcloneinandorid.databinding.ActivityProfileBinding
import com.example.whatsupcloneinandorid.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {
private lateinit var binding: ActivityProfileBinding
    val permisions:Array<String> = arrayOf(Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
        )
 val permissioncode = 140201;
private lateinit var database:FirebaseDatabase
private lateinit var storage: FirebaseStorage
private lateinit var selectedImg:Uri
private lateinit var dialog:AlertDialog
private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()



        val builder = AlertDialog.Builder(this)
        builder.setMessage("Pleas wait...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        binding.logoImage.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
               requestPermissions(permisions,permissioncode)
            }else{
                var popupMenu = PopupMenu(this,binding.logoImage)
                popupMenu.inflate(R.menu.imagemenu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.camersource ->{
                            val intent = Intent()

                            intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                            CamereResultLauncher.launch(intent)
                        }
                        R.id.gallaysource ->{
                            val intent = Intent()
                            intent.action = Intent.ACTION_GET_CONTENT
                            intent.type = "image/*"

                            GalleryResultLauncher.launch(intent)
                        }
                    }
                    true
                }
                popupMenu.show()
            }


        }

binding.continueBtn.setOnClickListener {

    var nametext = binding.namedit.text.toString()
    if(TextUtils.isEmpty(nametext)){
        Toast.makeText(applicationContext,"Please select name",Toast.LENGTH_SHORT).show()
    }else if(selectedImg == null){
        Toast.makeText(applicationContext,"Please select Image",Toast.LENGTH_SHORT).show()
    }else{
        dialog.show()
        uploadData(nametext)
    }

}



    }

    private fun uploadData(nametext: String) {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {
                    uploadInfo(it.toString(),nametext)
                }
            }else{
                dialog.dismiss()
                Toast.makeText(applicationContext,"some error occured while uploading",Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    private fun uploadInfo(imageurl: String, nametext: String) {

val user = UserModel(auth.uid!!,nametext, auth.currentUser!!.phoneNumber!!,imageurl)

        database.reference.child("users").child(auth.uid!!).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
dialog.dismiss()
                startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                Toast.makeText(applicationContext,"Upload Image Successfully",Toast.LENGTH_SHORT).show()
            }else{
dialog.dismiss()
                Toast.makeText(applicationContext,"Some thing bad happened",Toast.LENGTH_SHORT).show()
            }
        }
    }

    var CamereResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val bitmap = result.data!!.extras!!["data"] as Bitmap?
            binding.logoImage.setImageBitmap(bitmap)
            selectedImg = getimageurifrombitmap(applicationContext, bitmap)
        }
    }

    private fun getimageurifrombitmap(applicationContext: Context, bitmap: Bitmap?): Uri {
        val bytobj = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, bytobj)
        val path = MediaStore.Images.Media.insertImage(
            applicationContext.contentResolver,
            bitmap,
            "anything",
            "this  is something else"
        )
        return Uri.parse(path)
    }

    var GalleryResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImg = result.data!!.data!!
            binding.logoImage.setImageURI(selectedImg)
        }
    }

}