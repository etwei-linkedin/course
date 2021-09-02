package com.example.course.ioc;

@Component
public class Starter {
  @Autowired
  private static StudentApplication studentApplication;
  @Autowired
  private static RegisterService registerService;

  public static void main(String[] args) throws Exception{
    Container.start();
    System.out.println(studentApplication);
    System.out.println(registerService);
  }
}
