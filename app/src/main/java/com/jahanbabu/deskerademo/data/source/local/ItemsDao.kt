package com.jahanbabu.deskerademo.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jahanbabu.deskerademo.data.Item

/**
 * Data Access Object for the items table.
 */
@Dao interface ItemsDao {

    /**
     * Select all items from the items table.
     *
     * @return all items.
     */
    @Query("SELECT * FROM Item") fun getItems(): List<Item>

    /**
     * Select related items from the items table.
     *
     * @return related items.
     */
//    @Query("SELECT * FROM Items WHERE title LIKE :title") fun getRelatedItems(title: String): List<Item>
    @Query("SELECT * FROM Item WHERE title LIKE :key || '%'") fun getRelatedItems(key: String): List<Item>

    /**
     * Select a item by id.
     *
     * @param itemId the item id.
     * @return the item with itemId.
     */
    @Query("SELECT * FROM Item WHERE id = :itemId") fun getItemById(itemId: Int): Item?

    /**
     * Insert a item in the database. If the item already exists, replace it.
     *
     * @param item the item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertItem(item: Item)

    /**
     * Update a item.
     *
     * @param item item to be updated
     * @return the number of items updated. This should always be 1.
     */
    @Update fun updateItem(item: Item): Int

    
    /**
     * Delete all items.
     */
    @Query("DELETE FROM Item") fun deleteItems()

}