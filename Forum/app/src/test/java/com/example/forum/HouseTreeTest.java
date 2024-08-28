package com.example.forum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * This class is the unitTest for HouseTree.class including validation test and edge test
 *
 * @author Xiaochen Lu
 */
public class HouseTreeTest {

    private HouseTree tree;

    @Before
    public void setUp() {
        tree = new HouseTree(null);
    }

    /**
     * Test if the tree is empty upon initialization and whether retrieval methods work appropriately on an empty tree.
     */
    @Test
    public void testEmptyTree() {
        assertNull(tree.getRoot());
        List<House> result = tree.getHousesPriceRange(50, 150);
        assertTrue(result.isEmpty());

        result = tree.toList();
        assertTrue(result.isEmpty());
    }

    /**
     * Test insertion of a single house into the tree and verify that it becomes the root.
     */
    @Test
    public void testInsertSingleHouse() {
        House house = new House("1", "CityA", "SuburbA", "StreetA", "10", "UnitA", 100, 1, "emailA@example.com", 5);
        tree.insert(house);

        assertEquals(house, tree.getRoot());
    }

    /**
     * Test insertion of multiple houses and then retrieving them based on a given price range.
     */
    @Test
    public void testInsertAndRetrieveWithinPriceRange() {
        House house1 = new House("1", "CityA", "SuburbA", "StreetA", "10", "UnitA", 70, 1, "emailA@example.com", 5);
        House house2 = new House("2", "CityB", "SuburbB", "StreetB", "20", "UnitB", 100, 2, "emailB@example.com", 10);
        House house3 = new House("3", "CityC", "SuburbC", "StreetC", "30", "UnitC", 150, 3, "emailC@example.com", 15);

        tree.insert(house1);
        tree.insert(house2);
        tree.insert(house3);

        List<House> result = tree.getHousesPriceRange(50, 120);
        assertEquals(2, result.size());
        assertTrue(result.contains(house1));
        assertTrue(result.contains(house2));
        assertFalse(result.contains(house3));
    }

    /**
     * Test tree balancing after inserting houses. The tree should re-balance itself to ensure efficient operations.
     */
    @Test
    public void testBalancingAfterInsertion() {
        House house1 = new House("1", "CityA", "SuburbA", "StreetA", "10", "UnitA", 60, 1, "emailA@example.com", 5);
        House house2 = new House("2", "CityB", "SuburbB", "StreetB", "20", "UnitB", 70, 2, "emailB@example.com", 10);
        House house3 = new House("3", "CityC", "SuburbC", "StreetC", "30", "UnitC", 80, 3, "emailC@example.com", 15);
        House house4 = new House("4", "CityD", "SuburbD", "StreetD", "40", "UnitD", 90, 4, "emailD@example.com", 20);

        tree.insert(house1);
        tree.insert(house2);
        tree.insert(house3);
        tree.insert(house4);

        assertEquals(house2, tree.getRoot());
        assertEquals(house1, tree.getRoot().getLeft());
        assertEquals(house3, tree.getRoot().getRight());
    }

    /**
     * Test if the iterator throws an exception when the tree is empty.
     */
    @Test(expected = java.util.NoSuchElementException.class)
    public void testIteratorException() {
        tree.iterator().next();
    }

    /**
     * Test if the iterator correctly traverses through houses in the tree.
     */
    @Test
    public void testIteratorTraversal() {
        House house1 = new House("1", "CityA", "SuburbA", "StreetA", "10", "UnitA", 60, 1, "emailA@example.com", 5);
        House house2 = new House("2", "CityB", "SuburbB", "StreetB", "20", "UnitB", 70, 2, "emailB@example.com", 10);
        House house3 = new House("3", "CityC", "SuburbC", "StreetC", "30", "UnitC", 80, 3, "emailC@example.com", 15);

        tree.insert(house1);
        tree.insert(house2);
        tree.insert(house3);

        Iterator<House> iterator = tree.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(house1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(house2, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(house3, iterator.next());

        assertFalse(iterator.hasNext());
    }
}

