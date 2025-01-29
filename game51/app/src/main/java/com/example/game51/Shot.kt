package com.example.game51

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Shot(context: Context, shx: Int, shy: Int) {
    private val shot: Bitmap
    private val context: Context
    val shx: Int
    var shy: Int

    init {
        this.context = context
        shot = BitmapFactory.decodeResource(context.resources, R.drawable.missle1)
        this.shx = shx
        this.shy = shy
    }

    fun getShot(): Bitmap {
        return shot
    }
}
