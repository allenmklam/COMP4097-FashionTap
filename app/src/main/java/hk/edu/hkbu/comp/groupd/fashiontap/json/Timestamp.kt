package hk.edu.hkbu.comp.groupd.fashiontap.json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Timestamp(
    val formatted: String,
    val raw: Long
): Parcelable