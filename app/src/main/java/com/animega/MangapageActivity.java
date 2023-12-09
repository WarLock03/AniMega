package com.animega;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.blogspot.atifsoftwares.animatoolib.*;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.*;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import xyz.hasnat.sweettoast.*;
import androidx.core.widget.NestedScrollView;

public class MangapageActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> detailMap = new HashMap<>();
	private HashMap<String, Object> titleMap = new HashMap<>();
	private double ratingsNumber = 0;
	private double releaseDate = 0;
	private String titleString = "";
	private double titleKeyNumber = 0;
	private double genreKeyNumber = 0;
	private String description1 = "";
	private String description2 = "";
	private String description3 = "";
	private String description4 = "";
	private HashMap<String, Object> titlesList = new HashMap<>();
	private String itemID = "";
	private double animeID = 0;
	
	private ArrayList<String> titlesStringList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> relationMap = new ArrayList<>();
	private ArrayList<String> genresString = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> recommendationsListMap = new ArrayList<>();
	
	private SwipeRefreshLayout swiperefreshlayout1;
	private NestedScrollView vscroll1;
	private LinearLayout linear1;
	private LinearLayout main_page;
	private LinearLayout linear_main;
	private RelativeLayout linear2;
	private LinearLayout linear26;
	private HorizontalScrollView hscroll1;
	private LinearLayout linear17;
	private LinearLayout linear19;
	private LinearLayout linear25;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private ImageView bannerImage;
	private LinearLayout toolbar;
	private LinearLayout linear7;
	private LinearLayout backBtn;
	private LinearLayout linear6;
	private ImageView imageview2;
	private LinearLayout score_linear;
	private ImageView imageview3;
	private TextView score;
	private CardView cardview1;
	private TextView anime_title;
	private ImageView coverImage;
	private RelativeLayout watch_btn;
	private LinearLayout linear18;
	private LinearLayout btn_clickEffect;
	private ImageView imageview4;
	private TextView textview1;
	private LinearLayout linear10;
	private LinearLayout linear11;
	private LinearLayout linear12;
	private LinearLayout linear14;
	private TextView textview2;
	private TextView anime_status;
	private TextView textview3;
	private TextView anime_format;
	private TextView textview5;
	private TextView anime_year;
	private TextView textview8;
	private HorizontalScrollView hscroll5;
	private LinearLayout linear_titles;
	private TextView textview9;
	private HorizontalScrollView hscroll2;
	private LinearLayout linear_genres;
	private TabLayout tablayout1;
	private LinearLayout linear_description_details;
	private RecyclerView recyclerview_relation;
	private RecyclerView recyclerview_recommend;
	private TextView descript_text;
	
	private RequestNetwork requestMangaInfo;
	private RequestNetwork.RequestListener _requestMangaInfo_request_listener;
	private TimerTask clickDelay;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.mangapage);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		swiperefreshlayout1 = findViewById(R.id.swiperefreshlayout1);
		vscroll1 = findViewById(R.id.vscroll1);
		linear1 = findViewById(R.id.linear1);
		main_page = findViewById(R.id.main_page);
		linear_main = findViewById(R.id.linear_main);
		linear2 = findViewById(R.id.linear2);
		linear26 = findViewById(R.id.linear26);
		hscroll1 = findViewById(R.id.hscroll1);
		linear17 = findViewById(R.id.linear17);
		linear19 = findViewById(R.id.linear19);
		linear25 = findViewById(R.id.linear25);
		linear3 = findViewById(R.id.linear3);
		linear4 = findViewById(R.id.linear4);
		bannerImage = findViewById(R.id.bannerImage);
		toolbar = findViewById(R.id.toolbar);
		linear7 = findViewById(R.id.linear7);
		backBtn = findViewById(R.id.backBtn);
		linear6 = findViewById(R.id.linear6);
		imageview2 = findViewById(R.id.imageview2);
		score_linear = findViewById(R.id.score_linear);
		imageview3 = findViewById(R.id.imageview3);
		score = findViewById(R.id.score);
		cardview1 = findViewById(R.id.cardview1);
		anime_title = findViewById(R.id.anime_title);
		coverImage = findViewById(R.id.coverImage);
		watch_btn = findViewById(R.id.watch_btn);
		linear18 = findViewById(R.id.linear18);
		btn_clickEffect = findViewById(R.id.btn_clickEffect);
		imageview4 = findViewById(R.id.imageview4);
		textview1 = findViewById(R.id.textview1);
		linear10 = findViewById(R.id.linear10);
		linear11 = findViewById(R.id.linear11);
		linear12 = findViewById(R.id.linear12);
		linear14 = findViewById(R.id.linear14);
		textview2 = findViewById(R.id.textview2);
		anime_status = findViewById(R.id.anime_status);
		textview3 = findViewById(R.id.textview3);
		anime_format = findViewById(R.id.anime_format);
		textview5 = findViewById(R.id.textview5);
		anime_year = findViewById(R.id.anime_year);
		textview8 = findViewById(R.id.textview8);
		hscroll5 = findViewById(R.id.hscroll5);
		linear_titles = findViewById(R.id.linear_titles);
		textview9 = findViewById(R.id.textview9);
		hscroll2 = findViewById(R.id.hscroll2);
		linear_genres = findViewById(R.id.linear_genres);
		tablayout1 = findViewById(R.id.tablayout1);
		linear_description_details = findViewById(R.id.linear_description_details);
		recyclerview_relation = findViewById(R.id.recyclerview_relation);
		recyclerview_recommend = findViewById(R.id.recyclerview_recommend);
		descript_text = findViewById(R.id.descript_text);
		requestMangaInfo = new RequestNetwork(this);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
				Animatoo.animateSlideDown(MangapageActivity.this);
			}
		});
		
		btn_clickEffect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_click_effect(btn_clickEffect, "#212121");
			}
		});
		
		tablayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				final int _position = tab.getPosition();
				if (_position == 0) {
					linear_description_details.setVisibility(View.VISIBLE);
					recyclerview_relation.setVisibility(View.GONE);
					recyclerview_recommend.setVisibility(View.GONE);
				}
				if (_position == 1) {
					linear_description_details.setVisibility(View.GONE);
					recyclerview_relation.setVisibility(View.VISIBLE);
					recyclerview_recommend.setVisibility(View.GONE);
				}
				if (_position == 2) {
					linear_description_details.setVisibility(View.GONE);
					recyclerview_relation.setVisibility(View.GONE);
					recyclerview_recommend.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				final int _position = tab.getPosition();
				
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				final int _position = tab.getPosition();
				
			}
		});
		
		_requestMangaInfo_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				linear_main.setVisibility(View.VISIBLE);
				swiperefreshlayout1.setRefreshing(false);
				try{
					detailMap = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (detailMap.containsKey("title")) {
						String title = (new Gson()).toJson(detailMap.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
						titleMap = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
						if (!titleMap.get("english").toString().equals(null)) {
							anime_title.setText(titleMap.get("english").toString());
						}
						else {
							if (!titleMap.get("romaji").toString().equals(null)) {
								anime_title.setText(titleMap.get("english").toString());
							}
							else {
								if (!titleMap.get("native").toString().equals(null)) {
									anime_title.setText(titleMap.get("english").toString());
								}
								else {
									anime_title.setText("Unknown Title");
								}
							}
						}
						try{
							titleString = titleMap.get("english").toString().concat(";".concat(titleMap.get("romaji").toString().concat(";".concat(titleMap.get("native").toString()))));
							titlesStringList = new ArrayList<String>(Arrays.asList(titleString.split(";")));
							titleKeyNumber = 0;
							for(int _repeat197 = 0; _repeat197 < (int)(titlesStringList.size()); _repeat197++) {
								LinearLayout.LayoutParams _lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								_lp.setMargins(5,5,5,5);
								
								TextView textview = new TextView(getApplicationContext());
								textview.setText(titlesStringList.get((int)(titleKeyNumber)));
								
								textview.setLayoutParams(_lp);
								
								textview.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFF424242));
								textview.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
								textview.setPadding(15,10,15,13);
								textview.setGravity(Gravity.CENTER);
								textview.setTextColor(0xFFF8F8FF);
								
								linear_titles.addView(textview);
								titleKeyNumber++;
							}
						}catch(NullPointerException e){
							LinearLayout.LayoutParams _lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							_lp.setMargins(5,5,5,5);
							
							TextView textview = new TextView(getApplicationContext());
							textview.setText("Error Fetching Titles");
							
							textview.setLayoutParams(_lp);
							
							textview.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFF424242));
							textview.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
							textview.setPadding(15,10,15,13);
							textview.setGravity(Gravity.CENTER);
							textview.setTextColor(0xFFF8F8FF);
							
							linear_titles.addView(textview);
							e.printStackTrace();
						}
					}
					if (detailMap.containsKey("image")) {
						if (!detailMap.get("image").toString().equals(null)) {
							
							androidx.swiperefreshlayout.widget.CircularProgressDrawable coverLoader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getApplicationContext());
							coverLoader.setStrokeWidth(6f);
							coverLoader.setCenterRadius(35f);
							coverLoader.start();
							 Glide.with(getApplicationContext())
							.load(Uri.parse(detailMap.get("image").toString()))
							.placeholder(coverLoader)
							.error(R.drawable.nocover)
							.into(coverImage);
						}
						else {
							coverImage.setImageResource(R.drawable.nocover);
						}
					}
					if (detailMap.containsKey("cover")) {
						if (!detailMap.get("cover").toString().equals(null)) {
							
							androidx.swiperefreshlayout.widget.CircularProgressDrawable bannerLoader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getApplicationContext());
							bannerLoader.setStrokeWidth(6f);
							bannerLoader.setCenterRadius(35f);
							bannerLoader.start();
							 Glide.with(getApplicationContext())
							.load(Uri.parse(detailMap.get("cover").toString()))
							.placeholder(bannerLoader)
							.error(R.drawable.nocover)
							.into(bannerImage);
						}
						else {
							bannerImage.setImageResource(R.drawable.nocover);
						}
					}
					if (detailMap.containsKey("rating")) {
						try{
							ratingsNumber = Double.parseDouble(detailMap.get("rating").toString());
							score.setText(String.valueOf((long)(ratingsNumber)));
						}catch(NullPointerException e){
							e.printStackTrace();
							score.setText("??");
						}
					}
					if (detailMap.containsKey("status")) {
						if (!detailMap.get("status").toString().equals(null)) {
							anime_status.setText(detailMap.get("status").toString());
						}
						else {
							anime_status.setText("Unknown");
						}
					}
					if (detailMap.containsKey("type")) {
						if (!detailMap.get("type").toString().equals(null)) {
							anime_format.setText(detailMap.get("type").toString());
						}
						else {
							anime_format.setText("Unknown");
						}
					}
					if (detailMap.containsKey("releaseDate")) {
						try{
							releaseDate = Double.parseDouble(detailMap.get("releaseDate").toString());
							anime_year.setText(String.valueOf((long)(releaseDate)));
						}catch(NullPointerException e){
							anime_year.setText("Unknown");
							e.printStackTrace();
						}
					}
					if (detailMap.containsKey("genres")) {
						try{
							genresString = new Gson().fromJson(detailMap.get("genres").toString(), new TypeToken<ArrayList<String>>(){}.getType());
							genreKeyNumber = 0;
							for(int _repeat215 = 0; _repeat215 < (int)(genresString.size()); _repeat215++) {
								LinearLayout.LayoutParams _lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								_lp1.setMargins(5,5,5,5);
								
								
								TextView textview1 = new TextView(getApplicationContext());
								textview1.setText(genresString.get((int)(genreKeyNumber)));
								
								textview1.setLayoutParams(_lp1);
								
								textview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFF424242));
								textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
								textview1.setGravity(Gravity.CENTER);
								textview1.setTextColor(0xFFF8F8FF);
								textview1.setPadding(15,10,15,13);
								
								linear_genres.addView(textview1);
								genreKeyNumber++;
							}
						}catch(NullPointerException e){
							LinearLayout.LayoutParams _lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							_lp1.setMargins(5,5,5,5);
							
							
							TextView textview1 = new TextView(getApplicationContext());
							textview1.setText("Unknown Genres");
							
							textview1.setLayoutParams(_lp1);
							
							textview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFF424242));
							textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
							textview1.setGravity(Gravity.CENTER);
							textview1.setTextColor(0xFFF8F8FF);
							textview1.setPadding(15,10,15,13);
							
							linear_genres.addView(textview1);
							e.printStackTrace();
						}
					}
					if (detailMap.containsKey("description")) {
						try{
							description1 = detailMap.get("description").toString().replace("<i>", "");
							description2 = description1.replace("</i>", "");
							description3 = description2.replace("<br>", "");
							description4 = description3.replace("<b>", "");
							descript_text.setText(description4.replace("</b>", ""));
						}catch(NullPointerException e){
							descript_text.setText("Unknown");
							e.printStackTrace();
						}
					}
					if (detailMap.containsKey("relations")) {
						String relation = (new Gson()).toJson(detailMap.get("relations"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						relationMap = new Gson().fromJson(relation, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview_relation.setAdapter(new Recyclerview_relationAdapter(relationMap));
					}
					if (detailMap.containsKey("recommendations")) {
						String recommendations = (new Gson()).toJson(detailMap.get("recommendations"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recommendationsListMap = new Gson().fromJson(recommendations, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview_recommend.setAdapter(new Recyclerview_recommendAdapter(recommendationsListMap));
					}
				}catch(NullPointerException e){
					e.printStackTrace();
					SweetToast.defaultLong(getApplicationContext(), "Something went wrong, please contact the developer");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		score_linear.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xB3212121));
		watch_btn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFFFF5722));
		backBtn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xB3212121));
		linear_main.setVisibility(View.GONE);
		score.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		anime_title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
		descript_text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
		anime_status.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
		anime_format.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
		anime_year.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 0);
		swiperefreshlayout1.setRefreshing(true);
		cardview1.setCardBackgroundColor(0xFFFFFFFF);
		cardview1.setRadius((float)5);
		cardview1.setCardElevation((float)20);
		cardview1.setPreventCornerOverlap(false);
		tablayout1.addTab(tablayout1.newTab().setText("Description"));
		tablayout1.addTab(tablayout1.newTab().setText("Related"));
		tablayout1.addTab(tablayout1.newTab().setText("Recommended"));
		tablayout1.setInlineLabel(true);
		tablayout1.setTabTextColors(0xFFFFFFFF, 0xFFFFFFFF);
		tablayout1.setTabRippleColor(new android.content.res.ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}}, 
		
		new int[] {0xFF212121}));
		tablayout1.setSelectedTabIndicatorColor(0xFFFF5722);
		recyclerview_recommend.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
		recyclerview_relation.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
		linear_description_details.setVisibility(View.VISIBLE);
		recyclerview_relation.setVisibility(View.GONE);
		recyclerview_recommend.setVisibility(View.GONE);
		if (SketchwareUtil.isConnected(getApplicationContext())) {
			requestMangaInfo.startRequestNetwork(RequestNetworkController.GET, getIntent().getStringExtra("path"), "a", _requestMangaInfo_request_listener);
		}
		else {
			swiperefreshlayout1.setRefreshing(false);
			linear_main.setVisibility(View.GONE);
			SweetToast.defaultLong(getApplicationContext(), "Failed to fetch data. Please check your internet connection");
		}
	}
	
	
	@Override
	public void onBackPressed() {
		finish();
		Animatoo.animateSlideDown(MangapageActivity.this);
	}
	public void _click_effect(final View _view, final String _c) {
		_view.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(_c)));
		_view.setClickable(true);
		
	}
	
	public static class Drawables {
		    public static android.graphics.drawable.Drawable getSelectableDrawableFor(int color) {
			        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				            android.graphics.drawable.StateListDrawable stateListDrawable = new android.graphics.drawable.StateListDrawable();
				            stateListDrawable.addState(
				                new int[]{android.R.attr.state_pressed},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#ffffff"))
				            );
				            stateListDrawable.addState(
				                new int[]{android.R.attr.state_focused},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
				            );
				            stateListDrawable.addState(
				                new int[]{},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
				            );
				            return stateListDrawable;
				        } else {
				            android.content.res.ColorStateList pressedColor = android.content.res.ColorStateList.valueOf(color);
				            android.graphics.drawable.ColorDrawable defaultColor = new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"));
				            
				android.graphics.drawable.Drawable rippleColor = getRippleColor(color);
				            return new android.graphics.drawable.RippleDrawable(
				                pressedColor,
				                defaultColor,
				                rippleColor
				            );
				        }
			    }
		
		    private static android.graphics.drawable.Drawable getRippleColor(int color) {
			        float[] outerRadii = new float[8];
			        Arrays.fill(outerRadii, 0);
			        android.graphics.drawable.shapes.RoundRectShape r = new android.graphics.drawable.shapes.RoundRectShape(outerRadii, null, null);
			        
			android.graphics.drawable.ShapeDrawable shapeDrawable = new 
			android.graphics.drawable.ShapeDrawable(r);
			        shapeDrawable.getPaint().setColor(color);
			        return shapeDrawable;
			    }
		 
		    private static int lightenOrDarken(int color, double fraction) {
			        if (canLighten(color, fraction)) {
				            return lighten(color, fraction);
				        } else {
				            return darken(color, fraction);
				        }
			    }
		 
		    private static int lighten(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        red = lightenColor(red, fraction);
			        green = lightenColor(green, fraction);
			        blue = lightenColor(blue, fraction);
			        int alpha = Color.alpha(color);
			        return Color.argb(alpha, red, green, blue);
			    }
		 
		    private static int darken(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        red = darkenColor(red, fraction);
			        green = darkenColor(green, fraction);
			        blue = darkenColor(blue, fraction);
			        int alpha = Color.alpha(color);
			 
			        return Color.argb(alpha, red, green, blue);
			    }
		 
		    private static boolean canLighten(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        return canLightenComponent(red, fraction)
			            && canLightenComponent(green, fraction)
			            && canLightenComponent(blue, fraction);
			    }
		 
		    private static boolean canLightenComponent(int colorComponent, double fraction) {
			        int red = Color.red(colorComponent);
			        int green = Color.green(colorComponent);
			        int blue = Color.blue(colorComponent);
			        return red + (red * fraction) < 255
			            && green + (green * fraction) < 255
			            && blue + (blue * fraction) < 255;
			    }
		 
		    private static int darkenColor(int color, double fraction) {
			        return (int) Math.max(color - (color * fraction), 0);
			    }
		 
		    private static int lightenColor(int color, double fraction) {
			        return (int) Math.min(color + (color * fraction), 255);
			    }
	}
	public static class CircleDrawables {
		    public static android.graphics.drawable.Drawable getSelectableDrawableFor(int color) {
			        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				            android.graphics.drawable.StateListDrawable stateListDrawable = new android.graphics.drawable.StateListDrawable();
				            stateListDrawable.addState(
				                new int[]{android.R.attr.state_pressed},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#ffffff"))
				            );
				            stateListDrawable.addState(
				                new int[]{android.R.attr.state_focused},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
				            );
				            stateListDrawable.addState(
				                new int[]{},
				                new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
				            );
				            return stateListDrawable;
				        } else {
				            android.content.res.ColorStateList pressedColor = android.content.res.ColorStateList.valueOf(color);
				            android.graphics.drawable.ColorDrawable defaultColor = new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"));
				            
				android.graphics.drawable.Drawable rippleColor = getRippleColor(color);
				            return new android.graphics.drawable.RippleDrawable(
				                pressedColor,
				                defaultColor,
				                rippleColor
				            );
				        }
			    }
		
		    private static android.graphics.drawable.Drawable getRippleColor(int color) {
			        float[] outerRadii = new float[180];
			        Arrays.fill(outerRadii, 80);
			        android.graphics.drawable.shapes.RoundRectShape r = new android.graphics.drawable.shapes.RoundRectShape(outerRadii, null, null);
			        
			android.graphics.drawable.ShapeDrawable shapeDrawable = new 
			android.graphics.drawable.ShapeDrawable(r);
			        shapeDrawable.getPaint().setColor(color);
			        return shapeDrawable;
			    }
		 
		    private static int lightenOrDarken(int color, double fraction) {
			        if (canLighten(color, fraction)) {
				            return lighten(color, fraction);
				        } else {
				            return darken(color, fraction);
				        }
			    }
		 
		    private static int lighten(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        red = lightenColor(red, fraction);
			        green = lightenColor(green, fraction);
			        blue = lightenColor(blue, fraction);
			        int alpha = Color.alpha(color);
			        return Color.argb(alpha, red, green, blue);
			    }
		 
		    private static int darken(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        red = darkenColor(red, fraction);
			        green = darkenColor(green, fraction);
			        blue = darkenColor(blue, fraction);
			        int alpha = Color.alpha(color);
			 
			        return Color.argb(alpha, red, green, blue);
			    }
		 
		    private static boolean canLighten(int color, double fraction) {
			        int red = Color.red(color);
			        int green = Color.green(color);
			        int blue = Color.blue(color);
			        return canLightenComponent(red, fraction)
			            && canLightenComponent(green, fraction)
			            && canLightenComponent(blue, fraction);
			    }
		 
		    private static boolean canLightenComponent(int colorComponent, double fraction) {
			        int red = Color.red(colorComponent);
			        int green = Color.green(colorComponent);
			        int blue = Color.blue(colorComponent);
			        return red + (red * fraction) < 255
			            && green + (green * fraction) < 255
			            && blue + (blue * fraction) < 255;
			    }
		 
		    private static int darkenColor(int color, double fraction) {
			        return (int) Math.max(color - (color * fraction), 0);
			    }
		 
		    private static int lightenColor(int color, double fraction) {
			        return (int) Math.min(color + (color * fraction), 255);
		}
	}
	
	public void drawableclass() {
	}
	
	public class Recyclerview_relationAdapter extends RecyclerView.Adapter<Recyclerview_relationAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview_relationAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.new_release_item, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final RelativeLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear_cover = _view.findViewById(R.id.linear_cover);
			final RelativeLayout linear_body = _view.findViewById(R.id.linear_body);
			final ImageView anime_cover = _view.findViewById(R.id.anime_cover);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear_clickEffect = _view.findViewById(R.id.linear_clickEffect);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final TextView anime_title = _view.findViewById(R.id.anime_title);
			final LinearLayout score_linear = _view.findViewById(R.id.score_linear);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView score = _view.findViewById(R.id.score);
			
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_view.setLayoutParams(_lp);
			score_linear.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xB3212121));
			cardview1.setCardBackgroundColor(0xFFFFFFFF);
			cardview1.setRadius((float)5);
			cardview1.setCardElevation((float)5);
			cardview1.setPreventCornerOverlap(false);
			anime_title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_title.setMaxLines(Integer.MAX_VALUE);
			anime_title.setTextSize(12);
			score.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
			score.setTextSize(11);
			try{
				ratingsNumber = Double.parseDouble(_data.get((int)_position).get("rating").toString());
				score.setText(String.valueOf((long)(ratingsNumber)));
			}catch(NullPointerException e){
				score.setText("??");
			}
			if (!_data.get((int)_position).get("image").toString().equals(null)) {
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("image").toString()))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(anime_cover);
			}
			else {
				anime_cover.setImageResource(R.drawable.nocover);
			}
			titlesList = _data.get((int)_position);
			if (titlesList.containsKey("title")) {
				String title = (new Gson()).toJson(titlesList.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titleMap = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					anime_title.setText(titleMap.get("userPreferred").toString().concat(" (".concat(_data.get((int)_position).get("type").toString().concat(")"))));
				} catch (Exception e) {
					e.printStackTrace();
					anime_title.setText(titleMap.get("english").toString().concat(" (".concat(_data.get((int)_position).get("type").toString().concat(")"))));
				}
			}
			linear_clickEffect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_click_effect(linear_clickEffect, "#212121");
					if (!_data.get((int)_position).get("type").toString().toUpperCase().equals("MANGA".toUpperCase())) {
						animeID = Double.parseDouble(_data.get((int)_position).get("id").toString());
						clickDelay = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Intent intent = new Intent(MangapageActivity.this, PagerActivity.class);
										intent.putExtra("path", "https://api-amvstrm.nyt92.eu.org/api/v2/info/".concat(String.valueOf((long)(animeID))));
										startActivity(intent);
										Animatoo.animateSlideUp(MangapageActivity.this);
										finish();
									}
								});
							}
						};
						_timer.schedule(clickDelay, (int)(300));
					}
					else {
						clickDelay = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										animeID = Double.parseDouble(_data.get((int)_position).get("id").toString());
										Intent intent = new Intent(MangapageActivity.this, MangapageActivity.class);
										intent.putExtra("path", "https://www.novelpubfile.xyz/meta/anilist-manga/info/".concat(String.valueOf((long)(animeID)).concat("?provider=mangadex")));
										startActivity(intent);
										Animatoo.animateSlideUp(MangapageActivity.this);
										finish();
									}
								});
							}
						};
						_timer.schedule(clickDelay, (int)(300));
					}
				}
			});
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	public class Recyclerview_recommendAdapter extends RecyclerView.Adapter<Recyclerview_recommendAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview_recommendAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.new_release_item, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final RelativeLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear_cover = _view.findViewById(R.id.linear_cover);
			final RelativeLayout linear_body = _view.findViewById(R.id.linear_body);
			final ImageView anime_cover = _view.findViewById(R.id.anime_cover);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear_clickEffect = _view.findViewById(R.id.linear_clickEffect);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final TextView anime_title = _view.findViewById(R.id.anime_title);
			final LinearLayout score_linear = _view.findViewById(R.id.score_linear);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView score = _view.findViewById(R.id.score);
			
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_view.setLayoutParams(_lp);
			score_linear.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xB3212121));
			cardview1.setCardBackgroundColor(0xFFFFFFFF);
			cardview1.setRadius((float)5);
			cardview1.setCardElevation((float)5);
			cardview1.setPreventCornerOverlap(false);
			anime_title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
			score.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
			try{
				ratingsNumber = Double.parseDouble(_data.get((int)_position).get("rating").toString());
				score.setText(String.valueOf((long)(ratingsNumber)));
			}catch(NullPointerException e){
				score.setText("??");
			}
			if (!_data.get((int)_position).get("image").toString().equals(null)) {
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("image").toString()))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(anime_cover);
			}
			else {
				anime_cover.setImageResource(R.drawable.nocover);
			}
			titlesList = _data.get((int)_position);
			if (titlesList.containsKey("title")) {
				String title = (new Gson()).toJson(titlesList.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titleMap = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					anime_title.setText(titleMap.get("userPreferred").toString());
				} catch (Exception e) {
					e.printStackTrace();
					anime_title.setText(titleMap.get("english").toString());
				}
			}
			linear_clickEffect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_click_effect(linear_clickEffect, "#212121");
					animeID = Double.parseDouble(_data.get((int)_position).get("id").toString());
					clickDelay = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Intent intent = new Intent(MangapageActivity.this, MangapageActivity.class);
									intent.putExtra("path", "https://www.novelpubfile.xyz/meta/anilist-manga/info/".concat(String.valueOf((long)(animeID)).concat("?provider=mangadex")));
									startActivity(intent);
									Animatoo.animateSlideUp(MangapageActivity.this);
									finish();
								}
							});
						}
					};
					_timer.schedule(clickDelay, (int)(300));
				}
			});
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}