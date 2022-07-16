package com.rmiragaya.deptnasachallenge.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class DateResponseItem(
    val date: String?,
    val datePhotos: DateResponse?,
    var downloadState: DownloadState?
) : Parcelable