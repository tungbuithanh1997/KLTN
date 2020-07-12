package ducthuan.com.lamdep.ObjectClass;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ducthuan.com.lamdep.Interface.ILoadMore;

public class LoadMoreScroll extends RecyclerView.OnScrollListener {
    int itemandautien = 0;
    int tongitem = 0;
    int itemloadtruoc = 10;
    RecyclerView.LayoutManager layoutManager;
    ILoadMore iLoadMore;

    public LoadMoreScroll(RecyclerView.LayoutManager layoutManager,ILoadMore iLoadMore){
        this.layoutManager = layoutManager;
        this.iLoadMore = iLoadMore;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        tongitem = layoutManager.getItemCount();

        if(layoutManager instanceof LinearLayoutManager){
            itemandautien = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }else if(layoutManager instanceof GridLayoutManager){
            itemandautien = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if(tongitem <= (itemandautien + itemloadtruoc)){
            iLoadMore.LoadMore(tongitem);
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}
