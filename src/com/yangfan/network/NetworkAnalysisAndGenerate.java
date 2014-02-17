package com.yangfan.network;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;


/**
 * Created by Lxy on 14-2-4.
 */
public class NetworkAnalysisAndGenerate {
    private JPanel panelMain;
    private JPanel panelNetworkDisplay;
    private JTextField textFieldFilePath;
    private JTextField textFieldNetType;
    private JTextField textFieldNodeNum;
    private JTextField textFieldEdgeNum;
    private JTextField textFieldAveDeg;
    private JTextField textFieldShortPath;
    private int nodeNumber;
    private int edgeNumber;
    private NetworkAnalysisAndGenerate mainClass;
    private JFrame mainFrame;

    private void displayNetwork(Graph graph) {

        //initialize panel

        panelNetworkDisplay.removeAll();

        //integrate to GUI

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);
        CellConstraints myConstraint = new CellConstraints();
        panelNetworkDisplay.add(view, myConstraint.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
        viewer.enableAutoLayout();
        panelNetworkDisplay.updateUI();
    }

    private double computeTheShortestPath(Graph graph) {
        double sumOfShort = 0.0;

        for (Node node : graph) {
            int[] dist = new int[graph.getNodeCount()];
            boolean[] visited = new boolean[graph.getNodeCount()];

            final int INF = Integer.MAX_VALUE - 1000;

            for (int i = 0; i < graph.getNodeCount(); ++i)
                dist[i] = INF;
            dist[node.getIndex()] = 0;

            for (int i = 0; i < graph.getNodeCount(); ++i)
                visited[i] = false;

            for (int i = 1; i < graph.getNodeCount(); ++i) {
                int minValue = INF;
                int minNode = 0;
                for (int j = 0; j < graph.getNodeCount(); ++j) {
                    if (!visited[j] && dist[j] < minValue) {
                        minNode = j;
                        minValue = dist[j];
                    }
                }
                if (minValue == INF) break;
                visited[minNode] = true;

                Node indexNode = graph.getNode(minNode);
                Iterator<Edge> leavingEdges = indexNode.getEnteringEdgeIterator();

                while (leavingEdges.hasNext()) {
                    Edge edge = leavingEdges.next();
                    Node nodeTemp = edge.getOpposite(indexNode);
                    if (dist[nodeTemp.getIndex()] > minValue + 1) {
                        dist[nodeTemp.getIndex()] = minValue + 1;
                    }
                }

            }

            for (int i = 0; i < graph.getNodeCount(); ++i)
                sumOfShort += dist[i];
        }

        return sumOfShort * 1.0 / (graph.getNodeCount() * (graph.getNodeCount() - 1));
    }

