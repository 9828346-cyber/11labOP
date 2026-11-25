public class TimeSpanDemo {
    public static void main(String[] args) {
        try {
            TimeSpan valid = new TimeSpan(2, 30);
            valid.add(45);
            System.out.println("Після додавання: " + valid); // 3h 15m

            valid.scale(2);
            System.out.println("Після масштабування: " + valid); // 6h 30m

            // 1. Спроба створити некоректний інтервал (хвилини > 59)
            new TimeSpan(1, 75);
        } catch (IllegalInputTimeSpanException e) {
            System.err.println("Помилка вхідних даних: " + e.getMessage());
        }

        try {
            // 2. Спроба отримати від'ємний інтервал при відніманні
            TimeSpan t1 = new TimeSpan(1, 20);
            t1.subtract(2, 0);
        } catch (NegativeTimeSpanException e) {
            System.err.println("Від'ємний інтервал: " + e.getMessage());
        }

        try {
            // 3. Некоректний множник у scale
            TimeSpan t2 = new TimeSpan(1, 15);
            t2.scale(0);
        } catch (IllegalScaleFactorException e) {
            System.err.println("Помилка масштабування: " + e.getMessage());
        }
    }
}