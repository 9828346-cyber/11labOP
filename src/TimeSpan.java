import java.util.Objects;

public class TimeSpan {
    private int hours;
    private int minutes;

    public TimeSpan() {
        this.hours = 0;
        this.minutes = 0;
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин не може бути від'ємною: " + minutes
            );
        }
        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    public TimeSpan(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    private void validateHoursAndMinutes(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    String.format("Некоректний інтервал часу: %d год. %d хв. (години >= 0, 0 <= хвилини <= 59)", hours, minutes)
            );
        }
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public double getTotalHours() {
        return hours + minutes / 60.0;
    }

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    public void add(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        int totalMinutes = getTotalMinutes() + hours * 60 + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин для додавання не може бути від'ємною: " + minutes
            );
        }
        int totalMinutes = getTotalMinutes() + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    public void add(TimeSpan timeSpan) {
        if (timeSpan == null) {
            throw new IllegalInputTimeSpanException("Об'єкт TimeSpan не може бути null");
        }
        add(timeSpan.getHours(), timeSpan.getMinutes());
    }

    public void subtract(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        int resultMinutes = getTotalMinutes() - (hours * 60 + minutes);
        if (resultMinutes < 0) {
            throw new NegativeTimeSpanException(
                    "Результат віднімання дає від'ємний інтервал часу (" + resultMinutes + " хв.)"
            );
        }
        this.hours = resultMinutes / 60;
        this.minutes = resultMinutes % 60;
    }

    public void subtract(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин для віднімання не може бути від'ємною: " + minutes
            );
        }
        int resultMinutes = getTotalMinutes() - minutes;
        if (resultMinutes < 0) {
            throw new NegativeTimeSpanException(
                    "Результат віднімання дає від'ємний інтервал часу (" + resultMinutes + " хв.)"
            );
        }
        this.hours = resultMinutes / 60;
        this.minutes = resultMinutes % 60;
    }

    public void subtract(TimeSpan timeSpan) {
        if (timeSpan == null) {
            throw new IllegalInputTimeSpanException("Об'єкт TimeSpan не може бути null");
        }
        subtract(timeSpan.getHours(), timeSpan.getMinutes());
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException(
                    "Множник масштабування має бути додатним цілим числом, отримано: " + factor
            );
        }
        int totalMinutes = getTotalMinutes() * factor;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    @Override
    public String toString() {
        return hours + "h " + minutes + "m";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSpan)) return false;
        TimeSpan timeSpan = (TimeSpan) o;
        return hours == timeSpan.hours && minutes == timeSpan.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }
}