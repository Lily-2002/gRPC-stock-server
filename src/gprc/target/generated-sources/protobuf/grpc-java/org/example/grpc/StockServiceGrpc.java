package org.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service definition
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.70.0)",
    comments = "Source: stock_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StockServiceGrpc {

  private StockServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "stockservice.StockService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.grpc.LookupRequest,
      org.example.grpc.LookupResponse> getLookupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Lookup",
      requestType = org.example.grpc.LookupRequest.class,
      responseType = org.example.grpc.LookupResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.LookupRequest,
      org.example.grpc.LookupResponse> getLookupMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.LookupRequest, org.example.grpc.LookupResponse> getLookupMethod;
    if ((getLookupMethod = StockServiceGrpc.getLookupMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getLookupMethod = StockServiceGrpc.getLookupMethod) == null) {
          StockServiceGrpc.getLookupMethod = getLookupMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.LookupRequest, org.example.grpc.LookupResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Lookup"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.LookupRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.LookupResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("Lookup"))
              .build();
        }
      }
    }
    return getLookupMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.TradeRequest,
      org.example.grpc.TradeResponse> getTradeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Trade",
      requestType = org.example.grpc.TradeRequest.class,
      responseType = org.example.grpc.TradeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.TradeRequest,
      org.example.grpc.TradeResponse> getTradeMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.TradeRequest, org.example.grpc.TradeResponse> getTradeMethod;
    if ((getTradeMethod = StockServiceGrpc.getTradeMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getTradeMethod = StockServiceGrpc.getTradeMethod) == null) {
          StockServiceGrpc.getTradeMethod = getTradeMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.TradeRequest, org.example.grpc.TradeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Trade"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.TradeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.TradeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("Trade"))
              .build();
        }
      }
    }
    return getTradeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.UpdateRequest,
      org.example.grpc.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Update",
      requestType = org.example.grpc.UpdateRequest.class,
      responseType = org.example.grpc.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.UpdateRequest,
      org.example.grpc.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.UpdateRequest, org.example.grpc.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = StockServiceGrpc.getUpdateMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getUpdateMethod = StockServiceGrpc.getUpdateMethod) == null) {
          StockServiceGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.UpdateRequest, org.example.grpc.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Update"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("Update"))
              .build();
        }
      }
    }
    return getUpdateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StockServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceStub>() {
        @java.lang.Override
        public StockServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceStub(channel, callOptions);
        }
      };
    return StockServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static StockServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingV2Stub>() {
        @java.lang.Override
        public StockServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return StockServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StockServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingStub>() {
        @java.lang.Override
        public StockServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceBlockingStub(channel, callOptions);
        }
      };
    return StockServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StockServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceFutureStub>() {
        @java.lang.Override
        public StockServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceFutureStub(channel, callOptions);
        }
      };
    return StockServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service definition
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Lookup stock price and volume
     * </pre>
     */
    default void lookup(org.example.grpc.LookupRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.LookupResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLookupMethod(), responseObserver);
    }

    /**
     * <pre>
     * Trade stocks (buy/sell)
     * </pre>
     */
    default void trade(org.example.grpc.TradeRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.TradeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTradeMethod(), responseObserver);
    }

    /**
     * <pre>
     * Update stock price
     * </pre>
     */
    default void update(org.example.grpc.UpdateRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.UpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StockService.
   * <pre>
   * Service definition
   * </pre>
   */
  public static abstract class StockServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StockServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StockService.
   * <pre>
   * Service definition
   * </pre>
   */
  public static final class StockServiceStub
      extends io.grpc.stub.AbstractAsyncStub<StockServiceStub> {
    private StockServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Lookup stock price and volume
     * </pre>
     */
    public void lookup(org.example.grpc.LookupRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.LookupResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLookupMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Trade stocks (buy/sell)
     * </pre>
     */
    public void trade(org.example.grpc.TradeRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.TradeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTradeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Update stock price
     * </pre>
     */
    public void update(org.example.grpc.UpdateRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.UpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StockService.
   * <pre>
   * Service definition
   * </pre>
   */
  public static final class StockServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<StockServiceBlockingV2Stub> {
    private StockServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Lookup stock price and volume
     * </pre>
     */
    public org.example.grpc.LookupResponse lookup(org.example.grpc.LookupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLookupMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Trade stocks (buy/sell)
     * </pre>
     */
    public org.example.grpc.TradeResponse trade(org.example.grpc.TradeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTradeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update stock price
     * </pre>
     */
    public org.example.grpc.UpdateResponse update(org.example.grpc.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service StockService.
   * <pre>
   * Service definition
   * </pre>
   */
  public static final class StockServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StockServiceBlockingStub> {
    private StockServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Lookup stock price and volume
     * </pre>
     */
    public org.example.grpc.LookupResponse lookup(org.example.grpc.LookupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLookupMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Trade stocks (buy/sell)
     * </pre>
     */
    public org.example.grpc.TradeResponse trade(org.example.grpc.TradeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTradeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update stock price
     * </pre>
     */
    public org.example.grpc.UpdateResponse update(org.example.grpc.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StockService.
   * <pre>
   * Service definition
   * </pre>
   */
  public static final class StockServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<StockServiceFutureStub> {
    private StockServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Lookup stock price and volume
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.LookupResponse> lookup(
        org.example.grpc.LookupRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLookupMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Trade stocks (buy/sell)
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.TradeResponse> trade(
        org.example.grpc.TradeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTradeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Update stock price
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.UpdateResponse> update(
        org.example.grpc.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOOKUP = 0;
  private static final int METHODID_TRADE = 1;
  private static final int METHODID_UPDATE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOOKUP:
          serviceImpl.lookup((org.example.grpc.LookupRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.LookupResponse>) responseObserver);
          break;
        case METHODID_TRADE:
          serviceImpl.trade((org.example.grpc.TradeRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.TradeResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((org.example.grpc.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.UpdateResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLookupMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.LookupRequest,
              org.example.grpc.LookupResponse>(
                service, METHODID_LOOKUP)))
        .addMethod(
          getTradeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.TradeRequest,
              org.example.grpc.TradeResponse>(
                service, METHODID_TRADE)))
        .addMethod(
          getUpdateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.UpdateRequest,
              org.example.grpc.UpdateResponse>(
                service, METHODID_UPDATE)))
        .build();
  }

  private static abstract class StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StockServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.grpc.StockServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StockService");
    }
  }

  private static final class StockServiceFileDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier {
    StockServiceFileDescriptorSupplier() {}
  }

  private static final class StockServiceMethodDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    StockServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StockServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StockServiceFileDescriptorSupplier())
              .addMethod(getLookupMethod())
              .addMethod(getTradeMethod())
              .addMethod(getUpdateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
