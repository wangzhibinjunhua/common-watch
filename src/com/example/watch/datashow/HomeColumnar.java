package com.example.watch.datashow;

import java.util.List;

import com.example.watch.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 鏌辩姸鍥�
 * 
 * @author Administrator
 * 
 */
public class HomeColumnar extends View {

	private List<Score> score;
	private float tb;
	private float interval_left_right;
	private Paint paint_date, paint_rectf_gray, paint_rectf_blue,paint_line;

	private int fineLineColor = 0xffcc0000;//0x5faaaaaa; 
	private int blueLineColor = 0xff00ffff; 

	public HomeColumnar(Context context, List<Score> score) {
		super(context);
		init(score);
	}

	public void init(List<Score> score) {
		if (null == score || score.size() == 0)
			return;
		this.score = score;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		interval_left_right = tb * 5.0f;
		
		paint_line=new Paint();
		paint_line.setStrokeWidth(tb * 0.1f);
		paint_line.setColor(Color.BLACK);
		paint_line.setAntiAlias(true);
		paint_line.setTextSize(tb * 1.2f);
		
		paint_date = new Paint();
		paint_date.setStrokeWidth(tb * 0.1f);
		paint_date.setTextSize(tb * 1.2f);
		paint_date.setColor(fineLineColor);
		paint_date.setTextAlign(Align.CENTER);

		paint_rectf_gray = new Paint();
		paint_rectf_gray.setStrokeWidth(tb * 0.1f);
		paint_rectf_gray.setColor(fineLineColor);
		paint_rectf_gray.setStyle(Style.FILL);
		paint_rectf_gray.setAntiAlias(true);

		paint_rectf_blue = new Paint();
		//paint_rectf_blue.setStrokeWidth(tb * 0.1f);
		paint_rectf_blue.setStrokeWidth(tb * 0.1f);
		paint_rectf_blue.setColor(blueLineColor);
		paint_rectf_blue.setStyle(Style.FILL);
		paint_rectf_blue.setAntiAlias(true);

		setLayoutParams(new LayoutParams(
				(int) (this.score.size() * interval_left_right),
				LayoutParams.MATCH_PARENT));
	}

	protected void onDraw(Canvas c) {
		if (null == score || score.size() == 0)
			return;
		drawDate(c);
		drawRectf(c);
		drawLine(c);
	}
	
	void drawLine(Canvas c){
		c.drawLine(0, getHeight() - tb * 1.5f-120*(tb*10.0f/100), getWidth(), getHeight() - tb * 1.5f-120*(tb*10.0f/100), paint_line);
		c.drawLine(0, getHeight() - tb * 1.5f-80*(tb*10.0f/100), getWidth(), getHeight() - tb * 1.5f-80*(tb*10.0f/100), paint_line);
		c.drawText("120", tb*0.5f,getHeight() - tb * 1.7f-120*(tb*10.0f/100), paint_line);
		c.drawText("80", tb*0.5f,getHeight() - tb * 1.7f-80*(tb*10.0f/100), paint_line);
	}

	/**
	 * 缁樺埗鐭╁舰
	 * 
	 * @param c
	 */
	public void drawRectf(Canvas c) {
		for (int i = 0; i < score.size(); i++) {

			RectF f = new RectF();
//			f.set(tb * 0.2f + interval_left_right * i,
//					getHeight() - tb * 11.0f, tb * 3.2f + interval_left_right
//							* i, getHeight() - tb * 2.0f);
			float dbp_base=score.get(i).dbp * (tb * 10.0f / 100);
			f.set(tb * 0.2f + interval_left_right * i+tb*1.6f,
					getHeight() - (dbp_base + tb * 1.5f), tb * 3.2f + interval_left_right
							* i, getHeight() - tb * 1.5f);
			c.drawRoundRect(f, tb * 0.3f, tb * 0.3f, paint_rectf_gray);

			float sbp_base = score.get(i).sbp * (tb * 10.0f / 100);
			//Log.d("wzb","sbp="+sbp_base+" dbp="+dbp_base+" s"+score.get(i).dbp+" d"+score.get(i).sbp+" tb:"+tb);
			RectF f1 = new RectF();
//			f1.set(tb * 0.2f + interval_left_right * i, getHeight()
//					- (base + tb * 1.5f), tb * 3.2f + interval_left_right * i,
//					getHeight() - tb * 1.5f);
			f1.set(tb * 0.2f + interval_left_right * i, getHeight()
					- (sbp_base + tb * 1.5f), tb * 1.6f + interval_left_right * i,
					getHeight() - tb * 1.5f);
			c.drawRoundRect(f1, tb * 0.3f, tb * 0.3f, paint_rectf_blue);
		}
	}

	/**
	 * 缁樺埗鏃ユ湡
	 * 
	 * @param c
	 */
	public void drawDate(Canvas c) {
		for (int i = 0; i < score.size(); i++) {
			String date = score.get(i).date;
			String date_1 = date
					.substring(date.indexOf("-") + 1, date.length());
			c.drawText(date_1, tb * 1.7f + interval_left_right * i,
					getHeight(), paint_date);

		}
	}
}
