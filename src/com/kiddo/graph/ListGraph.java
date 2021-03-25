package com.kiddo.graph;

import com.kiddo.Union.GenericUnionFind;
import com.kiddo.heap.MinHeap;

import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("all")
public class ListGraph<V, E> extends Graph<V, E> {

    /**
     * 存储顶点与V的对应关系
     */
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    /**
     * 存储所有的边
     */
    private Set<Edge<V, E>> edges = new HashSet<>();
    private Comparator<Edge<V, E>> edgeComparator = new Comparator<Edge<V, E>>() {
        @Override
        public int compare(Edge<V, E> o1, Edge<V, E> o2) {
            return weightManager.compare(o1.weight, o2.weight);
        }
    };

    public ListGraph() {
    }

    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Vertex<?, ?> vertex = (Vertex<?, ?>) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "value=" + value +
                    '}';
        }
    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;


        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }

        Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Edge<?, ?> edge1 = (Edge<?, ?>) o;
            return Objects.equals(from, edge1.from) &&
                    Objects.equals(to, edge1.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }

    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) {
            return;
        }
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        //判断顶点存在性
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.weight = weight;
        //边已存在
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) {
            return;
        }
        //迭代器遍历***
        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.to.inEdges.remove(edge);
            iterator.remove(); //迭代器的删除，可以边删除边遍历
            edges.remove(edge);
        }
        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.from.outEdges.remove(edge);
            iterator.remove();
            edges.remove(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        Vertex<V, E> toVertex = vertices.get(to);
        if (fromVertex == null || toVertex == null) {
            return;
        }
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertices.size();
    }

    @Override
    public void bfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return;
        }
        Set<Vertex<V, E>> visitedVertex = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visitedVertex.add(beginVertex);
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            System.out.println(vertex.value);
            for (Edge<V, E> outEdge : vertex.outEdges) {
                if (!visitedVertex.contains(outEdge.to)) {
                    queue.offer(outEdge.to);
                    visitedVertex.add(outEdge.to);
                }
            }
        }
    }

    @Override
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return;
        }
        Set<Vertex<V, E>> visitedVertex = new HashSet<>();
        dfs(beginVertex, visitedVertex);
    }

    private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertex) {
        System.out.println(vertex.value);
        visitedVertex.add(vertex);
        for (Edge<V, E> outEdge : vertex.outEdges) {
            if (!visitedVertex.contains(outEdge.to)) {
                dfs(outEdge.to, visitedVertex);
            }
        }
    }

    //非递归形式
    public void dfs2(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        Set<Vertex<V, E>> visitedVertex = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();
        stack.push(beginVertex);
        visitedVertex.add(beginVertex);
        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            System.out.println(vertex.value);
            for (Edge<V, E> outEdge : vertex.outEdges) {
                if (!visitedVertex.contains(outEdge.to)) {
                    stack.push(outEdge.from);
                    stack.push(outEdge.to);
                    visitedVertex.add(outEdge.to);
                    break;
                }
            }
        }
    }

    @Override
    public Set<EdgeInfo<V, E>> mst() {
        return Math.random() > 0.5 ? prim() : kruskal();
    }

    private Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        if (!it.hasNext()) {
            return null;
        }
        Vertex<V, E> vertex = it.next();  //random vertex
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        Set<Vertex<V, E>> addedVertex = new HashSet<>();
        addedVertex.add(vertex);
        MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator);
        int edgeSize = vertices.size() - 1;
        while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {
            Edge<V, E> edge = heap.remove();
            if (addedVertex.contains(edge.to)) {
                continue;
            }
            edgeInfos.add(edge.info());
            addedVertex.add(edge.to);
            heap.addAll(edge.to.outEdges);
        }
        return edgeInfos;
    }

    //时间复杂度ElogE
    private Set<EdgeInfo<V, E>> kruskal() {
        GenericUnionFind<Vertex<V, E>> uf = new GenericUnionFind<>();   //使用并查集
        vertices.forEach((V key, Vertex<V, E> vertex) -> {   //V
            uf.makeSet(vertex);
        });
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        MinHeap<Edge<V, E>> heap = new MinHeap<>(edges, edgeComparator);    //E
        Set<Vertex<V, E>> addedVertex = new HashSet<>();
        int edgeSize = vertices.size() - 1;
        while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {  // E
            Edge<V, E> edge = heap.remove();  //logE
            if (uf.isSame(edge.from, edge.to)) {
                continue;
            }
            edgeInfos.add(edge.info());
            uf.union(edge.from, edge.to);
        }
        return edgeInfos;
    }

    @Override
    public List<V> topologicalSort() {
        List<V> list = new ArrayList<>();
        //临时存放顶点
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        //存储顶点的入度
        Map<Vertex<V, E>, Integer> ins = new HashMap<>();
        //初始化 (度为0放入队列，顶点度信息存入map）
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex);
            } else {
                ins.put(vertex, in);
            }
        });
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            //放入返回结果
            list.add(vertex.value);
            for (Edge<V, E> outEdge : vertex.outEdges) {
                Vertex<V, E> toVertex = outEdge.to;
                int newIn = ins.get(toVertex) - 1;
                if (newIn == 0) {
                    queue.offer(toVertex);
                } else {
                    ins.put(toVertex, newIn);
                }
            }
        }
        return list;
    }


    @Override
    public Map<V, PathInfo<V, E>> shortestPath(V begin) {
        return Math.random() > 0.5 ? dijkstra(begin) : bellmanFord(begin);
    }

    //floyd
    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
        Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();
        //初始化paths，放入已存在边
        for (Edge<V, E> edge : edges) {
            Map<V, PathInfo<V, E>> toPathMap = paths.get(edge.from.value);
            if (toPathMap == null) {
                toPathMap = new HashMap<>();
                paths.put(edge.from.value, toPathMap);
            }
            PathInfo<V, E> pathInfo = new PathInfo<>(edge.weight);
            pathInfo.edgeInfos.add(edge.info());
            toPathMap.put(edge.to.value, pathInfo);
        }
        //遍历每个顶点
        vertices.forEach((V v2, Vertex<V, E> vertex2) -> {
            vertices.forEach((V v1, Vertex<V, E> vertex1) -> {
                vertices.forEach((V v3, Vertex<V, E> vertex3) -> {
                    if (v1.equals(v2) || v1.equals(v3) || v2.equals(v3)) {  //相同顶点不必进行比较操作
                        return;
                    }
                    //v1 -> v2的最短路径
                    PathInfo<V, E> pathInfo12 = getPathInfo(v1, v2, paths);
                    //v2 -> v3的最短路径
                    PathInfo<V, E> pathInfo23 = getPathInfo(v2, v3, paths);
                    //v2 -> v3的最短路径
                    PathInfo<V, E> pathInfo13 = getPathInfo(v1, v3, paths);
                    if (pathInfo12 == null || pathInfo23 == null) {
                        return;
                    }
                    E newWeight = weightManager.add(pathInfo12.weight, pathInfo23.weight);
                    if (pathInfo13 != null && weightManager.compare(pathInfo13.weight, newWeight) > 0) {
                        return;
                    }
                    if (pathInfo13 == null) {
                        pathInfo13 = new PathInfo<>();
                        paths.get(v1).put(v3, pathInfo13);
                    } else {
                        pathInfo13.edgeInfos.clear();
                    }
                    pathInfo13.weight = newWeight;
                    pathInfo13.edgeInfos.addAll(pathInfo12.edgeInfos);
                    pathInfo13.edgeInfos.addAll(pathInfo23.edgeInfos);
                });
            });
        });
        return paths;
    }

    private Map<V, PathInfo<V, E>> dijkstra(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return null;
        }
        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();  //对外公开
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();    //存放已知最短路径信息
        paths.put(beginVertex, new PathInfo<>(weightManager.zero()));   //源点

