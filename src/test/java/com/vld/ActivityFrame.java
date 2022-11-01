package com.vld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ActivityFrame  extends JFrame {

    private final MainPanel mainPanel;
    private final ButtonsActionListener buttonsActionListener;


    private final JLabel activityIDLabel = new  JLabel("ID");
    private final JTextField activityIDField = new JTextField(150);



    private final JLabel activityNameLabel = new JLabel("Name");
    private final JTextField activityNameField = new JTextField(150);

    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");
    private String activityName;
    private int activityID;

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public ActivityFrame(MainPanel mainPanel){

        Dimension size = new Dimension(350,150) ;

        this.activityID = -1;
        this.mainPanel =mainPanel;
        this.buttonsActionListener = mainPanel.getButtonsActionListener();

        this.setSize(size);
        this.setMinimumSize(size);
        this.setTitle("Activity");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(mainPanel.getParentFrame());
        this.setLayout(new GridBagLayout());

        activityIDField.setEnabled(false);

        this.pack();





        this.add(activityIDLabel,getNewGridBagConstraints(0,0,1,1,0,0));
        this.add(activityIDField,getNewGridBagConstraints(1,0,1,1,1,0));

        this.add(activityNameLabel,getNewGridBagConstraints(0,1,1,1,0,0));
        this.add(activityNameField,getNewGridBagConstraints(1,1,1,1,1,0));

        this.add(saveButton,getNewGridBagConstraints(0,2,1,1,0,0));
        this.add(cancelButton,getNewGridBagConstraints(1,2,1,1,1,0));


       saveButton.addActionListener(buttonsActionListener);
       cancelButton.addActionListener(buttonsActionListener);


    }

    public void setActivityID(int activityID){
        this.activityID = activityID;
        activityIDField.setText("" + activityID);
    }

    public void setActivityName(String activityName){
        activityNameField.setText(activityName);
    }

    public int  getActivityID(){
        return activityID;
    }

    public String getActivityName(){
       return activityNameField.getText();
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
