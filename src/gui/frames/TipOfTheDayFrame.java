/*
* Copyright 2023 by Timo von Oertzen and Andreas M. Brandmaier
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package gui.frames;

import engine.Preferences;
import gui.MessageObject;
import gui.Utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TipOfTheDayFrame extends JDialog implements ActionListener, MouseListener
{

	JButton nextTip;
	JButton close;
	JCheckBox dontshow;
    JLabel dontshowlabel;
    
    String[] tips = {
    "デスクトップ、エッジ、ノードなど、Onyxのどこでも右クリックでコンテキスト・メニューを開けます。右クリックがない場合はコントロールキーを押しながら左クリックしてください。"
    + "潜在変数はモデルパネル上でダブルクリック、観測変数はシフトキー＋ダブルクリックで作成できます。",
    "変数間のパスは、1つの変数から別の変数へ右ドラッグ（マウスボタンを押したまま移動）で作成できます。",
    "データセットやモデルは、ファイルブラウザからOnyxデスクトップへドラッグ＆ドロップで読み込めます。",
    "データセット内の変数をモデルパネルへドラッグすると観測変数を作成できます。",
    "パス上のラベルは、マウスでドラッグすればエッジに沿って移動できます。",
    "すべての観測変数にデータがセットされると、モデル推定が自動で始まります。変数にデータをセットするには、データセットからモデル内の観測変数へドラッグしてください。",
    "Onyxのグラフィカルモデルは、ビットマップ画像（JPEG, PNG）、ベクター形式（EPS, PDF）、スクリプト言語（OpenMx, Mplus, lavaan）など様々な形式で書き出せます。",
    "コントロールキー＋Z（Macでは⌘+Z）で直前の操作を元に戻せます。",
    "1つの変数から別の変数へ右ドラッグすると回帰パスが作成されます。シフトキーを押しながらドラッグすると共分散パスが作成されます。",
    "共分散パスの曲率は2つのコントロールポイントで柔軟に調整できます。エッジのコンテキストメニューで「手動エッジ制御」を選択するとコントロールポイントが表示されます。",
    "パスの見た目は、線の色、太さ、ラベルのフォントサイズなどさまざまな方法で変更できます。"
    };
	private Image image;
	private JLabel imageLabel;
	private JLabel tipText;
	
	int tipCounter = -1;
	
	public TipOfTheDayFrame()
	{
		 URL  url =  this.getClass().getResource("/icons/mono/lightbulb32.png"); 
		 if (url != null) {
		 	image = Utilities.resizeImage(new ImageIcon(url).getImage(),24,24 );
		 }	
		
		nextTip = new JButton("次のヒント");
		close = new JButton("閉じる");
		dontshow = new JCheckBox("起動時にヒントを表示");
		
		nextTip.addActionListener(this);
		close.addActionListener(this);
		dontshow.addActionListener(this);
		
		// wait for preferences to be loaded!
	/*	while(true) {
			i
			Thread.sleep(100);
		}*/
		
		dontshow.setSelected(
				Boolean.parseBoolean(
						Preferences.getAsString("ShowTipOfTheDay")));
		//dontshowlabel = new JLabel();
		imageLabel = new JLabel( new ImageIcon(image) );
		
		tipText = new JLabel();
		
		
		nextTip.addMouseListener(this);
		dontshow.addMouseListener(this);
		this.addMouseListener(this);
		tipText.addMouseListener(this);
		close.addMouseListener(this);
		
//		this.setLayout();
		tipText.setBackground(Color.WHITE);
		tipText.setBorder(BorderFactory.createEmptyBorder());
		JPanel buttonPanel = new JPanel();
		

		buttonPanel.add(dontshow);
		buttonPanel.add(nextTip);
		buttonPanel.add(close);
		
		this.add(imageLabel, BorderLayout.WEST);
		this.add(tipText, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		// restore tip
		try {
		tipCounter = Integer.parseInt(Preferences.getAsString("CurrentTipOfTheDay"))
				;
		} catch (Exception e) {}
		
		nextTip();
		
		this.setModal(true);
		this.setSize(450,270);
		this.setLocationRelativeTo(null); // center x,y
		
	}
	
	private void nextTip() {
    tipCounter++;
    tipCounter %= tips.length;

    this.tipText.setText(
        "<html>"+
        "<h2>ご存知でしたか？</h2><hr>"+tips[tipCounter]+
        "</html>");
		
//		this.tipText.setAlignmentY(0);
		this.tipText.setVerticalTextPosition(SwingConstants.TOP);
		this.tipText.setVerticalAlignment(SwingConstants.TOP);
		this.tipText.setPreferredSize(new Dimension(400,300));
		this.tipText.setOpaque(true);
//		this.tipText.
		
		Preferences.set("CurrentTipOfTheDay", Integer.toString( tipCounter));
		
	}

	public static void main(String[] args)
	{
		new TipOfTheDayFrame();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==nextTip)
		{
			nextTip();
		}
		
		if (arg0.getSource()==close) {
			this.dispose();
		}
		if (arg0.getSource()==dontshow) {
			Preferences.set("ShowTipOfTheDay", Boolean.toString(dontshow.isSelected()));
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (Utilities.isRightMouseButton(e)) {
			JOptionPane.showMessageDialog(this, "ここだけはコンテキストメニューがありません。ごめんなさい。");
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
