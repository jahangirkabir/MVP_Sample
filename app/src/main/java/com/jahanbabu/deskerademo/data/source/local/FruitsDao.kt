package com.jahanbabu.deskerademo.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jahanbabu.deskerademo.data.Fruit

/**
 * Data Access Object for the fruits table.
 */
@Dao interface FruitsDao {

    /**
     * Select all fruits from the fruits table.
     *
     * @return all fruits.
     */
    @Query("SELECT * FROM Fruit") fun getFruits(): List<Fruit>

    /**
     * Select related fruits from the fruits table.
     *
     * @return related fruits.
     */
//    @Query("SELECT * FROM Fruits WHERE title LIKE :title") fun getRelatedFruits(title: String): List<Fruit>
    @Query("SELECT * FROM Fruit WHERE name LIKE :key || '%'") fun getRelatedFruits(key: String): List<Fruit>

    /**
     * Select a fruit by id.
     *
     * @param fruitId the fruit id.
     * @return the fruit with fruitId.
     */
    @Query("SELECT * FROM Fruit WHERE id = :fruitId") fun getFruitById(fruitId: Int): Fruit?

    /**
     * Insert a fruit in the database. If the fruit already exists, replace it.
     *
     * @param fruit the fruit to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertFruit(fruit: Fruit)

    /**
     * Update a fruit.
     *
     * @param fruit fruit to be updated
     * @return the number of fruits updated. This should always be 1.
     */
    @Update fun updateFruit(fruit: Fruit): Int

    
    /**
     * Delete all fruits.
     */
    @Query("DELETE FROM Fruit") fun deleteFruits()

}