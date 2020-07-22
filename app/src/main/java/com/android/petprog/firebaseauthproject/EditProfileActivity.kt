package com.android.petprog.firebaseauthproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.IOException


private const val TAG = "EditProfileActivity"
private const val PICK_IMAGE = 123

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null

    private lateinit var emailTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var phoneNoEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var profileImageView: ImageView

    private var imagePath: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && requestCode == RESULT_OK && data?.data != null) {
            imagePath = data.data
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(this.contentResolver, imagePath!!)
                    val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)
                    profileImageView.setImageBitmap(bitmap)
                } else {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, imagePath)
                    profileImageView.setImageBitmap(bitmap)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseAuth = Firebase.auth

        if (firebaseAuth?.currentUser == null) {
            finish()
            startActivity(Intent(this, SignInActivity::class.java))
        }

        databaseReference = Firebase.database.reference
        nameEditText = findViewById(R.id.EditTextName)
        surnameEditText = findViewById(R.id.EditTextSurname)
        phoneNoEditText = findViewById(R.id.EditTextPhoneNo)
        saveButton = findViewById(R.id.btnSaveButton)
        emailTextView = findViewById(R.id.textViewEmailAdress)
        profileImageView = findViewById(R.id.update_imageView)

        val user = firebaseAuth?.currentUser
        saveButton.setOnClickListener(this)
        emailTextView.text = user?.email


        firebaseStorage = Firebase.storage
        storageRef = firebaseStorage?.reference

        profileImageView.setOnClickListener {
            val profileIntent = Intent()
            profileIntent.type = "image/*"
            profileIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(profileIntent, "Select Image."),
                PICK_IMAGE
            );
        }

    }

    override fun onClick(view: View?) {
        if (view == saveButton) {
            if (imagePath == null) {
                val drawable = resources.getDrawable(R.drawable.defavatar)
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.defavatar)

                userInformation()
                finish()
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                userInformation()
            }
        }
    }

    private fun userInformation() {
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val phoneNo = phoneNoEditText.text.toString().trim()

        val userInfo = UserInformation(name, surname, phoneNo)
        val user = firebaseAuth?.currentUser
        databaseReference?.child(user?.uid!!)?.setValue(userInfo)
        Toast.makeText(this, "User information updated", Toast.LENGTH_LONG).show()
    }
}
