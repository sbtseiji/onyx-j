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
package gui.tutorial;

import java.awt.Component;

import engine.Dataset;
import engine.ModelListener;
import engine.ModelRun.Status;
import engine.ModelRun.Warning;
import engine.RawDataset;
import engine.backend.Model.Strategy;
import gui.Desktop;
import gui.DesktopListener;
import gui.graph.Edge;
import gui.graph.Node;
import gui.views.DataView;
import gui.views.ModelView;
import gui.views.View;

public class TutorialFirstSteps implements DesktopListener, ModelListener {

	int counter = 0;
	
	TutorialView tutorialView;
	Desktop desktop;

	private ModelView modelView;
	DataView dv;
	
	Node node1, node2, node3;
	
	public TutorialFirstSteps(Desktop desktop)
	{
	
		
		//if (ok) {
		tutorialView = new TutorialView(desktop);
		desktop.add(tutorialView);
		this.desktop = desktop;
		desktop.addDesktopListener(this);
		
		step();
		
		//}
	}
	
	
	public void step()
	{
		
		if (counter == 0) {
			tutorialView.setText("Onyxへようこそ！ このチュートリアルでは、Onyxでのモデリングの基本を案内します。まずは空のモデルを作成しましょう。ワークスペースをダブルクリックしてください。");

			for (View v : desktop.getViews()) {
				if (v instanceof gui.views.ModelView) {
					modelView = ((ModelView)v);
					modelView.getModelRequestInterface().addModelListener(this);
					counter++;
				}
			}
		}
		
		
		if (counter == 1) {
			tutorialView.setText("	よくできました！ 次はデータセットを開きましょう。上部メニューバーから「チュートリアルを読み込む」を選び、「Simple Regression Example」を読み込んでください。'.");
		
			for (View v : desktop.getViews()) {
				if (v instanceof gui.views.DataView) {
					dv = (DataView)v;
					if (dv.getName().equals("Simple Regression Example")) counter++;
//					counter++;
				}
			}
			
		}

		if (counter == 2) {
			tutorialView.setText("次に、データセットからモデルへ変数を追加しましょう。データセット内の変数Xをクリックし、マウスボタンを押したままモデルパネルへドラッグしてください。");

			for (Node node : modelView.getGraph().getNodes()) {
				if (node.getCaption().equals("X_c")) {
					counter++;
					node1 = node;
					break;
				}
			}
			
		}
		
		if (counter == 3) {
			tutorialView.setText("素晴らしい！ 次はデータセットからもう一つの変数をモデルへ追加しましょう。変数Yを同じようにドラッグしてください。");

			for (Node node : modelView.getGraph().getNodes()) {
				if (node.getCaption().equals("Y_c")) {
					counter++;
					node2 = node;
					break;
				}
			}
			
		}
		
		if (counter == 4) {
			tutorialView.setText("次は潜在変数を追加します。モデルパネルをダブルクリックして潜在変数を作成してください。");

			for (Node node : modelView.getGraph().getNodes()) {
				if (node.isLatent()) {
					counter++;
					node3 = node;break;
				}
			}
			
		}
		
		if (counter == 5) {
			tutorialView.setText("潜在変数のパス（いわゆる因子負荷）を作成しましょう。潜在変数を右クリックし、X_cへパスをドラッグしてください！");
			for (Edge edge : modelView.getGraph().getEdges()) {
				if (edge.getSource()==node3 && edge.getTarget()==node1) {
					counter++; break;
				}
			}
		}
		
		if (counter == 6) {
			tutorialView.setText("素晴らしい！ 同じ方法で潜在変数からY_cにもパスをつなげてください！");
			for (Edge edge : modelView.getGraph().getEdges()) {
				if (edge.getSource()==node3 && edge.getTarget()==node2) {
					counter++; break;
				}
			}
		}
		
		if (counter == 7) {
			tutorialView.setText("現在自由に推定されている潜在構成概念の分散を1に固定しましょう。潜在変数上の両矢印パスを右クリックして「パラメータを固定」を選択し、もう一度右クリックして値を「1」に設定してください。");
			for (Edge edge : modelView.getGraph().getEdges()) {
				if (edge.getSource()==node3 && edge.getTarget()==node3) {
					if (edge.isFixed() && edge.getValue()==1)
						counter++; break;
				}
			}
		}
		
		if (counter == 8) {
			tutorialView.setText("潜在変数からY_cへのパスの負荷を自由に推定できるようにしましょう。該当パスを右クリックし「パラメタを推定」を選択してください。");
			for (Edge edge : modelView.getGraph().getEdges()) {
				if (edge.getSource()==node3 && edge.getTarget()==node2) {
					if (edge.isFree())
						counter++; break;
				}
			}
		}
		
		
		
		if (counter == 9) {
			tutorialView.setText("おめでとうございます！ これでチュートリアルは終了です。右クリックして「閉じる」を選ぶとこの画面を閉じられます。");

		}
		
	}


	@Override
	public void viewAdded() {
		step();
		
		//desktop.
		desktop.moveToFront(tutorialView);
		//	tutorialView.
	}


	@Override
	public void addNode(Node node) {
		step();
		
	}


	@Override
	public void addEdge(Edge edge) {
		step();
		
	}


	@Override
	public void swapLatentToManifest(Node node) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeName(String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeEdge(int source, int target, boolean isDoubleHeaded) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeNode(int id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteModel() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void cycleArrowHeads(Edge edge) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void swapFixed(Edge edge) {
		step();
		
	}


	@Override
	public void changeStatus(Status status) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void notifyOfConvergedUnitsChanged() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setValue(Edge edge) {
		step();
		
	}


	@Override
	public void notifyOfStartValueChange() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeParameterOnEdge(Edge edge) {
		step();
		
	}


	@Override
	public void notifyOfWarningOrError(Warning warning) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void newData(int N, boolean isRawData) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeNodeCaption(Node node, String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setDefinitionVariable(Edge edge) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void notifyOfClearWarningOrError(Warning warning) {}


	public View getView() {
		return this.tutorialView;
	}


	@Override
	public void unsetDefinitionVariable(Edge edge) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setGroupingVariable(Node node) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unsetGroupingVariable(Node node) {
		// TODO Auto-generated method stub
		
	}


    @Override
    public void notifyOfFailedReset() {
        // TODO Auto-generated method stub
        
    }


	@Override
	public void addDataset(Dataset dataset, int x, int y) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addDataset(double[][] dataset, String name, String[] additionalVariableNames, int x, int y) {
		// TODO Auto-generated method stub
		
	}


    @Override
    public void addAuxiliaryVariable(String name, int index) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void addControlVariable(String name, int index) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void removeAuxiliaryVariable(int index) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void removeControlVariable(int index) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void notifyOfStrategyChange(Strategy strategy) {
        // TODO Auto-generated method stub
        
    }
	
	
}
