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
package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelpFrame extends JFrame
{

	JLabel label;
	
	public HelpFrame()
	{
		String text = "デスクトップの空白部分をダブルクリックするとモデルパネルを作成できます。"
			+ "モデルパネルの空白部分をダブルクリックすると新しい潜在変数を作成できます。"
			+ "モデルパネルの空白部分をシフトキーを押しながらダブルクリックすると観測変数を作成できます。"
			+ "任意の変数を右クリックし、他の変数へドラッグすると回帰パスを作成できます。"
			+ "任意の変数を右クリックし、シフトキーを押しながら他の変数へドラッグすると（共）分散パスを作成できます。";

		label = new JLabel(text);
	}
	
}
