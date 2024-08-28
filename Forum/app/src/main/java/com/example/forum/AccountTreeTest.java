package com.example.forum;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
/**
 * This class is the unitTest for AccountTree.class including validation test and edge test
 *
 * @author Xiaochen Lu
 */
public class AccountTreeTest {

    private AccountTree tree;

    @Before
    public void setUp() {
        tree = new AccountTree(null);
    }

    /**
     * Test if the tree is empty upon initialization.
     */
    @Test
    public void testEmptyTree() {
        assertNull(tree.getRoot());
        List<String> result = tree.toList();
        assertTrue(result.isEmpty());
    }

    /**
     * Test insertion of accounts and searching for them by name.
     */
    @Test
    public void testInsertAndSearch() {
        Account account1 = new Account("Alice", "pass1", 1, 1);
        tree.insert(account1);
        assertEquals(account1, tree.search("Alice"));

        Account account2 = new Account("Bob", "pass2", 2, 2);
        tree.insert(account2);
        assertEquals(account2, tree.search("Bob"));

        assertNull(tree.search("Eve"));
    }

    /**
     * Test insertion and deletion of accounts.
     */
    @Test
    public void testInsertAndDelete() {
        Account account1 = new Account("Alice", "pass1", 1, 1);
        Account account2 = new Account("Bob", "pass2", 2, 2);

        tree.insert(account1);
        tree.insert(account2);

        tree.delete("Alice");
        assertNull(tree.search("Alice"));
        assertEquals(account2, tree.search("Bob"));

        tree.delete("Bob");
        assertNull(tree.search("Bob"));
    }

    /**
     * Test if the iterator throws an exception when the tree is empty.
     */
    @Test(expected = java.util.NoSuchElementException.class)
    public void testIteratorException() {
        tree.iterator().next();
    }

    /**
     * Test if the iterator correctly traverses through accounts in the tree.
     */
    @Test
    public void testIteratorTraversal() {
        Account account1 = new Account("Alice", "pass1", 1, 1);
        Account account2 = new Account("Bob", "pass2", 2, 2);
        Account account3 = new Account("Eve", "pass3", 3, 3);

        tree.insert(account1);
        tree.insert(account2);
        tree.insert(account3);

        Iterator<Account> iterator = tree.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(account1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(account2, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(account3, iterator.next());

        assertFalse(iterator.hasNext());
    }

    /**
     * Test the conversion of the tree to a list representation.
     */
    @Test
    public void testToList() {
        Account account1 = new Account("Alice", "pass1", 1, 1);
        Account account2 = new Account("Bob", "pass2", 2, 2);

        tree.insert(account1);
        tree.insert(account2);

        List<String> result = tree.toList();

        assertEquals(2, result.size());
        assertTrue(result.contains("Alice;pass1;1;1"));
        assertTrue(result.contains("Bob;pass2;2;2"));
    }
}

