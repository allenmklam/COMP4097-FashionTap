package hk.edu.hkbu.comp.groupd.fashiontap

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import java.util.ArrayList

class PickingItem(itemView:View):RecyclerView.ViewHolder(itemView) {

    private var clothesList: ArrayList<String> = arrayListOf()
    private var pantsList: ArrayList<String> = arrayListOf()

    fun bindClothes(path: String) {
        clothesList.add(path)
    }

    fun bindPants(path: String) {
        pantsList.add(path)
    }
}