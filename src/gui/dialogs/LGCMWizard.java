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
package gui.dialogs;

import engine.ModelRequestInterface;
import gui.Desktop;
import gui.graph.Edge;
import gui.graph.Node;
import gui.views.ModelView;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LGCMWizard extends Dialog implements ChangeListener {

	
	Desktop desktop;
	
	JSpinner numObsInput;
	JTextArea nameObsInput;

	private JTextArea nameErrInput;

	private JTextArea nameIceptInput;

	private JTextArea nameSlopeInput;

	private JCheckBox uniqueResiduals, latentCovariance;

	private SpinnerNumberModel centerModel;

	private JSpinner numCenter;

	private JComboBox<String> typeOfChange;
	
	//final String[] changeTypes = {"Linear","Quadratic","Cubic"};
	
	public LGCMWizard(Desktop desktop)
	{
		super("潜在成長曲線モデルウィザード");
		this.desktop = desktop;

		Dimension d = new Dimension(150,30);

		// # of observations
		numObsInput = new JSpinner(new SpinnerNumberModel(5,2,100,1));
		numObsInput.addChangeListener(this);
		this.addElement("観測時点数", numObsInput);

		centerModel = new SpinnerNumberModel(1,1,5,1);
		numCenter = new JSpinner(centerModel);
		this.addElement("観測の中心時点", numCenter);

		// observation name
		nameObsInput = new JTextArea("x");
		nameObsInput.setSize(d);
		this.addElement("観測変数名", nameObsInput);

		// error term name
		nameErrInput = new JTextArea("e");
		nameErrInput.setSize(d);
		this.addElement("残差分散項名", nameErrInput);

		// icept - slope correlation
		nameSlopeInput = new JTextArea("傾き");
		nameSlopeInput.setSize(d);
		this.addElement("傾き項名", nameSlopeInput);
		nameIceptInput = new JTextArea("切片");
		nameIceptInput.setSize(d);
		this.addElement("切片項名", nameIceptInput);

		uniqueResiduals = new JCheckBox("時間ごとの固有分散");
		this.addElement("残差分散", uniqueResiduals);

		latentCovariance = new JCheckBox("切片と傾きの共分散を推定");
		this.addElement("切片と傾きの共分散", latentCovariance);

		this.addSendButton("作成");

		this.pack();

		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		ModelView mv = new ModelView(desktop);
		desktop.add(mv);
		
		ModelRequestInterface model = mv.getModelRequestInterface();
		
		int numObs = Integer.parseInt((String) numObsInput.getValue().toString());

		double[] timepoints = new double[numObs];
		for (int i=0; i < numObs; i++)
		{
			timepoints[i] = i / (numObs-1.0);
		}
		
		int xOffset = 70;
		int yOffset = 100;
		
		int N = timepoints.length;
		
		//System.out.println("Rebuild with "+N+ " time points");
		
		Node icept = new Node(nameIceptInput.getText());
		icept.setPosition(xOffset,yOffset+70);
		model.requestAddNode(icept);
		Node slope = new Node(nameSlopeInput.getText());
		slope.setPosition(xOffset+200, yOffset+70);
		model.requestAddNode(slope);
		
		
		if (latentCovariance.isSelected()) {
			Edge e = new Edge(icept, slope);
			e.setDoubleHeaded(true);
			e.setFixed(false);
			model.requestAddEdge(e);
		}
		
		Node[] obs = new Node[N];
		for (int i=0; i < N; i++) {
			obs[i] = new Node(nameObsInput.getText()+(i+1));
			obs[i].setIsLatent(false);
			obs[i].setPosition(xOffset+i*80, yOffset+200);
			model.requestAddNode(obs[i]);
			//obs[i].setConnected(true); // workaround
		}
		
		int centerAt = Integer.parseInt(numCenter.getValue().toString())-1;
		
		// Loadings
		for (int i=0; i < N; i++) {
			Edge edge1 = new Edge(icept, obs[i],false);
			model.requestAddEdge(edge1);
			
			int loading = (i-centerAt);
			
			if (loading != 0) {
				Edge edge2 = new Edge(slope, obs[i],false);
				edge2.setValue(loading);
				edge2.edgeLabelRelativePosition = ((i+1)/(float)N);
				model.requestAddEdge(edge2);
			}
		}
		
		// residual variances
		for (int i=0; i < N; i++) {
			Edge edge = new Edge(obs[i], obs[i],true);
			edge.setValue(1.0);
			if (uniqueResiduals.isSelected())
				edge.setParameterName(nameErrInput.getText()+(i+1));
			else
				edge.setParameterName(nameErrInput.getText());
			edge.setAutomaticNaming(false);
			edge.setFixed(false);
			model.requestAddEdge(edge);
		}
		
		// latent variances
		Edge edgeI = new Edge(icept, icept, true);
		//edgeI.setValue(sigmaI);
		edgeI.setFixed(false);
		//edgeI.edgeStyle = Edge.EdgeStyle.ALWAYS_LABEL;
		edgeI.setParameterName("sigma_i");
		edgeI.setAutomaticNaming(false);
		edgeI.setFixed(false);
		model.requestAddEdge(edgeI);
		
		Edge edgeS = new Edge(slope, slope, true);
		edgeS.setValue(1.0);
		edgeS.setParameterName("sigma_s");
		edgeS.setAutomaticNaming(false);
		edgeS.setFixed(false);
		model.requestAddEdge(edgeS);
		
		// add mean
		
		Node mean = new Node();
		mean.setPosition(xOffset+100, yOffset-60);
		mean.setTriangle(true);
		model.requestAddNode(mean);
		
		Edge edgeMi = new Edge(mean, icept);
		edgeMi.setFixed(false);
		edgeMi.setParameterName("mu_i");
		model.requestAddEdge(edgeMi);
		
		Edge edgeMs = new Edge(mean, slope);
		edgeMs.setFixed(false);
		edgeMs.setParameterName("mu_s");
		model.requestAddEdge(edgeMs);
		
	
	/*	Node slopeNode = new Node();
		Node iceptNode = new Node();
		mv.getModelRequestInterface().requestAddNode(iceptNode);
		mv.getModelRequestInterface().requestAddNode(slopeNode);

		iceptNode = 
		
		int x = 0;
		for (int i = 0; i <numObs; i++)
		{
			Node obsNode = new Node();
			mv.getModelRequestInterface().requestAddNode(obsNode);
			Node errNode = new Node();
			mv.getModelRequestInterface().requestAddNode(errNode);
			
			// add paths
			Edge edge1 = new Edge(iceptNode, obsNode);
			Edge edge2 = new Edge(slopeNode, obsNode);
			Edge edge3 = new Edge(errNode, obsNode);
			
			mv.getModelRequestInterface().requestAddEdge(edge1);
			mv.getModelRequestInterface().requestAddEdge(edge2);
			mv.getModelRequestInterface().requestAddEdge(edge3);
		}*/
		
		this.dispose();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		
		if (arg0.getSource() == numObsInput)
		{
			int max =  Integer.parseInt(numObsInput.getValue().toString());
			centerModel.setMaximum( max );
			if (Integer.parseInt(numCenter.getValue().toString()) > max) {
				numCenter.setValue(max);
			}
		}
		
	}
	
	
	
	
	
}
