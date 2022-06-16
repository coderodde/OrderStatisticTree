package net.coderodde.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class Demo {
    
    private static void profileAgainstTreeSet() {
        long seed = System.nanoTime();
        final int size = 1_000_000;
        Random random = new Random(seed);
        TreeSet<Integer> treeSet = new TreeSet<>();
        OrderStatisticTree<Integer> orderTree = new OrderStatisticTree<>();
        
        System.out.println("Seed = " + seed);
        
        List<Integer> contents = new ArrayList<>(size);
        List<Integer> toRemove = new ArrayList<>(size);
        
        for (int i = 0; i < size; ++i) {
            contents.add(random.nextInt());
        }
        
        long startTime = System.nanoTime(); 
        
        for (Integer i : contents) {
            treeSet.add(i);
        }
        
        long endTime = System.nanoTime();
        
        System.out.printf("TreeSet.add() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        startTime = System.nanoTime();
        
        for (Integer i : contents) {
            orderTree.add(i);
        }
            
        endTime = System.nanoTime();
        
        System.out.printf("OrderStatisticsTree.add() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        System.out.println("Healthy: " + orderTree.isHealthy());
        
        ////
        startTime = System.nanoTime();
        
        for (Integer i : treeSet) {
            
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("TreeSet iterator in %.2f milliseconds.\n",
                          (endTime - startTime) / 1e6);
        
        ////
        startTime = System.nanoTime();
        
        for (Integer i : orderTree) {
            
        }
        
        endTime = System.nanoTime();
        System.out.printf(
                "OrderStatisticsTree iterator in %.2f milliseconds.\n",
                (endTime - startTime) / 1e6f);
        
        ////
        for (int i = 0; i < size / 4; ++i) {
            toRemove.add(contents.get(random.nextInt(contents.size())));
        }
        
        startTime = System.nanoTime();
        
        for (Integer i : toRemove) {
            treeSet.remove(i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("TreeSet.remove() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        startTime = System.nanoTime();
        
        for (Integer i : toRemove) {
            orderTree.remove(i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("OrderStatisticsTree.remove() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        System.out.println("Healthy: " + orderTree.isHealthy());
        
        Iterator<Integer> iterator1 = treeSet.iterator();
        Iterator<Integer> iterator2 = orderTree.iterator();
        
        while (true) {
            if (iterator1.hasNext() != iterator2.hasNext()) {
                throw new IllegalStateException(
                        "Iterators disagree on hasNext!");
            }
            
            if (iterator1.hasNext() == false) {
                break;
            }
            
            Integer int1 = iterator1.next();
            Integer int2 = iterator2.next();
            
            if (!int1.equals(int2)) {
                throw new IllegalStateException(
                        "Iterators returned different values!");
            }
        }
        
        final int n = 500;
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < n; ++i) {
            get(treeSet, i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("TreeSet get() hack in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < n; ++i) {
            orderTree.get(i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("OrderStatisticsTree.get() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < n; ++i) {
            indexOf(treeSet, i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("TreeSet indexOf() hack in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < n; ++i) {
            orderTree.indexOf(i);
        }
        
        endTime = System.nanoTime();
        
        System.out.printf("OrderStatisticsTree.indexOf() in %.2f millseconds.\n",
                          (endTime - startTime) / 1e6);
    }
    
    public static void main(String[] args) {
        profileAgainstTreeSet();
    }
    
    private static Integer get(TreeSet<Integer> set, int index) {
        Iterator<Integer> i = set.iterator();
        
        while (i.hasNext()) {
            if (index == 0) {
                return i.next();
            }
            
            i.next();
            index++;
        }
        
        return -1;
    }
    
    private static int indexOf(TreeSet<Integer> set, Integer element) {
        int index = 0;
        
        for (Integer i : set) {
            if (i.equals(element)) {
                return index;
            }
            
            ++index;
        }
        
        return -1;
    }
}
