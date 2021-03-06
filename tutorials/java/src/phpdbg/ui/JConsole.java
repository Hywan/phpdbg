package phpdbg.ui;


import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_UP;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author krakjoe
 */
public class JConsole extends javax.swing.JDialog {
    /**
     * Creates user interface
     * @param parent
     * @param modal
     */
    public JConsole(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stdoutPopupMenu = new javax.swing.JPopupMenu();
        resetStdout = new javax.swing.JMenuItem();
        systrayMenu = new java.awt.PopupMenu();
        systrayExitMenuItem = new java.awt.MenuItem();
        host = new javax.swing.JTextField();
        stdoutPort = new javax.swing.JTextField();
        stdinCheckBox = new javax.swing.JCheckBox();
        stdoutCheckBox = new javax.swing.JCheckBox();
        openButton = new javax.swing.JButton();
        stdinPort = new javax.swing.JTextField();
        hostnameLabel = new javax.swing.JLabel();
        input = new javax.swing.JTextField();
        outputScrollPane = new javax.swing.JScrollPane();
        output = new phpdbg.ui.JTerminalPane();
        echoCheckBox = new javax.swing.JCheckBox();
        commandLabel = new javax.swing.JLabel();

        resetStdout.setText("Clear");
        resetStdout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetStdoutActionPerformed(evt);
            }
        });
        stdoutPopupMenu.add(resetStdout);

        systrayMenu.setLabel("phpdbg");
        systrayMenu.setName("");
        systrayMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systrayMenuActionPerformed(evt);
            }
        });

        systrayExitMenuItem.setLabel("Exit");
        systrayExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systrayExitMenuItemActionPerformed(evt);
            }
        });
        systrayMenu.add(systrayExitMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("phpdbg jui");

        host.setText("127.0.0.1");
        host.setToolTipText("Set the hostname, or IPv4 address of the machine running the phpdbg remote console server");

        stdoutPort.setText("8000");
        stdoutPort.setToolTipText("By default, stdin * 2");

        stdinCheckBox.setSelected(true);
        stdinCheckBox.setText("stdin:");
        stdinCheckBox.setToolTipText("Set the port for stdin, or uncheck to disable stdin");

        stdoutCheckBox.setSelected(true);
        stdoutCheckBox.setText("stdout:");
        stdoutCheckBox.setToolTipText("Set the port for stdout, or unset to disable stdout");

        openButton.setText("Connect");
        openButton.setActionCommand("open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        stdinPort.setText("4000");
        stdinPort.setToolTipText("The listen port passed to phpdbg (-l option)");

        hostnameLabel.setText("Hostname:");

        input.setToolTipText("Enter phpdbg commands here!");
        input.setEnabled(false);
        input.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputKeyReleased(evt);
            }
        });

        output.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        output.setComponentPopupMenu(stdoutPopupMenu);
        outputScrollPane.setViewportView(output);

        echoCheckBox.setSelected(true);
        echoCheckBox.setToolTipText("Check to echo sent commands in output");
        echoCheckBox.setEnabled(false);
        echoCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        echoCheckBox.setLabel("echo");

        commandLabel.setText("Command:");
        commandLabel.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hostnameLabel)
                            .addComponent(commandLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(input)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(echoCheckBox))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(host, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stdinCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stdinPort, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(stdoutCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stdoutPort, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(openButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(echoCheckBox)
                    .addComponent(commandLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(stdoutPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(stdinCheckBox)
                        .addComponent(stdoutCheckBox)
                        .addComponent(stdinPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hostnameLabel)
                        .addComponent(host, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(openButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputKeyReleased
        switch (evt.getKeyCode()) {
            case VK_ENTER: {
                if (in != null) {
                    history.add(input.getText());
                    synchronized(in) {
                        in.notifyAll();
                    }
                }
            } break;
 
            case VK_UP: {
                String last = history.last();
                if (last.length() > 0) {
                    input.setText(last);
                }
            } break;
                
            case VK_DOWN: {
                String next = history.next();
                if (next.length() > 0) {
                    input.setText(next);
                }
            } break;
        }
    }//GEN-LAST:event_inputKeyReleased

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        try {
            if (!connected) {
                connect();
            } else {
                disconnect();
            }
        } catch (IOException ex) {
            messageBox(ex.getMessage(), MessageType.ERROR);
        }
    }//GEN-LAST:event_openButtonActionPerformed

    private void resetStdoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetStdoutActionPerformed
        // TODO add your handling code here:
        output.setText(null);
    }//GEN-LAST:event_resetStdoutActionPerformed

    private void systrayExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systrayExitMenuItemActionPerformed
        // TODO add your handling code here:
        dialog.disconnect();
        System.exit(0);
    }//GEN-LAST:event_systrayExitMenuItemActionPerformed

    private void systrayMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systrayMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_systrayMenuActionPerformed
    
    private void disconnect() {
        if (in != null) {
            in.quit();
        }
        if (out != null) {
            out.quit();
        }
    }
    
    private void connect() throws IOException {
        Integer ports[] = new Integer[2];
        String address = getHost();

        if (address != null) {
            ports[0] = stdinCheckBox.isSelected() ? getStdinPort() : -1;
            ports[1] = stdoutCheckBox.isSelected() ? getStdoutPort() : -1;

            if (ports[0] != 0 && ports[1] != 0) {
                if (stdinCheckBox.isSelected()) {
                    if (ports[0] > 0) {
                        in = new DebugSocket(
                                address, ports[0], this, true);
                        in.start();
                    }
                }

                if (stdoutCheckBox.isSelected()) {
                    if (ports[1] > 0) {
                        out = new DebugSocket(
                               address, ports[1], this, false);
                        out.start();
                    }
                }
            }
        }
    }
    
    public Boolean isConnected() {
        return connected;
    }
    
    public Boolean isEchoing() {
        return echoCheckBox.isSelected();
    }
    
    public void setConnected(Boolean isConnected) {
        synchronized(this) {
            if (isConnected) {
                connected = true;
                openButton.setText("Disconnect");
                host.setEnabled(false);
                stdinPort.setEnabled(false);
                stdinCheckBox.setEnabled(false);
                if (stdinCheckBox.isSelected()) {
                    input.setEnabled(true);
                    commandLabel.setEnabled(true);
                    echoCheckBox.setEnabled(true);
                } else {
                    input.setEnabled(false);
                    commandLabel.setEnabled(false);
                    echoCheckBox.setEnabled(false);
                }
                stdoutPort.setEnabled(false);
                stdoutCheckBox.setEnabled(false);
                hostnameLabel.setEnabled(false);
                commandLabel.setEnabled(true);
            } else {
                connected = false;
                openButton.setText("Connect");
                host.setEnabled(true);
                stdinPort.setEnabled(true);
                input.setEnabled(false);
                commandLabel.setEnabled(false);
                echoCheckBox.setEnabled(false);
                stdinCheckBox.setEnabled(true);
                stdoutPort.setEnabled(true);
                stdoutCheckBox.setEnabled(true);
                hostnameLabel.setEnabled(true);
            }
        }
    }
    
    public JTextField getInputField() { return input; }
    public JTerminalPane getOutputField() { return output; }
    
    public String getHost() { 
        String address = host.getText();
        if (address != null && address.length() > 0) {
            return address;
        } else {
            messageBox("Invalid hostname provided!", MessageType.WARN);
        }
        
        return null;
    }
    
    public Integer getStdinPort() { 
        try {
            return Integer.parseInt(stdinPort.getText()); 
        } catch (NumberFormatException ex) {
            messageBox("Invalid stdin port provided!", MessageType.WARN);
        }
        return 0;
    }
    
    public Integer getStdoutPort() { 
        try {
            return Integer.parseInt(stdoutPort.getText()); 
        } catch (NumberFormatException ex) {
            messageBox("Invalid stdout port provided!", MessageType.WARN);
        }
        return 0;
    }
    
    public JScrollPane getOutputScrollPane() {
        return outputScrollPane;
    }
    
    public synchronized void messageBox(String message) {
        messageBox(message, MessageType.INFO);
    }
    
    public synchronized void messageBox(String message, MessageType type) {
        JOptionPane.showMessageDialog(this, message, "phpdbg jui", type.getType());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
 
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(
                            info.getClassName());
                    break;
                } catch (ClassNotFoundException | 
                        InstantiationException | 
                        IllegalAccessException | 
                        UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(JConsole.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                dialog = new JConsole(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.disconnect();
                        System.exit(0);
                    }
                });
                try {
                    if (tray == null) {
                        Image trayIconImage = ImageIO.read(
                            JConsole.class.getResource("logo-dbg.png"));
                        dialog.setIconImage(trayIconImage);

                        tray = new TrayIcon(trayIconImage);
                        tray.setPopupMenu(systrayMenu);
                        tray.setToolTip("phpdbg - The Interactive PHP Debugger");

                        SystemTray.getSystemTray().add(tray);
                    } 
                } catch ( AWTException | IOException ex) {
                    dialog.messageBox(ex.getMessage(), MessageType.ERROR);
                }
                dialog.setVisible(true);
            }
        });
    }
    
    private static DebugSocket in;
    private static DebugSocket out;
    private static JConsole dialog;
    private static Boolean connected = false;
    private static CommandHistory history = new CommandHistory();
    private static TrayIcon tray;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel commandLabel;
    private javax.swing.JCheckBox echoCheckBox;
    private javax.swing.JTextField host;
    private javax.swing.JLabel hostnameLabel;
    private javax.swing.JTextField input;
    private javax.swing.JButton openButton;
    private phpdbg.ui.JTerminalPane output;
    private javax.swing.JScrollPane outputScrollPane;
    private javax.swing.JMenuItem resetStdout;
    private javax.swing.JCheckBox stdinCheckBox;
    private javax.swing.JTextField stdinPort;
    private javax.swing.JCheckBox stdoutCheckBox;
    private javax.swing.JPopupMenu stdoutPopupMenu;
    private javax.swing.JTextField stdoutPort;
    private java.awt.MenuItem systrayExitMenuItem;
    private static java.awt.PopupMenu systrayMenu;
    // End of variables declaration//GEN-END:variables
    public enum MessageType {
        INFO (JOptionPane.INFORMATION_MESSAGE),
        WARN (JOptionPane.WARNING_MESSAGE),
        ERROR (JOptionPane.ERROR_MESSAGE);
        
        private final Integer type;
        private MessageType(Integer type) {
            this.type = type;
        }
        public Integer getType() { return this.type; }
        public Boolean equals(Integer other)  { return this.type.equals(other); }
        public Boolean equals(MessageType other)  { return this.type.equals(other.getType()); }
    }
}
