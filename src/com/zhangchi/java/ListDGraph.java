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
 * �ڽ�����Adjacency List��ʵ�ֵ�����ͼ
 * @param <V>
 */
public class ListDGraph<V> implements DGraph<V>{

    /**
     * ������������ж�Ӧ�Ķ����Լ����Դ˶���Ϊ���ı�
     */
    private class VE {
        /**�˶���*/
        private V v;
        /**�Դ˶���Ϊ���ıߵļ��ϣ���һ���б��б��ÿһ����һ����*/
        private List<Edge<V>> mEdgeList;
        
        /**
         * ����һ���µĶ������
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
         * ��һ������ӵ��߼�����
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
         * ��ȡĳ����
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
         * ��ȡĳ����
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
     * ������ȵĵ�����
     */
    private class BFSIterator implements Iterator<V> {
        /**�ѷ��ʹ��Ķ����б�*/
        private List<V> mVisitList = null;
        /**�����ʵĶ������*/
        private Queue<V> mVQueue = null;
        
        /**
         * ���������ȵ�����
         * @param dg
         * @param root
         */
        public BFSIterator(V root) {
            mVisitList = new LinkedList<V>();
            mVQueue = new LinkedList<V>();
            
            //����ʼ�ڵ������
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
            //1.ȡ����Ԫ��
            V v = mVQueue.poll();
            
            if(v != null) {
                //2.����Ԫ�ص��ڽӱ��ж�Ӧ��������У���Щ������Ҫ��������������
                //1)û���ʹ���
                //2)���ڶ����У�
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
                
                //3.���˶�����ӵ��ѷ��ʹ��Ķ����б���
                mVisitList.add(v);
            }
            
            //4.���س����е�Ԫ��
            return v;
        }

        @Override
        public void remove() {
            // ��ʱ��ʵ��
        }
        
    }
    
    /**�����б����ڻᾭ�����в���ɾ����ʹ���������*/
    private LinkedList<VE> mVEList;
    
