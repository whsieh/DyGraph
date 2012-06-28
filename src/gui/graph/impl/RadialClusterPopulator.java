package gui.graph.impl;

import gui.graph.*;
import gui.graph.util.IDCounter;

public class RadialClusterPopulator extends GPopulator {

    int clusterSize;
    int numClusters;
    
    public RadialClusterPopulator(GViewer view, int clusterSize, int numClusters) {
        super(view);
        this.clusterSize = clusterSize;
        this.numClusters = numClusters;
    }
    
    @Override
    public void populate() {
        VertexPainter[][] clusters = new VertexPainter[numClusters][clusterSize];
        VertexPainter[] anchors = new VertexPainter[numClusters];
        for(int anch = 0; anch < numClusters; anch++) {
            anchors[anch] = view.addVertex("#"+IDCounter.next(),
                    (int)(Math.random()*800),
                (int)(Math.random()*800));
            for(int clust = 0; clust < clusterSize; clust++) {
                clusters[anch][clust] = view.addVertex(
                        "#"+IDCounter.next(),(int)(Math.random()*800),
                (int)(Math.random()*800));
                view.addEdge(anchors[anch],clusters[anch][clust]);
            }
            if (anch != 0) {
                view.addEdge(anchors[anch],anchors[anch-1]);
            }
        }
    }
    
    
}
