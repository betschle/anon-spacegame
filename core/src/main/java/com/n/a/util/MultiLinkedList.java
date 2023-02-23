package com.n.a.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Mult-Linked list with multiple parents and multiple children.
 * Capable to carry a value, T.
 * @param <T> the value to be stored
 */
public class MultiLinkedList<T> {

    private Node<T> root;

    public static class Node<T> {
        private T value;
        private List<Node> parents = new ArrayList<>();
        private List<Node> children = new ArrayList<>();

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public void addParent(Node<T> node) {
            if(Objects.equals(this, node)) return;
            this.parents.add(node);
            if( !node.containsChild(this)) {
                node.addChild(this);
            }
        }

        public void removeParent(Node<T> node) {
            if(Objects.equals(this, node)) return;
            this.children.remove(node);
            if( node.containsParent(node) ) {
                node.removeParent(node);
            }
        }

        public void removeParentAt(int index) {
            Node<T> removed = this.children.remove(index);
            if( this.containsParent(removed) ) {
                removed.removeParent(this);
            }
        }

        public Node<T> getParentAt(int index) {
            return this.parents.get(index);
        }

        public int getParentCount() {
            return this.parents.size();
        }

        public boolean containsParent(Node<T> parent) {
            return this.parents.contains(parent);
        }

        public void addChild(Node<T> node) {
            if(Objects.equals(this, node)) return;
            this.children.add(node);
            if( !node.containsParent(this) ) {
                node.addParent(this);
            }
        }

        public void removeChild(Node<T> node) {
            if(Objects.equals(this, node)) return;
            this.children.remove(node);
            if( node.containsParent(this) ) {
                node.parents.remove(this);
            }
        }

        public void removeChildAt(int index) {
            Node<T> removed = this.children.remove(index);
            if( removed.containsParent(this) ) {
                removed.parents.remove(this);
            }
        }

        public Node<T> getChildAt(int index) {
            return this.children.get(index);
        }

        public int getChildrenCount() {
            return this.children.size();
        }

        public boolean containsChild(Node<T> child) {
            return this.children.contains(child );
        }

        @Override
        public String toString() {
            return "Node{" +
                    " value=" + value +
                    ", children=" + children +
                    "}\n";
        }
    }

    public void setRoot(Node<T> node) {
        this.root = node;
    }

    public static <T> Node<T> getNode( T parent, T... children) {
        Node<T> node = new Node<>();
        node.setValue(parent);
        for( T child : children) {
            Node<T> childNode = new Node<>();
            childNode.setValue(child);
            node.addChild(childNode);
        }
        return node;
    }

    @Override
    public String toString() {
        return "MultiLinkedList{" +
                "root=" + root +
                '}';
    }

    public static void main(String[] args) {
        MultiLinkedList<String> list = new MultiLinkedList<>();
        Node<String> node = getNode("Humans", "People", "Children", "Elderly");
        Node<String> people = node.getChildAt(0);
        Node<String> elderly = node.getChildAt(2);

        Node<String> magneto = getNode("Magneto");
        Node<String> villains = getNode("Villains", "Joker", "Doc Oc");
        villains.addChild(magneto);
        elderly.addChild(magneto);

        people.addChild( getNode("Heroes", "Superman", "Spiderman"));
        people.addChild( villains );
        people.addChild( getNode("Neutrals", "Batman", "Megamind"));

        list.setRoot(node);
        System.out.println(list);
    }
}