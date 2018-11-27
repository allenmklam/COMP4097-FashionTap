package hk.edu.hkbu.comp.groupd.fashiontap

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), DiscreteScrollView.OnItemChangedListener<MyAdapter.MyViewHolder> {

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

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_home)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

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

        return view
    }

    private fun loadData() {
        log("Loading data...")


//        for (i in 1..10) {
//            clothesList.add(MyItem("", "https://cdn.shopify.com/s/files/1/1285/7087/products/LOST_IN_JAPAN_launching_5.18_b171bbcc-3eb1-40c8-acb7-ddc8a26d06ed_1024x1024.png?v=1527262491"))
//            pantsList.add(MyItem("", "https://mbtskoudsalg.com/images/black-jeans-png-1.png"))
//        }
//        val items = HashMap<String, Any>()
//        items.put("clothes", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fclothes%2Fclothes1.png?alt=media&token=92448462-7072-41a8-915e-a9ad4e49301f")
//        items.put("clothes", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fclothes%2Fclothes2.png?alt=media&token=094b1c1c-557d-47e1-9369-bac70604b323")
//        items.put("clothes", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fclothes%2Fclothes3.jpg?alt=media&token=e08b52d4-bd33-4167-9f15-32a18186a2cd")
//        items.put("clothes", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fclothes%2Fclothes4.png?alt=media&token=8f48eb2d-0f9d-493e-a60e-64592459b6eb")

//        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("clothes").add(items)

//        items.put("pants", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fpants%2Fpants1.png?alt=media&token=91bffa5c-d9b2-476c-8f45-139273322844")
//        items.put("pants", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fpants%2Fpants2.png?alt=media&token=31b22b6c-902f-4d03-8330-b052685b8a09")
//        items.put("pants", "https://firebasestorage.googleapis.com/v0/b/hkbu-comp4097-fashiontap.appspot.com/o/user%2Fkvw0o16q6KYMWcV7ZYBxmIC9A2A2%2Fpants%2Fpants3.png?alt=media&token=3e00718d-6ded-4e3a-8179-06caa804dabd")

//        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("pants").add(items)

        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("clothes").get().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d(TAG, "Document url => " + document.data["clothes"].toString())
                            clothesList.add(MyItem("",  document.data["clothes"].toString()))
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
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
    }

    private fun log(message: String) {
        Log.d("BackgroundToForeground", message)
    }

}