package jp.ac.chiba_fjb.sambple.starinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements XmlReader.OnStarListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView textView = findViewById(R.id.textView);
		textView.setText("データの読み込み中\n");
		XmlReader.getStar("http://www.walk-in-starrysky.com/star.do",this);
	}

	@Override
	public void onStar(List<Map> stars) {
		TextView textView = findViewById(R.id.textView);
		if(stars!=null) {
			textView.setText("結果\n");
			for (Map map : stars) {
				textView.append(String.format("[%s]%s\n", map.get("hrNo"), map.get("rightAscension")));
			}
		}else{
			textView.setText("エラー\n");
		}
	}
}
