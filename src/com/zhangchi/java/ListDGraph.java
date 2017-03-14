package com.zhangchi.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

/**
 * 邻接链表（Adjacency List）实现的有向图
 * @param <V>
 */
public class ListDGraph<V> implements DGraph<V>{

    /**
     * 顶点对象，其中有对应的顶点以及从以此顶点为起点的边
     */
    private class VE {
        /**此顶点*/
        private V v;
        /**以此顶点为起点的边的集合，是一个列表，列表的每一项是一条边*/
        private List<Edge<V>> mEdgeList;
        
        /**
         * 构造一个新的顶点对象
         * @param v
         */
        public VE(V v) {
            this.v = v;
            this.mEdgeList = new LinkedList<Edge<V>>();
            //Utils.log("VE construct : %s", v);
        }
        
        @Override
        public String toString() {
            String ret = String.format("v : %s , list len : %s",
                                       v, mEdgeList.size());
            return ret;
        }
        
        /**
         * 将一条边添加到边集合中
         * @param e
         */
        public void addEdge(Edge<V> e) {
            //Utils.log("add edge : %s", e);
            if(getEdge(e.getDest()) == null) {
                mEdgeList.add(e);
            } else {
                Utils.log("edge exist : %s", e);
            }
        }
        
        /**
         * 读取某条边
         * @param dest
         * @return
         */
        public Edge<V> getEdge(V dest) {
            Edge<V> ret = null;
            if(dest != null) {
                for(Edge<V> edge : mEdgeList) {
                    if(edge.getDest() != null &&
                       dest.equals(edge.getDest())) {
                        //Utils.log("get edge : %s", edge);
                        ret = edge;
                        break;
                    }
                }
            }
            return ret;
        }
        
        /**
         * 读取某条边
         * @param dest
         * @return
         */
        public Edge<V> removeEdge(V dest) {
            Edge<V> ret = null;
            if(dest != null) {
                for(Edge<V> edge : mEdgeList) {
                    if(edge.getDest() != null &&
                       dest.equals(edge.getDest())) {
                        //Utils.log("remove edge : %s", edge);
                        ret = edge;
                        mEdgeList.remove(edge);
                        break;
                    }
                }
            }
            return ret;
        }
    }

    /**
     * 广度优先的迭代器
     */
    private class BFSIterator implements Iterator<V> {
        /**已访问过的顶点列表*/
        private List<V> mVisitList = null;
        /**待访问的顶点队列*/
        private Queue<V> mVQueue = null;
        
        /**
         * 构造广度优先迭代器
         * @param dg
         * @param root
         */
        public BFSIterator(V root) {
            mVisitList = new LinkedList<V>();
            mVQueue = new LinkedList<V>();
            
            //将初始节点入队列
            mVQueue.offer(root);
        }
        
