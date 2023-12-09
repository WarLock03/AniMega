package com.animega;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
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

public class SearchFragmentActivity extends Fragment {
	
	private Timer _timer = new Timer();
	
	private String convertStringInput = "";
	private String resultSearchInput = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String description1 = "";
	private String description2 = "";
	private String description3 = "";
	private String descendants4 = "";
	private double ratings = 0;
	private HashMap<String, Object> titlesMap = new HashMap<>();
	private HashMap<String, Object> titlesList = new HashMap<>();
	private String animeID = "";
	private String url = "";
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> searchHistoryListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> searchTypeListMap = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear_search;
	private RecyclerView search_cycleList;
	private ProgressBar progressbar1;
	private RecyclerView recyclerview1;
	private EditText edittext1;
	private Spinner spinner1;
	
	private RequestNetwork requestData;
	private RequestNetwork.RequestListener _requestData_request_listener;
	private TimerTask clickDelay;
	private Intent intent = new Intent();
	private SharedPreferences history;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.search_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		linear1 = _view.findViewById(R.id.linear1);
		linear_search = _view.findViewById(R.id.linear_search);
		search_cycleList = _view.findViewById(R.id.search_cycleList);
		progressbar1 = _view.findViewById(R.id.progressbar1);
		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		edittext1 = _view.findViewById(R.id.edittext1);
		spinner1 = _view.findViewById(R.id.spinner1);
		requestData = new RequestNetwork((Activity) getContext());
		history = getContext().getSharedPreferences("searchHistory", Activity.MODE_PRIVATE);
		
		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (_charSeq.length() > 0) {
					convertStringInput = _charSeq;
					if (convertStringInput.contains(" ")) {
						resultSearchInput = convertStringInput.replace(" ", "%20");
					}
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("search", convertStringInput);
						searchHistoryListMap.add(_item);
					}
					
