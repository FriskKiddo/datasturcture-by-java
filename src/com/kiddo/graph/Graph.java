package com.kiddo.graph;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Graph<V, E> {

    protected WeightManager<E> weightManager;

    public Graph() {
    }

    public Graph(WeightManager<E> weightManager) {
        this.weightManager = weightManager;
    }

    /**
     * 添加一个顶点
     * @param v
     */
    public abstract void addVertex(V v);

    /**
     * 添加一条边
     * @param from
     * @param to
     */
    public abstract void addEdge(V from, V to);

    /**
     * 添加一条带权重边
     * @param from
     * @param to
     * @param weight
     */
    public abstract void addEdge(V from, V to, E weight);

    /**
     * 删除一个顶点
     * @param v
     */
    public abstract void removeVertex(V v);


    /**
     * 删除一条边
     * @param from
     * @param to
     */
    public abstract void removeEdge(V from, V to);

    /**
     * 获取边数
     * @return
     */
    public abstract int edgesSize();

    /**
     * 获取顶点数
     * @return
     */
    public abstract int verticesSize();

    /**
     * 广度优先
     * @param begin 遍历起始位置
     */
    public abstract void bfs(V begin);

    /**
     * 深度优先
     * @param begin 遍历起始位置
     */
    public abstract void dfs(V begin);

    /**
     * 构建最小生成树
     *
     * @return
     */
    public abstract Set<EdgeInfo<V, E>> mst();

    /**
     * 拓扑排序
     * @return
     */
    public abstract List<V> topologicalSort();

    /**
     * 单源最短路径
     * @param
     * @param
     */
    public abstract Map<V, PathInfo<V,E>> shortestPath(V begin);

    /**
     * 多源最短路径
     * @return  各个起点到各个终点的最短路径信息
     */
    public abstract Map<V, Map<V, PathInfo<V, E>>> shortestPath();

    /**
     * 可公开的path
     * @param <V>
     * @param <E>
     */
    public static class PathInfo<V,E>{
        protected E weight;
        protected LinkedList<EdgeInfo<V, E>> edgeInfos = new LinkedList();

        public PathInfo() {
        }

        public PathInfo(E weight) {
            this.weight = weight;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        public LinkedList<EdgeInfo<V, E>> getEdgeInfos() {
            return edgeInfos;
        }

        public void setEdgeInfos(LinkedList<EdgeInfo<V, E>> edgeInfos) {
            this.edgeInfos = edgeInfos;
        }

        @Override
        public String toString() {
            return "PathInfo{" +
                    "weight=" + weight +
                    ", edgeInfos=" + edgeInfos +
                    '}';
        }
    }

    //可公开的Edge
    public static class EdgeInfo<V,E>{
        private V from;
        private V to;
        private E weight;

        public V getFrom() {
            return from;
        }

        public void setFrom(V from) {
            this.from = from;
        }

        public V getTo() {
            return to;
        }

        public void setTo(V to) {
            this.to = to;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        protected EdgeInfo(V from, V to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }

}
