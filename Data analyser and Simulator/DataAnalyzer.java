// File Name DataAnalyzer.java
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.awt.*;


public class DataAnalyzer  {

  public static DatagramSocket serverSocket;
  public static String dt, v, rm;
  public static Socket server;
  public static JFrame frame,display,displ;
  public static int k=0,l=0, port,lk=0,isConnect=0;
  public static int[] ctarr=new int[100];
  public static String pack="",line=null;
  public static int ct=0,psf=0,pst=0;
  public static List<Object[]> row = new ArrayList<Object[]>();
  public static Object lock = new Object();
  public static Thread th = new Thread();


   public static void main(String [] args) {

      try {

        //Settings Window Creation using Swing

        String DataTypes[]={"BOOL","CHAR","BYTE","INT","LONG INT","SHORT INT","FLOAT","DOUBLE"};
        String columns[]={"NO.","TYPE","FROM BYTE","TO BYTE","REMARKS"};
        String columnsP[]={"NO.","TYPE","VALUE","FROM BYTE","TO BYTE","REMARKS"};

        JButton addB = new JButton("ADD");
        JButton clearB = new JButton("CLEAR");
        JButton listenB = new JButton("START LISTENING");
        JButton deleteB = new JButton("DEL");
        JButton startB = new JButton("START");
        JButton settingB = new JButton("SETTINGS");
        JButton loadB = new JButton("LOAD");
        JButton saveB = new JButton("SAVE");
        JButton importB = new JButton("Import Settings");
        JButton exportB = new JButton("Export Settings");
        JButton insertB = new JButton("INSERT");
        JButton pauseB = new JButton("PAUSE");
        JComboBox<String> cb=new JComboBox<>(DataTypes);
        JTextField ctrfield=new JTextField("1");
        JLabel l1 = new JLabel("Data Type");
        JLabel l2 = new JLabel("No. of values ");
        JLabel l3 = new JLabel("Packet Structure ");
        JLabel l4 = new JLabel("Packet Size(bits) ");
        JLabel l6 = new JLabel("Hex value");
        JTextArea hex = new JTextArea("");
        JTextArea ps = new JTextArea("0");
        TableModel tmodel = new DefaultTableModel(columns, 0);
        //DefaultTableModel model = (DefaultTableModel) disp.getModel();
        JTable disp = new JTable(tmodel);
        DefaultTableModel model = (DefaultTableModel)disp.getModel();


        TableModel tmodelP = new DefaultTableModel(columnsP,0);
        JTable pdump = new JTable(tmodelP);
        DefaultTableModel modelP = (DefaultTableModel)pdump.getModel();


        JTextField pf= new JTextField("7978");
        JLabel l5 = new JLabel("Port Number");
        JScrollPane dispi = new JScrollPane(disp,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane pdumpi = new JScrollPane(pdump,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


          frame = new JFrame("Data Analyzer<Settings>");
          display = new JFrame("Data Analyzer<Packet Dump>");
      //    displ = new JFrame("Data Analyzer<Packet Dump>");

          //Set bounds for different components
          hex.setLineWrap(true);
          hex.setWrapStyleWord(true);


          dispi.setBounds(0,30, 600 ,100);
        //  disp.setEditable(false);
        //  addB.setBounds(190,170,100,25);
          exportB.setBounds(435,170,145,25);
          importB.setBounds(435,210,145,25);
          listenB.setBounds(190,260,220,25);
          deleteB.setBounds(190,210,100,25);
          insertB.setBounds(190,170,220,25);
          clearB.setBounds(310,210,100,25);
          l6.setBounds(20,365,120,25);
          hex.setBounds(20,395,960,50);
          pauseB.setBounds(10,465,476,30);
          settingB.setBounds(510,465,476,30);
          saveB.setBounds(435,260,145,25);
          loadB.setBounds(435,260,145,25);
          l1.setBounds(20,150,140,25);
          l2.setBounds(200,150,140,25);
          l3.setBounds(10,10 , 200, 20);
          l4.setBounds(460, 150 , 120 ,25);
          l5.setBounds(20,230,140,25);
          pf.setBounds(20,260,140,25);

        //  ps.setEditable(false);
          ctrfield.setBounds(200,180,100,25);
          cb.setBounds(20, 180,140,25);
          pdumpi.setBounds(2,2,996,360);
        //  pdump.setEditable(false);

          //Add components to frame and set frame properties

          frame.add(cb);frame.add(l1);frame.add(l3);frame.add(pf);frame.add(deleteB);frame.add(insertB);
          frame.add(dispi);frame.add(l5);frame.add(listenB);frame.add(clearB);frame.add(saveB);frame.add(importB);frame.add(exportB);
          frame.setLayout(null);
          frame.setSize(600,350);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
          frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        //  frame.setVisible(true);

          //Add components to display window and set its properties
          display.setLayout(null);
          display.setSize(1000,550);
          display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          display.setLocation(dim.width/2-display.getSize().width/2, dim.height/2-display.getSize().height/2);
          display.add(pdumpi);display.add(pauseB);display.add(settingB);display.add(l6);display.add(hex);
          display.setVisible(true);


          //Add event listeners to different components

          addB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent push1) {
              try{
              String ctrStr = ctrfield.getText();
              String ch = cb.getItemAt(cb.getSelectedIndex());
              String rem = "";
              ctarr[l++]=Integer.parseInt(ctrStr);
              psf=(Integer.parseInt(ps.getText()))/8+1;
              if(ch=="INT"){
                pack+="i";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                rem = "Integer";
              }
              if(ch=="SHORT INT"){
                pack+="s";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                rem = "Integer(16-bit)";
              }
              if(ch=="LONG INT"){
                pack+="l";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                rem = "Integer(64-bit)";
              }
              if(ch=="BYTE"){
                pack+="o";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+8*ctarr[l-1]));
                rem = "Integer(8-bit)";
              }
              if(ch=="CHAR"){
                pack+="c";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                rem = "Character";
              }
              if(ch=="FLOAT"){
                pack+="f";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                rem = "Floating point";
              }

              if(ch=="DOUBLE"){
                pack+="d";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                rem = "Float(High Precision)";

              }
              if(ch=="BOOL"){
                pack+="b";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+1*ctarr[l-1]));
                rem = "True or False ";
              }
             // System.out.println(pack);
              pst=((Integer.parseInt(ps.getText()))-1)/8+1;
              //disp.append("["+ch+"*"+ctrStr+"]");
              model.addRow(new Object[]{Integer.toString(++ct),ch,Integer.toString(psf),Integer.toString(pst),rem});
              disp.scrollRectToVisible(disp.getCellRect(model.getRowCount()-1, 0, true));
            }
              catch(Exception e1){
              e1.printStackTrace();}
            }
          });

          insertB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push11){
              if(disp.getSelectionModel().isSelectionEmpty()){
                addB.doClick();
              }
              else{

                ps.setText("0");
                try{
                String ctrStr = ctrfield.getText();
                String ch = cb.getItemAt(cb.getSelectedIndex());
                String rem = "";
                ctarr[l++]=Integer.parseInt(ctrStr);
                //psf=(Integer.parseInt(ps.getText()))/8+1;
                if(ch=="INT"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "i").toString();
                }
                if(ch=="SHORT INT"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "s").toString();

                }
                if(ch=="LONG INT"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "l").toString();
                }
                if(ch=="BYTE"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "o").toString();
                }
                if(ch=="CHAR"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "c").toString();

                }
                if(ch=="FLOAT"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "f").toString();
                }

                if(ch=="DOUBLE"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "d").toString();

                }
                if(ch=="BOOL"){
                  pack = new StringBuilder(pack).insert(disp.getSelectedRow(), "b").toString();
                }
                //System.out.println(pack);
                //disp.append("["+ch+"*"+ctrStr+"]");
                l=0;
                ct=0;

                int rowCount = model.getRowCount();
                //Remove rows one by one from the end of the table
                for (int i = rowCount - 1; i >= 0; i--) {
                  model.removeRow(i);
                }

                for(int i=0;i<pack.length();i++){

                  try{
                   ctrStr = ctrfield.getText();
                   rem = "";
                   ch = "";
                  ctarr[l++]=Integer.parseInt(ctrStr);
                  psf=(Integer.parseInt(ps.getText()))/8+1;
                  if(pack.charAt(i)=='i'){
                    ch="INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                    rem = "Integer";
                  }
                  if(pack.charAt(i)=='s'){
                    ch="SHORT INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                    rem = "Integers(32-bit)";
                  }
                  if(pack.charAt(i)=='l'){
                    ch ="LONG INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                    rem = "Integers(64-bit)";
                  }
                  if(pack.charAt(i)=='o'){
                    ch ="BYTE";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+8*ctarr[l-1]));
                    rem = "Integer(8-bit)";
                  }
                  if(pack.charAt(i)=='c'){
                    ch ="CHAR";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                    rem = "Character";
                  }
                  if(pack.charAt(i)=='f'){
                    ch ="FLOAT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                    rem = "Floating point";
                  }

                  if(pack.charAt(i)=='d'){
                    ch ="DOUBLE";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                    rem = "Float(High Precision)";

                  }
                  if(pack.charAt(i)=='b'){
                    ch ="BOOL";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+1*ctarr[l-1]));
                    rem = "True or False";

                  }
                 // System.out.println(pack);
                  pst=(Integer.parseInt(ps.getText()))/8+1;
                  //disp.append("["+ch+"*"+ctrStr+"]");
                  model.addRow(new Object[]{Integer.toString(++ct),ch,Integer.toString(psf),Integer.toString(pst),rem});
                }
                catch(Exception e1){
                e1.printStackTrace();}

              }
              }catch(Exception e){e.printStackTrace();}
            }}
          });

          deleteB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent push2) {
              if(disp.getSelectionModel().isSelectionEmpty()){
              if(l!=0)
              l--;

              //System.out.println(pack.charAt(l));
              if(pack.charAt(l)=='f'||pack.charAt(l)=='i'){
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())-32*ctarr[l]));}
              if(pack.charAt(l)=='c'){
              ps.setText(Integer.toString(Integer.parseInt(ps.getText())-16*ctarr[l]));}
              if(pack.charAt(l)=='b'){
              ps.setText(Integer.toString(Integer.parseInt(ps.getText())-1*ctarr[l]));}
              pack= pack.substring(0,l);
              int rowCount = model.getRowCount()-1;
              model.removeRow(rowCount);


            }
            else{
              System.out.println(pack);
              StringBuilder sb = new StringBuilder(pack);
              sb.deleteCharAt(disp.getSelectedRow());
              pack = sb.toString();
              System.out.println(pack);
              model.removeRow(disp.getSelectedRow());}

            l=0;
            ct=0;
            ps.setText("0");

            int rowCount = model.getRowCount();
            //Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
              model.removeRow(i);
            }

            for(int i=0;i<pack.length();i++){

              try{
              String ctrStr = ctrfield.getText();
              String rem = "";
              String ch = "";
              ctarr[l++]=Integer.parseInt(ctrStr);
              psf=(Integer.parseInt(ps.getText()))/8+1;
              if(pack.charAt(i)=='i'){
                ch="INT";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                rem = "Integer";
              }
              if(pack.charAt(i)=='s'){
                ch="SHORT INT";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                rem = "Integers(32-bit)";
              }
              if(pack.charAt(i)=='l'){
                ch ="LONG INT";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                rem = "Integers(64-bit)";
              }
              if(pack.charAt(i)=='o'){
                ch ="BYTE";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+8*ctarr[l-1]));
                rem = "Integer(8-bit)";
              }
              if(pack.charAt(i)=='c'){
                ch ="CHAR";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                rem = "Character";
              }
              if(pack.charAt(i)=='f'){
                ch ="FLOAT";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                rem = "Floating point";
              }

              if(pack.charAt(i)=='d'){
                ch ="DOUBLE";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                rem = "Float(High Precision)";

              }
              if(pack.charAt(i)=='b'){
                ch ="BOOL";
                ps.setText(Integer.toString(Integer.parseInt(ps.getText())+1*ctarr[l-1]));
                rem = "True or False";

              }
             // System.out.println(pack);
              pst=(Integer.parseInt(ps.getText())-1)/8+1;
              //disp.append("["+ch+"*"+ctrStr+"]");
              model.addRow(new Object[]{Integer.toString(++ct),ch,Integer.toString(psf),Integer.toString(pst),rem});
            }
              catch(Exception e1){
              e1.printStackTrace();}

            }
          }
          });

          clearB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent push3) {
              l=0;
              pack = "";
              int rowCount = model.getRowCount();
              //Remove rows one by one from the end of the table
              for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
              }
              ps.setText("0");
            }
          });

          listenB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent push4) {
              try{
              saveB.doClick();
              port = Integer.parseInt(pf.getText());
              frame.dispose();
              display.setVisible(true);
              serverSocket = new DatagramSocket(port);
//              pdump.setText("Start Waiting for client on port "+serverSocket.getLocalPort() +"?");
              startB.doClick();
            }catch(Exception ex){ex.printStackTrace();}
            }
          });listenB.doClick();

          startB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push5){
              try{
                  //  pdump.setText("");
                  int rowCount = modelP.getRowCount();

                  for (int i = rowCount - 1; i >= 0; i--) {
                     modelP.removeRow(i);
                    // System.out.println(i+"rc");
                 }
                   File fil = new File(System.getProperty("user.dir")+"/PacketDump.txt");
                   FileWriter fo= new FileWriter(fil,true);
                   String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                   fo.write("----------------------------------\n----------------------------------\nTimeStamp :: "+timeStamp+"\n");
                   fo.close();

                  // pdump.setText("Hello ");
                   //Start listening to socket for client to get connected

                    //SwingUtilities.invokeAndWait(acceptSocketThread);
                    System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                  //  pdump.append("\nConnected");
                  new Thread(new Runnable() {
                    public void run() {
                      try{

              //      server = serverSocket.accept();
                    isConnect=1;

                    //Connection confirmation with client
                  //  pdump.append("\n\nJust connected to " + server.getRemoteSocketAddress());
                //    System.out.println("Just connected to " + server.getRemoteSocketAddress());
                  //  DataInputStream in = new DataInputStream(server.getInputStream());
                  //  String line =null;



                         String str=null;
                         int ctr=0;
                         byte[] buf = new byte[1024];
                         DatagramPacket dp = new DatagramPacket(buf, 1024);
                        serverSocket.receive(dp);
                         line = new String(dp.getData(), 0, dp.getLength());
                         while (true) {
                           int pss=0,psff=0,pstt=0;
                           k=0;ct=0;
                           int temp[]=ctarr.clone();


                           File file = new File(System.getProperty("user.dir")+"/PacketDump.txt");
                           FileWriter f= new FileWriter(file,true);

                           row.clear();

                           if(lk==0){
                           for(int i=0;i<line.length()-8;i+=8){
                             int ctt =i/8+1;
                             String he = line.substring(i,i+8);
                           int decimal = Integer.parseInt(he,2);
                           String hexStr = Integer.toString(decimal,16);
                           hex.append("("+ctt+")" +hexStr+" ");}}



                          System.out.println("\n-----------------------------------------------------------");
                          f.write("\n-----------------------------------------------------------\n");
                    //      pdump.append("\n-----------------------------------------------------------\n");

                          System.out.println("Packet - "+ ++ctr );
                          f.write("Packet - "+ ctr+"\n");
                    //      pdump.append("Packet - "+ ctr+"\n");

                          for(int i=0; i < l ; i++){

                             v="--";dt="--";rm="--";
                             psff=pss/8+1;


                            //Extract boolean values

                          if(pack.charAt(i)=='b'){
                          System.out.print("{ [BOOL]");
                          f.write("{ [BOOL]");
                          dt = "BOOL";
                          rm = "True or False";
                          pss+=1;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                          //  pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            if(line.charAt(k++)=='0'){
                            System.out.print(" False ");
                            f.write(" False ");
                            v= "False";
                          }
                            else{
                            System.out.print(" True ");
                            f.write(" True ");
                            v = "True";
                          }

                          }
                            System.out.print("}");
                            f.write("}");
                            //pdump.append("}");
                        }

                        //Extract float values

                          if(pack.charAt(i)=='f'){
                          System.out.print("{ [FLOAT]");
                          f.write("{ [FLOAT]");
                          dt = "FLOAT";
                          rm = "Floating Point Number";
                          pss+=32;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                          //  pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str= line.substring(k+1,k+32);
                            int val = Integer.parseInt(str, 2);
                            float fl = Float.intBitsToFloat(val);
                            if(line.charAt(k)=='1'){
                            System.out.print(" -"+ fl);
                            f.write(" -"+ fl);
                            v=" -"+ fl;
                          }
                            else{
                            System.out.print(" "+fl+" ");
                            f.write(" "+fl+" ");
                            v=" "+fl;
                          }
                            k+=32;
                          }
                            System.out.print("}");
                            f.write("}");
                          //  pdump.append("}");
                        }

                        //Extract double values

                          if(pack.charAt(i)=='d'){
                        System.out.print("{ [DOUBLE]");
                        f.write("{ [DOUBLE]");
                        dt="DOUBLE";
                        rm = "High Precision Float";
                        pss+=64;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                          //  pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str=line.substring(k+1,k+64);
                            double val = Double.longBitsToDouble(Long.parseLong(str,2));
                            if(line.charAt(k)=='1'){
                            System.out.print(" -"+ val+ " ");
                            f.write(" -"+val+" ");
                            v=(" -"+val+" ");
                          }
                            else{
                            System.out.print(" "+val+" ");
                            f.write(" "+val+" ");
                          v=(" "+val+" ");
                          }
                            k+=64;
                          }
                            System.out.print("}");
                            f.write("}");
                          //  pdump.append("}");
                        }

                        //Extract character values

                          if(pack.charAt(i)=='c'){
                        System.out.print("{ [CHAR]");
                        f.write("{ [CHAR]");
                        dt = "CHAR";
                        rm = "CHARACTER";
                        pss+=16;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                            //pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str=line.substring(k,k+16);
                            int val = Integer.parseInt(str,2);
                            char a = (char)val;
                            System.out.print(" "+a+" ");
                            f.write(" "+a+" ");
                            v=(" "+a+" ");
                            k+=16;
                          }
                            System.out.print("}");
                            f.write("}");
                          //  pdump.append("}");
                        }

                        //Extract byte values

                          if(pack.charAt(i)=='o'){
                          System.out.print("{ [BYTE]");
                          f.write("{ [BYTE]");
                          dt= "BYTE";
                          rm = "8-Bit Integer";
                          pss+=8;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                  //          pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str= line.substring(k,k+8);
                            byte val = (byte)Integer.parseInt(str, 2);

                            System.out.print(" "+val+" ");
                            f.write(" "+val+" ");
                            v=(" "+val+" ");

                            k+=8;
                          }
                            System.out.print("}");
                            f.write("}");
                        //    pdump.append("}");

                        }


                        //Extract Integer values

                          if(pack.charAt(i)=='i'){
                          System.out.print("{ [INT]");
                          f.write("{ [INT]");
                          dt = "INT";
                          rm = "32-Bit Integer";
                          pss+=32;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                          //  pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str= line.substring(k,k+32);
                            int val = (int)Long.parseLong(str, 2);
                            if(line.charAt(k)=='1'){
                            System.out.print(" -"+ val+ " ");
                            f.write(" -"+val+" ");
                            v=(" -"+val+" ");
                          }
                            else{
                            System.out.print(" "+val+" ");
                            f.write(" "+val+" ");
                            v=(" "+val+" ");
                          }
                            k+=32;
                          }
                            System.out.print("}");
                            f.write("}");
                          //  pdump.append("}");

                        }

                        //Extract Short Integer values

                          if(pack.charAt(i)=='s'){
                          System.out.print("{ [SHORT INT]");
                          f.write("{ [SHORT INT]");
                          dt = "SHORT INT";
                          rm = "16-Bit Integer";
                          pss+=16;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                          //  pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str= line.substring(k,k+16);
                            short val = (short)Integer.parseInt(str, 2);
                            if(line.charAt(k)=='1'){
                            System.out.print(" -"+ val+ " ");
                            f.write(" -"+val+" ");
                            v=(" -"+val+" ");
                          }
                            else{
                            System.out.print(" "+val+" ");
                            f.write(" "+val+" ");
                            v=(" "+val+" ");
                          }
                            k+=16;
                          }
                            System.out.print("}");
                            f.write("}");
                            //pdump.append("}");

                        }

                        //Extract Long Integer values

                          if(pack.charAt(i)=='l'){
                          System.out.print("{ [LONG INT]");
                          f.write("{ [LONG INT]");
                          dt = "LONG INT";
                          rm = "64-Bit Integer";
                          pss+=64;
                          if(temp[i]<=0){
                            System.out.print("-- ");
                            f.write("-- ");
                        //    pdump.append("-- ");
                          }
                          else
                          while(temp[i]--!=0){
                            str= line.substring(k+1,k+64);
                            long val = Long.parseLong(str, 2);
                            if(line.charAt(k)=='1'){
                            System.out.print(" -"+ val+ " ");
                            f.write(" -"+val+" ");
                            v=(" -"+val+" ");
                          }
                            else{
                            System.out.print(" "+val+" ");
                            f.write(" "+val+" ");
                            v=(" "+val+" ");
                          }
                            k+=64;
                          }
                            System.out.print("}");
                            f.write("}");
                          //  pdump.append("}");

                        }
                        pstt= (pss-1)/8+1;

                      //  System.out.println(modelP.getColumnCount());
                      //  SwingUtilities.invokeLater(new Runnable(){public void run(){
                            //Update the model here
                            row.add(new Object[]{Integer.toString(++ct),dt, v, Integer.toString(psff),Integer.toString(pstt),rm});

                      //  }});



                        }


                          new Thread(new Runnable(){public void run(){
                          //Update the model here
                          try{
                          synchronized(lock){
                          if(lk==5)
                          lock.wait();}
                          for(Object[] item : row)
                          {
                            SwingUtilities.invokeLater(new Runnable(){public void run(){
                              //Update the model here
                              modelP.addRow(item);


                            }});
                        //    System.out.println(item);
                          }}catch(InterruptedException iex){iex.printStackTrace();}

                        }}).start();


                       serverSocket.receive(dp);
                      line = new String(dp.getData(), 0, dp.getLength());

                        if(lk==0){
                        int rowCount = modelP.getRowCount();

                        for (int i = rowCount - 1; i >= 0; i--) {
                           modelP.removeRow(i);
                      //     System.out.println(i+"rc");
                       }
                       hex.setText("");}


                        if(line == "break")
                        break;
                        f.write("\n");
                    //    pdump.append("\n");
                        f.close();
                      }
                        server.close();
                    }catch(EOFException eofx){try{server.close();serverSocket.close();}catch(Exception ee){ee.printStackTrace();}startB.doClick();}
                    catch(IOException ioee){try{server.close();serverSocket.close();}catch(Exception ee){ee.printStackTrace();}}
                    catch(Exception e2){e2.printStackTrace();}
                      }
                    }).start();





                 } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                 } catch (EOFException p) {
                   System.out.println("\n\n-----------------------------------------------------------\n");
                 } catch (IOException e) {
                    e.printStackTrace();
                 }catch(Exception ie){ie.printStackTrace();}
            }
          });startB.doClick();

          settingB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push6){
              try{
              display.dispose();
              serverSocket.close();
            //  server.close();
          //    pdump.setText("");
              int rowCount = modelP.getRowCount();
                //Remove rows one by one from the end of the table
              for (int i = rowCount - 1; i >= 0; i--) {
                  modelP.removeRow(i);
              }
              frame.setVisible(true);
            }catch(Exception ioex){ioex.printStackTrace();}
          }
          });

          Thread th = new Thread(new Runnable(){
            public void run(){

          pauseB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push12){
                  System.out.println("Clicked");
                  synchronized(lock){
                  if(pauseB.getText()=="PAUSE"){
                      lk=5;
                      pauseB.setText("RESUME");
                      System.out.println("Paused");
                    }
                    else
                  if(pauseB.getText()=="RESUME"){
                    lk=0;
                    int rowCount = modelP.getRowCount();

                    for (int i = rowCount - 1; i >= 0; i--) {
                       modelP.removeRow(i);
                  //     System.out.println(i+"rc");
                   }
                    lock.notify();
                    pauseB.setText("PAUSE");
                    System.out.println("Continue");

                  }}
                }
              });

            }
          });th.start();

          saveB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push7){
              try{
              File config = new File(System.getProperty("user.dir")+"/config.txt");
              FileWriter foo= new FileWriter(config);
              foo.write(pack+"\n");
              foo.write(pf.getText());
              foo.close();
            }catch(IOException ioe){}
            }
          });

          loadB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push8){
              try{
              FileReader fi = new FileReader(System.getProperty("user.dir")+"/config.txt");
              BufferedReader br = new BufferedReader(fi);
              pack=br.readLine();
              pf.setText(br.readLine());
              //System.out.println(System.getProperty("user.dir")+"/config.txt");
              //System.out.println(pack);
              l=0;
              ct=0;
              ps.setText("0");

              int rowCount = model.getRowCount();
              //Remove rows one by one from the end of the table
              for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
              }

              for(int i=0;i<pack.length();i++){

                try{
                String ctrStr = ctrfield.getText();
                String rem = "";
                String ch = "";
                ctarr[l++]=Integer.parseInt(ctrStr);
                psf=(Integer.parseInt(ps.getText()))/8+1;
                if(pack.charAt(i)=='i'){
                  ch="INT";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                  rem = "Integer";
                }
                if(pack.charAt(i)=='s'){
                  ch="SHORT INT";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                  rem = "Integers(32-bit)";
                }
                if(pack.charAt(i)=='l'){
                  ch ="LONG INT";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                  rem = "Integers(64-bit)";
                }
                if(pack.charAt(i)=='o'){
                  ch ="BYTE";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+8*ctarr[l-1]));
                  rem = "Integer(8-bit)";
                }
                if(pack.charAt(i)=='c'){
                  ch ="CHAR";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                  rem = "Character";
                }
                if(pack.charAt(i)=='f'){
                  ch ="FLOAT";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                  rem = "Floating point";
                }

                if(pack.charAt(i)=='d'){
                  ch ="DOUBLE";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                  rem = "Float(High Precision)";

                }
                if(pack.charAt(i)=='b'){
                  ch ="BOOL";
                  ps.setText(Integer.toString(Integer.parseInt(ps.getText())+1*ctarr[l-1]));
                  rem = "True or False";

                }
               // System.out.println(pack);
                pst=(Integer.parseInt(ps.getText()))/8+1;
                //disp.append("["+ch+"*"+ctrStr+"]");
                model.addRow(new Object[]{Integer.toString(++ct),ch,Integer.toString(psf),Integer.toString(pst),rem});
              }
                catch(Exception e1){
                e1.printStackTrace();}

              }
          //    disp.setText(br.readLine());
          //    ps.setText(br.readLine());
              br.close();
            }
              catch(FileNotFoundException fn){
                JOptionPane.showMessageDialog(null, "Config File not present!");
              }catch(IOException ioe){}
            }
          });loadB.doClick();

          importB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push9){

              JFileChooser fc=new JFileChooser();
              int j=fc.showOpenDialog(importB);
              if(j==JFileChooser.APPROVE_OPTION){

                try{
                File f=fc.getSelectedFile();
                FileReader fi = new FileReader(f.getPath());
                BufferedReader br = new BufferedReader(fi);
                pack=br.readLine();
                //System.out.println(System.getProperty("user.dir")+"/config.txt");
                //System.out.println(pack);
                l=0;
                ct=0;

                int rowCount = model.getRowCount();
                //Remove rows one by one from the end of the table
                for (int i = rowCount - 1; i >= 0; i--) {
                  model.removeRow(i);
                }

                for(int i=0;i<pack.length();i++){

                  try{
                  String ctrStr = ctrfield.getText();
                  String rem = "";
                  String ch = "";
                  ctarr[l++]=Integer.parseInt(ctrStr);
                  psf=(Integer.parseInt(ps.getText()))/8+1;
                  if(pack.charAt(i)=='i'){
                    ch="INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                    rem = "Integer";
                  }
                  if(pack.charAt(i)=='s'){
                    ch="SHORT INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                    rem = "Integers(32-bit)";
                  }
                  if(pack.charAt(i)=='l'){
                    ch ="LONG INT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                    rem = "Integers(64-bit)";
                  }
                  if(pack.charAt(i)=='o'){
                    ch ="BYTE";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+8*ctarr[l-1]));
                    rem = "Integer(8-bit)";
                  }
                  if(pack.charAt(i)=='c'){
                    ch ="CHAR";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+16*ctarr[l-1]));
                    rem = "Character";
                  }
                  if(pack.charAt(i)=='f'){
                    ch ="FLOAT";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+32*ctarr[l-1]));
                    rem = "Floating point";
                  }

                  if(pack.charAt(i)=='d'){
                    ch ="DOUBLE";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+64*ctarr[l-1]));
                    rem = "Float(High Precision)";

                  }
                  if(pack.charAt(i)=='b'){
                    ch ="BOOL";
                    ps.setText(Integer.toString(Integer.parseInt(ps.getText())+1*ctarr[l-1]));
                    rem = "True or False";

                  }
                 // System.out.println(pack);
                  pst=(Integer.parseInt(ps.getText())-1)/8+1;
                  //disp.append("["+ch+"*"+ctrStr+"]");
                  model.addRow(new Object[]{Integer.toString(++ct),ch,Integer.toString(psf),Integer.toString(pst),rem});
                }
                  catch(Exception e1){
                  e1.printStackTrace();}

                }
            //    disp.setText(br.readLine());
            //    ps.setText(br.readLine());
                br.close();
              }
                catch(FileNotFoundException fn){
                  JOptionPane.showMessageDialog(null, "Config File not present!");
                }catch(IOException ioe){}

               }
            }
          });

          exportB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent push10){
              JFileChooser fc=new JFileChooser();
              int j=fc.showOpenDialog(importB);
              if(j==JFileChooser.APPROVE_OPTION){

                File f=fc.getSelectedFile();
              //  System.out.println(f.getPath());

                try{
                File setting = new File(f.getPath());
                FileWriter fw= new FileWriter(setting);
                fw.write(pack+"\n");
                fw.close();
              }catch(IOException ioe){}

              }
            }
          });

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
