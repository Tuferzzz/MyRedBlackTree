package com.tufer;


public class Point<N> implements Comparable<N>{
    public Point parent;
    public Point left;
    public Point right;
    public Boolean color=MyRBTree.BLACK;
    public N num;

    public Point(Point parent, Point left, Point right, Boolean color, N num) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.color = color;
        this.num = num;
    }

    public Point(N num) {
        this.num = num;
    }

    public Point(N num, Point parent) {
        this.num = num;
        this.parent=parent;
    }

    @Override
    public String toString() {
        return "Point{" +
                "parent=" + (parent==null?"NULL":parent.num==null?"NULL":parent.num.toString()) +
                ", left=" + (left==null?"NULL":left.num==null?"NULL":left.num.toString()) +
                ", right=" + (right==null?"NULL":right.num==null?"NULL":right.num.toString()) +
                ", color=" + (color?"黑色":"红色") +
                ", num=" + (num==null?"NULL":num.toString()) +
                '}';
    }

    @Override
    public int compareTo(N o) {
        //Comparable<? super N> k = (Comparable<? super N>) this.num;
        //return k.compareTo(o);
        try {
            return ((Comparable<? super N>) num).compareTo(o);
        }catch (ClassCastException e){
            if(o instanceof Point){
                Comparable<? super N> k = (Comparable<? super N>) this.num;
                return k.compareTo(((N)((Point)o).num));
            }else{
                throw new ClassCastException();
            }




        }
    }
}
