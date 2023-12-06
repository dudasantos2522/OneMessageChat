package br.edu.scl.ifsp.ads.onemessagechat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var message: String = ""
): Parcelable