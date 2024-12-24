/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import component.Item_people;
import event.EventMenuLeft;
import event.PublicEvent;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import model.Model_User_Account;
import net.miginfocom.swing.MigLayout;
import service.Service;
import swing.ScrollBar;

/**
 *
 * @author Admin
 */
public class Menu_Left extends javax.swing.JPanel {
    private List<Model_User_Account> userAccount;
    /**
     * Creates new form Menu_Left
     */
    public Menu_Left() {
        initComponents();
        init();
    }
    private void init(){
           sp.setVerticalScrollBar(new ScrollBar()); //set cuộn page
        menuList.setLayout(new MigLayout("fillx", "0[]0", "0[]0"));
        userAccount = new ArrayList<>();// list của all info client khác với clien đăng nhập hiện tại
        PublicEvent.getInstance().addEventMenuLeft(new EventMenuLeft() {
               @Override
               public void newUser(List<Model_User_Account> users) {// all list info client khác với clien đăng nhập hiện tại
                   for (Model_User_Account d:users){//duyệt từng element của mảng users cho biến d
                       userAccount.add(d);
                       menuList.add(new Item_people(d), "wrap");
                       refreshMenuList();
                    }
                  }

               @Override
               public void userConnect(Model_User_Account user) {
                   for(Model_User_Account u:userAccount){
                       
                       if(u.getUserID() == user.getUserID()){
                          u.setStatus(true);
                          PublicEvent.getInstance().getEventMain().updateUser(u);
                          break;
                       }
                   }
                   if(true){
                       for(Component com:menuList.getComponents()){
                            Item_people item = (Item_people)com;
                            if(item.getUser().getUserID() == user.getUserID()){
                                item.updateStatus();
                                
                                break;
                            
                            }
                       } 
                    }
                  }

               @Override
               public void userDisconnect(int userID) {
                   for(Model_User_Account u:userAccount){
                       if(u.getUserID() == userID){
                          u.setStatus(false);
                          PublicEvent.getInstance().getEventMain().updateUser(u);
                          break;
                       }
                   }
                   if(true){
                       for(Component com:menuList.getComponents()){
                            Item_people item = (Item_people)com;
                            if(item.getUser().getUserID() == userID){
                                item.updateStatus();
//                                 Service.getInstance().getClient().emit("get_messages", );
                                break;
                            
                            }
                       } 
                    }
                  }
               
           });
       showMessage();
    }
    private void showMessage() {
        menuList.removeAll();
        for (Model_User_Account d:userAccount) {
            menuList.add(new Item_people(d), "wrap"); //thêm list user onl trong menulistleft
        }
        refreshMenuList();
    }
    private void showGroup() {
        menuList.removeAll();
        for (int i = 0; i < 5; i++) {
            menuList.add(new Item_people(null), "wrap");
        }
        refreshMenuList();
    }
    private void showBox() {
        menuList.removeAll();
        for (int i = 0; i < 10; i++) {
            menuList.add(new Item_people(null), "wrap");
        }
        refreshMenuList();
    }
      private void refreshMenuList() {
        menuList.repaint();
        menuList.revalidate();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menu = new javax.swing.JLayeredPane();
        jLabel2 = new javax.swing.JLabel();
        sp = new javax.swing.JScrollPane();
        menuList = new javax.swing.JLayeredPane();

        setBackground(new java.awt.Color(38, 34, 43));

        menu.setBackground(new java.awt.Color(34, 38, 43));
        menu.setOpaque(true);
        menu.setLayout(new java.awt.GridLayout(1, 3));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/comment.png"))); // NOI18N
        menu.add(jLabel2);

        sp.setBackground(new java.awt.Color(242, 242, 242));
        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        menuList.setBackground(new java.awt.Color(22, 25, 29));
        menuList.setOpaque(true);

        javax.swing.GroupLayout menuListLayout = new javax.swing.GroupLayout(menuList);
        menuList.setLayout(menuListLayout);
        menuListLayout.setHorizontalGroup(
            menuListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        menuListLayout.setVerticalGroup(
            menuListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        sp.setViewportView(menuList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
            .addComponent(sp, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane menu;
    private javax.swing.JLayeredPane menuList;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
