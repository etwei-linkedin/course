package com.example.course.ioc;

@Component
public class StudentApplication {

  @Kualifier("studentRegisterService")
  @Autowired
  RegisterService studentRegisterService;

  @Override
  public String toString() {
    return "StudentApplication{\n" +
        "studentRegisterService=" + studentRegisterService +
        "}\n";
  }
}
