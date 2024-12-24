/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import app.MessageType;
import event.PublicEvent;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import model.Model_File;
import model.Model_File_Sender;
import model.Model_MessageDB;
import model.Model_Package_Sender;
import model.Model_Receive_Message;
import model.Model_Send_Message;
import model.Model_User_Account;
import model.Model_Voice_Receive;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import swing.Recoder;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 1000;
//    private final String IP = "192.168.224.134"; //ip nối đến máy ảo 
    private final String IP = "localhost"; 
    private Model_User_Account user;
    private List<Model_File_Sender> fileSender;
    private ServiceFile serviceFile;
    private Recoder recoder;


    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() {
       fileSender = new ArrayList<>();
       serviceFile = new ServiceFile();
       recoder = new Recoder();
    }

    public void startServer() {
            //xóa file cache trong folder client_data
             File f = new File("client_data");
        for (File fs : f.listFiles()) {
            fs.delete();
        }
        
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);//kết nối đến server với ip và cổng đã khai báo
            client.on("list_user", new Emitter.Listener() {//bắt đầu chạy program thì gửi request : list_user đến server và chờ phản hồi từ server
                @Override
                public void call(Object... os) {
                    //list user nhận từ server
                    List<Model_User_Account> users = new ArrayList<>();
                    for (Object o : os){
                        Model_User_Account u = new Model_User_Account(o);
                        if (u.getUserID() != user.getUserID()) {// lấy all list info client khác đang có trong db gán vào array list trừ info của client hiện tại
                            users.add(u);
                            System.out.println(user.toString());
                        }
                       
                    }
                    PublicEvent.getInstance().getEventMenuLeft().newUser(users);
                    
                   }
            });
          
            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
               
                    boolean status = (Boolean) os[1];
                    if(status){
                          //connect
                          Model_User_Account user = new Model_User_Account(os[0]);
                          PublicEvent.getInstance().getEventMenuLeft().userConnect(user);
                    }else{
                        //disconnect
                        int userID = (Integer) os[0];
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userID);
                    
                    }
                    }
            });
            client.on("receive_data", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_File dataFile = new Model_File(os[0]);
                    try {
                        serviceFile.initFile(dataFile);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    }
            });
            
            client.on("receive_ms", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Receive_Message message = new Model_Receive_Message(os[0]);
           
                    if (message.getMessageType().getValue() == MessageType.IMAGE.getValue()) {
                        try {
                       File file =  serviceFile.receiveFile(message.getDataImage());
                       PublicEvent.getInstance().getEventChat().receiveMessage(message, file);
                       
                    } catch (IOException ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    }else if(message.getMessageType().getValue() == MessageType.FILE.getValue()){
                        PublicEvent.getInstance().getEventChat().receiveMessage(message, null);
                    }
                    else{
                    
                        PublicEvent.getInstance().getEventChat().receiveMessage(message, null);
                    }
                  }
                    
            });
            client.on("load_messages", new Emitter.Listener() {
    @Override
    public void call(Object... os) {
        try {
            // Dữ liệu tin nhắn được nhận từ server
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> messagesData = (List<Map<String, Object>>) os[0];

            // Xử lý từng tin nhắn
            for (Map<String, Object> messageData : messagesData) {
                Model_Receive_Message message = new Model_Receive_Message(messageData);

                if (message.getMessageType().getValue() == MessageType.IMAGE.getValue()) {
                    // Xử lý tin nhắn hình ảnh
                    try {
                        File file = serviceFile.receiveFile(message.getDataImage());
                        System.out.println("abc"+message.getDataImage());
                        PublicEvent.getInstance().getEventChat().receiveMessage(message, file);
                    } catch (IOException ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (message.getMessageType().getValue() == MessageType.FILE.getValue()) {
                    // Xử lý tin nhắn file
                    PublicEvent.getInstance().getEventChat().receiveMessage(message, null);
                } else {
                    // Xử lý tin nhắn văn bản
                    PublicEvent.getInstance().getEventChat().receiveMessage(message, null);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, "Error loading messages", ex);
        }
    }
});

            client.on("receive_voice", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Voice_Receive voice = new Model_Voice_Receive(os[0]);
                    PublicEvent.getInstance().getEventChat().receiveMessage(voice);
                    }
            });
            
            client.on("GetFile", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Package_Sender data = new Model_Package_Sender(os[0]);
                    saveFileIntoFolder(data);
                }
            });
            client.open();
        } catch (URISyntaxException e) {
            error(e);
        }
        client.on("receive_messages", new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        try {
            String jsonResponse = (String) args[0];
            JSONArray messageArray = new JSONArray(jsonResponse);

            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject messageJson = messageArray.getJSONObject(i);
                System.out.println("Message received: " + messageJson.toString());

                // Chuyển đổi JSONObject thành Model_MessageDB
                Model_MessageDB message =new Model_MessageDB();
                message.setIdsender(messageJson.getInt("senderId"));
                message.setIdreceiver(messageJson.getInt("receiverId"));
                message.setContent(messageJson.getString("content"));
                message.setTimestamp(messageJson.getString("timestamp"));
                System.out.println("Model_MessageDB: " + message.toString());
                // Gọi phương thức receiveMessage với đối tượng message
                PublicEvent.getInstance().getEventChat().receiveMessage(message);  
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});

    }
    
    
    
    public Model_File_Sender addFile(File file, Model_Send_Message message) throws IOException {
        Model_File_Sender data = new Model_File_Sender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        // cho send file từng cái một
        if(fileSender.size() == 1){
           data.initSend();
        }
        return data;
    }
    public void fileSendFinish(Model_File_Sender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()){
            // bắt đầu gửi file mới khi file cũ đã gửi hoàn thành
            fileSender.get(0).initSend();
        }
    }
    private void saveFileIntoFolder(Model_Package_Sender data){
        try {
            File file = new File(data.getFileName());
            FileOutputStream out = new FileOutputStream(file);
            out.write(data.getData());
            out.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    public void getMessages(int userId1, int userId2) {
        client.emit("get_messages", userId1, userId2);

//        client.on("messages_received", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                List<Model_MessageDB> messages = (List<Model_MessageDB>) args[0];
//                for (Model_MessageDB message : messages) {
//                    System.out.println("Message from " + message.getContent()+ ": " + message.getContent());
//                }
////                PublicEvent.getInstance().getEventChat().receiveMessage(message, null);
//            }
//        });
    }

    public Socket getClient() {
        return client;
    }
     public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
    }
    public Recoder getRecoder(){
        return recoder;
    }
    public void setRecoder(Recoder aRecoder) {
        recoder = aRecoder;
    }

    private void error(Exception e) {
        System.err.println(e);
    }
}