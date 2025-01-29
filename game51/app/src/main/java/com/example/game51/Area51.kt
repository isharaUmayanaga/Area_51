package com.example.game51
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
class Area51(context: Context) : View(context) {

    private var background: Bitmap
    private var lifeImage: Bitmap

    private val handler: Handler
    private val UPDATE_MILLTS = 30L
    private var points = 0
    private var life = 3
    private val scorePaint: Paint
    private val TEXT_SIZE = 80f
    private var paused = false
    private val vehicle: armyVehicle
    private val alienShip: Alien
    private val random: Random
    private val alienShots: ArrayList<Shot>
    private val armyShorts: ArrayList<Shot>
    private var alienExplosion = false
    private var explosion: Explosion? = null
    private val explosions: ArrayList<Explosion>
    private var lastShotTime: Long = 0
    private val SHOT_DELAY = 800   // Time delay between shots in milliseconds
    private val HIGH_SCORE_KEY = "high_score"

    init {
        random = Random()
        alienShots = ArrayList()
        armyShorts = ArrayList()
        explosions = ArrayList()

        val display: Display? = (context as Activity).display

        val size = Point()
        if (display != null) {
            display.getSize(size)
        }
        screenWidth = size.x
        screenHeight = size.y

        vehicle = armyVehicle(context)
        vehicle.vy = screenHeight - vehicle.getVehicleHeight() - 50 // Adjust as needed

        alienShip = Alien(context)

        background = BitmapFactory.decodeResource(resources, R.drawable.background)
        // Resize background to match screen size
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false)

        lifeImage = BitmapFactory.decodeResource(resources, R.drawable.life)
        // Resize life image to match screen size
        lifeImage = Bitmap.createScaledBitmap(lifeImage, screenWidth / 10, screenHeight / 10, false)

        // Use main looper explicitly
        handler = Handler(Looper.getMainLooper())

        scorePaint = Paint().apply {
            color = Color.RED
            textSize = TEXT_SIZE
            textAlign = Paint.Align.LEFT
        }
    }

    fun getHighScore(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }

    fun saveHighScore(context: Context, highScore: Int) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(HIGH_SCORE_KEY, highScore)
        editor.apply()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("Pt:$points", 0f, TEXT_SIZE, scorePaint)
        for (i in 1..life) { // Changed to include life = 0
            canvas.drawBitmap(lifeImage, (screenWidth - lifeImage.width * i).toFloat(), 0f, null)
        }

        if (life <= 0) { // Changed condition to check if life is less than or equal to 0
            paused = true
            handler.removeCallbacksAndMessages(null)

            // Check if current points beat the high score
            val highScore = getHighScore(context)
            if (points > highScore) {
                saveHighScore(context, points)
            }
            val intent = Intent(context, GameOver::class.java)
            intent.putExtra("point", points)

            intent.putExtra("highScore", highScore)

            context.startActivity(intent)
            (context as Activity).finish()
            return // Exit the method to prevent further drawing
        }





    alienShip.ax += alienShip.alienVelocity
        if (alienShip.ax + alienShip.getAlienWidth() >= screenWidth) {
            alienShip.alienVelocity *= -1
        }
        if (alienShip.ax <= 0) {
            alienShip.alienVelocity *= -1
        }
        if (!alienShotAction && alienShip.ax >= 200 + random.nextInt(400)) {
            val alienShot = Shot(context, alienShip.ax + alienShip.getAlienWidth() / 2, alienShip.ay)
            alienShots.add(alienShot)
            alienShotAction = true
        }

        if (!alienExplosion) {
            canvas.drawBitmap(alienShip.getAlien(), alienShip.ax.toFloat(), alienShip.ay.toFloat(), null)
        }

        if (vehicle.isAlive) {
            if (vehicle.vx > screenWidth - vehicle.getVehicleWidth()) {
                vehicle.vx = screenWidth - vehicle.getVehicleWidth()
            } else if (vehicle.vx < 0) {
                vehicle.vx = 0
            }
            canvas.drawBitmap(vehicle.getVehicle(), vehicle.vx.toFloat(), vehicle.vy.toFloat(), null)
        }

        for (i in alienShots.indices) {
            alienShots[i].shy += 15
            canvas.drawBitmap(alienShots[i].getShot(), alienShots[i].shx.toFloat(), alienShots[i].shy.toFloat(), null)
            if (alienShots[i].shx >= vehicle.vx &&
                alienShots[i].shx <= vehicle.vx + vehicle.getVehicleWidth() &&
                alienShots[i].shy >= vehicle.getVehicleHeight() &&
                alienShots[i].shy <= screenHeight
            ) {
                life--
                alienShots.removeAt(i)
                explosion = Explosion(context, vehicle.vx, vehicle.vy)
                explosions.add(explosion!!)
            } else if (alienShots[i].shy >= screenHeight) {
                alienShots.removeAt(i)
            }
            if (alienShots.isEmpty()) {
                alienShotAction = false
            }
        }

        for (i in armyShorts.indices) {
            armyShorts[i].shy -= 15
            canvas.drawBitmap(
                armyShorts[i].getShot(),
                armyShorts[i].shx.toFloat(),
                armyShorts[i].shy.toFloat(),
                null
            )
            if (armyShorts[i].shx >= alienShip.ax &&
                armyShorts[i].shx <= alienShip.ax + alienShip.getAlienWidth() &&
                armyShorts[i].shy <= alienShip.getAlienHeight() &&
                armyShorts[i].shy >= alienShip.ay
            ) {
                points += 10
                armyShorts.removeAt(i)
                explosion = Explosion(context, alienShip.ax, alienShip.ay)
                explosions.add(explosion!!)
            } else if (armyShorts[i].shy <= 0) {
                armyShorts.removeAt(i)
            }
        }

        for (i in explosions.indices) {
            explosions[i].getExplosion(explosions[i].explosionFrame)?.let {
                canvas.drawBitmap(
                    it,
                    explosions[i].eX.toFloat(),
                    explosions[i].eY.toFloat(),
                    null
                )
            }
            explosions[i].explosionFrame++
            if (explosions[i].explosionFrame > 8) {
                explosions.removeAt(i)
            }
        }

        if (!paused) {
            handler.postDelayed(runnable, UPDATE_MILLTS)
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastShotTime >= SHOT_DELAY && armyShorts.size < 1) {
                    val armyShot = Shot(context, vehicle.vx + vehicle.getVehicleWidth() / 2, vehicle.vy)
                    armyShorts.add(armyShot)
                    lastShotTime = currentTime
                }
            }
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> vehicle.vx = touchX
        }
        return true
    }


    private val runnable = Runnable { invalidate() }

    fun startGame() {
        // Reset game variables
        points = 0
        life = 3
        paused = false

        // Clear existing objects
        alienShots.clear()
        armyShorts.clear()
        explosions.clear()

        // Start the game loop
        handler.post(runnable)
    }

    companion object {
        var screenWidth = 0
        var screenHeight = 0
    }

    private var alienShotAction = false
}
