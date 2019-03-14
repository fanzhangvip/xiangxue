package com.enjoy.zero.cachedemo.ZImage;

public class ImageLoaderConfig {

    /**
     * 图片显示配置
     */
    private DisplayConfig displayConfig;

    /**
     * 缓存策略
     */
    private Cache bitmapCache = new MemoryCache();

    /**
     * 加载策略
     */
    private RequestPolicy requestPolicy;

    /**
     * 默认线程数
     */
    private int threadCount = Runtime.getRuntime().availableProcessors();

    private ImageLoaderConfig(){
        displayConfig = new DisplayConfig();
    }

    /**
     * 建造者模式
     */
    public static class Builder{

        /**
         * Builder持有外部类的引用，在new的时候创建出来
         */
        private ImageLoaderConfig config;

        public Builder(){
            config = new ImageLoaderConfig();
        }

        /**
         * 设置缓存策略
         * @param bitmapCache
         * @return
         */
        public Builder setCachePolicy(Cache bitmapCache){
            config.bitmapCache = bitmapCache;
            return this;
        }

        /**
         * 设置加载策略
         * @param requestPolicy
         * @return
         */
        public Builder setRequestPolicy(RequestPolicy requestPolicy){
            config.requestPolicy = requestPolicy;
            return this;
        }

        /**
         * 设置线程数
         * @param threadCount
         * @return
         */
        public Builder setThreadCount(int threadCount){
            config.threadCount = threadCount;
            return this;
        }

        /**
         * 设置加载过程中的图片
         * @param resId
         * @return
         */
        public Builder setLoadingImage(int resId){
            if (config.displayConfig == null){
                throw new NullPointerException("you have not set DisplayConfig,DisplayConfig is null");
            }
            config.displayConfig.loadingImage = resId;
            return this;
        }

        /**
         * 设置加载失败显示的图片
         * @param resId
         * @return
         */
        public Builder setErrorImage(int resId){
            if (config.displayConfig == null){
                throw new NullPointerException("you have not set DisplayConfig,DisplayConfig is null");
            }
            config.displayConfig.errorImage = resId;
            return this;
        }



        /**
         * 构建
         * @return
         */
        public ImageLoaderConfig build(){
            return config;
        }
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public Cache getBitmapCache() {
        return bitmapCache;
    }

    public RequestPolicy getRequestPolicy() {
        return requestPolicy;
    }

    public int getThreadCount() {
        return threadCount;
    }


}
