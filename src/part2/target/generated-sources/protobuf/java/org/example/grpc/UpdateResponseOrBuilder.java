// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: stock_service.proto

// Protobuf Java Version: 3.25.5
package org.example.grpc;

public interface UpdateResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:stockservice.UpdateResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 1: success, -1: invalid stock, -2: invalid price
   * </pre>
   *
   * <code>int32 status = 1;</code>
   * @return The status.
   */
  int getStatus();
}
