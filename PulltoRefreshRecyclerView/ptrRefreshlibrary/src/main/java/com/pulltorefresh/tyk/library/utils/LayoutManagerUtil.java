package com.pulltorefresh.tyk.library.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.Arrays;

/**
 * @author chenzj
 * 类的描述 -
 * 2016/10/9 10:36
 * admin@chenzhongjin.cn
 */
public class LayoutManagerUtil {

    public static int getFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisibleItemPosition = -1;

        switch (getLayoutManagerType(layoutManager)) {
            case LINEAR:
            case GRID:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                firstVisibleItemPosition = caseStaggeredGrid(layoutManager, true);
                break;
        }
        return firstVisibleItemPosition;
    }

    public static int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        switch (getLayoutManagerType(layoutManager)) {
            case LINEAR:
            case GRID:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                lastVisibleItemPosition = caseStaggeredGrid(layoutManager, false);
                break;
        }
        return lastVisibleItemPosition;
    }

    private static LAYOUT_MANAGER_TYPE getLayoutManagerType(RecyclerView.LayoutManager layoutManager) {
        LAYOUT_MANAGER_TYPE layoutManagerType;

        if (layoutManager instanceof GridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
        } else if (layoutManager instanceof LinearLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager " +
                    "and StaggeredGridLayoutManager");
        }
        return layoutManagerType;
    }

    private static int caseStaggeredGrid(RecyclerView.LayoutManager layoutManager, boolean findFirst) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];

        if (findFirst) {
            staggeredGridLayoutManager.findFirstVisibleItemPositions(positions);
            Arrays.sort(positions);
            return positions[0];
        } else {
            staggeredGridLayoutManager.findLastVisibleItemPositions(positions);
            Arrays.sort(positions);
            return positions[positions.length - 1];
        }
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }
}
