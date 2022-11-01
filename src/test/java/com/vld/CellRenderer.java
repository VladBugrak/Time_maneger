package com.vld;

import com.vld.MainPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class CellRenderer extends DefaultTableCellRenderer {

    private final MainPanel mainPanel;

    public CellRenderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);




            if(table == mainPanel.getMainTable()){
                cell.setForeground(Color.BLACK);

                if(mainPanel.getButtonsActionListener().isRecordIsRunning() && table.getRowCount() == row + 1) {
                    cell.setBackground(Color.ORANGE);
                }
                else {
                    cell.setBackground(Color.WHITE);
                }

                if( table.getRowCount() == row + 1
                        &&(column == StaticFunctions.getTableColumnIndex(table, "Time")
                        || column == StaticFunctions.getTableColumnIndex(table, "Total"))
                ) cell.setForeground(Color.BLUE);






            }

            if(table == mainPanel.getActivityReferenceTable()){

                cell.setForeground(Color.BLACK);

                if(isSelected)
                    cell.setBackground(Color.ORANGE);
                else
                    cell.setBackground(Color.WHITE);

                





            }


        return cell;
    }
}
