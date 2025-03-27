package com.dbsnetwork.iptv

import android.os.Parcel
import android.os.Parcelable

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val expiresAt: Long? = null,
    val permissions: List<String>? = null,
    val message: String? = null,
) {

}