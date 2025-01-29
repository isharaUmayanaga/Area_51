package com.example.game51

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Random

@RequiresApi(Build.VERSION_CODES.R)
class armyVehicle(private val context: Context) {
    private var vehicle: Bitmap
    var vx: Int = 0
    var vy: Int = 0
    val isAlive: Boolean = true
    private var vehicleVelocity: Int = 0
    private val random: Random = Random()

    init {

        vehicle = BitmapFactory.decodeResource(context.resources, R.drawable.vehicle1)
          resetArmyVehicle()
    }

    fun getVehicle(): Bitmap {
        return vehicle
    }

    fun getVehicleWidth(): Int {
        return vehicle.width
    }

    fun getVehicleHeight(): Int {
        return vehicle.height
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun resetArmyVehicle() {
         vx = random.nextInt(Area51.screenWidth)
         vy = Area51.screenHeight - vehicle.height

        vehicleVelocity = 10 + random.nextInt(6)
    }
}