					history.edit().putString("data", new Gson().toJson(searchHistoryListMap)).commit();
					progressbar1.setVisibility(View.VISIBLE);
					recyclerview1.setVisibility(View.VISIBLE);
					if (SketchwareUtil.isConnected(getContext().getApplicationContext())) {
						requestData.startRequestNetwork(RequestNetworkController.GET, url.concat(resultSearchInput), "a", _requestData_request_listener);
					}
					else {
						SweetToast.defaultLong(getContext().getApplicationContext(), "No internet connection!");
					}
				}
				else {
					progressbar1.setVisibility(View.GONE);
					recyclerview1.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (_position == 0) {
					url = "https://www.novelpubfile.xyz/meta/anilist/";
				}
				if (_position == 1) {
					url = "https://www.novelpubfile.xyz/meta/anilist-manga/";
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		_requestData_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				progressbar1.setVisibility(View.GONE);
				try{
					map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (map.containsKey("results")) {
						String results = (new Gson()).toJson(map.get("results"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						listmap = new Gson().fromJson(results, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						recyclerview1.setAdapter(new Recyclerview1Adapter(listmap));
					}
				}catch(NullPointerException e){
					e.printStackTrace();
					SweetToast.defaultLong(getContext().getApplicationContext(), "Something went wrong, please contact the developer");
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
		linear_search.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xFF424242));
		edittext1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
		recyclerview1.setLayoutManager(new LinearLayoutManager(getContext()));
		search_cycleList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
		progressbar1.setVisibility(View.GONE);
		spinner1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF212121));
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("searchType", "ANIME");
			searchTypeListMap.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("searchType", "MANGA");
			searchTypeListMap.add(_item);
		}
		
		spinner1.setAdapter(new Spinner1Adapter(searchTypeListMap));
		spinner1.setSelection((int)(0));
		if (!history.getString("data", "").equals("")) {
			searchHistoryListMap = new Gson().fromJson(history.getString("data", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			Collections.reverse(searchHistoryListMap);
			while(searchHistoryListMap.size() > 5) {
				searchHistoryListMap.remove((int)(5));
			}
			search_cycleList.setAdapter(new Search_cycleListAdapter(searchHistoryListMap));
		}
		else {
			search_cycleList.setVisibility(View.GONE);
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
	
	public class Search_cycleListAdapter extends RecyclerView.Adapter<Search_cycleListAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Search_cycleListAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
			View _v = _inflater.inflate(R.layout.search_history, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_view.setLayoutParams(_lp);
			textview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)8, 0xFF424242));
			textview1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			textview1.setText(_data.get((int)_position).get("search").toString());
			textview1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					resultSearchInput = _data.get((int)_position).get("search").toString();
					edittext1.setText(resultSearchInput);
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
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
			View _v = _inflater.inflate(R.layout.search_resulta, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final RelativeLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear_clickEffect = _view.findViewById(R.id.linear_clickEffect);
			final ImageView imageCover = _view.findViewById(R.id.imageCover);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final TextView anime_title = _view.findViewById(R.id.anime_title);
			final LinearLayout linear5 = _view.findViewById(R.id.linear5);
			final LinearLayout linear6 = _view.findViewById(R.id.linear6);
			final TextView anime_synopsis = _view.findViewById(R.id.anime_synopsis);
			final LinearLayout linear7 = _view.findViewById(R.id.linear7);
			final LinearLayout linear8 = _view.findViewById(R.id.linear8);
			final LinearLayout linear9 = _view.findViewById(R.id.linear9);
			final ImageView imageview2 = _view.findViewById(R.id.imageview2);
			final TextView anime_status = _view.findViewById(R.id.anime_status);
			final ImageView imageview3 = _view.findViewById(R.id.imageview3);
			final TextView anime_type = _view.findViewById(R.id.anime_type);
			final ImageView imageview4 = _view.findViewById(R.id.imageview4);
			final TextView anime_rating = _view.findViewById(R.id.anime_rating);
			
			cardview1.setCardBackgroundColor(0xFF212121);
			cardview1.setRadius((float)10);
			cardview1.setCardElevation((float)10);
			cardview1.setPreventCornerOverlap(false);
			anime_title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			anime_synopsis.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			anime_status.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			anime_type.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			anime_rating.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 0);
			try{
				description1 = _data.get((int)_position).get("description").toString().replace("<i>", "");
				description2 = description1.replace("</i>", "");
				description3 = description2.replace("<br>", "");
				descendants4 = description3.replace("<b>", "");
				anime_synopsis.setText(descendants4.replace("</b>", ""));
			}catch(NullPointerException e){
				anime_synopsis.setText("Unknown Synopsis");
			}
			if (anime_synopsis.getText().toString().length() > 100) {
				anime_synopsis.setMaxLines(4);
			}
			else {
				anime_synopsis.setMaxLines(0);
			}
			try{
				anime_status.setText(_data.get((int)_position).get("status").toString());
			}catch(NullPointerException e){
				anime_status.setText("??");
			}
			try{
				anime_type.setText(_data.get((int)_position).get("type").toString());
			}catch(NullPointerException e){
				anime_type.setText("??");
			}
			try{
				ratings = Double.parseDouble(_data.get((int)_position).get("rating").toString());
				anime_rating.setText(String.valueOf((long)(ratings)));
			}catch(NullPointerException e){
				anime_rating.setText("??");
			}
			try{
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable loader = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getContext().getApplicationContext());
				loader.setStrokeWidth(6f);
				loader.setCenterRadius(35f);
				loader.start();
				 Glide.with(getContext().getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("image").toString()))
				.placeholder(loader)
				.error(R.drawable.nocover)
				.into(imageCover);
			}catch(NullPointerException e){
				imageCover.setImageResource(R.drawable.nocover);
			}
			titlesMap = _data.get((int)_position);
			String title = (new Gson()).toJson(titlesMap.get("title"), new TypeToken<HashMap<String, Object>>(){}.getType());
			titlesList = new Gson().fromJson(title, new TypeToken<HashMap<String, Object>>(){}.getType());
			try{
				anime_title.setText(titlesList.get("userPreferred").toString());
			}catch(NullPointerException e){
				anime_title.setText(titlesList.get("english").toString());
			}
			linear_clickEffect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_click_effect(linear_clickEffect, "#212121");
					if (!_data.get((int)_position).get("type").toString().toUpperCase().equals("MANGA".toUpperCase())) {
						clickDelay = new TimerTask() {
							@Override
							public void run() {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										animeID = _data.get((int)_position).get("id").toString();
										intent.setClass(getContext().getApplicationContext(), PagerActivity.class);
										intent.putExtra("path", "https://api-amvstrm.nyt92.eu.org/api/v2/info/".concat(animeID));
										startActivity(intent);
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
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										animeID = _data.get((int)_position).get("id").toString();
										intent.setClass(getContext().getApplicationContext(), MangapageActivity.class);
										intent.putExtra("path", "https://www.novelpubfile.xyz/meta/anilist-manga/info/".concat(animeID.concat("?provider=mangadex")));
										startActivity(intent);
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
	
	public class Spinner1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Spinner1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.search_type, null);
			}
			
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			textview1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/comforta_medium.ttf"), 1);
			textview1.setText(_data.get((int)_position).get("searchType").toString());
			
			return _view;
		}
	}
}