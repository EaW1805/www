package com.eaw1805.www.scenario.algorithms;


public interface AStarNode<H> {
    String getId();
    H getObject();
    AStarNode<H> getParent();
    double getWeight();
    double getTotalWeight();
    AStarNode<H> parentNode(AStarNode<H> parent);
    int getIndexX();
    int getIndexY();
}
