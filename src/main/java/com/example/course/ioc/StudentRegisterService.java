package com.example.course.ioc;

@Component
@Kualifier(value = "studentRegisterService")
public class StudentRegisterService implements RegisterService {
  @Override
  public String toString() {
    return "this is student register service instance : " + super.toString() + "\n";
  }
}
