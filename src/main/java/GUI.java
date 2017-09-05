import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;

public class GUI extends JFrame implements Thread.UncaughtExceptionHandler, ActionListener {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final String WINDOW_TITLE = "Weather";
    private final JPanel infoJPanel = new JPanel(new GridLayout(7, 2));
    private final JPanel inputJPanel = new JPanel(new GridLayout(1, 2));
    private final JTextField cityField = new JTextField();
    private final JTextField cityIdField = new JTextField();
    private final JTextField lonField = new JTextField();
    private final JTextField latField = new JTextField();
    private final JTextField sunriseField = new JTextField();
    private final JTextField sunsetField = new JTextField();
    private final JTextField countryField = new JTextField();
    private final JTextField temperatureField = new JTextField();
    private final JTextField humidityField = new JTextField();
    private final JTextField pressureField = new JTextField();
    private final JTextField windDirection = new JTextField();
    private final JTextField windSpeedField = new JTextField();
    private final JTextField cloudsField = new JTextField();
    private final JTextField lastUpdateField = new JTextField();
    private final JTextField textFieldCity = new JTextField("Krasnodar");
    private final JButton btnGet = new JButton("Get");

    GUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(WINDOW_TITLE);
        setVisible(true);

        inputJPanel.add(textFieldCity);
        inputJPanel.add(btnGet);
        textFieldCity.addActionListener(this);
        btnGet.addActionListener(this);
        add(inputJPanel, BorderLayout.NORTH);

        assembleInfoPanel();
        add(infoJPanel, BorderLayout.CENTER);
        getCityWeather("Krasnodar");
    }

    private void assembleInfoPanel() {
        infoJPanel.add(cityField);
        cityField.setEditable(false);

        infoJPanel.add(cityIdField);
        cityIdField.setEditable(false);

        infoJPanel.add(latField);
        latField.setEditable(false);

        infoJPanel.add(lonField);
        lonField.setEditable(false);

        infoJPanel.add(sunriseField);
        sunriseField.setEditable(false);

        infoJPanel.add(sunsetField);
        sunsetField.setEditable(false);

        infoJPanel.add(countryField);
        countryField.setEditable(false);

        infoJPanel.add(temperatureField);
        temperatureField.setEditable(false);

        infoJPanel.add(humidityField);
        humidityField.setEditable(false);

        infoJPanel.add(pressureField);
        pressureField.setEditable(false);

        infoJPanel.add(windDirection);
        windDirection.setEditable(false);

        infoJPanel.add(windSpeedField);
        windSpeedField.setEditable(false);

        infoJPanel.add(cloudsField);
        cloudsField.setEditable(false);

        infoJPanel.add(lastUpdateField);
        lastUpdateField.setEditable(false);
    }

    private void fillTheInfoPanel(Weather weather) {
        cityField.setText("City: " + weather.getCity());
        cityIdField.setText("City ID: " + weather.getCityId());
        latField.setText("Latitude: " + weather.getLat());
        lonField.setText("Longitude: " + weather.getLon());
        sunriseField.setText("Sunrise: " + weather.getSunrise());
        sunsetField.setText("Sunset: " + weather.getSunset());
        countryField.setText("Country zone: " + weather.getCountry());
        temperatureField.setText("temperature: " + weather.getTemperature());
        humidityField.setText("humidity: " + weather.getHumidity());
        pressureField.setText("pressure: " + weather.getPressure());
        windDirection.setText("Wind Direction: " + weather.getWindDirection());
        windSpeedField.setText("Wind Speed: " + weather.getWindSpeed());
        cloudsField.setText("clouds: " + weather.getClouds());
        lastUpdateField.setText("Last Update: " + weather.getLastupdate());
    }


    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String msg;
        if (stackTraceElements.length == 0) msg = "Пустой stackTraceElements";
        else msg = e.getClass().getCanonicalName() + ": " + e.getMessage() + " \n" +
                stackTraceElements[0].toString();
        JOptionPane.showMessageDialog(null, msg, "Exception:", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnGet || src == textFieldCity) {
            getCityWeather(textFieldCity.getText());
        }  else throw new RuntimeException("Неизвестный src = " + src);
    }

    private void getCityWeather(String city) {
        try {
            fillTheInfoPanel(getWeather(city));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных: " + e, "Ошибка получения данных о городе '" + city + "'", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Weather getWeather(String city) throws Exception {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + ",ru&units=metric&mode=xml&appid=6d0f23a5071298a2af64c8245db45058");
        InputStream inputStream = url.openStream();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        return Parser.parse(document);
    }
}
