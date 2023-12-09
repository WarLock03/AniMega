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
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.blogspot.atifsoftwares.animatoolib.*;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.*;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import xyz.hasnat.sweettoast.*;

public class EpisodesActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private HashMap<String, Object> map = new HashMap<>();
	private String episodeID = "";
	private double episodes = 0;
	private String EpisodeTitle = "";
	private String quality = "";
	private double malId = 0;
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmapEpisodes = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private ListView listview1;
	private ProgressBar progressbar1;
	
	private RequestNetwork requestData;
	private RequestNetwork.RequestListener _requestData_request_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.episodes);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear1 = findViewById(R.id.linear1);
		linear3 = findViewById(R.id.linear3);
		listview1 = findViewById(R.id.listview1);
		progressbar1 = findViewById(R.id.progressbar1);
		requestData = new RequestNetwork(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				final com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(EpisodesActivity.this);
				
				final View bottomSheetView; bottomSheetView = getLayoutInflater().inflate(R.layout.quality_choices,null );
				bottomSheetDialog.setContentView(bottomSheetView);
				
				bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
				bottomSheetDialog.setCancelable(true);
				final LinearLayout linear_quality = (LinearLayout) bottomSheetView.findViewById(R.id.linear_quality);
				final Button btn1 = (Button)
				bottomSheetDialog.findViewById(R.id.btn1);
				btn1.setText("360p");
				final Button btn2 = (Button)
				bottomSheetDialog.findViewById(R.id.btn2);
				btn2.setText("480p");
				final Button btn3 = (Button)
				bottomSheetDialog.findViewById(R.id.btn3);
				btn3.setText("720p");
				final Button btn4 = (Button)
				bottomSheetDialog.findViewById(R.id.btn4);
				btn4.setText("1080p");
				final TextView text_1 = (TextView) bottomSheetView.findViewById(R.id.text_1);
				text_1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
				btn1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
				btn2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
				btn3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
				btn4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
				btn1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						Intent intent = new Intent(EpisodesActivity.this, PlayerActivity.class);
						intent.putExtra("watch", "https://www.novelpubfile.xyz/meta/anilist/watch/".concat(listmap.get((int)_position).get("id").toString()));
						quality = "Low";
						intent.putExtra("quality", quality);
						intent.putExtra("animeID", listmap.get((int)_position).get("id").toString());
						episodes = Double.parseDouble(listmap.get((int)_position).get("number").toString());
						try{
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)).concat(" : ".concat(listmap.get((int)_position).get("title").toString())));
						}catch(NullPointerException e){
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)));
						}
						intent.putExtra("Title", EpisodeTitle);
						startActivity(intent);
						Animatoo.animateSwipeLeft(EpisodesActivity.this);
						bottomSheetDialog.dismiss();
					}
				});
				btn2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						Intent intent = new Intent(EpisodesActivity.this, PlayerActivity.class);
						intent.putExtra("watch", "https://www.novelpubfile.xyz/meta/anilist/watch/".concat(listmap.get((int)_position).get("id").toString()));
						intent.putExtra("animeID", listmap.get((int)_position).get("id").toString());
						quality = "Medium";
						intent.putExtra("quality", quality);
						episodes = Double.parseDouble(listmap.get((int)_position).get("number").toString());
						try{
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)).concat(" : ".concat(listmap.get((int)_position).get("title").toString())));
						}catch(NullPointerException e){
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)));
						}
						intent.putExtra("Title", EpisodeTitle);
						startActivity(intent);
						Animatoo.animateSwipeLeft(EpisodesActivity.this);
						bottomSheetDialog.dismiss();
					}
				});
				btn3.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						quality = "High";
						Intent intent = new Intent(EpisodesActivity.this, PlayerActivity.class);
						intent.putExtra("watch", "https://www.novelpubfile.xyz/meta/anilist/watch/".concat(listmap.get((int)_position).get("id").toString()));
						intent.putExtra("animeID", listmap.get((int)_position).get("id").toString());
						intent.putExtra("quality", quality);
						episodes = Double.parseDouble(listmap.get((int)_position).get("number").toString());
						try{
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)).concat(" : ".concat(listmap.get((int)_position).get("title").toString())));
						}catch(NullPointerException e){
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)));
						}
						intent.putExtra("Title", EpisodeTitle);
						startActivity(intent);
						Animatoo.animateSwipeLeft(EpisodesActivity.this);
						bottomSheetDialog.dismiss();
					}
				});
				btn4.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						quality = "Highest";
						Intent intent = new Intent(EpisodesActivity.this, PlayerActivity.class);
						intent.putExtra("watch", "https://www.novelpubfile.xyz/meta/anilist/watch/".concat(listmap.get((int)_position).get("id").toString()));
						intent.putExtra("animeID", listmap.get((int)_position).get("id").toString());
						intent.putExtra("quality", quality);
						episodes = Double.parseDouble(listmap.get((int)_position).get("number").toString());
						try{
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)).concat(" : ".concat(listmap.get((int)_position).get("title").toString())));
						}catch(NullPointerException e){
							EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)));
						}
						intent.putExtra("Title", EpisodeTitle);
						startActivity(intent);
						Animatoo.animateSwipeLeft(EpisodesActivity.this);
						bottomSheetDialog.dismiss();
					}
				});
				bottomSheetDialog.show();
			}
		});
		
		_requestData_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				progressbar1.setVisibility(View.GONE);
				try {
					map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
					if (map.containsKey("episodes")) {
						String ep = (new Gson()).toJson(map.get("episodes"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						listmap = new Gson().fromJson(ep, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						listview1.setAdapter(new Listview1Adapter(listmap));
					}
				} catch (Exception e) {
					e.printStackTrace();
					SweetToast.defaultLong(getApplicationContext(), "Something went wrong. Please try again");
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
		setTitle("Select Episodes");
		progressbar1.setVisibility(View.VISIBLE);
		if (SketchwareUtil.isConnected(getApplicationContext())) {
			requestData.startRequestNetwork(RequestNetworkController.GET, getIntent().getStringExtra("path"), "a", _requestData_request_listener);
		}
		else {
			progressbar1.setVisibility(View.GONE);
			listview1.setVisibility(View.GONE);
			SweetToast.defaultLong(getApplicationContext(), "No internet connection!");
		}
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
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
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.episodeslist, null);
			}
			
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final RelativeLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			
			cardview1.setCardBackgroundColor(0xFFFFFFFF);
			cardview1.setRadius((float)50);
			cardview1.setCardElevation((float)10);
			cardview1.setPreventCornerOverlap(false);
			episodeID = _data.get((int)_position).get("id").toString();
			textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/comforta_medium.ttf"), 1);
			try{
				
				androidx.swiperefreshlayout.widget.CircularProgressDrawable swipe = new androidx.swiperefreshlayout.widget.CircularProgressDrawable(getApplicationContext());
				swipe.setStrokeWidth(6f);
				swipe.setCenterRadius(35f);
				swipe.start();
				 Glide.with(getApplicationContext())
				.load(Uri.parse(_data.get((int)_position).get("image").toString()))
				.placeholder(swipe)
				.error(R.drawable.nocover)
				.into(imageview1);
			}catch(NullPointerException e){
				imageview1.setImageResource(R.drawable.nocover);
			}
			try {
				episodes = Double.parseDouble(_data.get((int)_position).get("number").toString());
				textview2.setText("Episode ".concat(String.valueOf((long)(episodes)).concat(": ".concat(_data.get((int)_position).get("title").toString()))));
				EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)).concat(": ".concat(_data.get((int)_position).get("title").toString())));
			} catch (Exception e) {
				textview2.setText("Episode ".concat(String.valueOf((long)(episodes))));
				EpisodeTitle = "Episode ".concat(String.valueOf((long)(episodes)));
			}
			
			return _view;
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