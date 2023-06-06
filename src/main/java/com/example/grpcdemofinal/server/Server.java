package com.example.grpcdemofinal.server;

import com.example.AddStudentResponse;
import com.example.AllStudentsResponse;
import com.example.Student;
import com.example.StudentResponse;
import com.example.StudentServiceGrpc;
import io.grpc.Status;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@GRpcService
public class Server extends StudentServiceGrpc.StudentServiceImplBase
{
    Logger logger = LoggerFactory.getLogger(Server.class);
    private List<Student> studentList = new ArrayList<>();


    @Override
    public void getStudent(com.example.StudentRequest request,
                           io.grpc.stub.StreamObserver<com.example.StudentResponse> responseObserver)
    {
        logger.info("request alindi = "+ request.getId());
        StudentResponse studentResponse = StudentResponse.newBuilder().setAge(18).setName("Burak").build();
        responseObserver.onNext(studentResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void addStudent(com.example.Student request,
                           io.grpc.stub.StreamObserver<com.example.AddStudentResponse> responseObserver) {

        int id = request.getId();
        String name = request.getName();
        int age = request.getAge();

        Student student = Student.newBuilder()
                .setId(id)
                .setName(name)
                .setAge(age)
                .build();

        studentList.add(student);
        boolean success = true;
        String message = "Student added successfully.";

        AddStudentResponse response = AddStudentResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    public void getAllStudents(com.google.protobuf.Empty request,
                               io.grpc.stub.StreamObserver<com.example.AllStudentsResponse> responseObserver) {
        // Tüm öğrencileri al
        AllStudentsResponse.Builder studentListBuilder = AllStudentsResponse.newBuilder();
        studentListBuilder.addAllStudents(studentList);

        // Cevabı gönder
        responseObserver.onNext(studentListBuilder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void getStudentById(com.example.StudentIdRequest request,
                               io.grpc.stub.StreamObserver<com.example.StudentResponse> responseObserver) {

        int id = request.getId();

        Student student = findStudentById(id);

        if (student != null)
        {
            StudentResponse response = StudentResponse.newBuilder()
                    .setId(student.getId())
                    .setName(student.getName())
                    .setAge(student.getAge())
                    .build();

            responseObserver.onNext(response);
        }
        else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Student not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    private Student findStudentById(int id) {
        for (Student student : studentList) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}
