package com.example.game.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.game.R
import kotlin.math.cos
import kotlin.math.sin

enum class WeaponType {
    FIREBALL, LASER, ICE
}

class Projectile(
    val startX: Float,
    val startY: Float,
    val angle: Float,
    val speed: Float = 8f,
    val damage: Int = 1,
    val type: WeaponType = WeaponType.FIREBALL,
    val bitmap: Bitmap? = null
) {
    var posX = startX
    var posY = startY
    val radius = 16f
    var collisionBox: Rect
    var active = true
    
    init {
        collisionBox = Rect(
            (posX - radius).toInt(),
            (posY - radius).toInt(),
            (posX + radius).toInt(),
            (posY + radius).toInt()
        )
    }
    
    fun update() {
        posX += cos(angle.toDouble()).toFloat() * speed
        posY += sin(angle.toDouble()).toFloat() * speed
        
        collisionBox.left = (posX - radius).toInt()
        collisionBox.top = (posY - radius).toInt()
        collisionBox.right = (posX + radius).toInt()
        collisionBox.bottom = (posY + radius).toInt()
    }
    
    fun isOutOfBounds(screenWidth: Int, screenHeight: Int): Boolean {
        return posX < -100 || posX > screenWidth + 100 || posY < -100 || posY > screenHeight + 100
    }
    
    fun draw(canvas: Canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, (posX - radius).toFloat(), (posY - radius).toFloat(), null)
        } else {
            // Fallback: desenhar círculo se imagem não carregar
            val paint = Paint().apply {
                color = when (type) {
                    WeaponType.FIREBALL -> Color.RED
                    WeaponType.LASER -> Color.CYAN
                    WeaponType.ICE -> Color.BLUE
                }
                style = Paint.Style.FILL
            }
            canvas.drawCircle(posX, posY, radius, paint)
        }
    }
}
