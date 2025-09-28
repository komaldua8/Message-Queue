public class MessageQueue<T>{
    private final Object[] buffer;
    private int head=0;
    private int tail=0;
    private int size=0; //number of elements
    private final int capacity;

    public MessageQueue(int capacity){
        if(capacity<=0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity=capacity;
        this.buffer=new Object[capacity];
    }
    public synchronized void put(T msg) throws InterruptedException{
        while(size==capacity){
            wait();
        }
        buffer[tail]=msg;
        tail=(tail+1)%capacity;
        size++;
        notifyAll();
    }
    public synchronized T take() throws InterruptedException{
        while(size==0){
            wait();
        }
        T msg = (T) buffer[head];
        buffer[head]=null;
        head=(head+1)%capacity;
        size--;
        notifyAll();
        return msg;
    }
    public synchronized int size() {
        return size;
    }
}