package com.zhangchi.java;

/**
 * һ���ߣ����Ը�����Ҫ�̳д���
 * @param <V>
 */
public class Edge<V> {
    /**���*/
    private V src;
    /**�յ�*/
    private V dest;
    /**Ȩֵ*/
    private TriggerEvent triggerEvent;
    
    /**
     * ����Ȩֵ��һ����
     * @param src
     * @param dest
     */
    public Edge(V src, V dest) {
        this(src, dest, null);
    }
    
    /**
     * ��Ȩֵ��һ����
     * @param src
     * @param dest
     * @param weight
     */
    public Edge(V src, V dest, TriggerEvent triggerEvent) {
        this.src = src;
        this.dest = dest;
        this.triggerEvent = triggerEvent;
    }
    
    /**
     * ��ȡ���
     * @return
     */
    public V getSource() {
        return this.src;
    }
    
    /**
     * ��ȡ�յ�
     * @return
     */
    public V getDest() {
        return this.dest;
    }
    
    /**
     * ��ȡ�����¼�
     * @return
     */
    public TriggerEvent getTriggerEvent() {
        return this.triggerEvent;
    }
    
    @Override
    public String toString() {
        String ret = String.format("src : %s , dest : %s ", src, dest);
        return ret;
    }
}
