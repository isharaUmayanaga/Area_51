package com.example.game51

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Explosion(context: Context, eX: Int, eY: Int) {
     val explosion = arrayOfNulls<Bitmap>(7)
     val eX: Int
     val eY: Int

    var explosionFrame: Int = 0

    init {
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion4)
        explosion[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion5)
        explosion[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion6)
        explosion[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion7)

        this.explosionFrame = 0
        this.eX = eX
        this.eY = eY
    }

    fun getExplosion(explosionFrame: Int): Bitmap? {
        return if (explosionFrame in 0 until explosion.size) {
            explosion[explosionFrame]
        } else {
            null // Return null or handle the out-of-bounds case appropriately
        }
    }
}
