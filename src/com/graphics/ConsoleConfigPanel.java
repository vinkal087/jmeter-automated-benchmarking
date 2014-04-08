package com.graphics;


 
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
 
/**
 * Stub for displaying Configuration item.
 * 
 * @author andre
 * 
 */
public class ConsoleConfigPanel extends JFrame {
 
  private static final long serialVersionUID = 1L;
  private JPanel mainPane;
  private PropertiesConfiguration properties;
  private PropertiesConfiguration labels;
  private HashMap<String, JTextField> textFields;
  private static final Logger log = Logger.getLogger(ConsoleConfigPanel.class);
  String propertyFile;
 
  /**
   * This is the default constructor
   * 
   * @param propertiesConfiguration
   * @throws ConfigurationException
   */
  public ConsoleConfigPanel(PropertiesConfiguration propertiesConfiguration,String propertyFile) {
    super();
    this.properties = propertiesConfiguration;
    this.propertyFile=propertyFile;
    try {
      this.labels = new PropertiesConfiguration("abc.properties");
    } catch (Exception e) {
      log.warn("Label definitions not found.", e);
    }
    initialize();
 
  }
 
  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    mainPane = new JPanel();
    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
    mainPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    this.textFields = new HashMap<String, JTextField>();
    JPanel configEntry = new JPanel();
    configEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
    configEntry.setLayout(new BoxLayout(configEntry, BoxLayout.PAGE_AXIS));
 
    for (Object key : IteratorUtils.toList(properties.getKeys())) {
      String strKey = (String) key;
      JLabel label = new JLabel(label(strKey));
      label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      label.setAlignmentX(LEFT_ALIGNMENT);
      mainPane.add(label);
      JTextField textField = new JTextField(render(properties.getProperty(strKey)));
      textField.setColumns(30);
      textField.setAlignmentX(LEFT_ALIGNMENT);
      textFields.put(strKey, textField);
      mainPane.add(textField);
    }
 
    JPanel buttonPanel = new JPanel();
    buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
    buttonPanel.add(Box.createHorizontalGlue());
    JButton okButton = new JButton("Next");
    okButton.addActionListener(new ActionListener() {
 
      public void actionPerformed(ActionEvent e) {
        saveAndClose();
        if(propertyFile.contains("VMDetails")){
        	PropertiesConfiguration p;
			try {
				p = new PropertiesConfiguration("resource/Jmeter.properties");
				new ConsoleConfigPanel(p,"Jmeter");
			} catch (ConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        }
        else if(propertyFile.contains("Jmeter")){
        	PropertiesConfiguration p;
			try {
				p = new PropertiesConfiguration("resource/Remote.properties");
				new ConsoleConfigPanel(p,"Remote");
			} catch (ConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        else if(propertyFile.contains("Remote")){
        	
        }
      }
    });
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
 
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });
 
    buttonPanel.add(cancelButton);
    buttonPanel.add(okButton);
    mainPane.add(buttonPanel);
 
    setSize(500, 500);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setContentPane(mainPane);
    setResizable(false);
    pack();
    setVisible(true);
  }
 
  private String label(String strKey) {
    if (labels != null && labels.containsKey(strKey)) {
      return labels.getString(strKey) + ":";
    } else {
      return strKey + ":";
    }
  }
 
  private String render(Object object) {
    if (object instanceof List) {
      String raw = object.toString();
      return raw.substring(1, raw.length() - 1);
    }
    return object.toString();
  }
 
  private void saveAndClose() {
 
    for (Entry<String, JTextField> entry : textFields.entrySet()) {
      properties.setProperty(entry.getKey(), entry.getValue().getText());
    }
    try {
      properties.save();
    } catch (Exception e) {
      log.warn("Could not save properties", e);
    }
    close();
  }
 
  private void close() {
    this.setVisible(false);
    this.dispose();
  }
  
  public static void main(String[] args) throws ConfigurationException {
	PropertiesConfiguration p=new PropertiesConfiguration("resource/VMDetails.properties");
	new ConsoleConfigPanel(p,"VMDetails");
	
}
 
}