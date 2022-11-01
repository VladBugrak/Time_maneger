package com.vld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;

public class ButtonsActionListener implements ActionListener, Runnable {

    private final MainTableModel mainTableModel;
    private final ActivityReferenceTableModel activityReferenceTableModel;
    private final ConnectionDB connectionDB;
    private final MainPanel mainPanel;
    private boolean recordIsRunning;
    private MainFrame parentFrame;
    private Date dayStart;
    private boolean onlyForTodaySelection;
    private JTable activityReferenceTable;
    private int activity_id;
    private int runCounter = 0;
    private boolean showBreaks;

    public boolean isRecordIsRunning() {
        return recordIsRunning;
    }

    public void setOnlyForTodaySelection(boolean onlyForTodaySelection) {
        this.onlyForTodaySelection = onlyForTodaySelection;
        refresh();
    }

    public ButtonsActionListener(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.onlyForTodaySelection = false;
        this.parentFrame = mainPanel.getParentFrame();
        this.showBreaks = false;//this.parentFrame.getShowBreaksCheckBox().isSelected();
        this.mainTableModel = mainPanel.getMainTableModel();
        activityReferenceTableModel = mainPanel.getActivityReferenceTableModel();
        this.connectionDB = mainPanel.getConnectionDB();
        this.recordIsRunning = false;
        Thread thread = new Thread(this);
        thread.start();
        dayStart = StaticFunctions.getStartOfDayDate(StaticFunctions.getDate());
        activityReferenceTable = mainPanel.getActivityReferenceTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == mainPanel.getButtonStart())
            start();
        if (e.getSource() == mainPanel.getButtonRefresh())
            refresh();
        if (e.getSource() == mainPanel.getButtonFinish())
            finish();
        if (e.getSource() == mainPanel.getButtonAdd())
            addActivity();
        if (e.getSource() == mainPanel.getButtonEdit())
            editActivity();
        if (e.getSource() == mainPanel.getButtonDelete())
            deleteActivity();
        if (e.getSource() == mainPanel.getActivityFrame().getCancelButton())
            mainPanel.getActivityFrame().setVisible(false);
        if (e.getSource() == mainPanel.getActivityFrame().getSaveButton())
            saveActivity();
        if (e.getSource() == mainPanel.getButtonRefreshActivity())
            refreshActivityDateTable();
        if( e.getSource() ==  parentFrame.getShowBreaksCheckBox()){
            showBreaks = parentFrame.getShowBreaksCheckBox().isSelected();
            refreshTableDate();
        }
        if( e.getSource() ==  parentFrame.getForAllPeriodButton()){
            setOnlyForTodaySelection(parentFrame.getOnlyForTodayButton().isSelected());
        }
        if( e.getSource() ==  parentFrame.getOnlyForTodayButton()){
            setOnlyForTodaySelection(parentFrame.getOnlyForTodayButton().isSelected());
        }
        if( e.getSource() ==  parentFrame.getExitMenuItem()){
            System.exit(0);
        }



    }


    boolean isNewActivityNameUnique(int id,String name){

        boolean isUnique = true;

        try {
            String query = "select * from activity where name = ? and id != ? ";

            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,id);
            ResultSet rs = preparedStatement.executeQuery();

            isUnique = !rs.next();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(!isUnique) StaticFunctions.showMessage("The name \"" + name + "\" is not unique!");

        return  isUnique;

    }

    private void saveActivity(){
        int activityID = mainPanel.getActivityFrame().getActivityID();
        String activityName = mainPanel.getActivityFrame().getActivityName();

        if(!isNewActivityNameUnique(activityID,activityName)) return;



        if(activityID == -1){ //create new activity
            String query = "insert into activity (name) value (?);";


            try {
                PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
                preparedStatement.setString(1,activityName);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        } else { // update existing activity
            String query = "update activity set name = ? where id = ?;";


            try {
                PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
                preparedStatement.setString(1,activityName);
                preparedStatement.setInt(2,activityID);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        refreshActivityDateTable();
        refreshTableDate();

        mainPanel.getActivityFrame().setVisible(false);





    }

    private void addActivity() {
       ActivityFrame activityFrame = mainPanel.getActivityFrame();
       activityFrame.setActivityID(-1);
       activityFrame.setActivityName("");
       activityFrame.setVisible(true);

    }

    private void editActivity() {

        getActivity_id();
        if(activity_id == -1){
            StaticFunctions.showMessage("You haven't specified a kind of activity!");
            return;
        }

        ActivityFrame activityFrame = mainPanel.getActivityFrame();
        activityFrame.setActivityID(activity_id);
        activityFrame.setActivityName(getActivityName());
        activityFrame.setVisible(true);
    }

    private void deleteActivity() {

        getActivity_id();
        if(activity_id == -1){
            StaticFunctions.showMessage("You haven't specified a kind of activity!");
            return;
        }
        int numberOfActivityUsage = getNumberOfActivityUsage(activity_id);

        int chosenOption = JOptionPane.showConfirmDialog(
                        null,
                "There are "
                        + numberOfActivityUsage
                        + " records that uses this activity."
                        + "\n\n Deletion this activity means deletion all of this records. "
                        + "\n\n Would you like to continue?",
                "",
                JOptionPane.YES_NO_OPTION);



        if(chosenOption == JOptionPane.NO_OPTION) return;

        String query = "delete from time_usage where activity_id = ?";

        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,activity_id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

         query = "delete from activity where id = ?;";


        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,activity_id);
            preparedStatement.execute();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        refreshActivityDateTable();
        refreshTableDate();


    }

    private int getNumberOfActivityUsage(int activity_id){

        String query = "select count(*) as numberOfUses from time_usage where activity_id = ?";

        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,activity_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) return resultSet.getInt("numberOfUses");
            else return 0;



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;


    }

    private void refresh() {
       // mainTableModel.refreshTableDate();
        refreshTableDate();
    }

    private void finish() {

        if(recordIsRunning){
            mainPanel.setFinishDate(StaticFunctions.getDate());
            mainPanel.setDowntimeLabelColor(Color.RED);
            mainPanel.updateDowntimeLabel();

            recordIsRunning = false;
            updateCurrentRecord(true);
            refreshActivityDateTable();
        }



    }

    private void start() {

        if(recordIsRunning) {
            return;
        }

        getActivity_id();
        if(activity_id == -1){
            StaticFunctions.showMessage("You haven't specified a kind of activity!");
            return;
        }

        //refreshActivityDateTable();


        mainPanel.setFinishDate(StaticFunctions.getDate());
        mainPanel.setDowntimeLabelColor(Color.BLUE);
        mainPanel.updateDowntimeLabel();


        recordIsRunning = true;
        updateCurrentRecord(true);

        String query = """
                INSERT INTO `time_manager`.`time_usage`
                (
                `activity_id`,
                `usage_in_a_row`,
                `start_date`,
                `finish_date`
                )
                VALUES
                (
                ?,
                ?,
                ?,
                ?
                );
                             
                """;





        int usage_in_a_row = getLastRow();


        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        try {


            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, activity_id);
            preparedStatement.setInt(2, usage_in_a_row);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //mainTableModel.refreshTableDate();
        refreshTableDate();


    }

    private void getActivity_id() {

        int firstSelectedRowIndex = activityReferenceTable.getSelectedRow();
        if(firstSelectedRowIndex != -1) {
            int id_ColumnIndex = activityReferenceTable.getColumnModel().getColumnIndex("ID");
            String str_id = activityReferenceTable.getValueAt(firstSelectedRowIndex, id_ColumnIndex).toString();
            activity_id = Integer.parseInt(str_id);
        } else
        activity_id = -1;
    }

    private String getActivityName() {

        int firstSelectedRowIndex = activityReferenceTable.getSelectedRow();
        if(firstSelectedRowIndex != -1) {
            int id_ColumnIndex = activityReferenceTable.getColumnModel().getColumnIndex("Name");
            return   activityReferenceTable.getValueAt(firstSelectedRowIndex, id_ColumnIndex).toString();

        } else
            return "";
    }

    public int getLastRow() {

        String query = """
                                
                                
                SELECT
                                                        `time_usage`.`id`,
                                                        `time_usage`.`activity_id`,
                                                        `time_usage`.`usage_in_a_row`,
                                                        `time_usage`.`start_date`,
                                                        `time_usage`.`finish_date`,
                                                        `time_usage`.`useful_time_duration`,
                                                        `time_usage`.`useless_time_duration`,
                                                        `time_usage`.`useful_time_total`,
                                                        `time_usage`.`useless_time_total`
                                                    FROM
                                                        `time_manager`.`time_usage`
                                                            INNER JOIN
                                                        (SELECT
                                                            MAX(`time_usage`.`id`) AS id
                                                        FROM
                                                            `time_manager`.`time_usage`
                                                            Where 1=1
                                                            and `time_usage`.`start_date` >= ?
                                                            and `time_usage`.`start_date` <= ?
                                                            ) AS `lastRow` ON `lastRow`.`id` = `time_usage`.`id`;
                """;

        java.util.Date date = StaticFunctions.getDate();
        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setTimestamp(1, StaticFunctions.getStartOfDay(date));
            preparedStatement.setTimestamp(2, StaticFunctions.getEndOfDay(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int i = resultSet.getInt("usage_in_a_row");
                i++;
                return i;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return 1; //1 by default


    }

    private void autoUpdateCurrentRecord() {


        updateCurrentRecord(false);


    }

    private void updateCurrentRecord(boolean isFinished) {
        String query = """
                UPDATE `time_manager`.`time_usage` t1
                                                inner join (select max(`time_usage`.`id`) as `id` from `time_manager`.`time_usage`)  t2
                                                on 1=1
                                                and t1.id = t2.id
                                                and t1.isFinished = 0
                                                inner join (
                                                SELECT
                                                                                      activity_id,
                                                                                      sum(`time_usage`.`useful_time_duration`) usefulTimeTotal,
                                                                                          SEC_TO_TIME(sum(`time_usage`.`useful_time_duration`)) usefulTimeTotal_time
                                                                                      FROM `time_manager`.`time_usage`
                                                                                     
                                                                                      where 1= 1
                                                                                      and activity_id = ?
                                                                                      and start_date>= ?
                                                                                      and start_date<= ?
                                                                                     
                                                                                      group by activity_id
                                                ) as totals
                                                on totals.activity_id =  t1.activity_id
                                                             
                                                               
                                                SET
                                                               
                                                `t1`.`finish_date` = ?,
                                                `t1`.`useful_time_duration` = time_to_sec(timediff(?,`t1`.`start_date`)),
                                                `t1`.`isFinished`  = ?,
                                                t1.useful_time_total = totals.usefulTimeTotal
                                
                                
                                
                """;

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        try {
            java.util.Date date = StaticFunctions.getDate();

            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, activity_id);
            preparedStatement.setTimestamp(2, StaticFunctions.getStartOfDay(date));
            preparedStatement.setTimestamp(3, StaticFunctions.getEndOfDay(date));
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setTimestamp(5, timestamp);
            preparedStatement.setInt(6, isFinished ? 1 : 0);

            preparedStatement.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //mainTableModel.refreshTableDate();
        refreshTableDate();
    }

    @Override
    public void run() {
        while (true) {

            StaticFunctions.sleep(1);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            // System.out.println(recordIsRunning);

             runCounter++;


            if (recordIsRunning) {
                if(runCounter >= 30){
                    runCounter = 0;
                    refreshActivityDateTable();
                }


                autoUpdateCurrentRecord();
                Date  currentDayStart = StaticFunctions.getStartOfDayDate(StaticFunctions.getDate());//StaticFunctions.getStartOfDay(StaticFunctions.getDate());
                if(dayStart.getTime() /1000 != currentDayStart.getTime() /1000){
                    finish();
                    dayStart = currentDayStart;
                    start();
                }
            }



            mainPanel.updateDowntimeLabel();


        }
    }

    public void refreshActivityDateTable(){

        activityReferenceTableModel.clearArrayList();

        String query = """
                                
                 select
                id,
                name,
                concat(truncate(forToday / 3600,0),":", right(concat("00",round(forToday % 3600 / 60,0)),2)) as forToday,
                concat(truncate(forWeek / 3600,0),":", right(concat("00",round(forWeek % 3600 / 60,0)),2)) as forWeek,
                concat(truncate(forMonth / 3600,0),":", right(concat("00",round(forMonth % 3600 / 60,0)),2)) as forMonth,
                concat(truncate(forAllPeriod / 3600,0),":", right(concat("00",round(forAllPeriod % 3600 / 60,0)),2)) as forAllPeriod
                from
                               
                               
                (Select
                a.id,
                a.name,
                               
                sum(case
                when(dayofyear(CURRENT_TIMESTAMP()) = dayofyear(tu.start_date) and year(CURRENT_TIMESTAMP()) = year(tu.start_date) )
                then tu.useful_time_duration else 0 end) as forToday,
                sum(case
                when(WEEKOFYEAR(CURRENT_TIMESTAMP()) = WEEKOFYEAR(tu.start_date) and year(CURRENT_TIMESTAMP()) = year(tu.start_date)  )
                then tu.useful_time_duration else 0 end) as forWeek,
                               
                sum(case
                when(month(CURRENT_TIMESTAMP()) = month(tu.start_date) and year(CURRENT_TIMESTAMP()) = year(tu.start_date)  )
                then tu.useful_time_duration else 0 end)  as forMonth,
                               
                sum(tu.useful_time_duration) as forAllPeriod
                               
                from activity as a
                               
                left join
                               
                time_usage as tu
                on a.id = tu.activity_id
                               
                group by
                a.id,
                a.name ) as subquery
                               
                order by
                forToday desc,
                forWeek desc,
                forMonth desc,
                forAllPeriod desc
                """;
        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            int rowCounter = 0;
            int activity_idRowNumber = -1;

            while (resultSet.next()){
                String id           = resultSet.getString("id");
                String name         = resultSet.getString("name");
                String forToday     = resultSet.getString("forToday");
                String forWeek      = resultSet.getString("forWeek");
                String forMonth     = resultSet.getString("forMonth");
                String forAllPeriod = resultSet.getString("forAllPeriod");

                String[] row = {id,name,forToday,forWeek,forMonth,forAllPeriod};
                activityReferenceTableModel.add(row);

                if(Integer.parseInt(id) == activity_id) activity_idRowNumber = rowCounter;


                rowCounter++;

            }
            activityReferenceTableModel.fireTableDataChanged();
            if(activity_idRowNumber >= 0)
                activityReferenceTable.setRowSelectionInterval(activity_idRowNumber,activity_idRowNumber);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }

    public void refreshTableDate(){


         mainTableModel.clearArrayList();


        String  query =  """
                SELECT
                    `time_usage`.`id`,
                    `time_usage`.`activity_id`,
                    `activity`.`name` AS activity_name,
                    `time_usage`.`usage_in_a_row`,
                    DATE_FORMAT(`time_usage`.`start_date`, '%T') AS `start_date`,
                    DATE_FORMAT(`time_usage`.`finish_date`, '%T') AS `finish_date`,
                    TIMEDIFF(`time_usage`.`finish_date`,
                            `time_usage`.`start_date`) AS useful_time_duration,
                    `time_usage`.`useless_time_duration`,
                    SEC_TO_TIME(`time_usage`.`useful_time_total`) as useful_time_total,
                    `time_usage`.`useless_time_total`
                FROM
                    `time_manager`.`time_usage`
                        LEFT JOIN
                    `time_manager`.`activity` ON `time_usage`.`activity_id` = `activity`.`id`
                Where 1=1
                    and if(?,`time_usage`.`start_date` >= ?, 1=1)
                    and if(?,`time_usage`.`start_date` <= ?, 1=1)
                    
                    
                ORDER BY `time_usage`.`start_date`
                ;
                """;


        try {
            PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(query);
            Date date = StaticFunctions.getDate();
            preparedStatement.setBoolean(1,onlyForTodaySelection);
            preparedStatement.setTimestamp(2,StaticFunctions.getStartOfDay(date));
            preparedStatement.setBoolean(3,onlyForTodaySelection);
            preparedStatement.setTimestamp(4,StaticFunctions.getEndOfDay(date));

            ResultSet resultSet = preparedStatement.executeQuery();


            Timestamp currentStartDate = null;
            Timestamp previousFinishDate = null;
            long uselessTime = 0;
            long uselessTimeTotal = 0;
            String previousFinish_date = "";


            while(resultSet.next()) {
                String activity_name = resultSet.getString("activity_name");
                String usage_in_a_row = resultSet.getString("usage_in_a_row");
                String start_date = resultSet.getString("start_date");
                String finish_date = resultSet.getString("finish_date");
                String useful_time_duration = resultSet.getString("useful_time_duration");


                String useful_time_total = resultSet.getString("useful_time_total");



                if(showBreaks && onlyForTodaySelection) {
                    currentStartDate = resultSet.getTimestamp("start_date");
                    if (previousFinishDate != null) {

                        uselessTime = currentStartDate.getTime() / 1000 - previousFinishDate.getTime() / 1000;
                        uselessTimeTotal += uselessTime;
                        String[] uselessTimeRow = {"", "", previousFinish_date, start_date, StaticFunctions.castToTimeString(uselessTime), StaticFunctions.castToTimeString(uselessTimeTotal)};
                        mainTableModel.addRow(uselessTimeRow);

                    }
                    previousFinishDate = resultSet.getTimestamp("finish_date");
                    previousFinish_date = finish_date;

                }

                String[] usefulTimeRow = {activity_name, usage_in_a_row, start_date, finish_date, useful_time_duration, useful_time_total};
                mainTableModel.addRow(usefulTimeRow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        mainTableModel.fireTableDataChanged();


    }

    public void closeUnclosedRecords(){

        String query = """
                update  time_usage as updatingTable
                inner join (select id from time_usage where isFinished = 0) as constrainingTable
                on updatingTable.id = constrainingTable.id
                set isFinished = 1                
                """;
        connectionDB.executeQuery(query);


    }

}
