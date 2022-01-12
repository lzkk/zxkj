package com.zxkj.common.practice.mode.behavior.responsibility;

import java.util.ArrayList;
import java.util.List;

class Client2 {
    public static void main(String[] args) {
        Handler.Builder<String> builder = new Handler.Builder<String>();
        List<Handler<String>> handlers = makeHanlders();
        for (Handler<String> handler : handlers) {
            builder.addHandler(handler);
        }
        Handler<String> firstHandler = builder.build();
        for (int i = 0; i < 25; ++i) {
            System.out.println("-----------------");
            Request<String> request = makeRequest(String.format("%03d", i));
            firstHandler.handleRequest(request);
        }
    }

    private static List<Handler<String>> makeHanlders() {
        List<Handler<String>> handlers = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            String request = String.format("%03d", i);
            handlers.add(makeHandler("handler" + request, makeRequest(request)));
        }
        return handlers;
    }

    private static Request<String> makeRequest(String request) {
        return new Request<String>(request) {
            @Override
            public boolean isSame(Request<String> request) {
                return this.getRequest().equals(request.getRequest());
            }
        };
    }

    private static Handler<String> makeHandler(final String handlerName, Request<String> request) {
        return new Handler<String>(request) {
            @Override
            protected void handle(Request<String> request) {
                System.out.println(String.format("%s deal with request: %s", handlerName, request));
            }
        };
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

        public abstract boolean isSame(Client2.Request<T> request);
    }

    // 抽象处理者
    static abstract class Handler<T> {
        private Client2.Handler<T> mNextHandler;
        private Client2.Request<T> mRequest;

        public Handler(Client2.Request<T> request) {
            this.mRequest = request;
        }

        public void setNextHandler(Client2.Handler<T> successor) {
            this.mNextHandler = successor;
        }

        public final void handleRequest(Client2.Request<T> request) {
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

        protected abstract void handle(Client2.Request<T> request);

        public static class Builder<T> {
            private Handler<T> mFirst;
            private Handler<T> mLast;

            public Builder<T> addHandler(Handler<T> handler) {
                do {
                    if (this.mFirst == null) {
                        this.mFirst = this.mLast = handler;
                        break;
                    }
                    this.mLast.setNextHandler(handler);
                    this.mLast = handler;

                } while (false);
                return this;
            }

            public Handler<T> build() {
                return this.mFirst;
            }
        }
    }
}