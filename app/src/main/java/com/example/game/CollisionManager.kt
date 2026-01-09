package com.example.game.game

import android.graphics.Rect

class CollisionManager {
    
    fun checkCollision(rect1: Rect, rect2: Rect): Boolean {
        return rect1.left < rect2.right &&
               rect1.right > rect2.left &&
               rect1.top < rect2.bottom &&
               rect1.bottom > rect2.top
    }
    
    fun checkPlayerEnemyCollisions(player: Player, enemies: List<Enemy>): List<Enemy> {
        val collidedEnemies = mutableListOf<Enemy>()
        for (enemy in enemies) {
            if (checkCollision(player.collisionBox, enemy.collisionBox)) {
                collidedEnemies.add(enemy)
            }
        }
        return collidedEnemies
    }
    
    fun checkProjectileEnemyCollisions(
        projectiles: List<Projectile>,
        enemies: List<Enemy>
    ): Map<Projectile, Enemy> {
        val collisions = mutableMapOf<Projectile, Enemy>()
        
        for (projectile in projectiles) {
            for (enemy in enemies) {
                if (checkCollision(projectile.collisionBox, enemy.collisionBox)) {
                    collisions[projectile] = enemy
                    break
                }
            }
        }
        return collisions
    }
}
