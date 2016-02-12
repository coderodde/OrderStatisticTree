package net.coderodde.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class implements an order statistic tree which is based on AVL-trees.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 11, 2016)
 * @param <T> the actual element type.
 */
public class OrderStatisticTree<T extends Comparable<? super T>> {

    private static final class Node<T> {
        T key;
        
        Node<T> parent;
        Node<T> left;
        Node<T> right;
        
        int height;
        int count;
        
        Node(T key) {
            this.key = key;
        }
    }
    
    private Node<T> root;
    private int size;
    private int modCount;
    
    public void add(T element) {
        Objects.requireNonNull(element, "The input element is null.");
        
        if (root == null) {
            root = new Node<>(element);
            size = 1;
            modCount++;
            return;
        }
        
        Node<T> parent = null;
        Node<T> node = root;
        int cmp;
        
        while (node != null) {
            cmp = element.compareTo(node.key);
            
            if (cmp == 0) {
                // The element is already in this tree.
                return;
            }
            
            parent = node;
            
            if (cmp < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        
        Node<T> newnode = new Node<>(element);
        
        if (element.compareTo(parent.key) < 0) {
            parent.left = newnode;
        } else {
            parent.right = newnode;
        }
        
        newnode.parent = parent;
        size++;
        Node<T> hi = parent;
        Node<T> lo = newnode;
        
        while (hi != null) {
            if (hi.left == lo) {
                hi.count++;
            }
            
            lo = hi;
            hi = hi.parent;
        }
        
        fixAfterModification(newnode, true);
    }
    
    public boolean contains(T element) {
        Node<T> x = root;
        int cmp;
        
        while (x != null && (cmp = element.compareTo(x.key)) != 0) {
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        
        return x != null;
    }
    
    public void remove(T element) {
        Node<T> x = root;
        int cmp;
        
        while (x != null && (cmp = element.compareTo(x.key)) != 0) {
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        
        if (x == null) {
            return;
        }
        
        x = deleteNode(x);
        fixAfterModification(x, false);
    }
    
    public T select(int index) {
        checkIndex(index);
        Node<T> node = root;
        
        while (true) {
            if (index > node.count) { // 10 - index = 10
                index -= node.count + 1;
                node = node.right;
            } else if (index < node.count) {
                node = node.left;
            } else {
                return node.key;
            }
        }
    }
    
    public int rank(T element) {
        Node<T> node = root;
        
        if (root == null) {
            return -1;
        }
        
        int rank = root.count;
        int cmp;
        
        while (node != null) {
            if ((cmp = element.compareTo(node.key)) < 0) {
                if (node.left == null) {
                    return -1;
                }
                
                rank -= (node.count - node.left.count);
                node = node.left;
            } else if (cmp > 0) {
                if (node.right == null) {
                    return -1;
                }
                
                rank += 1 + node.right.count;
                node = node.right;
            } else {
                break;
            }
        }
        
        return node == null ? -1 : rank;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        modCount += size;
        root = null;
        size = 0; 
    }
    
    private void checkIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                    "The input index is negative: " + index);
        }
        
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "The input index is too large: " + index +
                    ", the size of this tree is " + size);
        }
    }
    
    private Node<T> deleteNode(Node<T> node) {
        if (node.left == null && node.right == null) {
            // 'node' has no children.
            Node<T> parent = node.parent;
            
            if (parent == null) {
                // 'node' is the root node of this tree.
                root = null;
                size = 0;
                ++modCount;
                return node;
            }
            
            Node<T> lo = node;
            Node<T> hi = parent;
            
            while (hi != null) {
                if (hi.left == lo) {
                    hi.count--;
                }
                
                lo = hi;
                hi = hi.parent;
            }
            
            if (node == parent.left) {
                parent.left = null;
            } else {
                parent.right = null;
            }
            
            --size;
            ++modCount;
            return node;
        }
        
        if (node.left == null || node.right == null) {
            Node<T> child;
            
            // 'node' has only one child.
            if (node.left != null) {
                child = node.left;
            } else {
                child = node.right;
            }
            
            Node<T> parent = node.parent;
            child.parent = parent;
            
            if (parent == null) {
                root = child;
                --size;
                ++modCount;
                return node;
            }
            
            if (node == parent.left) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            
            --size;
            ++modCount;
            
            Node<T> hi = parent;
            Node<T> lo = child;
            
            while (hi != null) {
                if (hi.left == lo) {
                    hi.count--;
                }
                
                lo = hi;
                hi = hi.parent;
            }
            
            return node;
        }
        
        // 'node' has both children.
        T key = node.key;
        Node<T> successor = minimumNode(node.right);
        node.key = successor.key;
        Node<T> child = successor.right;
        Node<T> parent = successor.parent;
        
        if (parent.left == successor) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        
        if (child != null) {
            child.parent = parent;
        }
        
        Node<T> lo = child;
        Node<T> hi = parent;
        
        while (hi != null) {
            if (hi.left == lo) {
                hi.count--;
            }
            
            lo = hi;
            hi = hi.parent;
        }
        
//        parent.count--;
//        addToCounters(parent, -1);
        --size;
        ++modCount;
        return successor;
    }
     
    private Node<T> minimumNode(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        
        return node;
    }
     
    private int height(Node<T> node) {
        return node == null ? -1 : node.height;
    }
    
    private void addToCounters(Node<T> node, int value) {
        Node<T> parent = node.parent;
        
        while (parent != null) {
            if (parent.left == node) {
                parent.count += value;
            }
            
            node = parent;
            parent = parent.parent;
        }
    }
    
    private Node<T> leftRotate(Node<T> node1) {
        Node<T> node2 = node1.right;
        node2.parent = node1.parent;
        node1.parent = node2;
        node1.right = node2.left;
        node2.left = node1;
        
        if (node1.right != null) {
            node1.right.parent = node1;
        }
        
        node1.height = Math.max(height(node1.left), height(node1.right)) + 1;
        node2.height = Math.max(height(node2.left), height(node2.right)) + 1;
        node2.count += node1.count + 1;
        return node2;
    }
    
    private Node<T> rightRotate(Node<T> node1) {
        Node<T> node2 = node1.left;
        node2.parent = node1.parent;
        node1.parent = node2;
        node1.left = node2.right;
        node2.right = node1;
        
        if (node1.left != null) {
            node1.left.parent = node1;
        }
        
        node1.height = Math.max(height(node1.left), height(node1.right)) + 1;
        node2.height = Math.max(height(node2.left), height(node2.right)) + 1;
        node1.count -= node2.count + 1;
        return node2;
    }
    
    private Node<T> rightLeftRotate(Node<T> node1) {
        Node<T> node2 = node1.right;
        node1.right = rightRotate(node2);
        return leftRotate(node1);
    }
    
    private Node<T> leftRightRotate(Node<T> node1) {
        Node<T> node2 = node1.left;
        node1.left = leftRotate(node2);
        return rightRotate(node1);
    }
    
    // Fixing an insertion: use insertionMode = true.
    // Fixing a deletion: use insertionMode = false.
    private void fixAfterModification(Node<T> node, boolean insertionMode) {
        Node<T> parent = node.parent;
        Node<T> grandParent;
        Node<T> subTree;
        
        while (parent != null) {
            if (height(parent.left) == height(parent.right) + 2) {
                grandParent = parent.parent;
                
                if (height(parent.left.left) > height(parent.left.right)) {
                    subTree = rightRotate(parent);
                } else {
                    subTree = leftRightRotate(parent);
                }
                
                if (grandParent == null) {
                    root = subTree;
                } else if (grandParent.left == parent) {
                    grandParent.left = subTree;
                } else {
                    grandParent.right = subTree;
                }
                
                if (grandParent != null) {
                    grandParent.height = Math.max(
                            height(grandParent.left),
                            height(grandParent.right)) + 1;
                }
                
                if (insertionMode) {
                    // Whenever fixing after insertion, at most one rotation is
                    // required in order to maintain the balance.
                    return;
                }
            } 
            
            if (height(parent.right) == height(parent.left) + 2) {
                grandParent = parent.parent;
                
                if (height(parent.right.right) > height(parent.right.left)) {
                    subTree = leftRotate(parent);
                } else {
                    subTree = rightLeftRotate(parent);
                }
                
                if (grandParent == null) {
                    root = subTree;
                } else if (grandParent.left == parent) {
                    grandParent.left = subTree;
                } else {
                    grandParent.right = subTree;
                }
                
                if (grandParent != null) {
                    grandParent.height =
                            Math.max(height(grandParent.left),
                                     height(grandParent.right)) + 1;
                }
                
                if (insertionMode) {
                    return;
                }
            }
            
            parent.height = Math.max(height(parent.left), 
                                     height(parent.right)) + 1;
            parent = parent.parent;
        }
    }
    
    public boolean isHealthy() {
        if (root == null) {
            return true;
        }
        
//        boolean hasCycles = containsCycles();
//        
//        if (hasCycles) {
//            System.out.println("Has cycles.");
//            return false;
//        }
        
        boolean heightsOk = heightsAreCorrect();
        
        if (!heightsOk) {
            System.out.println("Heights are not correct.");
            return false;
        }
        
        boolean isBalanced = isBalanced();
        
        if (!isBalanced) {
            System.out.println("Is not balanced.");
            return false;
        }
        
//        boolean wellIndexed = isWellIndexed();
//        
//        if (!wellIndexed) {
//            System.out.println("Is not well indexed.");
//            return false;
//        }
        
        return true;
//        return !containsCycles() 
//                && heightsAreCorrect() 
//                && isBalanced()
//                && isWellIndexed();
    }
    
    private boolean containsCycles() {
        Set<Node<T>> visitedNodes = new HashSet<>();
        return containsCycles(root, visitedNodes);
    }
    
    private boolean containsCycles(Node<T> current, Set<Node<T>> visitedNodes) {
        if (current == null) {
            return false;
        }
        
        if (visitedNodes.contains(current)) {
            return true;
        }
        
        visitedNodes.add(current);
        
        return containsCycles(current.left, visitedNodes) 
                || containsCycles(current.right, visitedNodes);
    }
    
    private boolean heightsAreCorrect() {
        return getHeight(root) == root.height;
    }
    
    private int getHeight(Node<T> node) {
        if (node == null) {
            return -1;
        }
        
        int leftTreeHeight = getHeight(node.left);
        
        if (leftTreeHeight == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        
        int rightTreeHeight = getHeight(node.right);
        
        if (rightTreeHeight == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        
        if (node.height == Math.max(leftTreeHeight, rightTreeHeight) + 1) {
            return node.height;
        }
        
        return Integer.MIN_VALUE;
    }
    
    private boolean isBalanced() {
        return isBalanced(root);
    }
    
    private boolean isBalanced(Node<T> node) {
        if (node == null) {
            return true;
        }
        
        if (!isBalanced(node.left)) {
            return false;
        }
        
        if (!isBalanced(node.right)) {
            return false;
        }
        
        int leftHeight  = height(node.left);
        int rightHeight = height(node.right);
        
        if (Math.abs(leftHeight - rightHeight) < 2) {
            return true;
        }
        
        System.out.println("left: " + leftHeight + ", right: " + rightHeight +
                ", recorded: " + node.height);
        return false;
    }
    
    private boolean isWellIndexed() {
        boolean yeah = isWellIndexed(root);
        System.out.println("yeah: " + yeah);
//        return isWellIndexed(root);
        return yeah;
    }
    
    private boolean isWellIndexed(Node<T> node) {
        if (node == null) {
            return true;
        }
        
        int tmp;
        
        if (node.count != (tmp = getTreeSize(node.left))) {
            System.out.println("node.count = " + node.count + ", left tree size = " + tmp);
            return false;
        }
        
        return isWellIndexed(node.right);
    }
    
    private int getTreeSize(Node<T> node) {
        if (node == null) {
            return 0;
        }
        
        int leftSubTreeSize  = getTreeSize(node.left);
        int rightSubTreeSize = getTreeSize(node.right);
        
        return leftSubTreeSize + 1 + rightSubTreeSize;
    }
}
