package net.coderodde.util;

public class Demo {
    
    private static final int SIZE = 1000;
    
    public static void main(String[] args) {
        OrderStatisticTree<Integer> tree = new OrderStatisticTree<>();
        
        for (int i = 0; i < SIZE; ++i) {
            tree.add(i);
        }
        
        System.out.println("Healthy: " + tree.isHealthy());
    }
}
