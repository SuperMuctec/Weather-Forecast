import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame{

    private JSONObject weatherData;
    public WeatherAppGui() throws IOException {
        setTitle("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        File logofile = new File("E:\\Java\\Weather_Forecast\\assets\\icon.png");
        Image logo = ImageIO.read(logofile);
        setIconImage(logo);


        addGuiComponents();
    }
    private void addGuiComponents() throws IOException {
        JTextField searchTextField = new JTextField();

        searchTextField.setBounds(15,15,351,45);
        searchTextField.setFont(new Font("Times New Roman",Font.PLAIN,24));

        add(searchTextField);

        JLabel weatherConditionImage = new JLabel(loadImage("E:\\Java\\Weather_Forecast\\assets\\cloudy.png"));
        weatherConditionImage.setBounds(8,125,450,217);
        add(weatherConditionImage);

        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Times New Roman",Font.BOLD, 48));

        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel weatherConditonDesc = new JLabel("Cloudy");
        weatherConditonDesc.setBounds(0,405,450,36);
        weatherConditonDesc.setFont(new Font("Times New Roman",Font.PLAIN, 32));
        weatherConditonDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditonDesc);

        JLabel humidityImage = new JLabel(loadImage("E:\\Java\\Weather_Forecast\\assets\\humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("E:\\Java\\Weather_Forecast\\assets\\windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Times New Roman",Font.PLAIN,16));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("E:\\Java\\Weather_Forecast\\assets\\search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();

                if(userInput.replaceAll("\\s","").length() <= 0){
                    return;
                }
                try {
                    weatherData = WeatherApp.getWeatherData(userInput);
                    String weatherCondition = (String) weatherData.get("weather_condition");

                    switch (weatherCondition){
                        case "Clear":
                            try {
                                weatherConditionImage.setIcon(loadImage("E:\\Java\\Weather_Forecast\\assets\\clear.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case "Cloudy":
                            try {
                                weatherConditionImage.setIcon(loadImage("E:\\Java\\Weather_Forecast\\assets\\cloudy.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case "Rain":
                            try {
                                weatherConditionImage.setIcon(loadImage("E:\\Java\\Weather_Forecast\\assets\\rain.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case "Snow":
                            try {
                                weatherConditionImage.setIcon(loadImage("E:\\Java\\Weather_Forecast\\assets\\snow.png"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                    }

                    double temperature = (double) weatherData.get("temperature");
                    temperatureText.setText(temperature + " °C");

                    weatherConditonDesc.setText(weatherCondition);

                    long humidity = (long) weatherData.get("humidity");
                    humidityText.setText("<html><b>Humidity</b> "+ humidity + "%</html>");

                    double windspeed = (double) weatherData.get("windspeed");
                    windspeedText.setText("<html><b>Humidity</b> "+ windspeed + "km/h</html>");


                }
                catch (Exception e1){
                    temperatureText.setText("\s");
                    weatherConditonDesc.setText("Please enter a valid place name");
                    humidityText.setText("\s");
                    windspeedText.setText("\s");
                    
                }

            }
        });
        add(searchButton);
    }
    private ImageIcon loadImage(String path) throws IOException {
        File file = new File(path);
        Image image = ImageIO.read(file);

        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
