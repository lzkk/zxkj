package com.zxkj.common.practice.mode.behavior.responsibility;

class Client1 {
    public static void main(String[] args) {
        // 来一个具体处理者A
        Handler<String> handlerA = new ConcreteHandlerA(new ConcreteRequest("requestA"));
        // 来一个具体处理者B
        Handler<String> handlerB = new ConcreteHandlerB(new ConcreteRequest("requestB"));
        // A的下一节点为B
        handlerA.setNextHandler(handlerB);

        // 来一个消息：requestA
        Request<String> request = new ConcreteRequest("requestA");
        // 将消息交由具体处理者A
        handlerA.handleRequest(request);

        System.out.println("------------------------");

        // 来一个消息：requestB
        request = new ConcreteRequest("requestB");
        // 将消息交由具体处理者A
        handlerA.handleRequest(request);

        System.out.println("------------------------");

        // 来一个消息：requestC
        request = new ConcreteRequest("requestC");
        // 将消息交由具体处理者A
        handlerA.handleRequest(request);
    }

    // 抽象消息
    static abstract class Request<T> {
        private T mRequest;

        public Request(T request) {
            this.mRequest = request;
        }

        public T getRequest() {
            return this.mRequest;
        }

        @Override
        public String toString() {
            return this.mRequest.toString();
        }

        public abstract boolean isSame(Request<T> request);
    }

    // 具体消息
    static class ConcreteRequest extends Request<String> {

        public ConcreteRequest(String request) {
            super(request);
        }

        @Override
        public boolean isSame(Request<String> request) {
            return this.getRequest().equals(request.getRequest());
        }
    }

    // 抽象处理者
    static abstract class Handler<T> {
        private Handler<T> mNextHandler;
        private Request<T> mRequest;

        public Handler(Request<T> request) {
            this.mRequest = request;
        }

        public void setNextHandler(Handler<T> successor) {
            this.mNextHandler = successor;
        }

        public final void handleRequest(Request<T> request) {
            do {
                if (this.mRequest.isSame(request)) {
                    this.handle(request);
                    break;
                }
                if (this.mNextHandler != null) {
                    this.mNextHandler.handleRequest(request);
                    break;
                }
                System.out.println("No Handlers can handle this request: " + request);
            } while (false);
        }

        protected abstract void handle(Request<T> request);

    }

    // 具体处理者A
    static class ConcreteHandlerA extends Handler<String> {

        public ConcreteHandlerA(Request<String> request) {
            super(request);
        }

        @Override
        protected void handle(Request<String> request) {
            System.out.println(String.format("%s deal with request: %s", this.getClass().getSimpleName(), request));
        }
    }

    // 具体处理者B
    static class ConcreteHandlerB extends Handler<String> {

        public ConcreteHandlerB(Request<String> request) {
            super(request);
        }

        @Override
        protected void handle(Request<String> request) {
            System.out.println(String.format("%s deal with request: %s", this.getClass().getSimpleName(), request));
        }
    }
}