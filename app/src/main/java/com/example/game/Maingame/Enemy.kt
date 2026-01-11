package com.example.game.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.game.R
import kotlin.math.sqrt

class Enemy(
    context: Context,
    startX: Float,
    startY: Float,
    val enemyType: EnemyType = EnemyType.GOBLIN
) {
    var x: Float = startX
    var y: Float = startY
    var speed: Float = 2.5f
    var health: Int = 1
    var maxHealth: Int = 1
    var collisionBox: Rect
    
    private val frameWidth = 64f
    private val frameHeight = 64f
    
    private var bitmap: Bitmap? = null
    
    enum class EnemyType {
        GOBLIN, ORC, DEMON
    }
    
    init {
        when (enemyType) {
            EnemyType.GOBLIN -> {
                speed = 2.5f
                maxHealth = 1
                try {
                    bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.inimigo)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, frameWidth.toInt(), frameHeight.toInt(), true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            EnemyType.ORC -> {
                speed = 3.5f
                maxHealth = 2
                try {
                    bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.inimigo)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, frameWidth.toInt(), frameHeight.toInt(), true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            EnemyType.DEMON -> {
                speed = 4.5f
                maxHealth = 3
                try {
                    bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.inimigo)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, frameWidth.toInt(), frameHeight.toInt(), true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        
        health = maxHealth
        collisionBox = Rect(x.toInt(), y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt())
    }
    
    fun moveTowardsPlayer(playerX: Float, playerY: Float) {
        val dx = playerX - x
        val dy = playerY - y
        val distance = sqrt(dx * dx + dy * dy)
        
        if (distance > 0) {
            x += (dx / distance) * speed
            y += (dy / distance) * speed
        }
        
        collisionBox.left = x.toInt()
        collisionBox.top = y.toInt()
        collisionBox.right = (x + frameWidth).toInt()
        collisionBox.bottom = (y + frameHeight).toInt()
    }
    
    fun takeDamage(amount: Int) {
        health -= amount
    }
    
    fun isAlive(): Boolean = health > 0
    
    fun draw(canvas: Canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, x, y, null)
        } else {
            // Fallback: desenhar retângulo se imagem não carregar
            val paint = Paint().apply {
                color = when (enemyType) {
                    EnemyType.GOBLIN -> Color.GREEN
                    EnemyType.ORC -> Color.MAGENTA
                    EnemyType.DEMON -> Color.RED
                }
                style = Paint.Style.FILL
            }
            canvas.drawRect(x, y, x + frameWidth, y + frameHeight, paint)
        }
        
        if (health < maxHealth) {
            val healthBarPaint = Paint().apply {
                color = Color.RED
                style = Paint.Style.FILL
            }
            val healthBarBackgroundPaint = Paint().apply {
                color = Color.GRAY
                style = Paint.Style.FILL
            }
            
            canvas.drawRect(x, y - 10, x + frameWidth, y - 5, healthBarBackgroundPaint)
            canvas.drawRect(x, y - 10, x + (frameWidth * health / maxHealth), y - 5, healthBarPaint)
        }
    }
}
