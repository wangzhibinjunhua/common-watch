package com.example.watch.datashow;

import java.util.ArrayList;
import java.util.List;

import com.example.watch.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;

public class DataActivity extends Activity implements OnClickListener{

	LinearLayout arc;
    RelativeLayout pillars,linear;
    
	private ImageView backView;
	private TextView titleView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_datashow);
		initView();
		arc = (LinearLayout) findViewById(R.id.arc);
		arc.addView(new HomeArc(this, 90)); 
		
		List<Score> list = new ArrayList<Score>();
		for (int i = 0; i < 28; i++) {
			Score s = new Score();
			s.date = "2013-10-" + i;
			//s.score = getRandom(10,100);
			s.sbp=getRandom(105, 125);
			s.dbp=getRandom(68,85);
			list.add(s);
		}
		pillars= (RelativeLayout) findViewById(R.id.pillars);
		pillars.addView(new HomeColumnar(this,list)); 
		
		List<Integer> lists = new ArrayList<Integer>();
		for (int i = 0; i < 48; i++) {
			if (i < 8 || i == 28 || i == 12 || i == 18 || i == 20 || i == 30
					|| i == 34) {
				lists.add(0);
			} else {
				//lists.add(getRandom(0, 500));
				lists.add(getRandom(50, 100));
			}
		}
		linear= (RelativeLayout) findViewById(R.id.linear);
		linear.addView(new HomeDiagram(this,lists));
	}
	
	private void initView(){
		backView=(ImageView)findViewById(R.id.title_back);
		backView.setOnClickListener(this);
		titleView=(TextView)findViewById(R.id.title_text);
		titleView.setText("½¡¿µÖ¸Êý");
		
		
	}
	
	
	public int getRandom(int min,int max){
		return (int) Math.round(Math.random()*(max-min)+min);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
		
	}

}
