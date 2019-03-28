package com.tufer;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

public class MyRBTree<N> {
    public static final boolean RED = false;
    public static final boolean BLACK = true;
    private Point<N> root;
    private int size;
    private Comparator<? super N> comparator;
    private Set<Point<N>> rbTrees = new LinkedHashSet<>();

    public MyRBTree() {
        this.comparator = null;
    }


    public MyRBTree(Comparator<? super N> comparator) {
        this.comparator = comparator;
    }

    /**
     * From CLR 左旋转
     */
    private void rotateLeft(Point<N> p) {
        if (p != null) {
            Point r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /**
     * From CLR 右旋转
     */
    private void rotateRight(Point<N> p) {
        if (p != null) {
            Point<N> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    /**
     * From CLR 添加节点时通过变色或者旋转来达到平衡二叉树
     */
    private void fixAfterInsertion(Point<N> x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Point<N> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Point<N> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    /**
     * From CLR 删除节点后通过变色或者旋转来达到平衡二叉树
     */
    private void fixAfterDeletion(Point<N> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Point<N> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                Point<N> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    private Point<N> parentOf(Point<N> p) {
        return (p == null ? null : p.parent);
    }

    private boolean colorOf(Point<N> p) {
        return (p == null ? BLACK : p.color);
    }

    private void setColor(Point<N> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private Point<N> leftOf(Point<N> p) {
        return (p == null) ? null : p.left;
    }

    private Point<N> rightOf(Point<N> p) {
        return (p == null) ? null : p.right;
    }

    /**
     * 添加节点到树中
     * @param p 添加的节点
     */
    public void put(Point<N> p) {
        Point<N> t = root;
        if (t == null) {
            compare(p.num, p.num); // type (and possibly null) check

            root = new Point<N>(p.num, null);
            size = 1;
            return;
        }
        int cmp;
        Point<N> parent;
        // split comparator and comparable paths
        Comparator<? super N> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(p.num, t.num);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return;
            } while (t != null);
        } else {
            Comparable<? super N> k = (Comparable<? super N>) p.num;
            do {
                parent = t;
                cmp = k.compareTo(t.num);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return;
            } while (t != null);
        }
        Point<N> e = new Point<N>(p.num, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
    }

    /**
     * 直接添加数据到节点中
     * @param n 数据
     */
    public void put(N n){
        Point<N> point = new Point<N>(n);
        put(point);
    }

    /**
     * 删除指定节点
     * @param o 节点
     * @return 删除是否成功
     */
    public boolean remove(Object o) {
        Point<N> p = getPoint(o);
        if (p == null)
            return false;
        deleteEntry(p);
        return true;
    }

    /**
     * 删除指定节点
     * @param p 节点
     */
    private void deleteEntry(Point<N> p) {
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Point<N> s = successor(p);
            p.num = s.num;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Point<N> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    /**
     *返回待删除项的后续项，如果没有，则返回空值。
     * @param t 待删除项
     * @return 待删除项的后续项
     */
    private Point<N> successor(Point<N> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Point<N> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Point<N> p = t.parent;
            Point<N> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * 清空树中的元素
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * 比较器
     * @param k1 比较前因子
     * @param k2 比较后因子
     * @return 比较结果 （负数为k1<k2,正数为k1>k2）
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator == null ? ((Comparable<? super N>) k1).compareTo((N) k2)
                : comparator.compare((N) k1, (N) k2);
    }

    /**
     * 返回树中节点个数
     * @return 树中节点个数
     */
    public int size() {
        return size;
    }

    /**
     * 返回根节点
     * @return 根节点
     */
    public Point getRoot() {
        return root;
    }

    /**
     * 按中序遍历树
     * @param root 根节点
     */
    private void preOrder(Point<N> root) {

        if (null != root) {
            rbTrees.add(root);
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    /**
     * 获取树中所有节点按中序排序
     * @return
     */
    public Set<Point<N>> getRBTrees() {
        preOrder(root);
        return rbTrees;
    }

    /**
     * 查找节点并返回（默认的比较器）
     * @param o 查找的数据
     * @return 返回数据对应的节点
     */
    public final Point<N> getPoint(Object o) {
        // Offload comparator-based version for sake of performance
        if (comparator != null)
            return getEntryUsingComparator(o);
        if (o == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super N> k = (Comparable<? super N>) o;
        Point<N> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.num);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    /**
     * 判断
     * @param o
     * @return
     */
    public final boolean containsPoint(Object o){
        return getPoint(o)!=null;
    }

    /**
     * 使用自定义的比较器查找
     * @param o 查找的数据
     * @return 返回数据对应的节点
     */
    final Point<N> getEntryUsingComparator(Object o) {
        @SuppressWarnings("unchecked")
        N n = (N) o;
        Comparator<? super N> cpr = comparator;
        if (cpr != null) {
            Point<N> p = root;
            while (p != null) {
                int cmp = cpr.compare(n, p.num);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }

}
