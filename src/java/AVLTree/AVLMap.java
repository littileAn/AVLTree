package AVLTree;

import java.io.Serializable;

/**
 * Created by azj on 2018/3/8.
 */
public class AVLMap<K extends Comparable, V> implements Serializable {

    public class Node {
        K key;
        V value;
        int height;
        Node leftChild;
        Node rightChild;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

    Node root;

    /**
     * 获取以某个节点为AVL树的高度
     *
     * @param node
     * @return
     */
    private int getHeight(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private Node leftRotation(Node node) {
        Node child = node.rightChild;
        node.rightChild = child.leftChild;
        child.leftChild = node;

        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        child.height = Math.max(getHeight(child.leftChild), getHeight(child.rightChild)) + 1;

        return child;
    }

    private Node rightRotation(Node node) {
        Node child = node.leftChild;
        node.leftChild = child.rightChild;
        child.rightChild = node;

        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        child.height = Math.max(getHeight(child.leftChild), getHeight(child.rightChild)) + 1;

        return child;
    }

    private Node rightLeftRotation(Node node) {
        node.rightChild = rightRotation(node.rightChild);
        return leftRotation(node);
    }

    private Node leftRightRotation(Node node) {
        node.leftChild = leftRotation(node.leftChild);
        return rightRotation(node);
    }

    private Node insert(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key.compareTo(node.key) == 0) {
            node.value = value;
        } else if (key.compareTo(node.key) > 0) {
            //插入到右子树中
            node.rightChild = insert(node.rightChild, key, value);
            if (getHeight(node.rightChild) - getHeight(node.leftChild) == 2) {
                if (key.compareTo(node.rightChild.key) > 0)
                    node = leftRotation(node);
                else if (key.compareTo(node.rightChild.key) < 0)
                    node = rightLeftRotation(node);
            }
        } else {
            //插入到左子树中
            node.leftChild = insert(node.leftChild, key, value);
            if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
                if (key.compareTo(node.leftChild.key) < 0)
                    node = rightRotation(node);
                else if (key.compareTo(node.leftChild.key) > 0)
                    node = leftRightRotation(node);
            }
        }
        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        return node;
    }

    private Node delete(Node node, K key) {
        if (node != null) {
            if (key.compareTo(node.key) == 0) {
                if (node.leftChild != null && node.rightChild != null) {
                    if (getHeight(node.leftChild) > getHeight(node.rightChild)) {
                        Node pNode = getMax(node.leftChild);
                        node.key = pNode.key;
                        node.leftChild = delete(node.leftChild,pNode.key);
                    } else {
                        Node pNode = getMin(node.rightChild);
                        node.key = pNode.key;
                        node.rightChild = delete(node.rightChild, node.key);
                    }
                } else {
                    Node pNode = node;
                    if (node.leftChild != null)
                        node = node.leftChild;
                    else if (node.rightChild != null)
                        node = node.rightChild;
                    pNode = null;
                    return null;
                }
            } else if (key.compareTo(node.key) > 0) {
                node.rightChild = delete(node.rightChild, key);
                if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
                    if (getHeight(node.leftChild.rightChild) > getHeight(node.leftChild.leftChild)) {
                        node = leftRightRotation(node);
                    } else {
                        node = rightRotation(node);
                    }
                }
            } else {
                node.leftChild = delete(node.leftChild, key);
                if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
                    if (getHeight(node.rightChild.leftChild) > getHeight(node.rightChild.rightChild)) {
                        node = rightLeftRotation(node);
                    } else {
                        node = leftRotation(node);
                    }
                }
            }
        }
        return null;
    }

    private Node find(Node node, K key) {
        while (node != null) {
            if (key.compareTo(node.key) == 0) {
                return node;
            } else if (key.compareTo(node.key) > 0) {
                node = node.rightChild;
            } else {
                node = node.leftChild;
            }
        }
        return null;
    }

    private Node getMax(Node node){
        while (node != null) {
            if (node.rightChild == null)
                return node;
            node = node.rightChild;
        }
        return null;
    }

    private Node getMin(Node node){
        while (node != null) {
            if (node.leftChild == null)
                return node;
            node = node.leftChild;
        }
        return null;
    }

    /**
     * 获取整颗AVL树的高低
     *
     * @return
     */
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * 查找以key为根节点的AVL树的高度
     *
     * @param key
     * @return
     */
    public int getHeight(K key) {
        Node node = find(root, key);
        return node == null ? -1 : getHeight(node);
    }

    /**
     * 查找key是否存在
     *
     * @param key
     * @return
     */
    public boolean find(K key) {
        Node node = find(root, key);
        return node == null ? false : true;
    }

    /**
     * 插入节点
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    public boolean remove(K key) {
        Node node = delete(root, key);
        return node == null ? false : true;
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    public V get(K key) {
        Node node = find(root, key);
        return node == null ? null : node.value;
    }

    /**
     * 获取整颗树中的最大值
     *
     * @return
     */
    public V max() {
        return getMax(root).value;
    }

    /**
     * 获取整颗树中的最小值
     *
     * @return
     */
    public V min() {
        return getMin(root).value;
    }
}