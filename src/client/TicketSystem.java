package client;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import object.Concert;

public class TicketSystem extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTable concertTable;
    private JTextField ticketCount;
    private JButton book;
    private JTable ticketTable;
    private JTextField ticketReturn;
    private JButton btnReturn;
    private ArrayList<Concert> concerts = new ArrayList<>();
    private String [][] data_arr;
    private Client cl;

    public TicketSystem() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        buttonCancel.setText("Close this Garbage");

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        TicketSystem dialog = new TicketSystem();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }

    private void makeTable() throws IOException, InterruptedException {
        cl = new Client("localhost",15500);
        concerts = cl.getConcerts();
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        for (Concert c:concerts){
            ArrayList <String> tmp = new ArrayList<>();
            tmp.add(c.getName());
            tmp.add(Integer.toString(c.getCards()));
            data.add(tmp);
        }
        data_arr = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ArrayList<String> row = data.get(i);
            String[] copy = new String[row.size()];
            for (int j = 0; j < row.size(); j++) {
                copy[j] = row.get(j);
            }
            data_arr[i] = copy;
        }
        String[] title = new String[]{
                "Concert","Tickets"
        };
        concertTable = new JTable(data_arr,title);
        concertTable.setVisible(true);
        ticketTable = new JTable();
        ticketTable.setVisible(true);
    }

    private void createUIComponents() throws IOException, InterruptedException {
        // TODO: place custom component creation code here
        makeTable();
        book = new JButton("Book");
        book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tmp = concertTable.getSelectedRow();
                for (Concert c:concerts){
                    if(c.getName().equals(data_arr[tmp][0])){
                        int tickets = Integer.parseInt(ticketCount.getText());
                        try {
                            cl.getTickets(c,tickets);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
