package com.zxkj.common.practice.mode.behavior.responsibility;

public abstract class Handler<T> {
    private Handler<T> nextHandler;

    /**
     * 当前节点能处理就直接处理完毕后跳出，否则找到下一级节点同样逻辑处理
     */
    public void doWorkOnce(Request<T> request) {
        do {
            if (this.filter(request)) {
                this.process(request);
                break;
            }
            if (this.nextHandler != null) {
                this.nextHandler.doWorkOnce(request);
                break;
            }
            System.out.println("No Handler can handle this request: " + request);
        } while (false);
    }

    /**
     * 当前节点先处理，若存下下一级节点就继续处理
     *
     * @param request
     */
    public void doWork(Request<T> request) {
        this.process(request);
        if (this.nextHandler != null) {
            this.nextHandler.doWork(request);
        }
    }

    protected abstract boolean filter(Request<T> request);

    protected abstract void process(Request<T> request);

    public static class Builder<T> {
        private Handler<T> mFirst;
        private Handler<T> mLast;

        public Handler.Builder addBuilder(Handler handler) {
            if (this.mFirst == null) {
                this.mFirst = this.mLast = handler;
            } else {
                this.mLast.nextHandler = handler;
                this.mLast = handler;
            }
            return this;
        }

        public Handler build() {
            return this.mFirst;
        }
    }

    public static class Request<T> {
        private T data;

        private Request(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }

        @Override
        public String toString() {
            return this.data.toString();
        }

        public static <T> Handler.Request<T> build(T t) {
            return new Handler.Request<T>(t) {
            };
        }
    }
}