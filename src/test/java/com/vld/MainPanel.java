package com.vld;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Date;

public class MainPanel extends JPanel {

    private MainFrame parentFrame;
    private ConnectionDB connectionDB;

    // main table
    private final MainTableModel mainTableModel;
    private final JTable mainTable;
    private final JScrollPane mainTableScrollPane;

    private final ActivityFrame activityFrame;


    public JButton getButtonAdd() {
        return buttonAdd;
    }

    public JButton getButtonEdit() {
        return buttonEdit;
    }

    public JButton getButtonDelete() {
        return buttonDelete;
    }

    public  JButton getButtonRefreshActivity(){
        return buttonRefreshActivity;
    }

    // activity table
    private final ActivityReferenceTableModel activityReferenceTableModel;
    private final JTable activityReferenceTable;
    private final JScrollPane activityReferenceTableScrollPanel;


    private final ButtonsActionListener buttonsActionListener;

    public MainTableModel getMainTableModel() {
        return mainTableModel;
    }

    public JButton getButtonStart() {
        return buttonStart;
    }

    public JButton getButtonFinish() {
        return buttonFinish;
    }

    public JButton getButtonRefresh() {
        return buttonRefresh;
    }

    private final  CellRenderer cellRenderer;

    private final JButton buttonStart = new JButton("Start");
    private final JButton buttonFinish = new JButton("Finish");
    private final JButton buttonRefresh = new JButton("Refresh");

    private final JButton buttonAdd = new JButton("Add");
    private final JButton buttonEdit = new JButton("Edit");
    private final JButton buttonDelete = new JButton("Delete");
    private final JButton buttonRefreshActivity = new JButton("Refresh");


    private JLabel downtimeLabel = new JLabel();


    public void updateDowntimeLabel() {


        long downtimeCounter = 0;
        if(getFinishDate() != null) downtimeCounter = (StaticFunctions.getDate().getTime() - getFinishDate().getTime()) / 1000;
        downtimeLabel.setText(StaticFunctions.castToTimeString(downtimeCounter));
        //downtimeLabel.setVisible(downtimeCounter != 0);


    }

    public void setDowntimeLabelColor(Color color){
        downtimeLabel.setForeground(color);
    }


    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    private Date finishDate;

    public ButtonsActionListener getButtonsActionListener() {
        return buttonsActionListener;
    }

    public ConnectionDB getConnectionDB() {
        return connectionDB;
    }

    public MainPanel(MainFrame parentFrame){



        finishDate = StaticFunctions.getDate();

        this.parentFrame = parentFrame;
        this.connectionDB = parentFrame.getConnectionDB();
        mainTableModel = new MainTableModel();
        cellRenderer = new CellRenderer(this);


        mainTable = new JTable(mainTableModel);
        setMainTableRendering();


        mainTableScrollPane = new JScrollPane(mainTable);


        activityReferenceTableModel = new ActivityReferenceTableModel();
        activityReferenceTable = new JTable(activityReferenceTableModel);
        setActivityReferenceTableRendering();





        activityReferenceTableScrollPanel = new JScrollPane(activityReferenceTable);
        this.buttonsActionListener = new ButtonsActionListener(this);


        activityFrame = new ActivityFrame(this);

        setLayout(new GridBagLayout());
        init();


    }

    public ActivityFrame getActivityFrame(){
        return activityFrame;
    }

    private void setActivityReferenceTableRendering() {

        TableColumn name = StaticFunctions.getJTableColumn(activityReferenceTable,"name");
        TableColumn id = StaticFunctions.getJTableColumn(activityReferenceTable,"id");
        TableColumn forToday = StaticFunctions.getJTableColumn(activityReferenceTable,"forToday");
        TableColumn forWeek = StaticFunctions.getJTableColumn(activityReferenceTable,"forWeek");
        TableColumn forMonth = StaticFunctions.getJTableColumn(activityReferenceTable,"forMonth");
        TableColumn forAllPeriod = StaticFunctions.getJTableColumn(activityReferenceTable,"forAllPeriod");

        id.setMaxWidth(30);

        forToday.setMaxWidth(40);
        forWeek.setMaxWidth(45);
        forMonth.setMaxWidth(45);
        forAllPeriod.setMaxWidth(50);


        name.setCellRenderer(cellRenderer);
        id.setCellRenderer(cellRenderer);
        forToday.setCellRenderer(cellRenderer);
        forWeek.setCellRenderer(cellRenderer);
        forMonth.setCellRenderer(cellRenderer);
        forAllPeriod.setCellRenderer(cellRenderer);

        //columns' titles
        name.setHeaderValue("Name");
        id.setHeaderValue("ID");
        forToday.setHeaderValue("Today");
        forWeek.setHeaderValue("Week");
        forMonth.setHeaderValue("Month");
        forAllPeriod.setHeaderValue("Total");



    }