        @Override
        public boolean hasNext() {
            Utils.log("queue size : " + mVQueue.size());
            if(mVQueue.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public V next() {
            //1.取队列元素
            V v = mVQueue.poll();
            
            if(v != null) {
                //2.将此元素的邻接边中对应顶点入队列，这些顶点需要符合以下条件：
                //1)没访问过；
                //2)不在队列中；
                VE ve = getVE(v);
                if(ve != null) {
                    List<Edge<V>> list = ve.mEdgeList;
                    for(Edge<V> edge : list) {
                        V dest = edge.getDest();
                        if(!VinList(dest, mVisitList.iterator()) &&
                           !VinList(dest, mVQueue.iterator())) {
                            mVQueue.offer(dest);
                            //Utils.log("add to queue : " + dest);
                        }
                    }
                }
                
                //3.将此顶点添加到已访问过的顶点列表中
                mVisitList.add(v);
            }
            
            //4.返回出队列的元素
            return v;
        }

        @Override
        public void remove() {
            // 暂时不实现
        }
        
    }
    
    /**顶点列表，由于会经常进行插入删除，使用链表队列*/
    private LinkedList<VE> mVEList;
    
    /**
     * 构造邻接链表有向图
     */
    public ListDGraph() {
        mVEList = new LinkedList<VE>();
        //Utils.log("ListDGraph construct!");
    }
    
    @Override
    public int add(V v) {
        int index = -1;
        if(v != null) {
            //Utils.log("add v: %s", v);
            VE list = new VE(v);
            mVEList.add(list);
            index = mVEList.indexOf(list);
        }
        return index;
    }

    @Override
    public void add(Edge<V> e) {
        if(e != null) {
            //Utils.log("add edge: %s", e);
            VE ve = getVE(e.getSource());
            if(ve != null) {
                //若边的起点已经在列表里，则直接将其添加到对应的顶点对象中
                ve.addEdge(e);
            } else {
                //否则提示错误
                Utils.log("Error, can't find v : %s", e.getSource());
            }
        }
    }
    
    @Override
    public V remove(V v) {
        V ret = null;
        
        VE ve = removeVE(v);
        if(ve != null) {
            ret = ve.v;
        }
        
        removeRelateEdge(v);
        
        return ret;
    }

    @Override
    public Edge<V> remove(Edge<V> e) {
        Edge<V> ret = null;
        
        if(e != null) {
            VE ve = getVE(e.getSource());
            if(ve != null) {
                ret = ve.removeEdge(e.getDest());
            }
        }
        
        return ret;
    }

    @Override
    public V get(int index) {
        V ret = null;
        if(index >=0 && index < mVEList.size()) {
            VE ve = mVEList.get(index);
            if(ve != null) {
                ret = ve.v;
                //Utils.log("get , index : %s , v : %s", index, ret);
            }
        }
        return ret;
    }
    
    public int getIndex(V vtx){
    	for (VE ve : mVEList) {
			if(ve.v.equals(vtx)){
				return mVEList.indexOf(ve);
			}
		}
    	return -1;
    }

    @Override
    public Edge<V> get(int src, int dest) {
        Edge<V> ret = null;
        V s = get(src);
        V d = get(dest);
        if(s != null && d != null) {
            VE ve = getVE(s);
            if(ve != null) {
                ret = ve.getEdge(d);
            }
        }
        return ret;
    }

    @Override
    public Iterator<V> iterator(int type, V root) {
        Iterator<V> ret = null;
        if(type == ITERATOR_TYPE_BFS) {
            //广度优先的迭代器
            ret = new BFSIterator(root);
        } else if(type == ITERATOR_TYPE_DFS){
            //深度优先的迭代器，暂时未实现
        } else {
            //...
        }
        return ret;
    }

    @Override
    public void convertDAG() {
        // TODO Auto-generated method stub
        
    }

    //////////////////////////////私有方法//////////////////////////////
    /**
     * 从顶点对象列表中读取输入顶点对应的对象
     * @param v
     * @return
     */
    public VE getVE(V v) {
        VE ret = null;
        if(v != null) {
            for(VE ve : mVEList) {
                if(ve.v != null && v.equals(ve.v)) {
                    //Utils.log("getVE : %s", ve);
                    ret = ve;
                    break;
                }
            }
        }
        return ret;
    }
    
    /**
     * 从顶点对象列表中删除输入顶点对应的对象
     * @param v
     * @return 删除的顶点对象
     */
    public VE removeVE(V v) {
        VE ret = null;
        if(v != null) {
            for(VE ve : mVEList) {
                if(ve.v != null && v.equals(ve.v)) {
                    Utils.log("removeVE : %s", v);
                    ret = ve;
                    mVEList.remove(ve);
                    break;
                }
            }
        }
        return ret;
    }
    
    //检测有向图中环的个数
    public LinkedList<LinkedList<Edge<V>>> checkCircles(){
    	boolean[] instack = new boolean[mVEList.size()]; //已经访问的数组
    	int[] stack = new int[mVEList.size()];
    	int top=-1;
    	int count = 0;
    	LinkedList<LinkedList<V>> circles = new LinkedList<LinkedList<V>>();
    	LinkedList<LinkedList<Edge<V>>> edgecircles = new LinkedList<LinkedList<Edge<V>>>();
    	DFS_CheckCircle(0,top,instack,stack,circles,count);
    	//将以状态表示的环转换成用边表示的环
    	for (LinkedList<V> linkedList : circles) {
    		LinkedList<Edge<V>> tempEdgecircle = new LinkedList<Edge<V>>();
			for (int i = 0; i < (linkedList.size()-1); i++) {
				tempEdgecircle.add(get(getIndex(linkedList.get(i)), getIndex(linkedList.get(i+1))));
			}
			tempEdgecircle.add(get(getIndex(linkedList.get(linkedList.size()-1)), getIndex(linkedList.get(0))));
			edgecircles.add(tempEdgecircle);
		}
    	
    	
    	//剔除环列表中重复的环
    	for (LinkedList<Edge<V>> linkedList : edgecircles) {
			Collections.sort(linkedList,new Comparator<Edge<V>>() {

				@Override
				public int compare(Edge<V> o1, Edge<V> o2) {
					// TODO Auto-generated method stub
					return o1.getEventnum()-o2.getEventnum();
				}
			});
		}
    	HashSet<LinkedList<Edge<V>>> edgeSet = new HashSet<LinkedList<Edge<V>>>(edgecircles);
    
    
    	return new LinkedList<LinkedList<Edge<V>>>(edgeSet);
    }
    
    private void DFS_CheckCircle(int x, int top, boolean[] instack, int[] stack, LinkedList<LinkedList<V>> circle,int count) {
		// TODO Auto-generated method stub
        stack[++top]=x;  
        instack[x]=true;
        VE ve = getVE(get(x));
        for (int i=0;i<ve.mEdgeList.size();i++)  
        {
        	//System.out.println("num:"+ve.mEdgeList.get(i).getDest());
        	int index = getIndex(ve.mEdgeList.get(i).getDest());
            if (!instack[index])  
            {
            	DFS_CheckCircle(index,top,instack,stack,circle,count);  
            }  
            else //条件成立，表示下标为x的顶点到 下标为i的顶点有环  
            {  
                //从i到x是一个环，top的位置是x，下标为i的顶点在栈中的位置要寻找一下  
                //寻找起始顶点下标在栈中的位置
            	count++;
            	LinkedList<V> circlepath = new LinkedList<V>();
                int t=0;  
                for (t=top;stack[t]!=index;t--);  
                //输出环中顶点  
                for (int j=t;j<=top;j++)  
                {
                	circlepath.add(get(stack[j]));
                	//System.out.println("index:"+index+"--->"+stack[j]+"=="+get(stack[j]));
                }
                //System.out.println("circle"+count+":"+circlepath.toString());
                circle.add(circlepath);
            }  
        }  
        //处理完结点后，退栈  
        top--;  
        instack[x]=false;  
	}
    
    
    public LinkedList<LinkedList<Edge<V>>> DFS_ALL_LOOP_Travel(V root){
    	Map<Edge<V>, Integer> limitCountMap = new HashMap();
    	LinkedList<LinkedList<Edge<V>>> edgeCircles = checkCircles();
    	//添加每条边出现的次数限制条件
    	for (LinkedList<Edge<V>> linkedList : edgeCircles) {
			for (Edge<V> edge : linkedList) {
				if(limitCountMap.containsKey(edge)){
					limitCountMap.put(edge, limitCountMap.get(edge)+1);
				}
				else{
					limitCountMap.put(edge, 1);
				}
			}
		}
    	
    	for (VE ve : mVEList) {
			for (Edge<V> edge : ve.mEdgeList) {
				if (limitCountMap.containsKey(edge)) {
					
				}
				else{
					limitCountMap.put(edge, 1);
				}
			}
		}    	
    	
    	LinkedList<LinkedList<Edge<V>>> retList = new LinkedList<LinkedList<Edge<V>>>();
    	VE rootVE = getVE(root);
    	LinkedList<Edge<V>> startEdges = new LinkedList<Edge<V>>(rootVE.mEdgeList);
    	LinkedList<LinkedList<Edge<V>>> historyList = new LinkedList<LinkedList<Edge<V>>>();
    	//LinkedList<LinkedList<Edge<V>>> historyList = new LinkedList<LinkedList<Edge<V>>>();
    	Stack<Edge<V>> mstack = new Stack<>();
		
    	Collections.sort(startEdges,new Comparator<Edge<V>>() {

			@Override
			public int compare(Edge<V> o1, Edge<V> o2) {
				// TODO Auto-generated method stub
				return o1.getEventnum()-o2.getEventnum();
			}
		});
    	
		
		for(Edge<V> sedge : startEdges){
			//将第一条编号最小的边压入栈和历史记录列表中
			mstack.clear();
			historyList.clear();
			historyList.add(new LinkedList<Edge<V>>());
	    	mstack.push(sedge);
			historyList.getLast().add(sedge);
			historyList.add(new LinkedList<Edge<V>>());
			
			while (true) {
				Edge<V> topEdge = mstack.peek(); //栈顶的边对象
				VE topVE = getVE(topEdge.getDest()); //找栈顶边对象的下一个可访问边
				
				//回溯有两种情况：
				//(1)栈中某个边被访问的次数超过了3次（>3），回溯两步，弹出栈顶2次
					//每条边的限制访问次数根据有向图中环的个数确定，可能有些边处于多个环中，限制访问次数成倍增加
				//(2)访问到达路径的终点，回溯一步，弹出栈顶1次
				
				if(topVE.mEdgeList.size()==0){
					//第一种情况下的回溯
					/*
					for (LinkedList<Edge<V>> linkedList : historyList) {
						for (Edge<V> edge : linkedList) {
							System.out.print(edge.getEventnum()+" ");
						}
						System.out.println();
					}
					*/
					
					LinkedList<Edge<V>> tempList = new LinkedList<Edge<V>>();
					for (Edge<V> edge : mstack) {
						tempList.add(edge);
					}
					retList.add(tempList);
					mstack.pop();
					continue ;
				}
				
				//可访问边：未在历史记录列表中出现过，且能到达
				
				
				//可访问边的访问优先级：
				//(1)未访问边（访问次数为0）的优先级最高（按边的编号从小到大访问）
				//(2)访问次数少的边优先级较高（按访问次数从小到大访问）
				
				//countMap 统计栈中各条边被访问的次数,按访问次数进行排序
				//countAccessMap 统计可访问边被访问次数
				Map<Edge<V>, Integer> countMap = new HashMap<>();
				Map<Edge<V>, Integer> countAccessMap = new HashMap<>();
				Iterator<Edge<V>> iterator = mstack.iterator();
				while (iterator.hasNext()) {
					Edge<V> edge = (Edge<V>) iterator.next();
					if(countMap.containsKey(edge)){
						countMap.put(edge, countMap.get(edge)+1);
					}
					else{
						countMap.put(edge, 1);
					}
					if(topVE.mEdgeList.contains(edge)){
						if(countAccessMap.containsKey(edge)){
							countAccessMap.put(edge, countAccessMap.get(edge)+1);
						}
						else{
							countAccessMap.put(edge, 1);
						}
					}
				}
				
				
				for (Edge<V> edge : topVE.mEdgeList) {
					if(historyList.get(mstack.size()).contains(edge)){
						countAccessMap.remove(edge);
					}
					else{
						if(countAccessMap.containsKey(edge)){
							
						}
						else {
							countAccessMap.put(edge, 0);
						}
					}
				}
				
				//(1)栈中某个边被访问的次数超过了3次（>3），回溯两步，弹出栈顶2次
				boolean flag = false;
				for (Map.Entry<Edge<V>, Integer> entry : countMap.entrySet()) {
					if(entry.getValue() > limitCountMap.get(entry.getKey())*1){
						mstack.pop();
						historyList.remove(mstack.size());
						mstack.pop();
						flag = true;
						break ;
					}
				}
				if(flag){
					continue ;
				}
				
				
				//如果可访问边的个数为0,coutMap为空，则进行回溯
				if(countAccessMap.size()==0){
					if(mstack.size()==1){
						break;
					}
					else{
						historyList.remove(mstack.size());
						mstack.pop();
						continue ;
					}
				}
				
				
				//对访问边进行排序，访问次数从小到大，边的编号从小到大
				List<Map.Entry<Edge<V>, Integer>> countEntryList = new ArrayList<>(countAccessMap.entrySet());
				Collections.sort(countEntryList, new Comparator<Map.Entry<Edge<V>, Integer>>() {

					@Override
					public int compare(Entry<Edge<V>, Integer> o1, Entry<Edge<V>, Integer> o2) {
						// TODO Auto-generated method stub
						return (o1.getValue()-o2.getValue()==0)?(o1.getKey().getEventnum()-o2.getKey().getEventnum()):(o1.getValue()-o2.getValue());
					}				
				});
				
				//进栈的原则：不能加入历史记录列表中已经访问的边，可访问边按优先级进栈
				Edge<V> chooseEdge = countEntryList.get(0).getKey();

				historyList.get(mstack.size()).add(chooseEdge);
				if((mstack.size()+1)==historyList.size()){
					historyList.add(new LinkedList<Edge<V>>());
				}
				mstack.push(chooseEdge);
				
			}
			
		}

		return retList;
	}
	
    
    /**
     * 删除以某个点作为重点的边
     * @param v
     */
    private void removeRelateEdge(V v) {
        if(v != null) {
            for(VE ve : mVEList) {
                ve.removeEdge(v);
            }
        }
    }
    
    /**
     * 判断某个端点是否在某个列表里
     * @param v
     * @param it
     * @return
     */
    private boolean VinList(V v, Iterator<V> it) {
        boolean ret = false;
        
        if(v != null && it != null) {
            while(it.hasNext()) {
                V v_temp = it.next();
                if(v_temp != null && v_temp.equals(v)) {
                    ret = true;
                    break;
                }
            }
        }
        
        return ret;
    }
}