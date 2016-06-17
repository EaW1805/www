package com.eaw1805.www.scenario.algorithms;


import com.eaw1805.www.scenario.views.LoadingView;

import java.util.*;

public abstract class SupplyClusterAlgorithm<H> {

    AStarNode<H>[][] dataMatrix;

    final Set<String> idsSetted = new HashSet<String>();
    protected Set<AStarNode<H>> openSet = new TreeSet<AStarNode<H>>(new Comparator<AStarNode<H>>() {
        @Override
        public int compare(final AStarNode<H> o1, final AStarNode<H> o2) {
            if (o1.getId().equals(o2.getId())) {
                //if it is the same object.
                return 0;
            }
            if (o1.getTotalWeight() == o2.getTotalWeight()) {
                return 1;
            }
            return (int)(o1.getTotalWeight() - o2.getTotalWeight());
        }
    });
    protected Set<AStarNode<H>> closeSet = new TreeSet<AStarNode<H>>(new Comparator<AStarNode<H>>() {
        @Override
        public int compare(final AStarNode<H> o1, final AStarNode<H> o2) {
            if (o1.getId().equals(o2.getId())) {
                //if it is the same object.
                return 0;
            }
            if (o1.getTotalWeight() == o2.getTotalWeight()) {
                return 1;
            }
            return (int)(o1.getTotalWeight() - o2.getTotalWeight());
        }
    });

    AStarNode<H> startNode;


    public SupplyClusterAlgorithm(final AStarNode<H>[][] data, AStarNode<H> inStart) {

        dataMatrix = data;
        startNode = inStart;
    }

    //returns all sectors that are clustered around a source sector.
    public List<H> calculate() {
        openSet.add(startNode);
        final List<H> out = new ArrayList<H>();
        LoadingView.getInstance().requestShow();

        new com.google.gwt.user.client.Timer() {

            @Override
            public void run() {
                if (!openSet.iterator().hasNext()) {
                    onFinish();
                    LoadingView.getInstance().requestHide();
                    cancel();
                }
                //retrieve the new current node.
                final AStarNode<H> curNode = openSet.iterator().next();
                //remove it from the list.
                openSet.remove(curNode);
                //add it to the closed list.
                closeSet.add(curNode);
                idsSetted.add(curNode.getId());
                //calculate all children.

                /**
                 * Find all sectors that can be supplied by the hypothetical source.
                 */
                for (int indexX = curNode.getIndexX()-1; indexX <= curNode.getIndexX() + 1 ;indexX++) {
                    for (int indexY = curNode.getIndexY() -1; indexY <= curNode.getIndexY() + 1; indexY++) {
                        if (indexX < 0 || indexX >= dataMatrix.length || indexY < 0 || indexY >= dataMatrix[0].length) {
                            continue;
                        }
                        final AStarNode<H> node = dataMatrix[indexX][indexY];
                        if (idsSetted.contains(node.getId())) {
                            continue;
                        }
                        idsSetted.add(node.getId());

                        final AStarNode<H> fixedNode = node.parentNode(curNode);
                        if (fixedNode.getTotalWeight() <= 40) {
                            openSet.add(fixedNode);
                            out.add(fixedNode.getObject());
                            executeResult(fixedNode);
                        } else {
                            closeSet.add(fixedNode);
                        }

                    }
                }
            }
        }.scheduleRepeating(20);

        return out;
    }

    public abstract void executeResult(AStarNode<H> out);
    public abstract void onFinish();

}
