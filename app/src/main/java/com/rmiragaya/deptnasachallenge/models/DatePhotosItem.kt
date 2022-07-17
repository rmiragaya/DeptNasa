package com.rmiragaya.deptnasachallenge.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class DatePhotosItem(
    val caption: String?,
    val date: String?,
    val identifier: String?,
    val image: String?
) : Parcelable {
    override fun toString(): String {
        return "Caption: $caption\n" +
                "\nDate: $date\n" +
                "\nIdentifier: $identifier\n" +
                "\nImage: $image"
    }

    fun getOnlyDate(): String {
        return date?.replace("-", "/")?.split(" ")?.first().toString()
    }
}