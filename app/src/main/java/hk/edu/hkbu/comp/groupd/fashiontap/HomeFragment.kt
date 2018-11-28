package hk.edu.hkbu.comp.groupd.fashiontap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import hk.edu.hkbu.comp.groupd.fashiontap.transformer.InfiniteCarouselTransformer
import kotlinx.android.synthetic.main.abc_popup_menu_item_layout.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.random.Random


class HomeFragment : Fragment(), DiscreteScrollView.OnItemChangedListener<MyAdapter.MyViewHolder> {

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCurrentItemChanged(viewHolder: MyAdapter.MyViewHolder?, adapterPosition: Int) {
        val realPosition = mInfiniteScrollWrapper.realCurrentPosition
        log("onCurrentItemChanged $realPosition")
    }

    private lateinit var mInfiniteScrollWrapper: InfiniteScrollAdapter<*>
    private lateinit var mInfiniteScrollWrapper1: InfiniteScrollAdapter<*>
    private lateinit var mAdapter: MyAdapter
    private lateinit var mAdapter1: MyAdapter
    private var clothesList: ArrayList<MyItem> = arrayListOf()
    private var pantsList: ArrayList<MyItem> = arrayListOf()
    private val GALLERY = 1
    private val CAMERA = 2

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_home)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }

        mAdapter = MyAdapter(clothesList)
        mAdapter1 = MyAdapter(pantsList)

        // Infinite scroll
        mInfiniteScrollWrapper = InfiniteScrollAdapter.wrap(mAdapter)
        mInfiniteScrollWrapper1 = InfiniteScrollAdapter.wrap(mAdapter1)
        view.infinite_carousel.adapter = mInfiniteScrollWrapper
        view.infinite_carousel1.adapter = mInfiniteScrollWrapper1

        // Item transformer
        view.infinite_carousel.setItemTransformer(InfiniteCarouselTransformer())
        view.infinite_carousel1.setItemTransformer(InfiniteCarouselTransformer())

//        // Item change listener
        view.infinite_carousel.addOnItemChangedListener(this)
        view.infinite_carousel1.addOnItemChangedListener(this)

        // data
        loadData()

        view.listbutton.buttonColor = -12669000
        view.listbutton.setOnClickListener {
            val intentRegister = Intent(activity, ShowAllActivity::class.java)
            startActivity(intentRegister)
        }
        view.helpbutton.setOnClickListener {
            val intentRegister = Intent(activity, PickingActivity::class.java)
            startActivity(intentRegister)
        }
        view.addbutton.buttonColor = -22660044
        view.addbutton.setOnClickListener {
            showPictureDialog()
        }

        return view
    }

    private fun loadData() {
        log("Loading data...")

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("clothes").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, "Document url => " + document.data["clothes"].toString())
                            clothesList.add(MyItem("",  document.data["clothes"].toString()))
                        }
                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                                .collection("pants").get().addOnCompleteListener {
                                    task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            Log.d(TAG, "Document url => " + document.data["pants"].toString())
                                            pantsList.add(MyItem("",  document.data["pants"].toString()))
                                        }
                                        mAdapter.notifyDataSetChanged()
                                        mAdapter1.notifyDataSetChanged()
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.exception)
                                    }
                                }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }
    }

    private fun reloadData() {
        log("Loading data...")

        clothesList = arrayListOf()
        pantsList = arrayListOf()
        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("clothes").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, "Document url => " + document.data["clothes"].toString())
                            clothesList.add(MyItem("",  document.data["clothes"].toString()))
                        }
                        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                                .collection("pants").get().addOnCompleteListener {
                                    task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            Log.d(TAG, "Document url => " + document.data["pants"].toString())
                                            pantsList.add(MyItem("",  document.data["pants"].toString()))
                                        }
                                        mAdapter.notifyDataSetChanged()
                                        mAdapter1.notifyDataSetChanged()
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.exception)
                                    }
                                }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }
    }

    private fun log(message: String) {
        Log.d("BackgroundToForeground", message)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context!!)
        pictureDialog.setTitle("選擇行動")
        val pictureDialogItems = arrayOf("在相簿中選擇", "拍攝照片")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun checkPermission():Boolean {
        if ((ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED))
        {
            // Permission is not granted
            return false
        }
        return true
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity!!,
                arrayOf<String>(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        val listItems = arrayOf("衣服", "褲子")
        val mBuilder = AlertDialog.Builder(this!!.context!!)
        mBuilder.setTitle("選擇類型")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
            when(i) {
                0 -> {
                    if (requestCode == GALLERY)
                    {
                        if (data != null)
                        {
                            val contentURI = data!!.data
                            try
                            {
                                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                                val image = stream.toByteArray()
                                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                                FirebaseStorage.getInstance().reference.child("user/${FirebaseAuth.getInstance().currentUser!!.uid}/clothes/clothes${Random.nextInt(0, 100000)}.png")
                                        .putBytes(image).addOnSuccessListener{
                                            val items = HashMap<String, Any>()
                                            items["clothes"] = it.downloadUrl.toString()
                                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("clothes").add(items).addOnSuccessListener {
                                                reloadData()
                                            }
                                        }
                            }
                            catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                            }

                        }

                    }
                    else if (requestCode == CAMERA)
                    {
                        var thumbnail = data!!.extras!!.get("data") as Bitmap
                        thumbnail = RotateBitmap(thumbnail, 90f)
                        val stream = ByteArrayOutputStream()
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, stream)
                        val image = stream.toByteArray()
                        Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                        FirebaseStorage.getInstance().reference.child("user/${FirebaseAuth.getInstance().currentUser!!.uid}/clothes/clothes${Random.nextInt(0, 100000)}.png")
                                .putBytes(image).addOnSuccessListener{
                                    val items = HashMap<String, Any>()
                                    items["clothes"] = it.downloadUrl.toString()
                                    FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("clothes").add(items).addOnSuccessListener {
                                        reloadData()
                                    }
                                }
                    }
                }
                1 -> {
                    if (requestCode == GALLERY)
                    {
                        if (data != null)
                        {
                            val contentURI = data!!.data
                            try
                            {
                                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                                val image = stream.toByteArray()
                                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                                FirebaseStorage.getInstance().reference.child("user/${FirebaseAuth.getInstance().currentUser!!.uid}/pants/pants${Random.nextInt(0, 100000)}.png")
                                        .putBytes(image).addOnSuccessListener{
                                            val items = HashMap<String, Any>()
                                            items["pants"] = it.downloadUrl.toString()
                                            FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("pants").add(items).addOnSuccessListener {
                                                reloadData()
                                            }
                                        }
                            }
                            catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                            }

                        }

                    }
                    else if (requestCode == CAMERA)
                    {
                        var thumbnail = data!!.extras!!.get("data") as Bitmap
                        thumbnail = RotateBitmap(thumbnail, 90f)
                        val stream = ByteArrayOutputStream()
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, stream)
                        val image = stream.toByteArray()
                        Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                        FirebaseStorage.getInstance().reference.child("user/${FirebaseAuth.getInstance().currentUser!!.uid}/pants/pants${Random.nextInt(0, 100000)}.png")
                                .putBytes(image).addOnSuccessListener{
                                    val items = HashMap<String, Any>()
                                    items["pants"] = it.downloadUrl.toString()
                                    FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("pants").add(items).addOnSuccessListener {
                                        reloadData()
                                    }
                                }
                    }
                }
            }
            dialogInterface.dismiss()
        }
        // Set the neutral/cancel button click listener
        mBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val mDialog = mBuilder.create()
        mDialog.show()

    }

    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

}