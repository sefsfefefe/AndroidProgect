package org.feiqi.ui;

import org.feiqi.constant.Constant;

import com.android.test.login.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class ImageText extends LinearLayout{
	private Context mContext = null;
	private ImageView mImageView = null;
	private TextView mTextView = null;
	private final static int DEFAULT_IMAGE_WIDTH = 64;
	private final static int DEFAULT_IMAGE_HEIGHT = 64;
	private int CHECKED_COLOR = Color.rgb(29, 118, 199); //选中蓝色
	private int UNCHECKED_COLOR = Color.GRAY;    //自然灰色
	public ImageText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public ImageText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		
		/*LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
		 * 具体作用：
		 * 1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
		 * 2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。
		 * getSystemService()是Android很重要的一个API，
		 * 它是Activity的一个方法，根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。
		 * */
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View parentView = inflater.inflate(R.layout.image_text_layout, this, true);
		mImageView = (ImageView)findViewById(R.id.image_iamge_text);
		mTextView = (TextView)findViewById(R.id.text_iamge_text);
	}
	public void setImage(int id){
		if(mImageView != null){
			mImageView.setImageResource(id);
			setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		}
	}

	public void setText(String s){
		if(mTextView != null){
			mTextView.setText(s);
			mTextView.setTextColor(UNCHECKED_COLOR);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}
	private void setImageSize(int w, int h){
		if(mImageView != null){
			ViewGroup.LayoutParams params = mImageView.getLayoutParams();
			params.width = w;
			params.height = h;
			mImageView.setLayoutParams(params);
		}
	}
	public void setChecked(int itemID){
		if(mTextView != null){
			mTextView.setTextColor(CHECKED_COLOR);
		}
		int checkDrawableId = -1;
		switch (itemID){
		case Constant.BTN_FLAG_MESSAGE:
			checkDrawableId = R.drawable.activities_icon_running;
			break;
		case Constant.BTN_FLAG_CONTACTS:
			checkDrawableId = R.drawable.icon_found_team_gray;
			break;
		case Constant.BTN_FLAG_NEWS:
			checkDrawableId = R.drawable.news_selected;
			break;
		case Constant.BTN_FLAG_SETTING:
			checkDrawableId = R.drawable.setting_selected;
			break;
		default:break;
		}
		if(mImageView != null){
			mImageView.setImageResource(checkDrawableId);
		}
	}


	


}
