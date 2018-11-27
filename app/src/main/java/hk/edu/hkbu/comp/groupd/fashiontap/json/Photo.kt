package hk.edu.hkbu.comp.groupd.fashiontap.json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Photo(
    val active: Boolean,
    val comment_counter: Int,
    val crop: String,
    val crop_320: String,
    val crop_640: String,
    val crop_h: Int,
    val crop_w: Int,
    val description: String,
    val id: Int,
    val like_counter: Int,
    val timestamp: Timestamp,
    val upload: String,
    val upload_h: Int,
    val upload_w: Int,
    val url: String,
    val user: User,
    val user_id: Int,
    val viewer_commented: Boolean,
    val viewer_favorited: Boolean,
    val viewer_liked: Boolean,
    val viewer_owner: Boolean
): Parcelable