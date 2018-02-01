/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smshen;

import com.smshen.utils.PDM;
import com.smshen.utils.PDMColumn;
import com.smshen.utils.PDMTable;
import com.smshen.utils.Parser;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author ice
 */
public class ContactEditorUI extends javax.swing.JFrame {

    private javax.swing.JMenu jMenuAbout;
    private javax.swing.JMenu jmenuFile;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jmengFileOpen;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JScrollPane jScrollPaneLeft;
    private javax.swing.JScrollPane jScrollPaneRight;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;

    public ContactEditorUI() {
        initComponents();
    }

    private void initComponents() {

        jScrollPaneLeft = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPaneRight = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmenuFile = new javax.swing.JMenu();
        jmengFileOpen = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuAbout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        jScrollPaneLeft.setViewportView(jTree1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        jScrollPaneRight.setViewportView(jTable1);

        jmenuFile.setText("文件");

        jmengFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jmengFileOpen.setText("打开");
        jmengFileOpen.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // 文件打开事件
                chooserFile(evt);
            }
        });
        jmenuFile.add(jmengFileOpen);

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemExit.setText("退出");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jmenuFile.add(jMenuItemExit);

        jMenuBar1.add(jmenuFile);

        jMenuAbout.setText("关于");
        jMenuBar1.add(jMenuAbout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneLeft, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPaneRight, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPaneLeft)
                        .addComponent(jScrollPaneRight, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 选择文件
     *
     * @param evt
     */
    private void chooserFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JFileChooser jfc = new JFileChooser();
        //添加过滤
        FileNameExtensionFilter ff = new FileNameExtensionFilter(null, "pdm");
        jfc.setFileFilter(ff);

        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        Properties properties = System.getProperties();
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            System.out.println("key:" + key + " , value:" + properties.getProperty(key));
        }
        String workingdir = System.getProperty("user.home");
        jfc.setCurrentDirectory(new File(workingdir));

        File file = null;
        if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(this)) {
            file = jfc.getSelectedFile();
            try {
                PDM p = new Parser().pdmParser(file.getPath());
                DefaultMutableTreeNode top = new DefaultMutableTreeNode("表");


                for (PDMTable t : p.getTables()) {
                    System.out.println("table-->" + t.getName() + ", code-->" + t.getCode());
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(t);
                    top.add(child);
                }
                jTree1.setModel(new DefaultTreeModel(top));
                jTree1.addTreeSelectionListener(new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1
                                .getLastSelectedPathComponent();

                        if (node == null) {
                            return;
                        }

                        Object object = node.getUserObject();
                        if (node.isLeaf()) {
                            PDMTable pdmt = (PDMTable) object;
                            ArrayList<PDMColumn> cols = pdmt.getColumns();
                            String[] columnNames = {"名称", "CODE", "数据类型", "备注"};
                            cols.trimToSize();
                            Object[][] data = new Object[cols.size()][columnNames.length];

                            int i = 0;
                            for (PDMColumn col : cols) {
                                data[i][0] = col.getName();
                                data[i][1] = col.getCode();
                                data[i][2] = col.getDataType();
                                data[i][3] = col.getComment();
                                i++;
                            }
                            int s = Frame.MAXIMIZED_BOTH;
                            TableModel dataMode = new DefaultTableModel(data, columnNames);
                            jTable1.setModel(dataMode);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactEditorUI().setVisible(true);
            }
        });
    }


}
