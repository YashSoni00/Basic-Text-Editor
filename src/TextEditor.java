import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TextEditor extends JFrame implements ActionListener {

    // Create the components of the JFrame
    JTextArea textArea; // A multi-line area that displays plain text
    JSpinner fontSizeSpinner; // A single-line input field that lets the user select a number from an ordered sequence
    JLabel sizeLabel; // A display area for a short text string
    JButton fontColorButton; // An implementation of a "push" button
    JScrollPane scrollPane; // A scrollable view of a component
    JComboBox<String> fontBox; // A component that combines a button or editable field and a drop-down list
    JLabel fontLabel; // A display area for a short text string

    // Design the menu bar
    JMenuBar menuBar;

    public TextEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500, 550);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());

        // Customizing the JTextArea object
        textArea = new JTextArea();
        textArea.setLineWrap(true); // Wrap text to next line
        textArea.setWrapStyleWord(true); // Wrap at word boundaries
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        // Customizing the JScrollPane object
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Customizing the Spinner object
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(e -> {
            textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
        });

        // Customizing the font label
        sizeLabel = new JLabel("Size:");

        // Customizing the font color label
        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(colorChooser, "Choose a color", Color.black);
            textArea.setForeground(color);
        });

        // Customizing the font box
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        fontBox.addActionListener(e -> {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        });

        // Customizing the font label
        fontLabel = new JLabel("Font:");

        // Customizing the menu bar
        menuBar = new JMenuBar();
        JMenu fileMenu =  createMenu.apply(menuBar, "File");
        JMenu editMenu =  createMenu.apply(menuBar, "Edit");
        JMenu viewMenu =  createMenu.apply(menuBar, "View");
        JMenu helpMenu =  createMenu.apply(menuBar, "About");

        // Customizing the menu items
        JMenuItem newMenuItem = createMenuItem.apply(fileMenu, "New");
        JMenuItem openMenuItem = createMenuItem.apply(fileMenu, "Open");
        JMenuItem saveMenuItem = createMenuItem.apply(fileMenu, "Save");
        JMenuItem exitMenuItem = createMenuItem.apply(fileMenu, "Exit");

        JMenuItem cutMenuItem = createMenuItem.apply(editMenu, "Cut");
        JMenuItem copyMenuItem = createMenuItem.apply(editMenu, "Copy");
        JMenuItem pasteMenuItem = createMenuItem.apply(editMenu, "Paste");

        JMenuItem zoomInMenuItem = createMenuItem.apply(viewMenu, "Zoom In");
        JMenuItem zoomOutMenuItem = createMenuItem.apply(viewMenu, "Zoom Out");

        JMenuItem aboutMenuItem = createMenuItem.apply(helpMenu, "About");

        // Add an action listener to the menu item
        newMenuItem.addActionListener(e -> textArea.setText(""));
        openMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            // Add Filter to show only .txt files
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
            fileChooser.setMultiSelectionEnabled(false);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textArea.setText("");
                try {
                    java.util.Scanner scanner = new java.util.Scanner(file);
                    while (scanner.hasNext()) {
                        textArea.append(scanner.nextLine() + "\n");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        saveMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    java.io.PrintWriter writer = new java.io.PrintWriter(file);
                    writer.println(textArea.getText());
                    writer.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        exitMenuItem.addActionListener(e -> System.exit(0));

        cutMenuItem.addActionListener(e -> textArea.cut());
        copyMenuItem.addActionListener(e -> textArea.copy());
        pasteMenuItem.addActionListener(e -> textArea.paste());

        zoomInMenuItem.addActionListener(e -> {
            Font font = textArea.getFont();
            float size = font.getSize() + 2.0f;
            textArea.setFont(font.deriveFont(size));
        });
        zoomOutMenuItem.addActionListener(e -> {
            Font font = textArea.getFont();
            float size = font.getSize() - 2.0f;
            textArea.setFont(font.deriveFont(size));
        });

        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "This is a simple text editor created using Java Swing"));

        // Add the components to the JFrame
        this.setIconImage(new ImageIcon("icon/text_editor.png").getImage());
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontBox);
        this.add(fontColorButton);
        this.add(sizeLabel);
        this.add(fontSizeSpinner);
        this.add(scrollPane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Auto-generated method stub
    }

    // Function to create menu
    BiFunction<JMenuBar, String, JMenu> createMenu = (menuBar, menuName) -> {
        JMenu menu = new JMenu(menuName);
        menuBar.add(menu);
        return menu;
    };

    // Function to create menu item
    BiFunction<JMenu, String, JMenuItem> createMenuItem = (menu, menuItemName) -> {
        JMenuItem menuItem = new JMenuItem(menuItemName);

        // Customizing the menu item
        menuItem.setOpaque(true);
        menuItem.setMnemonic(menuItemName.charAt(0));

        // Add the menu item to the menu
        menu.add(menuItem);
        return menuItem;
    };
}
