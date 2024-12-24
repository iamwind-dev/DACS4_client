/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package component;

import app.MessageType;
import event.PublicEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import main.Main;
import model.Model_Send_Message;
import model.Model_User_Account;
import net.miginfocom.swing.MigLayout;
import service.Service;
import swing.FileChooser;
import swing.JIMSendTextPane;
import swing.ScrollBar;



public class chat_Bottom extends javax.swing.JPanel {


    public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
        panelMore.setUser(user);
    }

    private Model_User_Account user;
    
    public chat_Bottom() {
        initComponents();
        init();
    }
    private void init(){
        mig = new MigLayout("fillx, filly", "0[fill]0[]0[]2", "2[fill]2[]0");
        setLayout(mig);
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        JIMSendTextPane txt = new JIMSendTextPane();
        txt.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e) {
                refresh();
                if(e.getKeyChar() == 10 && e.isControlDown()){
                   //user nhấn ctrl + enter để gửi đoạn chat
                   eventSend(txt);
                }
            }
        });
        txt.setBorder(new EmptyBorder(5, 5, 5, 5));
        txt.setHintText("Nhập tin nhắn...");//set dòng chữ mờ trong thanh chat
        txt.setBackground(new Color(34,38,43));
        txt.setForeground(Color.WHITE);
        scroll.setViewportView(txt);
        ScrollBar sb = new ScrollBar();
        sb.setBackground(new Color(34,38,43));
        sb.setPreferredSize(new Dimension(2, 10));
        scroll.setVerticalScrollBar(sb);
        add(sb);
        add(scroll, "w 100%");
        JPanel pane = new JPanel();
        pane.setLayout(new MigLayout("filly","0[]5[]5[]5[]0","0[bottom]0"));
        pane.setPreferredSize(new Dimension(26, 24));
        pane.setBackground(new Color(34,38,43));
        JButton cmd = new JButton();
        cmd.setBackground(new Color(34,38,43));
        cmd.setIcon(new ImageIcon(getClass().getResource("/icon/paper-plane.png")));
        cmd.setBorder(null);
        cmd.setContentAreaFilled(false);
        cmd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               eventSend(txt);
            
            }
        });
        
        JButton cmdMore = new JButton();
        cmdMore.setIcon(new ImageIcon(getClass().getResource("/icon/smile.png")));
        cmdMore.setBorder(null);
        cmdMore.setContentAreaFilled(false);
        cmdMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if(panelMore.isVisible()){
                  cmdMore.setIcon(new ImageIcon(getClass().getResource("/icon/smile.png")));
                  panelMore.setVisible(false);
                  mig.setComponentConstraints(panelMore, "dock south,h 0!");
                  revalidate();
                  
              }else{
                  cmdMore.setIcon(new ImageIcon(getClass().getResource("/icon/more.png")));
                  panelMore.setVisible(true);
                  mig.setComponentConstraints(panelMore, "dock south,h 200!");//set chiều cao cho paneMore 
                  revalidate();
              }
            
            }
        });
        pane.add(getButtonFile());
        pane.add(getButtonImage());
        pane.add(cmdMore);
        pane.add(cmd);
        add(pane, "wrap");
        panelMore = new Panel_More();
        panelMore.setVisible(false);
        add(panelMore, "dock south,h 0!");// set cao = 0
        
    }
    
    private JButton getButtonImage(){
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/icon/upload (2).png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ea) {
                 JFileChooser ch = new JFileChooser();
                 FileChooser preview = new FileChooser();
                 ch.setAccessory(preview);
                 ch.addPropertyChangeListener(preview);
                 ch.setMultiSelectionEnabled(true);
                 ch.setFileFilter(new FileFilter(){
                     @Override
                     public boolean accept(File file) {
                            return file.isDirectory()||isImageFile(file);
                         }

                     @Override
                     public String getDescription() {
                            return "Image File";
                         }
                     
                 });
                 int option = ch.showOpenDialog(Main.getFrames()[0]);
                 if (option == JFileChooser.APPROVE_OPTION){
                        File files[] = ch.getSelectedFiles();
                        try {
                           for(File file:files){
                               Model_Send_Message message = new Model_Send_Message(MessageType.IMAGE, Service.getInstance().getUser().getUserID(), user.getUserID(), "");
                               Service.getInstance().addFile(file, message);
                               PublicEvent.getInstance().getEventChat().sendMessage(message);
                          
                           }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                   }
                     
                 }
        });
        return cmd;
        
    }
    private JButton getButtonFile(){
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/icon/link (1).png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser ch = new JFileChooser();
                int option = ch.showOpenDialog(Main.getFrames()[0]);
                 if (option == JFileChooser.APPROVE_OPTION){
                     try {
                        File file = ch.getSelectedFile();
                        Model_Send_Message message = new Model_Send_Message(MessageType.FILE, Service.getInstance().getUser().getUserID(), user.getUserID(), "");
                        Service.getInstance().addFile(file, message);
                        PublicEvent.getInstance().getEventChat().sendMessage(message); 
                     } catch (IOException evt) {
                     }
                   }
                //update next
               }
        });
        return cmd;
    }
    
    private boolean isImageFile(File file){ //kiểm tra đuôi file là định dạng image hợp lệ
            String name = file.getName().toLowerCase();
            return name.endsWith(".jpg")||name.endsWith(".png")||name.endsWith(".jpeg")||name.endsWith(".gif");
    }
    
    private void eventSend(JIMSendTextPane txt){ //event gửi đoạn chat
                 String text = txt.getText().trim();
                if(!text.equals("")){
                    Model_Send_Message message = new Model_Send_Message(MessageType.TEXT, Service.getInstance().getUser().getUserID(), user.getUserID(), text);
                    System.out.println(message.toString());
                    send(message);
                    PublicEvent.getInstance().getEventChat().sendMessage(message);//thêm đoạn chat qua 
                    txt.setText("");
                    txt.grabFocus();
                    refresh();
                }else{
                    txt.grabFocus();
                }
    }
    
    private void send(Model_Send_Message data){
        Service.getInstance().getClient().emit("send_to_user", data.toJSONObject());//gửi event "send to user" qua server xử lí
        
    }
    private void refresh(){
        revalidate();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(34, 38, 43));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    private MigLayout mig;
    private Panel_More panelMore;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
