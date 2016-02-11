package net.coderodde.util;

public class Demo {
    
    private static final int SIZE = 1000;
    
    public static void main(String[] args) {
        OrderStatisticTree<Integer> tree = new OrderStatisticTree<>();
        
        for (int i = 0; i < SIZE; ++i) {
            tree.add(i);
        }
        
        System.out.println("Healthy: " + tree.isHealthy());
        
        tree.add(10);
        tree.add(11);
        System.out.println("size: " + tree.size());
        
        for (int i = 0; i < SIZE; ++i) {
            if (!tree.contains(i)) {
                System.out.println("fdhsa");
            }
        }
        
        System.out.println(tree.contains(-1));
        
        for (int i = 100; i < 200; ++i) {
            tree.remove(i);
        }
        
        System.out.println("size after deletion: " + tree.size());
        
        for (int i = 100; i < 200; ++i) {
            if (tree.contains(i)) {
                System.out.println("wrong! " + i);
            }
        }
    }
}
