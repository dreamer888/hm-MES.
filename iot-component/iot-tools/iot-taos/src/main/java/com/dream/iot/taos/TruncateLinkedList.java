package com.dream.iot.taos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TruncateLinkedList<E>{

    private int size;
    private Node<E> first;
    private Node<E> last;

    public synchronized void addLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    public synchronized void addAll(Collection<E> list) {
        list.forEach(item -> this.addLast(item));
    }

    public List<E> subList(int formIndex, int toIndex) {
        if(formIndex >= this.size || this.size < toIndex) {
            throw new IndexOutOfBoundsException(formIndex + ">" + this.size);
        }

        int index = 0;
        List<E> list = new ArrayList<>(toIndex - formIndex);
        Node<E> current = first;
        while (index < toIndex) {
            if(index >= formIndex) {
                list.add(current.item);
            }

            index ++;
            current = current.next;
        }

        return list;
    }

    /**
     * 截取
     * @param formIndex 从此索引还是截取(包含forIndex)
     * @return
     */
    public synchronized void truncate(int formIndex) {
        if(formIndex == this.size) {
            first = last = null;
            this.size = 0;
            return;
        }

        if(formIndex > this.size) {
            throw new IndexOutOfBoundsException(formIndex + ">" + this.size);
        }


        int index = 0;
        Node<E> current = first;
        while (index <= formIndex) {

            if(index == formIndex) {
                current.prev = null;
                this.first = current;
                this.size -= formIndex;
                return;
            }

            index ++;
            current = current.next;
        }
    }

    public int size() {
        return size;
    }

    public Node<E> getFirst() {
        return first;
    }

    public Node<E> getLast() {
        return last;
    }

    @Override
    public String toString() {
        return "size="+this.size;
    }

    class Node<E> {
        private E item;
        private Node<E> prev;
        private Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
