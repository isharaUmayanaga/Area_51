package com.example.game51

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class Alien(private val context: Context) {
    private var alienShip: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.alien1)
    var ax: Int = 0
    var ay: Int = 0
    var alienVelocity: Int = 0
    private val random: Random = Random()

    init {

        resetAlien()
    }

    fun getAlien(): Bitmap {
        return alienShip
    }

    fun getAlienWidth(): Int {
        return alienShip.width
    }

    fun getAlienHeight(): Int {
        return alienShip.height
    }

    private fun resetAlien() {
        ax = 200 + random.nextInt(400)
        ay = 0
        alienVelocity = 14 + random.nextInt(10)
    }
}
