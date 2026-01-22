package com.gcs.game.utils; /**
 * @(#)JNI.java 使用JNI方式调用DLL
 */

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchProxy;

/**
 * 使用JaCoB实现RNG调用
 *
 * @author Jiangqx (修改为JNA实现)
 * @version 1.0
 * @since 1.0
 */
public class JacobRng implements AutoCloseable {

    /**
     * 定义DLL接口
     */
    private static final Object INIT_LOCK = new Object();

    private ActiveXComponent rng;
    private Dispatch rngDispatch;

    public DispatchProxy sCon = null;
    private volatile boolean initialized = false;
    private volatile boolean closed = false;

    /**
     * 默认构造函数
     */
    public JacobRng() {
        initialize();
    }

    private void initialize() {
        if (initialized) {
            return;
        }

        synchronized (INIT_LOCK) {
            if (initialized) {
                return;
            }

            try {
                ComThread.InitSTA();
                this.rng = new ActiveXComponent("MV_RNG.RNG");
                this.rngDispatch = rng.getObject();
                this.initialized = true;
                this.closed = false;
            } catch (Exception e) {
                // 初始化失败时释放COM线程
                ComThread.Release();
                throw new IllegalStateException("Failed to initialize RNG", e);
            }
        }
    }


    /**
     * 快速随机数生成
     */
    public long computeRandom() {
        return computeRandom(5000);
    }


    /**
     * 生成指定范围内的随机数
     *
     * @param max 最大值
     * @return 随机数
     */
    public long computeRandom(int max) {
        this.initialize();
        checkState();
        if (max <= 0) {
            String errorMsg = String.format("Max value must be positive, got: %d", max);
            throw new IllegalArgumentException("Max value must be positive, got: " + max);
        }
        try {
            int result = Dispatch.call(this.rngDispatch, "GetRandomNumber", max, false).getInt();
            return (long) result - 1;
        } catch (Exception e) {
            // 错误处理
            System.err.println("Failed to generate random number: " + e.getMessage());
        }
        return 0;
    }

    /**
     * 检查RNG状态
     */
    private void checkState() {
        if (closed) {
            throw new IllegalStateException("RNG is closed");
        }

        if (!initialized || rngDispatch == null) {
            throw new IllegalStateException("RNG not initialized properly");
        }
    }

    /**
     * 测试随机数生成功能
     */
    public static void main(String[] args) throws Exception {
        args = args.length == 0 ? new String[]{"10"} : args;
        int max = args.length > 1 ? 1000 : 5000;
        max = 10;

        long t1 = System.currentTimeMillis();
        try (JacobRng rng = new JacobRng()) {
            for (int i = 0; i < Integer.valueOf(args[0]).intValue(); ++i) {
                long l = rng.computeRandom(max);
                System.out.println((i + 1) + "\t" + l + "\t" + max);
            }
        }
        System.out.println("耗时: " + (System.currentTimeMillis() - t1) + "ms");
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }
        ComThread.Release();
        Exception error = null;
        rng = null;
        rngDispatch = null;
        closed = true;
        initialized = false;
        try {
            if (rng != null) {
                rng.safeRelease();
            }
        } catch (Exception e) {
            error = e;
        } finally {
            if (error != null) {
                throw new RuntimeException("Failed to close RNG properly", error);
            }
        }
    }

    public boolean isInitialized() {
        return initialized && !closed;
    }

}
