package com.example.game.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.game.R
import kotlin.math.abs

class Player(
    context: Context,
    screenWidth: Float,
    screenHeight: Float
) {
    enum class Direction {
        DOWN, UP, LEFT, RIGHT
    }

    var x = 0f
    var y = 0f
    var speed = 5f

    private var screenWidth: Float = screenWidth
    private var screenHeight: Float = screenHeight
    var frameWidth = 64f
    var frameHeight = 64f
    private val totalFrames = 4
    private var currentFrame = 0
    private var frameTicker: Long = 0

    private val spriteSheets = mutableMapOf<Direction, Bitmap>()
    var currentDirection = Direction.DOWN

    private var isMoving = false
    var collisionBox: Rect

    init {
        try {
            val originalDown = BitmapFactory.decodeResource(context.resources, R.drawable.mago)
            val originalUp = BitmapFactory.decodeResource(context.resources, R.drawable.magobackview)
            val originalLeft = BitmapFactory.decodeResource(context.resources, R.drawable.magovistalateralesquerda)
            val originalRight = BitmapFactory.decodeResource(context.resources, R.drawable.magovistalateral)

            val newHeight = 64f
            val newWidth = newHeight * totalFrames

            this.frameHeight = newHeight
            this.frameWidth = newWidth / totalFrames

            originalDown?.let { spriteSheets[Direction.DOWN] = Bitmap.createScaledBitmap(it, newWidth.toInt(), newHeight.toInt(), true) }
            originalUp?.let { spriteSheets[Direction.UP] = Bitmap.createScaledBitmap(it, newWidth.toInt(), newHeight.toInt(), true) }
            originalLeft?.let { spriteSheets[Direction.LEFT] = Bitmap.createScaledBitmap(it, newWidth.toInt(), newHeight.toInt(), true) }
            originalRight?.let { spriteSheets[Direction.RIGHT] = Bitmap.createScaledBitmap(it, newWidth.toInt(), newHeight.toInt(), true) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this.screenWidth = screenWidth
        this.screenHeight = screenHeight

        x = screenWidth / 2
        y = screenHeight / 2

        collisionBox = Rect(x.toInt(), y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt())
    }

    fun move(dx: Float, dy: Float) {
        currentDirection = if (abs(dx) > abs(dy)) {
            if (dx > 0) Direction.RIGHT else Direction.LEFT
        } else {
            if (dy > 0) Direction.DOWN else Direction.UP
        }

        x += dx * speed
        y += dy * speed

        if (x < 0) x = 0f
        if (y < 0) y = 0f
        if (x > screenWidth - frameWidth) x = screenWidth - frameWidth
        if (y > screenHeight - frameHeight) y = screenHeight - frameHeight

        isMoving = (dx != 0f || dy != 0f)
    }

    fun stopMoving() {
        isMoving = false
    }

    fun update() {
        if (isMoving) {
            frameTicker++
            if (frameTicker > 5) {
                currentFrame = (currentFrame + 1) % totalFrames
                frameTicker = 0
            }
        } else {
            currentFrame = 0
        }

        collisionBox.left = x.toInt()
        collisionBox.top = y.toInt()
        collisionBox.right = (x + frameWidth).toInt()
        collisionBox.bottom = (y + frameHeight).toInt()
    }

    fun draw(canvas: Canvas) {
        val startX = currentFrame * frameWidth
        val frameToDraw = Rect(startX.toInt(), 0, (startX + frameWidth).toInt(), frameHeight.toInt())
        val whereToDraw = Rect(x.toInt(), y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt())

        spriteSheets[currentDirection]?.let { sheet ->
            canvas.drawBitmap(sheet, frameToDraw, whereToDraw, null)
        }
    }
}
