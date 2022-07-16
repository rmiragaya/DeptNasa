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
): Parcelable