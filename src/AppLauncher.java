import javax.swing.*;
import java.io.IOException;

public class AppLauncher {
    public static void main(String[] Args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new WeatherAppGui().setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
