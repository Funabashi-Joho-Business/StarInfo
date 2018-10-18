package jp.ac.chiba_fjb.sambple.starinfo;

import android.os.Handler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



public class XmlReader {
	interface OnStarListener{
		void onStar(List<Map> stars);
	}

	public static void getStar(final String url, final OnStarListener listener){
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				final List<Map> list = new ArrayList();
				try {
					//XMLデータを読み出す
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(url);
					//最上位エレメントの確認
					Element element = doc.getDocumentElement();
					if(!"starrysky".equals(element.getTagName()))
						return;
					//子ノードを探索
					NodeList nodeList = element.getChildNodes();
					for(int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						//Starノードを見つけたらパラメータを格納
						if("star".equals(node.getNodeName())){
							Map map = new HashMap();
							NodeList nodeList2 = node.getChildNodes();
							for(int j = 0; j < nodeList2.getLength(); j++) {
								Node node2 = nodeList2.item(j);
								if(node2.getNodeType() == Node.ELEMENT_NODE){
									map.put(node2.getNodeName(),node2.getTextContent());
								}
							}
							list.add(map);
						}
					}
					//結果をメインスレッドのリスナーに通知
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onStar(list);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					//結果をメインスレッドのリスナーに通知
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onStar(null);
						}
					});
				}
			}
		}.start();

	}

}
