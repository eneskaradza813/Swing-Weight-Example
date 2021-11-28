package weightmonitor;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.*;
import javax.swing.*;
import com.toedter.calendar.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.*;

public class WeightMonitor extends JFrame{

    JMenuBar mainMenuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem newMenuItem = new JMenuItem("New Weight File");
    JMenuItem openMenuItem = new JMenuItem("Open Weight File");
    JMenuItem saveMenuItem = new JMenuItem("Save Weight File");
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    JTabbedPane weightTabbedPane = new JTabbedPane();
    JPanel editorPanel = new JPanel();
    WeightPlotPanel plotPanel = new WeightPlotPanel();
    JLabel fileLabel = new JLabel();
    JTextArea fileTextArea = new JTextArea();
    JCalendar weightCalendar = new JCalendar();
    JTextField weightTextField = new JTextField();
    JButton addButton = new JButton();
    JLabel weightLAbel = new JLabel();
    JLabel weightListLabel = new JLabel();
    JScrollPane weightScrollPane = new JScrollPane();
    JList weightsList = new JList();
    static DefaultListModel weightsListModel = new DefaultListModel();
    JButton deleteButton = new JButton();
    String lastFile = "";
    
    public static void main(String[] args) {

        new WeightMonitor().show();
    }

    public WeightMonitor() throws HeadlessException {
        setTitle("Weight Monitor");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitForm(e);
            }
      });
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints;
        setJMenuBar(mainMenuBar);
        mainMenuBar.add(fileMenu);
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMenuItemActionPerformed(e);
            }
        });
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMenuItemActionPerformed(e);
            }
        });
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMenuItemActionPerformed(e);
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitMenuItemActionPerformed(e);
            }
        });
        weightTabbedPane.setPreferredSize(new Dimension(500, 400));
        weightTabbedPane.addTab("Weight Editor", editorPanel);
        weightTabbedPane.addTab("Weight Plot", plotPanel);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        getContentPane().add(weightTabbedPane, gridConstraints);
        editorPanel.setBackground(new Color(192, 192, 255));
        editorPanel.setLayout(new GridBagLayout());
        plotPanel.setBackground(new Color(255, 192, 192));
        fileLabel.setText("Current Weight File");
        fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        editorPanel.add(fileLabel, gridConstraints);
        
        fileTextArea.setPreferredSize(new Dimension(220, 50));
        fileTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        fileTextArea.setEditable(false);
        fileTextArea.setBackground(Color.WHITE);
        fileTextArea.setLineWrap(true);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(0, 10, 10, 0);
        editorPanel.add(fileTextArea, gridConstraints);
        
        weightCalendar.setPreferredSize(new Dimension(220, 200));
        weightCalendar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 2;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(5, 10, 0, 5);
        editorPanel.add(weightCalendar, gridConstraints);
        weightCalendar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                weightCalendarPropertyChange(evt);
            }
        });
        weightLAbel.setText("Weight(lb)");
        weightLAbel.setFont(new Font("Arial", Font.BOLD, 14));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        editorPanel.add(weightLAbel, gridConstraints);
        
        weightTextField.setPreferredSize(new Dimension(100, 25));
        weightTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 5, 0, 0);
        editorPanel.add(weightTextField, gridConstraints);
        weightTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                weightTextFieldActionPerformed(e);
            }
        });
        addButton.setText("Add Weight to File");
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 4;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        editorPanel.add(addButton, gridConstraints);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });
        weightListLabel.setText("Date Weight(lb)");
        weightListLabel.setFont(new Font("Courier New", Font.PLAIN, 16));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        editorPanel.add(weightListLabel, gridConstraints);
        weightScrollPane.setPreferredSize(new Dimension(250, 300));
        weightsList.setFont(new Font("Courier New", Font.PLAIN, 16));
        weightScrollPane.setViewportView(weightsList);
        weightsList.setModel(weightsListModel);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 1;
        gridConstraints.gridheight = 3;
        gridConstraints.insets = new Insets(0, 5, 0, 0);
        gridConstraints.anchor = GridBagConstraints.NORTHWEST;
        editorPanel.add(weightScrollPane, gridConstraints);
        weightsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                weightListValueChanged(e);
            }
        });
        deleteButton.setText("Delete Selection");
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        gridConstraints.anchor = gridConstraints.CENTER;
        editorPanel.add(deleteButton, gridConstraints);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });
        
       initialize();
       pack();
    }
    private void saveWeightFile(String fn){
        try{
            PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter(fn)));
            fileTextArea.setText(fn);
            for(int i = 0; i < weightsListModel.getSize(); i++){
                outputFile.println(weightsListModel.getElementAt(i).toString());
            }
            outputFile.flush();
            outputFile.close();
            lastFile = fn;
        }catch(Exception ex){
            JOptionPane.showConfirmDialog(null, "An error ocurred saving the weight file", "File Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            
        }
    }
    static public String getDate(String s){
        s = s.substring(0, 10);
        return (s);
    }
    static public String getWeight(String s){
        s = s.substring(10);
        return (s.trim());
    }
    private int findDate(String d){
        if(!weightsListModel.isEmpty()){
            for(int i = 0; i < weightsListModel.getSize(); i++){
                boolean equals = getDate(weightsListModel.getElementAt(i).toString()).equals(d);
                return (i);
            }
        }
        return (-1);
    }
    private String formLine(String d, String w){
        int lineLenght = 19;
        String s = d;
        w = new DecimalFormat("0.0").format(Double.valueOf(w).doubleValue());
        for(int i = 0; i < lineLenght - 10 - w.length();i++)s+="";
        s += w;
        return (s);
    }
    private String dateToString(Date dd){
        String yString = String.valueOf(dd.getYear() + 1990);
        int m = dd.getMonth() + 1;
        String mString = new DecimalFormat("00").format(m);
        int d = dd.getDate();
        String dString = new DecimalFormat("00").format(d);
        return (yString + "" + mString + "" + dString);
    }
    static public Date stringToDate(String s){
        int y = Integer.valueOf(s.substring(0, 4)).intValue()-1900;
        int m = Integer.valueOf(s.substring(5, 7)).intValue() - 1;
        int d = Integer.valueOf(s.substring(8, 10)).intValue();
        return (new Date(y, m, d));
    }
    private void initialize(){
        weightTabbedPane.setSelectedIndex(0);
        weightCalendar.setDate(new Date());
        weightsListModel.clear();
        fileTextArea.setText("New File");
        weightTextField.setText("");
        weightTextField.requestFocus();
    }
    private boolean validateDecimalNumber(JTextField tf){
        String s = tf.getText().trim();
        boolean hasDecimal = false;
        boolean valid = true;
        if(s.length() == 0){
            valid = false;
        }else{
            for(int i = 0; i < s.length(); i++){
                char c= s.charAt(i);
                if(c >= '0' & c <= '9'){
                    continue;
                }
                else if(c == '.'&&!hasDecimal){
                    hasDecimal = true;
                }else{
                    valid = false;
                }
            }
        }
        tf.setText(s);
        if(!valid){
            tf.requestFocus();
        }
        return valid;
    }
    private void openWeightFile(String fn){
        try{
            initialize();
            BufferedReader inputFile = new BufferedReader(new FileReader(fn));
            fileTextArea.setText(fn);
            do{
                String s = inputFile.readLine();
                weightsListModel.addElement(s);
                
            }while(inputFile.ready());
            inputFile.close();
            lastFile = fn;
            int i = findDate(dateToString(weightCalendar.getDate()));
            if(i != -1){
                weightsList.setSelectedIndex(i);
            }
        }catch(Exception ex){
            JOptionPane.showConfirmDialog(null, "An error occured opening the weight file", "File Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            lastFile = "";
        }
    }
    void deleteButtonActionPerformed(ActionEvent e){
        weightsListModel.removeElementAt(weightsList.getSelectedIndex());
    }
    void newMenuItemActionPerformed(ActionEvent e){
        if(JOptionPane.showConfirmDialog(null, "Are you sure you want to start new weight file?", "New File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
            initialize();
        }
    }
    void openMenuItemActionPerformed(ActionEvent e){
        if(JOptionPane.showConfirmDialog(null, "Are you sure you want to open a weight file?", "New Weight File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
            JFileChooser openChooser = new JFileChooser();
            openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            openChooser.setDialogTitle("Open Weight File");
            openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Weight Files", "wgt"));
            if(openChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                openWeightFile(openChooser.getSelectedFile().toString());
            }
        }
    }
    void saveMenuItemActionPerformed(ActionEvent e){
        if(weightsListModel.isEmpty()){
            JOptionPane.showConfirmDialog(null, "You need to enter at least one weight value", "File Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        saveChooser.setDialogTitle("Save Weight File");
        saveChooser.addChoosableFileFilter(new FileNameExtensionFilter("Weight Files", "wgt"));
        if(saveChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            if(saveChooser.getSelectedFile().exists()){
                int response;
                response = JOptionPane.showConfirmDialog(null, saveChooser.getSelectedFile().toString() + " exists. Overwrite?", "Confirm Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.NO_OPTION){
                    return;
                }
            }
            String fileName = saveChooser.getSelectedFile().toString();
            int dotlocation = fileName.indexOf(".");
            if(dotlocation == -1){
                fileName += ".wgt";
            }else{
                fileName = fileName.substring(0, dotlocation) + ".wgt";
            }
            saveWeightFile(fileName);
        }
    }
    void exitMenuItemActionPerformed(ActionEvent e){
        exitForm(null);
    }
    void weightCalendarPropertyChange(PropertyChangeEvent evt){
        int i;
        i = findDate(dateToString(weightCalendar.getDate()));
        if(i != -1){
            weightsList.setSelectedIndex(i);
            weightTextField.setText(getWeight(weightsList.getSelectedValue().toString()));
            
        }else{
            weightsList.clearSelection();
            weightTextField.setText(" ");
            
        }
        weightTextField.requestFocus();
    }
    void weightTextFieldActionPerformed(ActionEvent e){
        addButton.doClick();
    }
    void addButtonActionPerformed(ActionEvent e){
        int i;
        if(!validateDecimalNumber(weightTextField)){
            JOptionPane.showConfirmDialog(null, "Empty or invalid weight entry.\nPleas correct", "Weight Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
       i = findDate(dateToString(weightCalendar.getDate()));
       if(i!=-1){
           weightsListModel.removeElementAt(i);
           String item = formLine(dateToString(weightCalendar.getDate()), weightTextField.getText());
           if(weightsListModel.isEmpty() || item.compareTo(weightsListModel.getElementAt(weightsListModel.size()-1).toString())>0){
               weightsListModel.addElement(item);
               weightsList.setSelectedIndex(weightsListModel.size() - 1);
           }else
           {
               for(i = weightsListModel.size() - 1; i >= 0; i--){
                   if((weightsListModel.getElementAt(i).toString().compareTo(item))<0){
                       break;
                   }
               }
               weightsListModel.insertElementAt(item, i + 1);
               weightsList.setSelectedIndex(i + 1);
           }
       }
    }
    void weightListValueChanged(ListSelectionListener e){
        if(weightsList.getSelectedIndex()>=0){
            weightCalendar.setDate(stringToDate(weightsList.getSelectedValue().toString()));
            weightTextField.setText(getWeight(weightsList.getSelectedValue().toString()));
            weightTextField.requestFocus();
        }
    }
    void exitForm(WindowEvent e){
        try{
            PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("weight.ini")));
            outputFile.println(lastFile);
            outputFile.close();
        }catch(Exception ex){
            
        }
        if(!lastFile.equals(""))
            saveWeightFile(lastFile);
        System.exit(0);
    }
    class WeightPlotPanel extends JPanel{

        Rectangle2D.Double plotFrame;
        
        @Override
       // @SuppressWarnings("empty-statement")
        protected void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D)g;
            super.paintComponent(g2D);
            
            plotFrame = new Rectangle2D.Double(50, 40, 420, 280);
            g2D.setPaint(Color.WHITE);
            g2D.fill(plotFrame);
            g2D.setStroke(new BasicStroke(2));
            g2D.setPaint(Color.BLACK);
            g2D.draw(plotFrame);
            
            int lSize = WeightMonitor.weightsListModel.getSize();
            double[] d = new double[lSize];
            double[] w = new double[lSize];
            double wMin, wMax;
            String s;
            int intervals;
            double gridSpacing, wLegend;
            
            if(lSize < 2)
                return;
            g2D.setStroke(new BasicStroke(2));
            g2D.setPaint(Color.BLUE);
            wMin = 1000.0;
            wMax = 0.0;
            long t1 = WeightMonitor.stringToDate(WeightMonitor.getDate(WeightMonitor.weightsListModel.getElementAt(0).toString())).getTime();
            for(int i = 0; i < lSize; i++){
                s = WeightMonitor.weightsListModel.getElementAt(i).toString();
                long t2 = WeightMonitor.stringToDate(WeightMonitor.getDate(s)).getTime();
               // d[i] = (double)((t2 - t1) / (24 3600 1000));
               w[i] = Double.valueOf(WeightMonitor.getWeight(s)).doubleValue();
               wMin = Math.max(w[i], wMax);
               
            }
            if(wMin == wMax)
                wMin = wMax -1;
            wMax = (double)((int)(wMax + 0.5));
            wMin = (double)((int)(wMin - 0.5));
            if(wMax - wMin <= 5.0)
                gridSpacing = 1.0;
            else if(wMax - wMin <= 10.0)
                gridSpacing = 2.0;
            else if(wMax - wMin <= 25.0)
                gridSpacing = 5.0;
            else if(wMax - wMin <= 50.0)
                gridSpacing = 10.0;
            else
                gridSpacing = 20.0;
            if(wMax % (int)gridSpacing != 0)
                wMax = gridSpacing * (int)(wMax / gridSpacing) + gridSpacing;
            if(wMin % (int)gridSpacing != 0)
                wMin = gridSpacing * (int)(wMin / gridSpacing);
            intervals = (int)((wMax - wMin)/gridSpacing);
            for(int i = 1; i < lSize; i++){
                Line2D.Double weightLine = new Line2D.Double(dToX(d[i -1], d[lSize -1]), wToY(w[i -1], wMin, wMax), dToX(d[i], d[lSize - 1]), wToY(w[i], wMin, wMax));
                g2D.draw(weightLine);
            }
            g2D.setStroke(new BasicStroke(1));
            g2D.setPaint(Color.BLACK);
            wLegend = wMin;
            for(int i = 0; i < intervals; i++){
                if(i > 0 && i < intervals){
                    Line2D.Double gridLine = new Line2D.Double(plotFrame.getX(), wToY(wLegend, wMin, wMax), plotFrame.getX() + plotFrame.getWidth(), wToY(wLegend, wMin, wMax));
                    g2D.draw(gridLine);
                    
                }
                wLegend += gridSpacing;
            }
            g2D.dispose();
        }
        
        private int dToX(double d, double dmax){
            return ((int)(d * (plotFrame.getWidth() -1) / dmax + plotFrame.getX()));
        }
        private int wToY(double w, double wmin, double wmax){
            return ((int)((wmax -w)*(plotFrame.getHeight() -1)/(wmax - wmin) + plotFrame.getY()));
        }
    }
}
