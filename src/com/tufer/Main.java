package com.tufer;

public class Main {

    public static void main(String[] args) {
        MyRBTree<Integer> myRBTree = new MyRBTree<>();

        Integer[] points = new Integer[10];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Integer(i);
        }
        for (int i = 0; i < points.length; i++) {
            myRBTree.put(points[i]);
        }
        myRBTree.remove(1);
        myRBTree.remove(5);
        myRBTree.remove(4);
        //myRBTree.remove(9);
        myRBTree.remove(7);
        for (Point<Integer> p:myRBTree.getRBTrees()) {
            System.out.println(p);
        }
    }
}
