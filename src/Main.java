import java.time.LocalDateTime;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private static final Double START_SUM = 50.0;
    private static final Integer START_PRICE = 40;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

        trade();

    }

    private static void trade() {
        SortedMap<LocalDateTime, Double> history = new TreeMap<>();
        Double currentSum = START_SUM;

        while (true) {
            Double currentPrice = generatePrice();
            Double oldPrice;
            if (history.isEmpty()) {
                oldPrice = currentPrice;
            } else {
                oldPrice = history.get(history.lastKey());
            }
            history.put(LocalDateTime.now(), currentPrice);
            if (currentPrice < oldPrice || currentPrice > oldPrice) {
                if (checkSum(currentSum, currentPrice)) {
                    Integer active = 0;
                    if (isFutureIncrease(history, currentPrice)) {
                        currentSum = buy(currentSum, currentPrice, active);
                    } else if (isFutureDecrease(history, currentPrice)) {
                        currentSum = sell(currentSum, currentPrice, active);
                    }
                }
            }
            System.out.println("Price = " + currentPrice);
            System.out.println("Sum = " + currentSum);
            if (currentSum > 100.0) {
                System.out.println("мы богаты!!");
                System.exit(0);
            }
            if (currentSum < 30.0) {
                System.out.println("мы банкроты!");
                System.exit(0);
            }
        }
    }

    private static Double sell(Double sum, Double price, Integer active) {
        sum += price;
        active -= 1;
        return sum;
    }

    private static boolean isFutureDecrease(SortedMap<LocalDateTime, Double> history, Double currentPrice) {
        if (history.isEmpty() || history.size() == 1) {
            return false;
        } else {
            AtomicReference<Double> average = new AtomicReference<>(0.0);
            history.values().forEach(aDouble -> average.updateAndGet(v -> v + aDouble));
            return (average.get() / history.values().size()) < currentPrice;
        }
    }

    private static boolean isFutureIncrease(SortedMap<LocalDateTime, Double> history, Double currentPrice) {
        if (history.isEmpty() || history.size() == 1) {
            return false;
        } else {
            AtomicReference<Double> average = new AtomicReference<>(0.0);
            history.values().forEach(aDouble -> average.updateAndGet(v -> v + aDouble));
            return (average.get() / history.values().size()) > currentPrice;
        }
    }

    private static Double buy(Double sum, Double price, Integer active) {
        sum -= price;
        active += 1;
        return sum;
    }

    private static boolean checkSum(Double currentSum, Double currentPrice) {
        if (currentPrice >= currentSum) {
            return false;
        } else {
            return true;
        }
    }

    private static Double generatePrice() {
        return START_PRICE + RANDOM.nextInt(70 - START_PRICE + 1) + Math.random();
    }

}