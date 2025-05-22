package airport;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new AirportFrame().setVisible(true);
        });
    }
}
