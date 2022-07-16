package com.rmiragaya.deptnasachallenge.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class PhotoList(
    val photoList : ArrayList<DatePhotosItem>? = arrayListOf()
): Parcelable