    private void setMainTableRendering() {
        TableColumn  activityColumn                 = StaticFunctions.getJTableColumn(mainTable,"activity");
        TableColumn  usage_in_a_rowColumn           = StaticFunctions.getJTableColumn(mainTable,"usage_in_a_row");
        TableColumn  start_dateColumn               = StaticFunctions.getJTableColumn(mainTable,"start_date");
        TableColumn  finish_dateColumn              = StaticFunctions.getJTableColumn(mainTable,"finish_date");
        TableColumn  useful_time_durationColumn     = StaticFunctions.getJTableColumn(mainTable,"useful_time_duration");
        TableColumn  useful_time_totalColumn        = StaticFunctions.getJTableColumn(mainTable,"useful_time_total");






        activityColumn.setCellRenderer(cellRenderer);
        usage_in_a_rowColumn.setCellRenderer(cellRenderer);
        start_dateColumn.setCellRenderer(cellRenderer);
        finish_dateColumn.setCellRenderer(cellRenderer);
        useful_time_durationColumn.setCellRenderer(cellRenderer);
        useful_time_totalColumn.setCellRenderer(cellRenderer);


        //columns' width
        usage_in_a_rowColumn.setMaxWidth(20);


        start_dateColumn.setMaxWidth(60);
        finish_dateColumn.setMaxWidth(60);
        useful_time_durationColumn.setMaxWidth(60);
        useful_time_totalColumn.setMaxWidth(60);

        //columns' titles
        usage_in_a_rowColumn.setHeaderValue("№");
        start_dateColumn.setHeaderValue("Start");
        finish_dateColumn.setHeaderValue("Finish");
        useful_time_durationColumn.setHeaderValue("Time");
        useful_time_totalColumn.setHeaderValue("Total");


    }

    public MainFrame getParentFrame() {
        return parentFrame;
    }

    public ActivityReferenceTableModel getActivityReferenceTableModel() {
        return activityReferenceTableModel;
    }

    public JTable getMainTable() {
        return mainTable;
    }

    public JTable getActivityReferenceTable() {
        return activityReferenceTable;
    }

    private void init() {

        buttonsActionListener.closeUnclosedRecords();
        buttonsActionListener.refreshTableDate();
        buttonsActionListener.refreshActivityDateTable();

        add(mainTableScrollPane,getNewGridBagConstraints(0,0,10,1,1,1));
        add(activityReferenceTableScrollPanel,getNewGridBagConstraints(10,0,5,1,1,1));
        initButtons();


    }

    private void initButtons(){


        //buttonStart.setBackground(Color.GREEN);

        add(buttonStart,getNewGridBagConstraints(0,1,1,1,0,0));
        add(buttonFinish,getNewGridBagConstraints(1,1,1,1,0,0));
        add(buttonRefresh,getNewGridBagConstraints(2,1,1,1,0,0));

        add(downtimeLabel,getNewGridBagConstraints(3,1,1,1,0,0));
        downtimeLabel.setForeground(Color.RED);
        Font font =  new Font("Verdana", Font.BOLD, 20);
        downtimeLabel.setFont(font);

        add(buttonAdd ,getNewGridBagConstraints(4,1,1,1,0,0));
        add(buttonEdit  ,getNewGridBagConstraints(5,1,1,1,0,0));
        add(buttonDelete  ,getNewGridBagConstraints(6,1,1,1,0,0));
        add(buttonRefreshActivity,getNewGridBagConstraints(7,1,1,1,0,0));


        buttonRefresh.addActionListener(buttonsActionListener);
        buttonStart.addActionListener(buttonsActionListener);
        buttonFinish.addActionListener(buttonsActionListener);

        buttonAdd.addActionListener(buttonsActionListener);
        buttonEdit.addActionListener(buttonsActionListener);
        buttonDelete.addActionListener(buttonsActionListener);
        buttonRefreshActivity.addActionListener(buttonsActionListener);


    }

    private GridBagConstraints getNewGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {

        final GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.gridx = gridx;
        gbConstraints.gridy = gridy;
        gbConstraints.gridwidth = gridwidth;
        gbConstraints.gridheight = gridheight;
        gbConstraints.weightx = weightx;
        gbConstraints.weighty = weighty;
        gbConstraints.anchor = GridBagConstraints.NORTH;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.insets = new Insets(2, 2, 2, 2); // Отступы (рассотяние между элементами
        gbConstraints.ipadx = 0; //Насколько будет увеличин минимальній размер компонента.
        gbConstraints.ipady = 0;

        return gbConstraints;
    }

}
