import java.time.LocalTime;

public class DemoApp{
    public static void main(String[] args){
        MessageQueue<String> queue = new MessageQueue<>(5);
        Runnable producer=()->{
            String name = Thread.currentThread().getName();
            try{
                for (int i = 1; i <= 5; i++) {
                    String msg = name + " -> Message " + i;
                    queue.put(msg);
                    log("Produced: " + msg);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Runnable consumer = () -> {
            String name = Thread.currentThread().getName();
            try {
                while (true) {
                    String msg = queue.take();
                    log(name + " consumed: " + msg);
                    Thread.sleep(600);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        for (int i = 1; i <= 2; i++) {
            new Thread(producer, "Producer-" + i).start();
        }
        for (int i = 1; i <= 2; i++) {
            new Thread(consumer, "Consumer-" + i).start();
        }
    }
    private static void log(String msg) {
        System.out.printf("[%s] %s%n", LocalTime.now(), msg);
    }
}