//        //初始化paths  === 实质上是对起始点的outEdge的to进行松弛
//        for (Edge<V, E> outEdge : beginVertex.outEdges) {
//            PathInfo<V, E> pathInfo = new PathInfo<>(outEdge.weight);
//            pathInfo.edgeInfos.add(outEdge.info());
//            paths.put(outEdge.to, pathInfo);
//        }

        while (!paths.isEmpty()) {
            //选权值最小边
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath(paths);
            Vertex<V, E> minVertex = minEntry.getKey();
            PathInfo<V, E> minPath = minEntry.getValue();
            //敲定为到该顶点最短路径
            selectedPaths.put(minVertex.value, minPath);
            paths.remove(minVertex);
            //minVertex.outEdges进行松弛操作
            for (Edge<V, E> outEdge : minVertex.outEdges) {
                if (selectedPaths.containsKey(outEdge.to.value)) {  //已经选择顶点无需松弛
                    continue;
                }
                relaxForDijkstra(outEdge, minPath, paths);
            }
        }
        selectedPaths.remove(begin);   //不需要起点
        return selectedPaths;
    }

    private Map<V, PathInfo<V, E>> bellmanFord(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return null;
        }
        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        PathInfo<V, E> beginPathInfo = new PathInfo<>(weightManager.zero());  //起点到起点的特殊零点
        selectedPaths.put(begin, beginPathInfo);
        int count = vertices.size() - 1;
        //每个边松弛V-1次（V为顶点数）
        for (int i = 0; i < count; i++) {
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
                if (fromPath == null) {
                    continue;  //边起点最短路径尚未求出
                }
                relaxForBellonFord(edge, fromPath, selectedPaths);
            }
        }

        //检测是否有负权环
        for (int i = 0; i < count; i++) {
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
                if (fromPath == null) {
                    continue;
                }
                if (relaxForBellonFord(edge, fromPath, selectedPaths)) {   //还有边能继续松弛
                    System.out.println("----有负权环----");
                    return null;
                }
            }
        }
        selectedPaths.remove(begin);   //不需要起点
        return selectedPaths;
    }

    /**
     * 松弛
     *
     * @param edge     需要进行松弛的边
     * @param fromPath edge的from的最短路径信息
     * @param paths    其他未离开桌面顶点的最短路径信息，
     */
    private void relaxForDijkstra(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        //new & old
        PathInfo<V, E> toPathInfo = paths.get(edge.to);
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        //null == infinity
        if (toPathInfo != null && weightManager.compare(newWeight, toPathInfo.weight) >= 0) {
            return;
        }
        if (toPathInfo == null) {   //can not reach before
            toPathInfo = new PathInfo<>();
            paths.put(edge.to, toPathInfo);
        } else {
            toPathInfo.edgeInfos.clear();
        }
        toPathInfo.weight = newWeight;
        toPathInfo.edgeInfos.addAll(fromPath.edgeInfos);
        toPathInfo.edgeInfos.add(edge.info());
    }

    private Map.Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Iterator<Map.Entry<Vertex<V, E>, PathInfo<V, E>>> iterator = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = iterator.next();  //mot empty
        while (iterator.hasNext()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> entry = iterator.next();
            if (weightManager.compare(entry.getValue().weight, minEntry.getValue().weight) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;   //entry=顶点-(路径-路径权重和)
    }

    /**
     * @param edge     需要松弛边
     * @param fromPath edge的from的最短路径信息
     * @param paths    存放可到达的顶点的路径信息
     * @return 是否进行松弛
     */
    private boolean relaxForBellonFord(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<V, PathInfo<V, E>> paths) {
        //new & old
        PathInfo<V, E> oldPathInfo = paths.get(edge.to.value);
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        //null == infinity
        if (oldPathInfo != null && weightManager.compare(newWeight, oldPathInfo.weight) >= 0) {
            return false;
        }
        if (oldPathInfo == null) {
            oldPathInfo = new PathInfo<>();
            paths.put(edge.to.value, oldPathInfo);
        } else {
            oldPathInfo.edgeInfos.clear();
        }
        oldPathInfo.weight = newWeight;
        oldPathInfo.edgeInfos.addAll(fromPath.edgeInfos);
        oldPathInfo.edgeInfos.add(edge.info());
        return true;
    }

    /**
     * 获取from到to的路径信息
     *
     * @param from
     * @param to
     * @param paths
     * @return
     */
    private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> paths) {
        Map<V, PathInfo<V, E>> map = paths.get(from);
        return map == null ? null : map.get(to);
    }

    public void print() {
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
        });

        edges.forEach((Edge<V, E> edge) -> {
            System.out.println(edge);
        });
    }
}