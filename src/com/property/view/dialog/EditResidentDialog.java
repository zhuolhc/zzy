package com.property.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.property.model.Resident;
import com.property.dao.ResidentDAO;

public class EditResidentDialog extends JDialog {
    private boolean confirmed = false;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField prepayAmountField;
    private JTextField arrearsAmountField;
    private Resident resident;
    
    public EditResidentDialog(JFrame parent, String residentId) {
        super(parent, "编辑住户", true);
        
        // 获取住户信息
        ResidentDAO residentDAO = new ResidentDAO();
        this.resident = residentDAO.getResidentById(residentId);
        
        if (this.resident == null) {
            throw new IllegalArgumentException("找不到指定的住户");
        }
        
        initComponents();
        loadResidentData();
        setSize(400, 500);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        // 组件初始化代码与AddResidentDialog基本相同
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ... 添加所有组件 ...
        
        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("姓名："), gbc);
        
        nameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        
        // 电话
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("电话："), gbc);
        
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // 地址
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("地址："), gbc);
        
        addressField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(addressField, gbc);
        
        // 预付金额
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("预付金额："), gbc);
        
        prepayAmountField = new JTextField("0.0", 20);
        gbc.gridx = 1;
        mainPanel.add(prepayAmountField, gbc);
        
        // 欠费金额
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("欠费金额："), gbc);
        
        arrearsAmountField = new JTextField("0.0", 20);
        gbc.gridx = 1;
        mainPanel.add(arrearsAmountField, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                updateResident();
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    // 加载住户数据到表单
    private void loadResidentData() {
        nameField.setText(resident.getName());
        phoneField.setText(resident.getPhone());
        addressField.setText(resident.getAddress());
        prepayAmountField.setText(String.valueOf(resident.getPrepayAmount()));
        arrearsAmountField.setText(String.valueOf(resident.getArrearsAmount()));
    }
    
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入姓名！");
            return false;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入电话！");
            return false;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入地址！");
            return false;
        }
        
        try {
            double prepayAmount = Double.parseDouble(prepayAmountField.getText().trim());
            if (prepayAmount < 0) {
                JOptionPane.showMessageDialog(this, "预付金额不能为负数！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的预付金额！");
            return false;
        }
        
        try {
            double arrearsAmount = Double.parseDouble(arrearsAmountField.getText().trim());
            if (arrearsAmount < 0) {
                JOptionPane.showMessageDialog(this, "欠费金额不能为负数！");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的欠费金额！");
            return false;
        }
        
        return true;
    }
    
    private void updateResident() {
        resident.setName(nameField.getText().trim());
        resident.setPhone(phoneField.getText().trim());
        resident.setAddress(addressField.getText().trim());
        resident.setPrepayAmount(Double.parseDouble(prepayAmountField.getText().trim()));
        resident.setArrearsAmount(Double.parseDouble(arrearsAmountField.getText().trim()));
        
        ResidentDAO residentDAO = new ResidentDAO();
        residentDAO.updateResident(resident);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
} 