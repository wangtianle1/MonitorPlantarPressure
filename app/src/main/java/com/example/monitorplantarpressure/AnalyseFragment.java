package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnalyseFragment extends Fragment {
    public static final String SECTION_STRING = "analyse";
    private RecyclerView rl_list;

    private ArrayList<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();

    private CollectRecycleAdapter mCollectRecyclerAdapter;

    public static AnalyseFragment newInstance(String sectionNumber) {
        AnalyseFragment analyseFragment = new AnalyseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SECTION_STRING, sectionNumber);
        analyseFragment.setArguments(bundle);
        return analyseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anaylse_fragment, container, false);
        rl_list = view.findViewById(R.id.rl_list);
        initRecyclerView(view);
        initData();
        return view;
    }

    private void initData() {
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoodsName("消息中心");
        goodsEntity.setGoodsPrice("「全新升级」");
        goodsEntityList.add(goodsEntity);
        GoodsEntity goodsEntity1 = new GoodsEntity();
        goodsEntity1.setGoodsName("玩一玩");
        goodsEntity1.setGoodsPrice("签到兑礼「超级大奖」");
        goodsEntityList.add(goodsEntity1);
        GoodsEntity goodsEntity2 = new GoodsEntity();
        goodsEntity2.setGoodsName("买卖闲置");
        goodsEntity2.setGoodsPrice("逐件查验 | 你喜欢的苹果手机直降1400元！");
        goodsEntityList.add(goodsEntity2);
        GoodsEntity goodsEntity3 = new GoodsEntity();
        goodsEntity3.setGoodsName("借钱 · 备用金");
        goodsEntity3.setGoodsPrice("最高20万 | 官方通道 安全便捷");

        goodsEntityList.add(goodsEntity3);

    }

    private void initRecyclerView(View view) {

        //获取RecyclerView
        rl_list = (RecyclerView) view.findViewById(R.id.rl_list);
        //创建adapter
        mCollectRecyclerAdapter = new CollectRecycleAdapter(getActivity(), goodsEntityList);
        //给RecyclerView设置adapter
        rl_list.setAdapter(mCollectRecyclerAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        rl_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        rl_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        mCollectRecyclerAdapter.setOnItemClickListener(new CollectRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, GoodsEntity data) {
                //此处进行监听事件的业务处理
                Toast.makeText(getActivity(), "全新功能正在开发中，敬请期待！", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
