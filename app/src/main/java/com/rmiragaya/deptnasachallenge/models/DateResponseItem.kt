package com.rmiragaya.deptnasachallenge.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class DateResponseItem(
    var date: String = "",
    var datePhotos: ArrayList<DatePhotosItem>? = null,
    var downloadState: DownloadState? = null
) : Parcelable