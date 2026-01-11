package com.example.game.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.game.R
import kotlin.math.atan2

class Weapon(val type: WeaponType, context: Context? = null) {
    var fireRate: Int = 30
    var fireCounter: Int = 0
    var damage: Int = 1
    var speed: Float = 8f
    
    private var projectileBitmap: Bitmap? = null
    
    init {
        when (type) {
            WeaponType.FIREBALL -> {
                fireRate = 30
                damage = 1
                speed = 8f
                try {
                    projectileBitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.licorfireball)
                    projectileBitmap = Bitmap.createScaledBitmap(projectileBitmap!!, 32, 32, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            WeaponType.LASER -> {
                fireRate = 20
                damage = 2
                speed = 12f
                try {
                    projectileBitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.barris)
                    projectileBitmap = Bitmap.createScaledBitmap(projectileBitmap!!, 32, 32, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            WeaponType.ICE -> {
                fireRate = 40
                damage = 1
                speed = 6f
                try {
                    projectileBitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.bombas)
                    projectileBitmap = Bitmap.createScaledBitmap(projectileBitmap!!, 32, 32, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun update() {
        fireCounter++
    }
    
    fun canShoot(): Boolean = fireCounter >= fireRate
    
    fun shoot(playerX: Float, playerY: Float, targetX: Float, targetY: Float): Projectile? {
        if (canShoot()) {
            fireCounter = 0
            val dx = targetX - playerX
            val dy = targetY - playerY
            val angle = atan2(dy.toDouble(), dx.toDouble()).toFloat()
            
            return Projectile(playerX, playerY, angle, speed, damage, type, projectileBitmap)
        }
        return null
    }
}
