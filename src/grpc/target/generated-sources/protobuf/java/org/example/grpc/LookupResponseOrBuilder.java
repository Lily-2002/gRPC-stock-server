// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: stock_service.proto

// Protobuf Java Version: 3.25.5
package org.example.grpc;

public interface LookupResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:stockservice.LookupResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>double price = 1;</code>
   * @return The price.
   */
  double getPrice();

  /**
   * <code>int32 volume = 2;</code>
   * @return The volume.
   */
  int getVolume();
}