    private void networkAnalysis(Graph graph) {

        //compute node average degree

        long degreeSum = 0;
        for (Edge eade : graph.getEachEdge()) {
            if (eade.isDirected())
                degreeSum += 2;
            else
                degreeSum += 4;
        }
        double averageDegree = 1.0 * degreeSum / graph.getNodeCount();
        textFieldAveDeg.setText(String.format("%.2f", averageDegree));

        //compute the shortest path of network

        textFieldShortPath.setText(String.format("%.2f", computeTheShortestPath(graph)));

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new FormLayout("fill:275px:noGrow,left:4dlu:noGrow,fill:585dlu:grow", "center:d:grow"));
        panelMain.setAutoscrolls(false);
        panelMain.setMinimumSize(new Dimension(100, 480));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow", "center:338px:grow,top:4dlu:noGrow,center:326px:grow"));
        CellConstraints cc = new CellConstraints();
        panelMain.add(panel1, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:16px:noGrow,left:4dlu:noGrow,fill:80px:grow,left:4dlu:noGrow,fill:140px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:40px:noGrow,top:5dlu:noGrow,center:60px:noGrow,top:5dlu:noGrow,center:60px:noGrow,top:5dlu:noGrow,center:63px:noGrow,top:5dlu:noGrow,center:60px:grow"));
        panel1.add(panel2, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        panel2.setBorder(BorderFactory.createTitledBorder("Network Info"));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("File Path");
        panel2.add(label1, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.TOP));
        textFieldFilePath = new JTextField();
        textFieldFilePath.setEditable(false);
        textFieldFilePath.setText("");
        panel2.add(textFieldFilePath, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.TOP));
        final JLabel label2 = new JLabel();
        label2.setText("Network Type");
        panel2.add(label2, cc.xy(3, 5, CellConstraints.DEFAULT, CellConstraints.TOP));
        textFieldNetType = new JTextField();
        textFieldNetType.setEditable(false);
        panel2.add(textFieldNetType, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.TOP));
        final JLabel label3 = new JLabel();
        label3.setText("Node Number");
        panel2.add(label3, cc.xy(3, 7, CellConstraints.DEFAULT, CellConstraints.TOP));
        textFieldNodeNum = new JTextField();
        textFieldNodeNum.setEditable(false);
        panel2.add(textFieldNodeNum, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.TOP));
        final JLabel label4 = new JLabel();
        label4.setText("Edge Number");
        panel2.add(label4, cc.xy(3, 9, CellConstraints.DEFAULT, CellConstraints.TOP));
        textFieldEdgeNum = new JTextField();
        textFieldEdgeNum.setEditable(false);
        panel2.add(textFieldEdgeNum, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.TOP));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:5px:noGrow,left:5dlu:noGrow,fill:193px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:38px:noGrow,top:4dlu:noGrow,center:51px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:57px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.add(panel3, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        panel3.setBorder(BorderFactory.createTitledBorder("Network Analysis"));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer4, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer5, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setText("Average Degree Of Node :");
        panel3.add(label5, cc.xy(3, 3));
        final JLabel label6 = new JLabel();
        label6.setText("The Shortest Path Of Network :");
        panel3.add(label6, cc.xy(3, 7));
        textFieldAveDeg = new JTextField();
        textFieldAveDeg.setEditable(false);
        panel3.add(textFieldAveDeg, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        textFieldShortPath = new JTextField();
        textFieldShortPath.setEditable(false);
        panel3.add(textFieldShortPath, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer6, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        panelNetworkDisplay = new JPanel();
        panelNetworkDisplay.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        panelMain.add(panelNetworkDisplay, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        panelNetworkDisplay.setBorder(BorderFactory.createTitledBorder("Network Display"));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    class RandomNetworkGenerate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nodeNumber = -1;
            new NetworkSettings(mainClass, mainFrame);
            if (nodeNumber == -1) {
                return;
            }

            //generate random network

            Graph graph = new SingleGraph("Random Network");

            int nodeSum = nodeNumber;
            int edgeSum = edgeNumber;

            boolean visited[][] = new boolean[nodeSum + 5][nodeSum + 5];
            for (int i = 0; i < visited.length; ++i)
                Arrays.fill(visited[i], false);

            graph.addAttribute("ui.stylesheet", "node { fill-color: red;}");
            graph.setStrict(false);

            //Initialize nodes

            for (int v = 0; v < nodeSum; ++v) {
                graph.addNode(Integer.toString(v));
            }

            //Initialize edges

            int tempEdgeSum = 0;
            while (tempEdgeSum < edgeSum) {

                int to = (int) (Math.random() * nodeSum);
                int from = (int) (Math.random() * nodeSum);

                while (to == from) {
                    from = (int) (Math.random() * nodeSum);
                }

                if (visited[to][from])
                    continue;

                visited[to][from] = true;
                visited[from][to] = true;

                String edgeId = "edge" + Integer.toString(from) + "to" + Integer.toString(to);

                graph.addEdge(edgeId, from, to, false);
                tempEdgeSum++;
            }

            for (Node node : graph) {
                node.addAttribute("ui.label", node.getId());
            }

            displayNetwork(graph);
            textFieldFilePath.setText("NULL");
            textFieldNetType.setText("Random Network");
            textFieldNodeNum.setText(Integer.toString(nodeNumber));
            textFieldEdgeNum.setText(Integer.toString(edgeNumber));
            networkAnalysis(graph);
        }

    }

    class ScaleFreeNetworkGenerate implements ActionListener {

        protected String styleSheet =
                "node {" +
                        "   fill-color: green;" +
                        "}" +
                        "node.marked {" +
                        "   fill-color: red;" +
                        "}";

        @Override
        public void actionPerformed(ActionEvent e) {
            nodeNumber = -1;
            new NetworkSettings(mainClass, mainFrame);
            if (nodeNumber == -1)
                return;

            //generate scale-free network

            Graph graph = new SingleGraph("Scale Network");

            int nodeSum = nodeNumber;
            int edgeSum = edgeNumber;

            boolean visited[][] = new boolean[nodeSum + 5][nodeSum + 5];
            for (int i = 0; i < visited.length; ++i)
                Arrays.fill(visited[i], false);

            //node color

            graph.addAttribute("ui.stylesheet", styleSheet);
            graph.setStrict(false);

            //generate scale-free network

            int delta_m = (int) (nodeSum - Math.sqrt(nodeSum * nodeSum - 2 * edgeSum));
            if (delta_m < 2) delta_m = 2;
            else if (delta_m > edgeSum) delta_m = edgeSum;

            double CDF[] = new double[nodeSum + 5];

            //Initialize a simple network

            graph.addNode("0");
            graph.addNode("1");
            graph.addNode("2");
            String edgeId = "edge0to1";
            graph.addEdge(edgeId, 0, 1, false);
            edgeId = "edge1to2";
            graph.addEdge(edgeId, 1, 2, false);
            edgeId = "edge2to0";
            graph.addEdge(edgeId, 2, 0, false);


            while (graph.getNodeCount() < nodeSum) {
                int new_node = graph.getNodeCount();
                graph.addNode(Integer.toString(graph.getNodeCount()));

                int edges = Math.min(delta_m, graph.getNodeCount() - 1);

                for (int tempEdge = 0; tempEdge < edges; ++tempEdge) {
                    double rand = Math.random();
                    int tempEdgeSum = graph.getNodeCount();
                    CDF[0] = (double) graph.getNode("0").getDegree() / (tempEdgeSum * 2);

                    for (int tempNode = 1; tempNode < graph.getNodeCount(); ++tempNode) {
                        CDF[tempNode] = CDF[tempNode - 1] + (double) graph.getNode(Integer.toString(tempNode)).getDegree() / (tempEdgeSum * 2);

                        if (rand < CDF[0] || rand >= CDF[tempNode - 1] && rand < CDF[tempNode]) {
                            int destNode = 0;
                            if (rand < CDF[0])
                                destNode = 0;
                            else
                                destNode = tempNode;

                            while (new_node == destNode || visited[new_node][destNode]) {
                                destNode++;
                                if (destNode >= graph.getNodeCount() - 1)
                                    destNode = 0;
                            }
                            edgeId = "edge" + Integer.toString(new_node) + "to" + Integer.toString(destNode);
                            graph.addEdge(edgeId, new_node, destNode, false);
                            visited[new_node][destNode] = true;
                            visited[destNode][new_node] = true;
                            break;
                        }

                    }
                }
            }

            //node label

            for (Node node : graph) {
                node.addAttribute("ui.label", node.getId());
            }

            graph.getNode("0").setAttribute("ui.class", "marked");
            graph.getNode("1").setAttribute("ui.class", "marked");
            graph.getNode("2").setAttribute("ui.class", "marked");

            displayNetwork(graph);
            textFieldFilePath.setText("NULL");
            textFieldNetType.setText("Scale-Free Network");
            textFieldNodeNum.setText(Integer.toString(nodeNumber));
            textFieldEdgeNum.setText(Integer.toString(edgeNumber));
            networkAnalysis(graph);
        }

    }

    class FileFilterGML extends FileFilter {  //file filter

        public boolean accept(File file) {
            if (file.isDirectory())
                return true;
            return file.getName().endsWith(".gml");
        }

        public String getDescription() {
            return "*.gml";
        }
    }

    class OpenGMLFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser("E:\\jar\\data");

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileFilterGML());

            if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Graph graph = new MultiGraph("Complex Network");
                FileSource fs = null;

                try {
                    fs = FileSourceFactory.sourceFor(file.getAbsolutePath());
                } catch (IOException ee) {
                    ee.printStackTrace();
                }

                fs.addSink(graph);

                try {
                    fs.readAll(file.getAbsolutePath());
                } catch (IOException ee) {
                    ee.printStackTrace();
                } finally {
                    fs.removeSink(graph);
                }

                displayNetwork(graph);
                textFieldFilePath.setText(file.getAbsolutePath());
                textFieldNetType.setText("NULL");
                textFieldNodeNum.setText(Integer.toString(graph.getNodeCount()));
                textFieldEdgeNum.setText(Integer.toString(graph.getEdgeCount()));
                networkAnalysis(graph);
            }
        }

    }

    public void setNetworkInfo(int nodeNum, int edgeNum) {
        nodeNumber = nodeNum;
        edgeNumber = edgeNum;
    }

    public NetworkAnalysisAndGenerate(JFrame frame) {

        //Add Menu

        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();
        menu = new JMenu("Function");

        menuBar.add(menu);

        menuItem = new JMenuItem("Open GML File");
        menuItem.addActionListener(new OpenGMLFile());
        menu.add(menuItem);

        submenu = new JMenu("Generate Network");
        menuItem = new JMenuItem("Random Network");
        menuItem.addActionListener(new RandomNetworkGenerate());
        submenu.add(menuItem);
        menuItem = new JMenuItem("Scale-Free Network");
        menuItem.addActionListener(new ScaleFreeNetworkGenerate());
        submenu.add(menuItem);
        menu.add(submenu);

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

        mainClass = this;
        mainFrame = frame;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("NetworkAnalysisAndGenerate");
        frame.setContentPane(new NetworkAnalysisAndGenerate(frame).panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
