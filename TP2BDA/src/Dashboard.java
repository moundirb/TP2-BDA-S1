import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Dashboard {
    private JPanel panel1;
    private JRadioButton rb2;
    private JButton jb1;
    private JRadioButton rb3;
    private JRadioButton rb4;
    private JRadioButton rb5;
    private JRadioButton rb6;
    private JRadioButton rb7;
    private JRadioButton rb8;
    private JRadioButton rb9;
    private JTable table1;
    private JRadioButton rb1;
    private JLabel lable1;

    private Connection connection;
    private ButtonGroup buttonGroup;

    public Dashboard() {
        buttonGroup = new ButtonGroup();

        // Add radio buttons to the group
        buttonGroup.add(rb1);
        buttonGroup.add(rb2);
        buttonGroup.add(rb3);
        buttonGroup.add(rb4);
        buttonGroup.add(rb5);
        buttonGroup.add(rb6);
        buttonGroup.add(rb7);
        buttonGroup.add(rb8);
        buttonGroup.add(rb9);
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click event
                if (rb1.isSelected()) {
                    executeQuery("SELECT COUNT(*) FROM country");
                } else if (rb2.isSelected()) {
                    executeQuery("SELECT country.Name AS CountryName, city.Name AS CapitalName, countrylanguage.Language\n" + "FROM country\n" + "JOIN city ON country.Capital = city.ID\n" + "JOIN countrylanguage ON country.Code = countrylanguage.CountryCode;\n");
                } else if (rb3.isSelected()) {
                    executeQuery("SELECT Name FROM city WHERE CountryCode = 'DZA'");
                } else if (rb4.isSelected()) {
                    executeQuery("SELECT Name FROM city WHERE CountryCode = 'DZA' ORDER BY Population ASC LIMIT 1");
                } else if (rb5.isSelected()) {
                    executeQuery("SELECT Language FROM countrylanguage ORDER BY Percentage DESC LIMIT 1");
                } else if (rb6.isSelected()) {
                    executeQuery("SELECT Name FROM country WHERE LifeExpectancy < 50");
                } else if (rb7.isSelected()) {
                    executeQuery("SELECT Continent, COUNT(*) AS count FROM country GROUP BY Continent ORDER BY count DESC LIMIT 1");
                } else if (rb8.isSelected()) {
                    executeQuery("SELECT city.Name\n" +
                            "FROM city\n" +
                            "JOIN country ON city.CountryCode = country.Code\n" +
                            "WHERE country.Capital <> city.ID AND country.Continent = 'Asia';\n");
                } else if (rb9.isSelected()) {
                    executeQuery("SELECT AVG(Population) FROM country WHERE Continent = 'Europe'");
                }
            }

        });

        // Initialize database connection
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/world",
                    "root",
                    ""
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeQuery(String sql) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            // Assuming you have a DefaultTableModel for your table
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Add columns to the model
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                model.addColumn(metaData.getColumnName(column));
            }

            // Add rows to the model
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    row[column - 1] = resultSet.getObject(column);
                }
                model.addRow(row);
            }

            // Set the model to the table
            table1.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard");
        frame.setContentPane(new Dashboard().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
