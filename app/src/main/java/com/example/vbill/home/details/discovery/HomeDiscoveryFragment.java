package com.example.vbill.home.details.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.adapter.DiscoveryMenuAdapter;
import com.example.vbill.adapter.DiscoveryRecommendMenuAdapter;
import com.example.vbill.bean.DiscoveryMenu;
import com.example.vbill.bean.ImageInfo;
import com.example.vbill.bean.discovery.recommend.DiscoveryRecommendResponse;
import com.example.vbill.bean.discovery.recommend.RecommendInfo;
import com.example.vbill.bean.discovery.recommend.RecommendMenu;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.example.vbill.util.Utility;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeDiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeDiscoveryFragment extends Fragment implements View.OnClickListener {
    private static HomeDiscoveryFragment fragment;
    Context context;
    private RecyclerView recyclerView;
    DiscoveryRecommendResponse discoveryRecommendResponse;
    private RecyclerView recommendMenuView;

    public FragmentActivity activity;
    public SharedPreferences loginPref;
    public SharedPreferences recommendPref;
    public SharedPreferences.Editor editor;

    private String userId;

    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点

    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeDiscoveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeDiscoveryFragment newInstance(String param1, String param2) {
        HomeDiscoveryFragment fragment = new HomeDiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeDiscoveryFragment getInstance() {
        try {
            if (fragment == null) {
                fragment = new HomeDiscoveryFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home_discovery);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_discovery, container, false);

        //
        recyclerView = view.findViewById(R.id.home_discovery_menu);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DiscoveryMenuAdapter discoveryMenuAdapter = new DiscoveryMenuAdapter(container.getContext(),initDescoveryMenu());
        recyclerView.setAdapter(discoveryMenuAdapter);

        activity = getActivity();
        loginPref = activity.getSharedPreferences("login", activity.MODE_PRIVATE);
        userId = String.valueOf(loginPref.getInt("userId", -1));
        requestDiscoveryRecommend(userId, view, container);

        //轮播
        initView(view);
        initEvent();
        imageStart(view, container);
        return view;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        imageInfoList = new ArrayList<>();
//        imageInfoList.add(new ImageInfo(1, "6 1 8 权益来袭", "", "http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834a75bb01156b5c9ea15cebf2f.jpg", "http://www.cnblogs.com/luhuan/"));
//        imageInfoList.add(new ImageInfo(1, "吃美食立减 10元", "", "http://img1.juimg.com/160917/328298-16091H1535663.jpg", "http://www.cnblogs.com/luhuan/"));
//        imageInfoList.add(new ImageInfo(1, "旅游才是续命良药", "", "http://e.hiphotos.baidu.com/image/pic/item/6a600c338744ebf85ed0ab2bd4f9d72a6059a705.jpg", "http://www.cnblogs.com/luhuan/"));
//        imageInfoList.add(new ImageInfo(1, "理财干货", "仅展示", "http://b.hiphotos.baidu.com/image/h%3D300/sign=8ad802f3801001e9513c120f880e7b06/a71ea8d3fd1f4134be1e4e64281f95cad1c85efa.jpg", ""));
//        imageInfoList.add(new ImageInfo(1, "签到送积分", "仅展示", "http://e.hiphotos.baidu.com/image/h%3D300/sign=73443062281f95cab9f594b6f9177fc5/72f082025aafa40fafb5fbc1a664034f78f019be.jpg", ""));
        imageInfoList.add(new ImageInfo(1, "6 1 8 权益来袭", "", Constants.SERVER_PREFIX + "v1/esc/images/recommend/lunbo_618.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "吃美食立减 10元", "", Constants.SERVER_PREFIX + "v1/esc/images/recommend/lunbo_meishi.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "旅游才是续命良药", "", Constants.SERVER_PREFIX + "v1/esc/images/recommend/lunbo_lvxing.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "理财干货", "仅展示", Constants.SERVER_PREFIX + "v1/esc/images/recommend/lunbo_licai.jpg", ""));
        imageInfoList.add(new ImageInfo(1, "签到送积分", "仅展示", Constants.SERVER_PREFIX + "v1/esc/images/recommend/lunbo_qiandao.jpg", ""));

    }

    /**
     * 初始化控件
     */
    private void initView(View view) {

        mViewPager = view.findViewById(R.id.viewPager);
        mTvPagerTitle = view.findViewById(R.id.tv_pager_title);
        mLineLayoutDot = view.findViewById(R.id.lineLayout_dot);

    }

    private void imageStart(View view, ViewGroup container) {
        //设置图片轮播
        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,
                R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
        String[] titles = new String[imageInfoList.size()];
        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

        for (int i = 0; i < imageInfoList.size(); i++) {
            titles[i] = imageInfoList.get(i).getTitle();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(container.getContext());
            simpleDraweeView.setAspectRatio(1.78f);
            // 设置一张默认的图片
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(this.getResources())
                    .setPlaceholderImage(ContextCompat.getDrawable(container.getContext(), R.drawable.defult), ScalingUtils.ScaleType.CENTER_CROP).build();
            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            //加载高分辨率图片;
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                    .setResizeOptions(new ResizeOptions(1280, 720))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);

            simpleDraweeView.setId(imgaeIds[i]);//给view设置id
            simpleDraweeView.setTag(imageInfoList.get(i));
            simpleDraweeView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.Investment");
                    startActivity(intent);
                }
            });
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(view, mLineLayoutDot, fromResToDrawable(container.getContext(), R.drawable.ic_dot_focused), simpleDraweeViewList.size(), container);
        imageCarousel = new ImageCarousel(container.getContext(), mViewPager, mTvPagerTitle, dots, 5000);
        imageCarousel.init(simpleDraweeViewList, titles)
                .startAutoPlay();
        imageCarousel.start();

    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return 小点的Id
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgount, ViewGroup container) {
        final View dot = new View(container.getContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        //minSdkVersion 17 will support
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.addView(dot);
            }
        });

        return dot.getId();
    }


    /**
     * 资源图片转Drawable
     *
     * @param context 上下文
     * @param resId   资源ID
     * @return 返回Drawable图像
     */
    public static Drawable fromResToDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
        //return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout 线性横向布局
     * @param backgount    小点资源图标
     * @param number       数量
     * @return 返回小点View集合
     */
    private List<View> addDots(View view, final LinearLayout linearLayout, Drawable backgount, int number, ViewGroup container) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount, container);
            dots.add(view.findViewById(dotId));

        }
        return dots;
    }

    private List<DiscoveryMenu> initDescoveryMenu() {
        DiscoveryMenu discoveryMenu1 = new DiscoveryMenu(R.drawable.discovery_shouzhangben, "手账本");
        DiscoveryMenu discoveryMenu2 = new DiscoveryMenu(R.drawable.discovery_meishi, "美食");
        DiscoveryMenu discoveryMenu3 = new DiscoveryMenu(R.drawable.discovery_yule, "娱乐");
        DiscoveryMenu discoveryMenu4 = new DiscoveryMenu(R.drawable.discovery_lvyou, "旅游");
        DiscoveryMenu discoveryMenu5 = new DiscoveryMenu(R.drawable.discovery_remenhuodong, "热门活动");
        DiscoveryMenu discoveryMenu6 = new DiscoveryMenu(R.drawable.discovery_offer, "限时特惠");
        DiscoveryMenu discoveryMenu7 = new DiscoveryMenu(R.drawable.discovery_touzi, "投资");
        DiscoveryMenu discoveryMenu8 = new DiscoveryMenu(R.drawable.discovery_chuxing, "出行");
        DiscoveryMenu discoveryMenu9 = new DiscoveryMenu(R.drawable.discovery_gouwu, "购物");
        DiscoveryMenu discoveryMenu10 = new DiscoveryMenu(R.drawable.discovery_shenghuojiaofei, "生活缴费");

        List<DiscoveryMenu> discoveryMenus = new ArrayList<>();
        discoveryMenus.add(discoveryMenu1);
        discoveryMenus.add(discoveryMenu2);
        discoveryMenus.add(discoveryMenu3);
        discoveryMenus.add(discoveryMenu4);
        discoveryMenus.add(discoveryMenu5);
        discoveryMenus.add(discoveryMenu6);
        discoveryMenus.add(discoveryMenu7);
        discoveryMenus.add(discoveryMenu8);
        discoveryMenus.add(discoveryMenu9);
        discoveryMenus.add(discoveryMenu10);
        return discoveryMenus;
    }

    public void requestDiscoveryRecommend(String userId, View view,ViewGroup container){

        String discoveryRecommendUrl = Constants.SERVER_PREFIX + "v1/esc/recommendations/" + userId;
//        String discoveryRecommendUrl = Constants.SERVER_PREFIX + "v1/esc/recommendations/5";
        HttpUtil.sendOkHttpGetRequest(discoveryRecommendUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                recommendPref = activity.getSharedPreferences("recommend", activity.MODE_PRIVATE);
                editor = recommendPref.edit();
                editor.putString("recommendResponseString", responseText);
                editor.commit();
                discoveryRecommendResponse = Utility.handleDiscoveryRecommend(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (discoveryRecommendResponse.getRecommendInfo().size() > 0 && 200 == (discoveryRecommendResponse.getStatusCode())) {
                            showRecommendMenu(discoveryRecommendResponse, view, container);
                        }else{
                            Toast.makeText(getContext(), "为您推荐列表无数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showRecommendMenu(DiscoveryRecommendResponse discoveryRecommendResponse, View view, ViewGroup container){
        recommendMenuView = (RecyclerView)view.findViewById(R.id.discovery_recommend_menu);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recommendMenuView.setLayoutManager(layoutManager);
        List<RecommendInfo> recommendInfos = discoveryRecommendResponse.getRecommendInfo();
        List<RecommendMenu> recommendMenus = new ArrayList<>();
        for (RecommendInfo recommendInfo:recommendInfos) {
            RecommendMenu recommendMenu = new RecommendMenu();
            recommendMenu.setImagePath(recommendInfo.getRecommendMenu().getImagePath());
            recommendMenu.setDescription1(recommendInfo.getRecommendMenu().getDescription1());
            recommendMenu.setDescription2(recommendInfo.getRecommendMenu().getDescription2());
            recommendMenu.setCid(recommendInfo.getBasic().getCid());
            recommendMenus.add(recommendMenu);
        }
        DiscoveryRecommendMenuAdapter discoveryMenuAdapter = new DiscoveryRecommendMenuAdapter(container.getContext(),recommendMenus);
        recommendMenuView.setAdapter(discoveryMenuAdapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
