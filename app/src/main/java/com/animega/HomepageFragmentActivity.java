package com.animega;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.blogspot.atifsoftwares.animatoolib.*;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.*;
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

public class HomepageFragmentActivity extends Fragment {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> anime_map = new HashMap<>();
	private double averageScore = 0;
	private HashMap<String, Object> animeData = new HashMap<>();
	private HashMap<String, Object> coverImage = new HashMap<>();
	private HashMap<String, Object> titles = new HashMap<>();
	private double episodesTotal = 0;
	private String descriptionCut1 = "";
	private String descriptionCut2 = "";
	private String backupBanner = "";
	private double slideNumber = 0;
	private double animeID = 0;
	private HashMap<String, Object> titleMap = new HashMap<>();
	private HashMap<String, Object> mapManga = new HashMap<>();
	private String mangaID = "";
	
	private ArrayList<HashMap<String, Object>> anime_listmap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> manga_listmap = new ArrayList<>();
	
	private SwipeRefreshLayout swiperefreshlayout1;
	private NestedScrollView vscroll1;
	private LinearLayout linear1;
	private ViewPager viewpager1;
	private LinearLayout trending_linear;
	private LinearLayout popular_linear;
	private LinearLayout linear2;
	private TextView textview1;
	private RecyclerView recyclerview1;
	private TextView textview2;
	private RecyclerView recyclerview2;
	private TextView textview3;
	private RecyclerView recyclerview3;
	
	private RequestNetwork getNewEpisodes;
	private RequestNetwork.RequestListener _getNewEpisodes_request_listener;
	private TimerTask sliderTimer;
	private RequestNetwork getPopular;
	private RequestNetwork.RequestListener _getPopular_request_listener;
	private TimerTask clickDelay;
	private Intent intent = new Intent();
	private RequestNetwork requestTrendingManga;
	private RequestNetwork.RequestListener _requestTrendingManga_request_listener;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.homepage_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		swiperefreshlayout1 = _view.findViewById(R.id.swiperefreshlayout1);
		vscroll1 = _view.findViewById(R.id.vscroll1);
		linear1 = _view.findViewById(R.id.linear1);
		viewpager1 = _view.findViewById(R.id.viewpager1);
		trending_linear = _view.findViewById(R.id.trending_linear);
		popular_linear = _view.findViewById(R.id.popular_linear);
		linear2 = _view.findViewById(R.id.linear2);
		textview1 = _view.findViewById(R.id.textview1);
		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		textview2 = _view.findViewById(R.id.textview2);
		recyclerview2 = _view.findViewById(R.id.recyclerview2);
		textview3 = _view.findViewById(R.id.textview3);
		recyclerview3 = _view.findViewById(R.id.recyclerview3);
		getNewEpisodes = new RequestNetwork((Activity) getContext());
		getPopular = new RequestNetwork((Activity) getContext());
		requestTrendingManga = new RequestNetwork((Activity) getContext());
		
		swiperefreshlayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swiperefreshlayout1.setRefreshing(true);
				if (SketchwareUtil.isConnected(getContext().getApplicationContext())) {
					getNewEpisodes.startRequestNetwork(RequestNetworkController.GET, "https://api-amvstrm.nyt92.eu.org/api/v2/trending?limit=20&p=1", "a", _getNewEpisodes_request_listener);
				}
				else {
					swiperefreshlayout1.setRefreshing(false);
					SweetToast.defaultLong(getContext().getApplicationContext(), "Failed to connect to server. Please check your internet connection and try again!");
				}
			}
		});
		
		_getNewEpisodes_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				swiperefreshlayout1.setRefreshing(false);
				try{
					anime_map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (anime_map.containsKey("results")) {
						String str = (new Gson()).toJson(anime_map.get("results"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						anime_listmap = new Gson().fromJson(str, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview1.setAdapter(new Recyclerview1Adapter(anime_listmap));
						viewpager1.setAdapter(new Viewpager1Adapter(anime_listmap));
						viewpager1.setPageTransformer(true, new DrawFromBackTransformer());
					}
					getPopular.startRequestNetwork(RequestNetworkController.GET, "https://api-amvstrm.nyt92.eu.org/api/v2/popular?limit=20&p=1", "a", _getPopular_request_listener);
				}catch(NullPointerException e){
					e.printStackTrace();
					SweetToast.defaultLong(getContext().getApplicationContext(), "Something error occured. Please contact developer!");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		_getPopular_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				try{
					anime_map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (anime_map.containsKey("results")) {
						String str = (new Gson()).toJson(anime_map.get("results"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						anime_listmap = new Gson().fromJson(str, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview2.setAdapter(new Recyclerview2Adapter(anime_listmap));
					}
				}catch(NullPointerException e){
					e.printStackTrace();
					SweetToast.defaultLong(getContext().getApplicationContext(), "Something error occured. Please contact developer!");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		_requestTrendingManga_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				try{
					mapManga = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (mapManga.containsKey("results")) {
						String str = (new Gson()).toJson(mapManga.get("results"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						manga_listmap = new Gson().fromJson(str, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview3.setAdapter(new Recyclerview3Adapter(manga_listmap));
					}
				}catch(NullPointerException e){
					e.printStackTrace();
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
		recyclerview1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
		recyclerview2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
		recyclerview3.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
		textview1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview2.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
		textview3.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
		swiperefreshlayout1.setRefreshing(true);
		if (SketchwareUtil.isConnected(getContext().getApplicationContext())) {
			getNewEpisodes.startRequestNetwork(RequestNetworkController.GET, "https://api-amvstrm.nyt92.eu.org/api/v2/trending?limit=20&p=1", "a", _getNewEpisodes_request_listener);
			requestTrendingManga.startRequestNetwork(RequestNetworkController.GET, "https://www.novelpubfile.xyz/meta/anilist-manga/trending?page=1&perPage=20", "a", _requestTrendingManga_request_listener);
		}
		else {
			swiperefreshlayout1.setRefreshing(false);
			SweetToast.defaultLong(getContext().getApplicationContext(), "Failed to connect to server. Please check your internet connection and try again!");
		}
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
	
	
	public void _ViewPagerAnimation() {
	}
	public static abstract class BaseTransformer implements androidx.viewpager.widget.ViewPager.PageTransformer {
			protected abstract void onTransform(View view, float position);
			@Override
			public void transformPage(View view, float position) {
					onPreTransform(view, position);
					onTransform(view, position);
					onPostTransform(view, position);
			}
			protected boolean hideOffscreenPages() {
					return true;
			}
			protected boolean isPagingEnabled() {
					return false;
			}
			protected void onPreTransform(View view, float position) {
					final float width = view.getWidth();
					view.setRotationX(0);
					view.setRotationY(0);
					view.setRotation(0);
					view.setScaleX(1);
					view.setScaleY(1);
					view.setPivotX(0);
					view.setPivotY(0);
					view.setTranslationY(0);
					view.setTranslationX(isPagingEnabled() ? 0f : -width * position);
					if (hideOffscreenPages()) {
							view.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
					} else {
							view.setAlpha(1f);
					}
			}
			protected void onPostTransform(View view, float position) {
			}
	}
	public static class AccordionTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					view.setPivotX(position < 0 ? 0 : view.getWidth());
					view.setScaleX(position < 0 ? 1f + position : 1f - position);
			}
	}
	public static class BackgroundToForegroundTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float height = view.getHeight();
					final float width = view.getWidth();
					final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);
					view.setScaleX(scale);
					view.setScaleY(scale);
					view.setPivotX(width * 0.5f);
					view.setPivotY(height * 0.5f);
					view.setTranslationX(position < 0 ? width * position : -width * position * 0.25f);
			}
			private static final float min(float val, float min) {
					return val < min ? min : val;
			}
	}
	public static class CubeInTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					view.setPivotX(position > 0 ? 0 : view.getWidth());
					view.setPivotY(0);
					view.setRotationY(-90f * position);
			}
			@Override
			public boolean isPagingEnabled() {
					return true;
			}
	}
	public static class CubeOutTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					view.setPivotX(position < 0f ? view.getWidth() : 0f);
					view.setPivotY(view.getHeight() * 0.5f);
					view.setRotationY(90f * position);
			}
			@Override
			public boolean isPagingEnabled() {
					return true;
			}
	}
	public static class DefaultTransformer extends BaseTransformer {
			@Override protected void onTransform(View view, float position) {}
			@Override public boolean isPagingEnabled() {
					return true;
			}
	}
	public static class DepthPageTransformer extends BaseTransformer {
			private static final float MIN_SCALE = 0.75f;
			@Override
			protected void onTransform(View view, float position) {
					if (position <= 0f) {
							view.setTranslationX(0f);
							view.setScaleX(1f);
							view.setScaleY(1f);
					} else if (position <= 1f) {
							final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
							view.setAlpha(1 - position);
							view.setPivotY(0.5f * view.getHeight());
							view.setTranslationX(view.getWidth() * -position);
							view.setScaleX(scaleFactor);
							view.setScaleY(scaleFactor);
					}
			}
			@Override
			protected boolean isPagingEnabled() {
					return true;
			}
	}
	public static class ZoomOutTranformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float scale = 1f + Math.abs(position);
					view.setScaleX(scale);
					view.setScaleY(scale);
					view.setPivotX(view.getWidth() * 0.5f);
					view.setPivotY(view.getHeight() * 0.5f);
					view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
					if(position == -1){
							view.setTranslationX(view.getWidth() * -1);
					}
			}
	}
	public static class StackTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
			}
	}
	public static class TabletTransformer extends BaseTransformer {
			private static final Matrix OFFSET_MATRIX = new Matrix();
			private static final Camera OFFSET_CAMERA = new Camera();
			private static final float[] OFFSET_TEMP_FLOAT = new float[2];
			@Override
			protected void onTransform(View view, float position) {
					final float rotation = (position < 0 ? 30f : -30f) * Math.abs(position);
					view.setTranslationX(getOffsetXForRotation(rotation, view.getWidth(), view.getHeight()));
					view.setPivotX(view.getWidth() * 0.5f);
					view.setPivotY(0);
					view.setRotationY(rotation);
			}
			protected static final float getOffsetXForRotation(float degrees, int width, int height) {
					OFFSET_MATRIX.reset();
					OFFSET_CAMERA.save();
					OFFSET_CAMERA.rotateY(Math.abs(degrees));
					OFFSET_CAMERA.getMatrix(OFFSET_MATRIX);
					OFFSET_CAMERA.restore();
					OFFSET_MATRIX.preTranslate(-width * 0.5f, -height * 0.5f);
					OFFSET_MATRIX.postTranslate(width * 0.5f, height * 0.5f);
					OFFSET_TEMP_FLOAT[0] = width;
					OFFSET_TEMP_FLOAT[1] = height;
					OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT);
					return (width - OFFSET_TEMP_FLOAT[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
			}
	}
	public static class ZoomInTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
					view.setScaleX(scale);
					view.setScaleY(scale);
					view.setPivotX(view.getWidth() * 0.5f);
					view.setPivotY(view.getHeight() * 0.5f);
					view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
			}
	}
	public static class ZoomOutSlideTransformer extends BaseTransformer {
			private static final float MIN_SCALE = 0.85f;
			private static final float MIN_ALPHA = 0.5f;
			@Override
			protected void onTransform(View view, float position) {
					if (position >= -1 || position <= 1) {
							final float height = view.getHeight();
							final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
							final float vertMargin = height * (1 - scaleFactor) / 2;
							final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;
							view.setPivotY(0.5f * height);
							if (position < 0) {
									view.setTranslationX(horzMargin - vertMargin / 2);
							} else {
									view.setTranslationX(-horzMargin + vertMargin / 2);
							}
							view.setScaleX(scaleFactor);
							view.setScaleY(scaleFactor);
							view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
					}
			}
	}
	public static class ForegroundToBackgroundTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float height = view.getHeight();
					final float width = view.getWidth();
					final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);
					view.setScaleX(scale);
					view.setScaleY(scale);
					view.setPivotX(width * 0.5f);
					view.setPivotY(height * 0.5f);
					view.setTranslationX(position > 0 ? width * position : -width * position * 0.25f);
			}
			private static final float min(float val, float min) {
					return val < min ? min : val;
			}
	}
	public static class ParallaxPageTransformer extends BaseTransformer {
		    private final int viewToParallax;
		    public ParallaxPageTransformer(final int viewToParallax) {
			        this.viewToParallax = viewToParallax;
			    }
		    @Override
		    protected void onTransform(View view, float position) {
			        int pageWidth = view.getWidth();
			        if (position < -1) {
				            view.setAlpha(1);
				        } else if (position <= 1) {
				            view.findViewById(viewToParallax).setTranslationX(-position * (pageWidth / 2));
				        } else {
				            view.setAlpha(1);
				        }
			    }
	}
	public static class RotateDownTransformer extends BaseTransformer {
			private static final float ROT_MOD = -15f;
			@Override
			protected void onTransform(View view, float position) {
					final float width = view.getWidth();
					final float height = view.getHeight();
					final float rotation = ROT_MOD * position * -1.25f;
					view.setPivotX(width * 0.5f);
					view.setPivotY(height);
					view.setRotation(rotation);
			}
			@Override
			protected boolean isPagingEnabled() {
					return true;
			}
	}
	public static class RotateUpTransformer extends BaseTransformer {
			private static final float ROT_MOD = -15f;
			@Override
			protected void onTransform(View view, float position) {
					final float width = view.getWidth();
					final float rotation = ROT_MOD * position;
					view.setPivotX(width * 0.5f);
					view.setPivotY(0f);
					view.setTranslationX(0f);
					view.setRotation(rotation);
			}
			@Override
			protected boolean isPagingEnabled() {
					return true;
			}
	}
	public static class DrawFromBackTransformer implements androidx.viewpager.widget.ViewPager.PageTransformer {
			private static final float MIN_SCALE = 0.75f;
			@Override
			public void transformPage(View view, float position) {
					int pageWidth = view.getWidth();
					if (position < -1 || position > 1) {
							view.setAlpha(0);
							return;
					}
					if (position <= 0) {
							view.setAlpha(1 + position);
							view.setTranslationX(pageWidth * -position);
							float scaleFactor = MIN_SCALE
									+ (1 - MIN_SCALE) * (1 - Math.abs(position));
							view.setScaleX(scaleFactor);
							view.setScaleY(scaleFactor);
							return;
					}
					if (position > 0.5 && position <= 1) {
							view.setAlpha(0);
							view.setTranslationX(pageWidth * -position);
							return;
					}
					if (position > 0.3 && position <= 0.5) {
							view.setAlpha(1);
							view.setTranslationX(pageWidth * position);
							float scaleFactor = MIN_SCALE;
							view.setScaleX(scaleFactor);
							view.setScaleY(scaleFactor);
							return;
					}
					if (position <= 0.3) {
							view.setAlpha(1);
							view.setTranslationX(pageWidth * position);
							float v = (float) (0.3 - position);
							v = v >= 0.25f ? 0.25f : v;
							float scaleFactor = MIN_SCALE + v;
							view.setScaleX(scaleFactor);
							view.setScaleY(scaleFactor);
					}
			}
	}
	public static class FlipHorizontalTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float rotation = 180f * position;
					view.setVisibility(rotation > 90f || rotation < -90f ? View.INVISIBLE : View.VISIBLE);
					view.setPivotX(view.getWidth() * 0.5f);
					view.setPivotY(view.getHeight() * 0.5f);
					view.setRotationY(rotation);
			}
	}
	public static class FlipVerticalTransformer extends BaseTransformer {
			@Override
			protected void onTransform(View view, float position) {
					final float rotation = -180f * position;
					view.setVisibility(rotation > 90f || rotation < -90f ? View.INVISIBLE : View.VISIBLE);
					view.setPivotX(view.getWidth() * 0.5f);
					view.setPivotY(view.getHeight() * 0.5f);
					view.setRotationX(rotation);
			}
	}
	{
	}
	
	public class Viewpager1Adapter extends PagerAdapter {
		
		Context _context;
		ArrayList<HashMap<String, Object>> _data;
		
		public Viewpager1Adapter(Context _ctx, ArrayList<HashMap<String, Object>> _arr) {
			_context = _ctx;
			_data = _arr;
		}
		
		public Viewpager1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_context = getContext().getApplicationContext();
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public boolean isViewFromObject(View _view, Object _object) {
			return _view == _object;
		}
		
		@Override
		public void destroyItem(ViewGroup _container, int _position, Object _object) {
			_container.removeView((View) _object);
		}
		
		@Override
		public int getItemPosition(Object _object) {
			return super.getItemPosition(_object);
		}
		
		@Override
		public CharSequence getPageTitle(int pos) {
			// Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method
			return "page " + String.valueOf(pos);
		}
		
		@Override
		public Object instantiateItem(ViewGroup _container,  final int _position) {
			View _view = LayoutInflater.from(_context).inflate(R.layout.carousel, _container, false);
			
			final RelativeLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final RelativeLayout linear3 = _view.findViewById(R.id.linear3);
			final ImageView banner_image = _view.findViewById(R.id.banner_image);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final LinearLayout linear_clickEffect = _view.findViewById(R.id.linear_clickEffect);
			final LinearLayout linear6 = _view.findViewById(R.id.linear6);
			final LinearLayout linear8 = _view.findViewById(R.id.linear8);
			final LinearLayout score_linear = _view.findViewById(R.id.score_linear);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView score = _view.findViewById(R.id.score);
			final TextView anime_title = _view.findViewById(R.id.anime_title);
			final LinearLayout linear7 = _view.findViewById(R.id.linear7);
			final TextView anime_description = _view.findViewById(R.id.anime_description);
			final TextView anime_status = _view.findViewById(R.id.anime_status);
			final TextView anime_type = _view.findViewById(R.id.anime_type);
			final TextView anime_episodes = _view.findViewById(R.id.anime_episodes);
			
			score_linear.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xB3212121));
			score.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_description.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_status.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_type.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_episodes.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			try{
				averageScore = Double.parseDouble(_data.get((int)_position).get("averageScore").toString());
				score.setText(String.valueOf((long)(averageScore)));
			}catch(NullPointerException e){
				e.printStackTrace();
				score.setText("??");
			}
			try{
				anime_status.setText("•".concat(_data.get((int)_position).get("status").toString()));
			}catch(NullPointerException e){
				e.printStackTrace();
				anime_status.setText("•".concat("??"));
			}
			try{
				episodesTotal = Double.parseDouble(_data.get((int)_position).get("episodes").toString());
				anime_episodes.setText("•".concat(String.valueOf((long)(episodesTotal))));
			}catch(NullPointerException e){
				e.printStackTrace();
				anime_episodes.setText("•".concat("??"));
			}
			try{
				anime_type.setText("•".concat(_data.get((int)_position).get("format").toString()));
			}catch(NullPointerException e){
				e.printStackTrace();
				anime_type.setText("•".concat("??"));
			}
			try{
				descriptionCut1 = _data.get((int)_position).get("description").toString().replace("<i>", "");
				descriptionCut2 = descriptionCut1.replace("</i>", "");
				anime_description.setText(descriptionCut2.replace("<br>", ""));
				if (anime_description.getText().toString().length() > 100) {
					anime_description.setMaxLines(3);
				}
				else {
					anime_description.setMaxLines(2);
				}
			}catch(NullPointerException e){
				e.printStackTrace();
				anime_description.setText("Unknown Description");
				anime_description.setMaxLines(0);
			}
			animeData = _data.get((int)_position);
			if (animeData.containsKey("title")) {
				String title = (new Gson()).toJson(animeData.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titles = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				try{
					anime_title.setText(titles.get("userPreferred").toString());
				}catch(NullPointerException e){
					e.printStackTrace();
					anime_title.setText(titles.get("english").toString());
				}
			}
			if (animeData.containsKey("coverImage")) {
				String images = (new Gson()).toJson(animeData.get("coverImage"), new TypeToken<HashMap<String, Object>>(){}.getType());
				coverImage = new Gson().fromJson(images, new TypeToken<HashMap<String, Object>>(){}.getType());
				backupBanner = coverImage.get("extraLarge").toString();
			}
			try{
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getContext().getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("bannerImage").toString()))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(banner_image);
			}catch(NullPointerException e){
				e.printStackTrace();
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getContext().getApplicationContext())
				.load(Uri.parse(backupBanner))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(banner_image);
			}
			linear_clickEffect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_click_effect(linear_clickEffect, "#212121");
					animeID = Double.parseDouble(_data.get((int)_position).get("id").toString());
					clickDelay = new TimerTask() {
						@Override
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									intent.setClass(getContext().getApplicationContext(), PagerActivity.class);
									intent.putExtra("path", "https://api-amvstrm.nyt92.eu.org/api/v2/info/".concat(String.valueOf((long)(animeID))));
									startActivity(intent);
								}
							});
						}
					};
					_timer.schedule(clickDelay, (int)(300));
				}
			});
			
			_container.addView(_view);
			return _view;
		}
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
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
			anime_title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			score.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			try{
				averageScore = Double.parseDouble(_data.get((int)_position).get("averageScore").toString());
				score.setText(String.valueOf((long)(averageScore)));
			}catch(NullPointerException e){
				e.printStackTrace();
				score.setText("??");
			}
			animeData = _data.get((int)_position);
			if (animeData.containsKey("coverImage")) {
				String coverImageStr = (new Gson()).toJson(animeData.get("coverImage"), new TypeToken<HashMap<String, Object>>(){}.getType());
				coverImage = new Gson().fromJson(coverImageStr, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					
					androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
					loader.setStrokeWidth(6f);
					loader.setCenterRadius(35f);
					loader.start();
					 Glide.with(getContext().getApplicationContext())
					.load(Uri.parse(coverImage.get("extraLarge").toString()))
					.placeholder(loader)
					.error(R.drawable.nocover)
					.into(anime_cover);
				} catch (Exception e) {
					e.printStackTrace();
					anime_cover.setImageResource(R.drawable.nocover);
				}
			}
			if (animeData.containsKey("title")) {
				String title = (new Gson()).toJson(animeData.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titles = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					anime_title.setText(titles.get("userPreferred").toString());
				} catch (Exception e) {
					e.printStackTrace();
					anime_title.setText(titles.get("english").toString());
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
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									intent.setClass(getContext().getApplicationContext(), PagerActivity.class);
									intent.putExtra("path", "https://api-amvstrm.nyt92.eu.org/api/v2/info/".concat(String.valueOf((long)(animeID))));
									startActivity(intent);
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
	
	public class Recyclerview2Adapter extends RecyclerView.Adapter<Recyclerview2Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview2Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
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
			anime_title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			score.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			try{
				averageScore = Double.parseDouble(_data.get((int)_position).get("averageScore").toString());
				score.setText(String.valueOf((long)(averageScore)));
			}catch(NullPointerException e){
				e.printStackTrace();
				score.setText("??");
			}
			animeData = _data.get((int)_position);
			if (animeData.containsKey("coverImage")) {
				String coverImageStr = (new Gson()).toJson(animeData.get("coverImage"), new TypeToken<HashMap<String, Object>>(){}.getType());
				coverImage = new Gson().fromJson(coverImageStr, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					
					androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
					loader.setStrokeWidth(6f);
					loader.setCenterRadius(35f);
					loader.start();
					 Glide.with(getContext().getApplicationContext())
					.load(Uri.parse(coverImage.get("extraLarge").toString()))
					.placeholder(loader)
					.error(R.drawable.nocover)
					.into(anime_cover);
				} catch (Exception e) {
					e.printStackTrace();
					anime_cover.setImageResource(R.drawable.nocover);
				}
			}
			if (animeData.containsKey("title")) {
				String title = (new Gson()).toJson(animeData.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titles = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				try {
					anime_title.setText(titles.get("userPreferred").toString());
				} catch (Exception e) {
					e.printStackTrace();
					anime_title.setText(titles.get("english").toString());
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
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									intent.setClass(getContext().getApplicationContext(), PagerActivity.class);
									intent.putExtra("path", "https://api-amvstrm.nyt92.eu.org/api/v2/info/".concat(String.valueOf((long)(animeID))));
									startActivity(intent);
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
	
	public class Recyclerview3Adapter extends RecyclerView.Adapter<Recyclerview3Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview3Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
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
			anime_title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			score.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			try{
				averageScore = Double.parseDouble(_data.get((int)_position).get("rating").toString());
				score.setText(String.valueOf((long)(averageScore)));
			}catch(NullPointerException e){
				score.setText("??");
			}
			if (!_data.get((int)_position).get("image").toString().equals(null)) {
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getContext().getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("image").toString()))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(anime_cover);
			}
			else {
				anime_cover.setImageResource(R.drawable.nocover);
			}
			titles = _data.get((int)_position);
			if (titles.containsKey("title")) {
				String title = (new Gson()).toJson(titles.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
				titleMap = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
				if (!titleMap.get("userPreferred").toString().equals(null)) {
					anime_title.setText(titleMap.get("userPreferred").toString());
				}
				else {
					if (!titleMap.get("english").toString().equals(null)) {
						anime_title.setText(titleMap.get("english").toString());
					}
					else {
						anime_title.setText("Unknown Title");
					}
				}
			}
			linear_clickEffect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_click_effect(linear_clickEffect, "#212121");
					mangaID = _data.get((int)_position).get("id").toString();
					clickDelay = new TimerTask() {
						@Override
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									intent.setClass(getContext().getApplicationContext(), MangapageActivity.class);
									intent.putExtra("path", "https://www.novelpubfile.xyz/meta/anilist-manga/info/".concat(mangaID.concat("?provider=mangadex")));
									startActivity(intent);
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
}