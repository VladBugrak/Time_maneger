package com.vld;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ActivityReferenceTableModel extends AbstractTableModel {

    private int columnCount = 6;
    private ArrayList<String[]> dataArrayList;

    public ActivityReferenceTableModel() {
        this.dataArrayList = new ArrayList<String[]>();
    }

    @Override
    public int getRowCount() {
        return dataArrayList.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] row = dataArrayList.get(rowIndex);
        return  row[columnIndex];

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0: return "id";
            case 1: return "name";
            case 2: return "forToday";
            case 3: return "forWeek";
            case 4: return "forMonth";
            case 5: return "forAllPeriod";
        }
        return "";

    }

    public void clearArrayList(){
        dataArrayList.clear();
    }

    public void add(String[] row){
        dataArrayList.add(row);
    }


}
