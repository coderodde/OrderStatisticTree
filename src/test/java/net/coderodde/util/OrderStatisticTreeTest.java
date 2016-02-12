package net.coderodde.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class OrderStatisticTreeTest {
    
    private final OrderStatisticTree<Integer> tree = new OrderStatisticTree<>();
    
    @Before
    public void before() {
        tree.clear();
    }
    
    @Test
    public void testAdd() {
        assertTrue(tree.isEmpty());
        
        for (int i = 10; i > 0; --i) {
            assertEquals(10 - i, tree.size());
            tree.add(2 * i);
            assertEquals(11 - i, tree.size());
            assertFalse(tree.isEmpty());
        }
        
        assertEquals(10, tree.size());
    }

    @Test
    public void testContains() {
        
        for (int i = 0; i < 100; ++i) {
            assertFalse(tree.contains(i));
            assertTrue(tree.isEmpty());
        }
        
        for (int i = 0; i < 100; ++i) {
            tree.add(i);
            assertFalse(tree.isEmpty());
        }
        
        for (int i = 99; i >= 0; --i) {
            assertTrue(tree.contains(i));
            assertFalse(tree.isEmpty());
        }
    }

    @Test
    public void testRemove() {
        for (int i = 30; i > 20; --i) {
            tree.add(i);
        }
        
        for (int i = 21, size = 10; i <= 27; ++i, --size) {
            assertEquals(size, tree.size());
            assertFalse(tree.isEmpty());
            tree.remove(i);
            assertFalse(tree.isEmpty());
            assertEquals(size - 1, tree.size());
        }
        
        assertEquals(3, tree.size());
    }

    @Test
    public void testSize() {
        for (int i = 0; i < 5; ++i) {
            assertEquals(i, tree.size());
            tree.add(i);
            assertEquals(i + 1, tree.size());
        }
    }
    
    @Test
    public void testSelect() {
        for (int i = 0; i < 100; ++i) {
            tree.add(2 * i);
        }
        
        for (int i = 0; i < tree.size(); ++i) {
            assertEquals(Integer.valueOf(2 * i), tree.select(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyTreeSelectThrowsOnNegativeIndex() {
        tree.select(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyTreeSelectThrowsOnTooLargeIndex() {
        tree.select(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSelectThrowsOnNegativeIndex() {
        for (int i = 0; i < 5; ++i) {
            tree.add(i);
        }
        
        tree.select(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSelectThrowsOnTooLargeIndex() {
        for (int i = 0; i < 5; ++i) {
            tree.add(i);
        }
        
        tree.select(5);
    }
    
    @Test
    public void testRank() {
        for (int i = 19; i >= 10; --i) {
            tree.add(3 * i);
        }
        
        for (int i = 10; i < 20; ++i) {
            assertEquals(i - 10, tree.rank(3 * i));
        }
        
        tree.clear();
        
        for (int i = 0; i < 10; ++i) {
            tree.add(i);
        }
        
        for (int i = 0; i < 10; ++i) {
            assertEquals(i, tree.rank(i));
        }
        
        tree.clear();
        
        for (int i = 0; i < 20; ++i) {
            tree.add(i);
        }
        
        for (int i = 30; i < 50; ++i) {
            tree.add(i);
        }
        
        for (int i = 20; i < 30; ++i) {
            assertEquals(-1, tree.rank(i));
        }
        
        for (int i = -10; i < 0; ++i) {
            assertEquals(-1, tree.rank(i));
        }
        
        for (int i = 50; i < 60; ++i) {
            assertEquals(-1, tree.rank(i));
        }
    }
}
