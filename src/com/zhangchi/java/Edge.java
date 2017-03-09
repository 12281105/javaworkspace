package com.zhangchi.java;

/**
 * 一条边，可以根据需要继承此类
 * @param <V>
 */
public class Edge<V> {
    /**起点*/
    private V src;
    /**终点*/
    private V dest;
    /**权值*/
    private TriggerEvent triggerEvent;
    
    /**
     * 不带权值的一条边
     * @param src
     * @param dest
     */
    public Edge(V src, V dest) {
        this(src, dest, null);
    }
    
    /**
     * 带权值的一条边
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
     * 获取起点
     * @return
     */
    public V getSource() {
        return this.src;
    }
    
    /**
     * 获取终点
     * @return
     */
    public V getDest() {
        return this.dest;
    }
    
    /**
     * 获取触发事件
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
