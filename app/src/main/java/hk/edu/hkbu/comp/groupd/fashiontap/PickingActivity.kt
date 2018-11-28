package hk.edu.hkbu.comp.groupd.fashiontap

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_picking.*
import kotlinx.android.synthetic.main.content_show_all.*
import kotlinx.android.synthetic.main.picking_item.*
import swipeable.com.layoutmanager.OnItemSwiped
import swipeable.com.layoutmanager.SwipeableLayoutManager
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper
import java.util.ArrayList


class PickingActivity : AppCompatActivity() {

    var adapter:PickingAdapter?=null
    private var clothesList: ArrayList<String> = arrayListOf()
    private var pantsList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picking)
        setSupportActionBar(picking_toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        picking_toolbar.setNavigationOnClickListener {
            finish()
        }

        retrievingImages()

    }

    //retrieving images
    fun retrievingImages(){

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("clothes").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        clothesList = arrayListOf()
                        for (document in task.result) {
                            clothesList.add(document.data["clothes"].toString())
                        }
                        settingUpAdapter()
                    } else {
                        Log.d(HomeFragment.TAG, "Error getting documents: ", task.exception)
                    }
                }

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("pants").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        pantsList = arrayListOf()
                        for (document in task.result) {
                            Log.d(HomeFragment.TAG, "Document url => " + document.data["pants"].toString())
                            pantsList.add(document.data["pants"].toString())
                        }
                        settingUpAdapter()
                    } else {
                        Log.d(HomeFragment.TAG, "Error getting documents: ", task.exception)
                    }
                }
    }

    //setting up adapter and recyclerview
    fun settingUpAdapter(){
        adapter = PickingAdapter(clothesList, pantsList)
        val swipeableTouchHelperCallback = object: SwipeableTouchHelperCallback(object: OnItemSwiped {
            override fun onItemSwiped() {
                adapter!!.removeTopItem()
            }
            override fun onItemSwipedLeft() {
            }
            override fun onItemSwipedRight() {
            }
            override fun onItemSwipedUp() {
            }
            override fun onItemSwipedDown() {
            }
        }) {
            override fun getAllowedSwipeDirectionsMovementFlags(viewHolder: RecyclerView.ViewHolder):Int {
                return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeableTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
        recycler_view.layoutManager = SwipeableLayoutManager().setAngle(10)
                .setAnimationDuratuion(450)
                .setMaxShowCount(3)
                .setScaleGap(0.1f)
                .setTransYGap(0)
        recycler_view.adapter = adapter
    }
}
