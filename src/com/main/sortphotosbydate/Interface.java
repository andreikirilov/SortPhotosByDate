package com.main.sortphotosbydate;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

public class Interface extends JFrame {

    static JTextArea jTextArea;
    static JTextField jTextField;
    static JProgressBar jProgressBar;

    private Interface() {
        super("Сортировка по дате съемки для любимой жены Надежды!");
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/resources/camera.png")));
        } catch (IOException ignored) {
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(824, 700));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        UIManager.put("FileChooser.lookInLabelText", "Директория");
        UIManager.put("FileChooser.folderNameLabelText", "Путь директории");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.cancelButtonText", "Нет, подождите...");
        UIManager.put("OptionPane.okButtonText", "Да, погнали!");
        Dimension mainDimension = new Dimension(150, 24);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(15);

        JPanel panel0 = new JPanel();
        panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder,
                "Выберите папку для сортировки в формате \"ММ.ДД.ГГ\"", 2, 2);
        panel0.setBorder(titledBorder);

        JPanel panel1 = new JPanel(flowLayout);
        JButton selectButton = new JButton("Выбрать папку");
        selectButton.setPreferredSize(mainDimension);
        panel1.add(selectButton);
        jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(600, 24));
        jTextField.setText("Необходимо указать путь к папке!");
        jTextField.setEditable(false);
        panel1.add(jTextField);

        JPanel panel2 = new JPanel(flowLayout);
        JCheckBox subfolder = new JCheckBox("Заходить в подпапки");
        subfolder.setSelected(true);
        panel2.add(subfolder);

        JPanel panel3 = new JPanel(flowLayout);
        JButton startButton = new JButton("Запустить");
        startButton.setPreferredSize(mainDimension);
        startButton.setEnabled(false);
        panel3.add(startButton);
        jProgressBar = new JProgressBar();
        jProgressBar.setPreferredSize(new Dimension(435, 24));
        jProgressBar.setStringPainted(true);
        jProgressBar.setMaximum(100);
        jProgressBar.setMinimum(0);
        panel3.add(jProgressBar);
        JButton exitButton = new JButton("Выйти");
        exitButton.setPreferredSize(mainDimension);
        panel3.add(exitButton);

        JPanel panel4 = new JPanel(flowLayout);
        jTextArea = new JTextArea();
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        panel4.add(jTextArea);
        JScrollPane jScrollPane = new JScrollPane(jTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setPreferredSize(new Dimension(766, 500));
        panel4.add(jScrollPane);

        panel0.add(panel1);
        panel0.add(panel2);
        panel0.add(panel3);
        panel0.add(panel4);
        getContentPane().add(panel0);

        selectButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setCurrentDirectory(new File("D:\\"));
            jFileChooser.setAcceptAllFileFilterUsed(false);
            int selected = jFileChooser.showDialog(null, "Выбрать папку");
            if (selected == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                jTextField.setText(file.getAbsolutePath());
                startButton.setEnabled(true);
            }
        });
        subfolder.addItemListener(e -> SortPhotosByDate.subfolder = e.getStateChange() == ItemEvent.SELECTED);
        startButton.addActionListener(e -> {
            int selected = JOptionPane.showConfirmDialog(null, "Наведем порядок в папке: \"" + jTextField.getText() + "\"?",
                    "Изменения необратимы!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (selected == JOptionPane.OK_OPTION) {
                new MySwingWorker().execute();
            }
        });
        exitButton.addActionListener(e -> dispose());
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Interface::new);
    }
}