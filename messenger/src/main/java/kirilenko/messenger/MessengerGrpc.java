package kirilenko.messenger;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: protocol.proto")
public final class MessengerGrpc {

  private MessengerGrpc() {}

  public static final String SERVICE_NAME = "Messenger";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<kirilenko.messenger.Message,
      kirilenko.messenger.Message> getSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendMessage",
      requestType = kirilenko.messenger.Message.class,
      responseType = kirilenko.messenger.Message.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<kirilenko.messenger.Message,
      kirilenko.messenger.Message> getSendMessageMethod() {
    io.grpc.MethodDescriptor<kirilenko.messenger.Message, kirilenko.messenger.Message> getSendMessageMethod;
    if ((getSendMessageMethod = MessengerGrpc.getSendMessageMethod) == null) {
      synchronized (MessengerGrpc.class) {
        if ((getSendMessageMethod = MessengerGrpc.getSendMessageMethod) == null) {
          MessengerGrpc.getSendMessageMethod = getSendMessageMethod = 
              io.grpc.MethodDescriptor.<kirilenko.messenger.Message, kirilenko.messenger.Message>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Messenger", "SendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kirilenko.messenger.Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kirilenko.messenger.Message.getDefaultInstance()))
                  .setSchemaDescriptor(new MessengerMethodDescriptorSupplier("SendMessage"))
                  .build();
          }
        }
     }
     return getSendMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MessengerStub newStub(io.grpc.Channel channel) {
    return new MessengerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MessengerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MessengerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MessengerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MessengerFutureStub(channel);
  }

  /**
   */
  public static abstract class MessengerImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendMessage(kirilenko.messenger.Message request,
        io.grpc.stub.StreamObserver<kirilenko.messenger.Message> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                kirilenko.messenger.Message,
                kirilenko.messenger.Message>(
                  this, METHODID_SEND_MESSAGE)))
          .build();
    }
  }

  /**
   */
  public static final class MessengerStub extends io.grpc.stub.AbstractStub<MessengerStub> {
    private MessengerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessengerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessengerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessengerStub(channel, callOptions);
    }

    /**
     */
    public void sendMessage(kirilenko.messenger.Message request,
        io.grpc.stub.StreamObserver<kirilenko.messenger.Message> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MessengerBlockingStub extends io.grpc.stub.AbstractStub<MessengerBlockingStub> {
    private MessengerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessengerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessengerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessengerBlockingStub(channel, callOptions);
    }

    /**
     */
    public kirilenko.messenger.Message sendMessage(kirilenko.messenger.Message request) {
      return blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MessengerFutureStub extends io.grpc.stub.AbstractStub<MessengerFutureStub> {
    private MessengerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MessengerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessengerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MessengerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<kirilenko.messenger.Message> sendMessage(
        kirilenko.messenger.Message request) {
      return futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_MESSAGE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MessengerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MessengerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((kirilenko.messenger.Message) request,
              (io.grpc.stub.StreamObserver<kirilenko.messenger.Message>) responseObserver);
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

  private static abstract class MessengerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MessengerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return kirilenko.messenger.Protocol.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Messenger");
    }
  }

  private static final class MessengerFileDescriptorSupplier
      extends MessengerBaseDescriptorSupplier {
    MessengerFileDescriptorSupplier() {}
  }

  private static final class MessengerMethodDescriptorSupplier
      extends MessengerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MessengerMethodDescriptorSupplier(String methodName) {
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
      synchronized (MessengerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MessengerFileDescriptorSupplier())
              .addMethod(getSendMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
