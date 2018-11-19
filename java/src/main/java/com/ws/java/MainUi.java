package com.ws.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 * 类名称：MainUi
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-19 17:19
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
public class MainUi extends JFrame implements ActionListener {

    //定义组件
    JButton jb1, jb2, jb3 = null;
    JRadioButton jrb1, jrb2 = null;
    JPanel jp1, jp2, jp3, jp4 = null;
    JTextField jtf = null;
    JLabel jlb1, jlb2, jlb3 = null;
    JPasswordField jpf = null;
    ButtonGroup bg = null;
    //设定用户名和密码
    final String stu_name = "王小明";
    final String stu_pwd = "1";
    final String stu_num = "14140301";
    final String tea_name = "王老师";
    final String tea_pwd = "1";
    final String tea_num = "00001";

    public MainUi() {
        //创建组件
        jb1 = new JButton("登录");
        jb2 = new JButton("重置");
        jb3 = new JButton("退出");

        //设置监听
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);

        jrb1 = new JRadioButton("教师");
        jrb2 = new JRadioButton("学生");
        bg = new ButtonGroup();
        bg.add(jrb1);
        bg.add(jrb2);
        jrb2.setSelected(true);  //初始页面默认选择权限为 学生

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();

        jlb1 = new JLabel("用户名：");
        jlb2 = new JLabel("密    码：");
        jlb3 = new JLabel("权    限：");

        jtf = new JTextField(10);
        jpf = new JPasswordField(10);
        //加入到JPanel中
        jp1.add(jlb1);
        jp1.add(jtf);

        jp2.add(jlb2);
        jp2.add(jpf);

        jp3.add(jlb3);      //添加标签
        jp3.add(jrb1);
        jp3.add(jrb2);

        jp4.add(jb1);       //添加按钮
        jp4.add(jb2);
        jp4.add(jb3);

        //加入JFrame中
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);

        this.setLayout(new GridLayout(4, 1));            //选择GridLayout布局管理器
        this.setTitle("学生成绩管理系统");
        this.setSize(300, 200);
        this.setLocation(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置当关闭窗口时，保证JVM也退出
        this.setVisible(true);
        this.setResizable(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
