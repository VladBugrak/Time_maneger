package com.vld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame  extends JFrame {

    private MainPanel mainPanel;
    private ConnectionDB connectionDB;
    private ButtonsActionListener buttonsActionListener;

    private JCheckBox showBreaksCheckBox;
    private JRadioButton forAllPeriodButton;
    private JRadioButton onlyForTodayButton;

    private JMenuItem exitMenuItem;


    public ConnectionDB getConnectionDB() {
        return connectionDB;
    }

    MainFrame(){


        connectionDB = new ConnectionDB();

        connectionDB.checkConnection();

        Dimension size = new Dimension(1200,800);

        this.setSize(size);
        this.setMinimumSize(size);
        this.setTitle("Time manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.mainPanel = new MainPanel(this);
        this.buttonsActionListener = this.mainPanel.getButtonsActionListener();
        this.add(mainPanel,BorderLayout.CENTER);


        createMenuBar();




    }


    public JCheckBox getShowBreaksCheckBox() {
        return showBreaksCheckBox;
    }

    public JRadioButton getForAllPeriodButton() {
        return forAllPeriodButton;
    }

    public JRadioButton getOnlyForTodayButton() {
        return onlyForTodayButton;
    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);


        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);

        JMenu dataSelectionMenu = new JMenu("data selection");
        onlyForTodayButton = new JRadioButton("Only for today");
        onlyForTodayButton.setSelected(true);
        mainPanel.getButtonsActionListener().setOnlyForTodaySelection(onlyForTodayButton.isSelected());
        forAllPeriodButton = new JRadioButton("For all period");
        ButtonGroup dataSelectionMenu_ButtonGroup = new ButtonGroup();
        dataSelectionMenu_ButtonGroup.add(onlyForTodayButton);
        dataSelectionMenu_ButtonGroup.add(forAllPeriodButton);
        dataSelectionMenu.add(onlyForTodayButton);
        dataSelectionMenu.add(forAllPeriodButton);
        settingsMenu.add(dataSelectionMenu);
        showBreaksCheckBox = new JCheckBox("Show breaks");
        showBreaksCheckBox.setSelected(false);
        settingsMenu.add(showBreaksCheckBox);




        showBreaksCheckBox.addActionListener(buttonsActionListener);
        onlyForTodayButton.addActionListener(buttonsActionListener);
        forAllPeriodButton.addActionListener(buttonsActionListener);





        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(buttonsActionListener);
        fileMenu.add(exitMenuItem);
    }

    JMenuItem getExitMenuItem(){
        return  exitMenuItem;
    }

}
