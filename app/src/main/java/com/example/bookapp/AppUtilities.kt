package com.example.bookapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Base64
import android.util.DisplayMetrics
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun Activity.getScreenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}


fun Context.getConnectivityManager() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

/**
 * This method uses a regex with the matches() method in
 * order to determine if the email address is valid or
 * not
 *
 * @param email
 * @return
 */

fun String.isEmail(): Boolean {
    val regex = "[a-zA-Z0-9]+@[a-z]+\\.[a-z]+"
    return this.matches(regex.toRegex())
}


fun Uri.toDrawable(context: Context): Drawable {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    return Drawable.createFromStream(inputStream, path.toString())
}

fun Drawable.fromUri(uri: Uri, context: Context): Drawable {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    return Drawable.createFromStream(inputStream, uri.toString())
}

fun Drawable.toBase64(): String {
    val bitmap = (this as BitmapDrawable).bitmap
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT);
}
//
//fun getRetrofit(): Retrofit {
//    return Retrofit.Builder()
//            .baseUrl("http://www.andreiram.co.uk/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//}
