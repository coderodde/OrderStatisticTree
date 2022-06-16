package net.coderodde.util;

import java.util.Set;

/**
 * This interface defines the API for an order statistic set. An order statistic
 * set is a sorted set that provides two additional methods:
 * <ul>
 *   <li><code>get(int index)</code> returns the <code><index</code>th smallest
 *       element,</li>
 *   <li><code>indexOf(T element)</code> returns the index of the input element.
 *   </li>
 * </ul>
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 16, 2016)
 */
public interface OrderStatisticSet<T> extends Set<T> {
    
    /**
     * Returns the <code>index</code>th smallest element from this set.
     * 
     * @param index the element index.
     * @return the <code>index</code>th smallest element.
     */
    T get(int index);
    
    /**
     * Returns the index of <code>element</code> in the sorted set.
     * 
     * @param element the query element.
     * @return the index of the query element or -1 if there is no such element
     *         in this set.
     */
    int indexOf(T element);
}
