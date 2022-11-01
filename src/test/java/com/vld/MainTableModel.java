package com.vld;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class MainTableModel  extends AbstractTableModel {

    private final int columnCount = 6;
    private final ArrayList<String[]> dataArrayList;
    private ConnectionDB connectionDB;



    public void  clearArrayList(){
        dataArrayList.clear();
    }

    public void addRow(String[] row){
        dataArrayList.add(row);
    }



    public ConnectionDB getConnectionDB() {
        return connectionDB;
    }

    public MainTableModel() {
        dataArrayList = new ArrayList<String[]>();
        connectionDB = new ConnectionDB();

    }


    public int getRowCount() {
        return dataArrayList.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0: return "activity";
            case 1: return "usage_in_a_row";
            case 2: return "start_date";
            case 3: return "finish_date";
            case 4: return "useful_time_duration";
            case 5: return "useful_time_total";

        }

        return "";

    }

    public int getColumnCount() {
        return columnCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataArrayList.get(rowIndex)[columnIndex];
    }




}