    /**
     * �����ڽ���������ͼ
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
                //���ߵ�����Ѿ����б����ֱ�ӽ�����ӵ���Ӧ�Ķ��������
                ve.addEdge(e);
            } else {
                //������ʾ����
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
            //������ȵĵ�����
            ret = new BFSIterator(root);
        } else if(type == ITERATOR_TYPE_DFS){
            //������ȵĵ���������ʱδʵ��
        } else {
            //...
        }
        return ret;
    }

    @Override
    public void convertDAG() {
        // TODO Auto-generated method stub
        
    }

    //////////////////////////////˽�з���//////////////////////////////
    /**
     * �Ӷ�������б��ж�ȡ���붥���Ӧ�Ķ���
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
     * �Ӷ�������б���ɾ�����붥���Ӧ�Ķ���
     * @param v
     * @return ɾ���Ķ������
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
    
    //�������ͼ�л��ĸ���
    public LinkedList<LinkedList<Edge<V>>> checkCircles(){
    	boolean[] instack = new boolean[mVEList.size()]; //�Ѿ����ʵ�����
    	int[] stack = new int[mVEList.size()];
    	int top=-1;
    	int count = 0;
    	LinkedList<LinkedList<V>> circles = new LinkedList<LinkedList<V>>();
    	LinkedList<LinkedList<Edge<V>>> edgecircles = new LinkedList<LinkedList<Edge<V>>>();
    	DFS_CheckCircle(0,top,instack,stack,circles,count);
    	//����״̬��ʾ�Ļ�ת�����ñ߱�ʾ�Ļ�
    	for (LinkedList<V> linkedList : circles) {
    		LinkedList<Edge<V>> tempEdgecircle = new LinkedList<Edge<V>>();
			for (int i = 0; i < (linkedList.size()-1); i++) {
				tempEdgecircle.add(get(getIndex(linkedList.get(i)), getIndex(linkedList.get(i+1))));
			}
			tempEdgecircle.add(get(getIndex(linkedList.get(linkedList.size()-1)), getIndex(linkedList.get(0))));
			edgecircles.add(tempEdgecircle);
		}
    	
    	
    	//�޳����б����ظ��Ļ�
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
            else //������������ʾ�±�Ϊx�Ķ��㵽 �±�Ϊi�Ķ����л�  
            {  
                //��i��x��һ������top��λ����x���±�Ϊi�Ķ�����ջ�е�λ��ҪѰ��һ��  
                //Ѱ����ʼ�����±���ջ�е�λ��
            	count++;
            	LinkedList<V> circlepath = new LinkedList<V>();
                int t=0;  
                for (t=top;stack[t]!=index;t--);  
                //������ж���  
                for (int j=t;j<=top;j++)  
                {
                	circlepath.add(get(stack[j]));
                	//System.out.println("index:"+index+"--->"+stack[j]+"=="+get(stack[j]));
                }
                //System.out.println("circle"+count+":"+circlepath.toString());
                circle.add(circlepath);
            }  
        }  
        //�����������ջ  
        top--;  
        instack[x]=false;  
	}
    
    
    public LinkedList<LinkedList<Edge<V>>> DFS_ALL_LOOP_Travel(V root){
    	Map<Edge<V>, Integer> limitCountMap = new HashMap();
    	LinkedList<LinkedList<Edge<V>>> edgeCircles = checkCircles();
    	//���ÿ���߳��ֵĴ�����������
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
			//����һ�������С�ı�ѹ��ջ����ʷ��¼�б���
			mstack.clear();
			historyList.clear();
			historyList.add(new LinkedList<Edge<V>>());
	    	mstack.push(sedge);
			historyList.getLast().add(sedge);
			historyList.add(new LinkedList<Edge<V>>());
			
			while (true) {
				Edge<V> topEdge = mstack.peek(); //ջ���ı߶���
				VE topVE = getVE(topEdge.getDest()); //��ջ���߶������һ���ɷ��ʱ�
				
				//���������������
				//(1)ջ��ĳ���߱����ʵĴ���������3�Σ�>3������������������ջ��2��
					//ÿ���ߵ����Ʒ��ʴ�����������ͼ�л��ĸ���ȷ����������Щ�ߴ��ڶ�����У����Ʒ��ʴ����ɱ�����
				//(2)���ʵ���·�����յ㣬����һ��������ջ��1��
				
				if(topVE.mEdgeList.size()==0){
					//��һ������µĻ���
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
				
				//�ɷ��ʱߣ�δ����ʷ��¼�б��г��ֹ������ܵ���
				
				
				//�ɷ��ʱߵķ������ȼ���
				//(1)δ���ʱߣ����ʴ���Ϊ0�������ȼ���ߣ����ߵı�Ŵ�С������ʣ�
				//(2)���ʴ����ٵı����ȼ��ϸߣ������ʴ�����С������ʣ�
				
				//countMap ͳ��ջ�и����߱����ʵĴ���,�����ʴ�����������
				//countAccessMap ͳ�ƿɷ��ʱ߱����ʴ���
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
				
				//(1)ջ��ĳ���߱����ʵĴ���������3�Σ�>3������������������ջ��2��
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
				
				
				//����ɷ��ʱߵĸ���Ϊ0,coutMapΪ�գ�����л���
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
				
				
				//�Է��ʱ߽������򣬷��ʴ�����С���󣬱ߵı�Ŵ�С����
				List<Map.Entry<Edge<V>, Integer>> countEntryList = new ArrayList<>(countAccessMap.entrySet());
				Collections.sort(countEntryList, new Comparator<Map.Entry<Edge<V>, Integer>>() {

					@Override
					public int compare(Entry<Edge<V>, Integer> o1, Entry<Edge<V>, Integer> o2) {
						// TODO Auto-generated method stub
						return (o1.getValue()-o2.getValue()==0)?(o1.getKey().getEventnum()-o2.getKey().getEventnum()):(o1.getValue()-o2.getValue());
					}				
				});
				
				//��ջ��ԭ�򣺲��ܼ�����ʷ��¼�б����Ѿ����ʵıߣ��ɷ��ʱ߰����ȼ���ջ
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
     * ɾ����ĳ������Ϊ�ص�ı�
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
     * �ж�ĳ���˵��Ƿ���ĳ���б���
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