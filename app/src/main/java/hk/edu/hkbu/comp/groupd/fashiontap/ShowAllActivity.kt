package hk.edu.hkbu.comp.groupd.fashiontap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_show_all.*
import kotlinx.android.synthetic.main.content_show_all.*
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShowAllActivity : AppCompatActivity() {

    val REQUEST_CODE = 1

    var customRecyclerAdapter:CustomRecyclerAdapter?=null
    var imageModel:ArrayList<ImageModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_all)
        setSupportActionBar(toolbar_all)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        toolbar_all.setNavigationOnClickListener {
            finish()
        }

        tabs.addTab(tabs.newTab().setText("衣服"));
        tabs.addTab(tabs.newTab().setText("褲子"));
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position) {
                        0 -> {
                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .collection("clothes").get().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            imageModel = ArrayList()
                                            for (document in task.result) {
                                                imageModel.add(ImageModel(document.data["clothes"].toString()))
                                            }
                                            settingUpAdapter()
                                        } else {
                                            Log.d(HomeFragment.TAG, "Error getting documents: ", task.exception)
                                        }
                                    }
                        }
                        1 -> {
                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .collection("pants").get().addOnCompleteListener {
                                        task ->
                                        if (task.isSuccessful) {
                                            imageModel = ArrayList()
                                            for (document in task.result) {
                                                Log.d(HomeFragment.TAG, "Document url => " + document.data["pants"].toString())
                                                imageModel.add(ImageModel(document.data["pants"].toString()))
                                            }
                                            settingUpAdapter()
                                        } else {
                                            Log.d(HomeFragment.TAG, "Error getting documents: ", task.exception)
                                        }
                                    }
                        }
                        else -> {
                            Log.d("Error", "Wrong tab selected")
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            }
        )

        retrievingImages()
    }

    //retrieving images
    fun retrievingImages(){

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("clothes").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            imageModel.add(ImageModel(document.data["clothes"].toString()))
                        }
                        settingUpAdapter()
                    } else {
                        Log.d(HomeFragment.TAG, "Error getting documents: ", task.exception)
                    }
        }
    }

    //setting up adapter and recyclerview
    fun settingUpAdapter(){
        customRecyclerAdapter = CustomRecyclerAdapter(imageModel)
        val layoutManager = GridLayoutManager(applicationContext,2)
        main_recyclerview.layoutManager = layoutManager
        main_recyclerview.adapter = customRecyclerAdapter
    }

//    //overriding onRequestPermissionResult
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if(requestCode == REQUEST_CODE){
//            if((grantResults[0] and grantResults.size)==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(applicationContext,"Permission Granted1",Toast.LENGTH_SHORT).show()
//                //retriving and setting up adapter on first run to handle crash
//                RetrievingImages()
//                settingUpAdapter()
//            }else{
//                Toast.makeText(applicationContext,"Permission Granted",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}
