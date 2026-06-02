package com.captchasonic.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.72.0)",
    comments = "Source: captchasonic/v1/sonic.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SonicServiceGrpc {

  private SonicServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "captchasonic.v1.SonicService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.captchasonic.v1.CreateTaskRequest,
      com.captchasonic.v1.CreateTaskResponse> getCreateTaskMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateTask",
      requestType = com.captchasonic.v1.CreateTaskRequest.class,
      responseType = com.captchasonic.v1.CreateTaskResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.captchasonic.v1.CreateTaskRequest,
      com.captchasonic.v1.CreateTaskResponse> getCreateTaskMethod() {
    io.grpc.MethodDescriptor<com.captchasonic.v1.CreateTaskRequest, com.captchasonic.v1.CreateTaskResponse> getCreateTaskMethod;
    if ((getCreateTaskMethod = SonicServiceGrpc.getCreateTaskMethod) == null) {
      synchronized (SonicServiceGrpc.class) {
        if ((getCreateTaskMethod = SonicServiceGrpc.getCreateTaskMethod) == null) {
          SonicServiceGrpc.getCreateTaskMethod = getCreateTaskMethod =
              io.grpc.MethodDescriptor.<com.captchasonic.v1.CreateTaskRequest, com.captchasonic.v1.CreateTaskResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateTask"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.CreateTaskRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.CreateTaskResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SonicServiceMethodDescriptorSupplier("CreateTask"))
              .build();
        }
      }
    }
    return getCreateTaskMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.captchasonic.v1.GetTaskResultRequest,
      com.captchasonic.v1.GetTaskResultResponse> getGetTaskResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTaskResult",
      requestType = com.captchasonic.v1.GetTaskResultRequest.class,
      responseType = com.captchasonic.v1.GetTaskResultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.captchasonic.v1.GetTaskResultRequest,
      com.captchasonic.v1.GetTaskResultResponse> getGetTaskResultMethod() {
    io.grpc.MethodDescriptor<com.captchasonic.v1.GetTaskResultRequest, com.captchasonic.v1.GetTaskResultResponse> getGetTaskResultMethod;
    if ((getGetTaskResultMethod = SonicServiceGrpc.getGetTaskResultMethod) == null) {
      synchronized (SonicServiceGrpc.class) {
        if ((getGetTaskResultMethod = SonicServiceGrpc.getGetTaskResultMethod) == null) {
          SonicServiceGrpc.getGetTaskResultMethod = getGetTaskResultMethod =
              io.grpc.MethodDescriptor.<com.captchasonic.v1.GetTaskResultRequest, com.captchasonic.v1.GetTaskResultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTaskResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.GetTaskResultRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.GetTaskResultResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SonicServiceMethodDescriptorSupplier("GetTaskResult"))
              .build();
        }
      }
    }
    return getGetTaskResultMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.captchasonic.v1.GetBalanceRequest,
      com.captchasonic.v1.GetBalanceResponse> getGetBalanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBalance",
      requestType = com.captchasonic.v1.GetBalanceRequest.class,
      responseType = com.captchasonic.v1.GetBalanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.captchasonic.v1.GetBalanceRequest,
      com.captchasonic.v1.GetBalanceResponse> getGetBalanceMethod() {
    io.grpc.MethodDescriptor<com.captchasonic.v1.GetBalanceRequest, com.captchasonic.v1.GetBalanceResponse> getGetBalanceMethod;
    if ((getGetBalanceMethod = SonicServiceGrpc.getGetBalanceMethod) == null) {
      synchronized (SonicServiceGrpc.class) {
        if ((getGetBalanceMethod = SonicServiceGrpc.getGetBalanceMethod) == null) {
          SonicServiceGrpc.getGetBalanceMethod = getGetBalanceMethod =
              io.grpc.MethodDescriptor.<com.captchasonic.v1.GetBalanceRequest, com.captchasonic.v1.GetBalanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBalance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.GetBalanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.GetBalanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SonicServiceMethodDescriptorSupplier("GetBalance"))
              .build();
        }
      }
    }
    return getGetBalanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.captchasonic.v1.HealthCheckRequest,
      com.captchasonic.v1.HealthCheckResponse> getHealthCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HealthCheck",
      requestType = com.captchasonic.v1.HealthCheckRequest.class,
      responseType = com.captchasonic.v1.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.captchasonic.v1.HealthCheckRequest,
      com.captchasonic.v1.HealthCheckResponse> getHealthCheckMethod() {
    io.grpc.MethodDescriptor<com.captchasonic.v1.HealthCheckRequest, com.captchasonic.v1.HealthCheckResponse> getHealthCheckMethod;
    if ((getHealthCheckMethod = SonicServiceGrpc.getHealthCheckMethod) == null) {
      synchronized (SonicServiceGrpc.class) {
        if ((getHealthCheckMethod = SonicServiceGrpc.getHealthCheckMethod) == null) {
          SonicServiceGrpc.getHealthCheckMethod = getHealthCheckMethod =
              io.grpc.MethodDescriptor.<com.captchasonic.v1.HealthCheckRequest, com.captchasonic.v1.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HealthCheck"))
              .setSafe(true)
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.captchasonic.v1.HealthCheckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SonicServiceMethodDescriptorSupplier("HealthCheck"))
              .build();
        }
      }
    }
    return getHealthCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SonicServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SonicServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SonicServiceStub>() {
        @java.lang.Override
        public SonicServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SonicServiceStub(channel, callOptions);
        }
      };
    return SonicServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static SonicServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SonicServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SonicServiceBlockingV2Stub>() {
        @java.lang.Override
        public SonicServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SonicServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return SonicServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SonicServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SonicServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SonicServiceBlockingStub>() {
        @java.lang.Override
        public SonicServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SonicServiceBlockingStub(channel, callOptions);
        }
      };
    return SonicServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SonicServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SonicServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SonicServiceFutureStub>() {
        @java.lang.Override
        public SonicServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SonicServiceFutureStub(channel, callOptions);
        }
      };
    return SonicServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createTask(com.captchasonic.v1.CreateTaskRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.CreateTaskResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateTaskMethod(), responseObserver);
    }

    /**
     */
    default void getTaskResult(com.captchasonic.v1.GetTaskResultRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.GetTaskResultResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTaskResultMethod(), responseObserver);
    }

    /**
     */
    default void getBalance(com.captchasonic.v1.GetBalanceRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.GetBalanceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBalanceMethod(), responseObserver);
    }

    /**
     */
    default void healthCheck(com.captchasonic.v1.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHealthCheckMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SonicService.
   */
  public static abstract class SonicServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SonicServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SonicService.
   */
  public static final class SonicServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SonicServiceStub> {
    private SonicServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SonicServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SonicServiceStub(channel, callOptions);
    }

    /**
     */
    public void createTask(com.captchasonic.v1.CreateTaskRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.CreateTaskResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateTaskMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTaskResult(com.captchasonic.v1.GetTaskResultRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.GetTaskResultResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTaskResultMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBalance(com.captchasonic.v1.GetBalanceRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.GetBalanceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void healthCheck(com.captchasonic.v1.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.captchasonic.v1.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SonicService.
   */
  public static final class SonicServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<SonicServiceBlockingV2Stub> {
    private SonicServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SonicServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SonicServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.captchasonic.v1.CreateTaskResponse createTask(com.captchasonic.v1.CreateTaskRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTaskMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.GetTaskResultResponse getTaskResult(com.captchasonic.v1.GetTaskResultRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTaskResultMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.GetBalanceResponse getBalance(com.captchasonic.v1.GetBalanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBalanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.HealthCheckResponse healthCheck(com.captchasonic.v1.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHealthCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service SonicService.
   */
  public static final class SonicServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SonicServiceBlockingStub> {
    private SonicServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SonicServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SonicServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.captchasonic.v1.CreateTaskResponse createTask(com.captchasonic.v1.CreateTaskRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTaskMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.GetTaskResultResponse getTaskResult(com.captchasonic.v1.GetTaskResultRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTaskResultMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.GetBalanceResponse getBalance(com.captchasonic.v1.GetBalanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBalanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.captchasonic.v1.HealthCheckResponse healthCheck(com.captchasonic.v1.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHealthCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SonicService.
   */
  public static final class SonicServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SonicServiceFutureStub> {
    private SonicServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SonicServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SonicServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.captchasonic.v1.CreateTaskResponse> createTask(
        com.captchasonic.v1.CreateTaskRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateTaskMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.captchasonic.v1.GetTaskResultResponse> getTaskResult(
        com.captchasonic.v1.GetTaskResultRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTaskResultMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.captchasonic.v1.GetBalanceResponse> getBalance(
        com.captchasonic.v1.GetBalanceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.captchasonic.v1.HealthCheckResponse> healthCheck(
        com.captchasonic.v1.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_TASK = 0;
  private static final int METHODID_GET_TASK_RESULT = 1;
  private static final int METHODID_GET_BALANCE = 2;
  private static final int METHODID_HEALTH_CHECK = 3;

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
        case METHODID_CREATE_TASK:
          serviceImpl.createTask((com.captchasonic.v1.CreateTaskRequest) request,
              (io.grpc.stub.StreamObserver<com.captchasonic.v1.CreateTaskResponse>) responseObserver);
          break;
        case METHODID_GET_TASK_RESULT:
          serviceImpl.getTaskResult((com.captchasonic.v1.GetTaskResultRequest) request,
              (io.grpc.stub.StreamObserver<com.captchasonic.v1.GetTaskResultResponse>) responseObserver);
          break;
        case METHODID_GET_BALANCE:
          serviceImpl.getBalance((com.captchasonic.v1.GetBalanceRequest) request,
              (io.grpc.stub.StreamObserver<com.captchasonic.v1.GetBalanceResponse>) responseObserver);
          break;
        case METHODID_HEALTH_CHECK:
          serviceImpl.healthCheck((com.captchasonic.v1.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.captchasonic.v1.HealthCheckResponse>) responseObserver);
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
          getCreateTaskMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.captchasonic.v1.CreateTaskRequest,
              com.captchasonic.v1.CreateTaskResponse>(
                service, METHODID_CREATE_TASK)))
        .addMethod(
          getGetTaskResultMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.captchasonic.v1.GetTaskResultRequest,
              com.captchasonic.v1.GetTaskResultResponse>(
                service, METHODID_GET_TASK_RESULT)))
        .addMethod(
          getGetBalanceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.captchasonic.v1.GetBalanceRequest,
              com.captchasonic.v1.GetBalanceResponse>(
                service, METHODID_GET_BALANCE)))
        .addMethod(
          getHealthCheckMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.captchasonic.v1.HealthCheckRequest,
              com.captchasonic.v1.HealthCheckResponse>(
                service, METHODID_HEALTH_CHECK)))
        .build();
  }

  private static abstract class SonicServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SonicServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.captchasonic.v1.SonicProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SonicService");
    }
  }

  private static final class SonicServiceFileDescriptorSupplier
      extends SonicServiceBaseDescriptorSupplier {
    SonicServiceFileDescriptorSupplier() {}
  }

  private static final class SonicServiceMethodDescriptorSupplier
      extends SonicServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SonicServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SonicServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SonicServiceFileDescriptorSupplier())
              .addMethod(getCreateTaskMethod())
              .addMethod(getGetTaskResultMethod())
              .addMethod(getGetBalanceMethod())
              .addMethod(getHealthCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
