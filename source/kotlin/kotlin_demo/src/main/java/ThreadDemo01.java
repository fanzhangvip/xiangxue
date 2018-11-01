
/**
 * [线程间通信]写两个线程，一个线程打印1-52，另一个线程打印字母A-Z。打印 顺序为12A34B56C……5152Z>
 * <p>
 * 1、创建两个线程实现Runnable接口重写run方法，一个用于打印数字，一个用于打印字母。
 * <p>
 * 2、创建一个测试类，在测试类中创建一个Object类的对象（作为两个线程的共享资源，以便实现线程间的通信），通过各类的构造方法传递过去。
 * <p>
 * 3、在两个类的run方法中都要用synchronized保证同步，即加锁。
 * <p>
 * 4、在数字类中用for循环每打印两个数字就唤醒其他线程，释放锁，进入阻塞状态。
 * <p>
 * 在字母类中每打印一个字母就唤醒其他线程，释放锁，进入阻塞状态。
 * <p>
 * 在写这个程序的时候有几点要注意的地方：
 * <p>
 * 1、两个线程要使用同一个资源才需相互通信，所以在测试类中创建共享资源，并通过构造方法分别传到各线程类中。
 * <p>
 * 2、两个线程哪个先运行（执行start（））哪个就先获得资源并执行
 * <p>
 * 3、在run方法体内写进程间的通信wait（）和notifyall（）时，一定要先写notifyall（）再写wait（）。
 * <p>
 * 原因：当你先写wait()时，本进程也进入休眠状态，再写notifyall（）唤醒所有线程时本线程以及其他线程被一块唤醒，竞争同一个资源，就会造成死锁。
 * <p>
 * 所以一定要先唤醒其他线程，再让本线程阻塞！
 *
 *
 */
public class ThreadDemo01 {


    static class NumberTask implements Runnable {
        private Object lock;//声明一个类的引用

        private Flag flag;

        public NumberTask(Object obj,Flag flag) {
            this.lock = obj;    //通过构造器将共享的资源-->对象传进来
            this.flag = flag;
        }

        @Override
        public void run() {
            synchronized (lock) {//给共享资源上锁
                for (int i = 1; i < 53; i++) {
                    System.out.print(i);
                    if (i % 2 == 0) {
                        this.flag.value = true;
                        lock.notifyAll();//唤醒其他线程

                        try {
                            lock.wait();//等待并释放锁
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (i == 52) {
                        System.out.println("");
                    }
                }
            }

        }

//        @Override
//        public synchronized void run() {
//            synchronized (lock) {//给共享资源上锁
//                for (int i = 1; i < 53; i++) {
//                    System.out.print(i);
//                    if (i % 2 == 0) {
//                        lock.notifyAll();//唤醒其他线程
//
//                        try {
//                            lock.wait();//等待并释放锁
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                    if (i == 52) {
//                        System.out.println("");
//                    }
//                }
//            }
//
//        }

    }

    static class Flag{
        boolean value = false;
    }

    static class CharTask implements Runnable {
        private Object lock;
        private Flag flag;
        public CharTask(Object obj,Flag flag) {
            this.lock = obj;
            this.flag = flag;
        }

        @Override
        public void run() {
            synchronized (lock) {
//                System.out.println("flag: " + this.flag.value);
                while(!this.flag.value){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("flag: " + this.flag.value);
                }
                for (int i = 0; i < 26; i++) {
                    System.out.print((char) (i + 'A'));
                    lock.notifyAll();//唤醒其他线程
                    try {
                        lock.wait();//释放锁等待
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

//        @Override
//        public synchronized void run() {
//            synchronized (lock) {
//                for (int i = 0; i < 26; i++) {
//                    System.out.print((char) (i + 'A'));
//                    lock.notifyAll();//唤醒其他线程
//                    try {
//                        lock.wait();//释放锁等待
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
    }

     static class ForRunnable implements Runnable {

        private int i = 0;

        public ForRunnable(int i){
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println("ThreadName: " + Thread.currentThread().getName() + " i: " + i);
        }
    };


    public static void main(String... args) throws InterruptedException {
        final Object lock = new Object();
        Flag flag = new Flag();
        NumberTask numberTask = new NumberTask(lock,flag);
        CharTask charTask = new CharTask(lock,flag);

//        for (int i = 0; i < 100; i++) {
//            Thread th1 = new Thread(numberTask);
//            Thread th2 = new Thread(charTask);
//            th1.start();//数字的线程先运行，数字先执行
//            th2.start();
//        }

        Thread th1 = new Thread(numberTask);
        Thread th2 = new Thread(charTask);
        th2.start();
        Thread.sleep(2000);
        th1.start();//数字的线程先运行，数字先执行

//		th2.start();
//		th1.start();字母的线程先运行，则先执行字母
//

//        for(int i = 0 ; i < 100; i ++){
//            Thread t = new Thread(new ForRunnable(i),"thread-"+ i);
//            t.start();
//        }

    }

}
