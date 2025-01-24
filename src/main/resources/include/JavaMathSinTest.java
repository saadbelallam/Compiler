public class JavaMathSinTest {

    public static void main(String[] args) {
        double angle;
        double sinValue;

        // Test pour angle = 0
        angle = 0.0;
        sinValue = Math.sin(angle);
        printResult("0.0", sinValue);

        // Test pour angle = PI
        angle = Math.PI;
        sinValue = Math.sin(angle);
        printResult("PI", sinValue);

        // Test pour angle = PI/6
        angle = Math.PI / 6;
        sinValue = Math.sin(angle);
        printResult("PI/6", sinValue);

        // Test pour angle = PI/4
        angle = Math.PI / 4;
        sinValue = Math.sin(angle);
        printResult("PI/4", sinValue);

        // Test pour angle = PI/2
        angle = Math.PI / 2;
        sinValue = Math.sin(angle);
        printResult("PI/2", sinValue);

        // Test pour angle = -PI/4
        angle = -Math.PI / 4;
        sinValue = Math.sin(angle);
        printResult("-PI/4", sinValue);

        // Test pour angle = 3*PI/2
        angle = 3 * Math.PI / 2;
        sinValue = Math.sin(angle);
        printResult("3*PI/2", sinValue);

        // Test pour angle = -3*PI/2
        angle = -3 * Math.PI / 2;
        sinValue = Math.sin(angle);
        printResult("-3*PI/2", sinValue);

        // Test pour un angle très grand (exemple : 2^15)
        angle = Math.pow(2, 15); // Très grand angle
        sinValue = Math.sin(angle);
        printResult("2^15", sinValue);

        // Test pour un angle très petit (exemple : 2^-15)
        angle = Math.pow(2, -15); // Très petit angle
        sinValue = Math.sin(angle);
        printResult("2^-15", sinValue);

        // Fin des tests
        System.out.println("Tests terminés.");
    }

    private static void printResult(String angleLabel, double sinValue) {
        System.out.printf("Angle: %s | Math.sin: %.15f%n", angleLabel, sinValue);
    }
}
