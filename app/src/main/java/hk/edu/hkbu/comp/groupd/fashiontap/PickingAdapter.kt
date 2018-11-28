package hk.edu.hkbu.comp.groupd.fashiontap

import android.app.LauncherActivity
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.Arrays
import kotlin.random.Random

class PickingAdapter(clothesList:ArrayList<String>, pantsList:ArrayList<String>):RecyclerView.Adapter<PickingAdapter.CustomViewHolder>() {
    val items:MutableList<Int> = ArrayList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
    var clothesList = clothesList
    var pantsList = pantsList

    override fun getItemCount(): Int {
        return items.count()
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent:ViewGroup, viewType:Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picking_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: CustomViewHolder, position:Int) {
        if(clothesList.size > 0 && pantsList.size > 0) {
            var clothesNum = Random.nextInt(0, Math.max(0, clothesList.size - 1))
            var pantsNum = Random.nextInt(0, Math.max(0, pantsList.size - 1))
            Picasso.get().load(clothesList[clothesNum]).into(holder.clothesImageView)
            Picasso.get().load(pantsList[pantsNum]).into(holder.pantsImageView)
        }
    }

    fun removeTopItem() {
        items.removeAt(0)

        notifyDataSetChanged()
    }

    class CustomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var clothesImageView = itemView.findViewById<ImageView>(R.id.clothes)
        var pantsImageView = itemView.findViewById<ImageView>(R.id.pant)
    }
}