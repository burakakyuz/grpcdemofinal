package com.example.grpcdemofinal.client;

import com.example.StudentRequest;
import com.example.StudentResponse;
import com.example.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client
{
    static Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args)
    {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();

        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);

        StudentResponse studentResponse = blockingStub.getStudent(StudentRequest.newBuilder().setId(5).build());
        logger.info("response = "+ studentResponse.getName() +" " + studentResponse.getAge());

    }
}
