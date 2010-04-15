/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.util;

/**
 *
 * @author Andreas
 */
public class MutableEntry<K, V> {
    private K k;
    private V v;

    public MutableEntry(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

    public void setKey(K k) {
        this.k = k;
    }

    public void setValue(V v) {
        this.v = v;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MutableEntry<K, V> other = (MutableEntry<K, V>) obj;
        if (this.k != other.k && (this.k == null || !this.k.equals(other.k))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.k != null ? this.k.hashCode() : 0);
        return hash;
    }
}
