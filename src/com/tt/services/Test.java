package src.com.tt.services;

import src.com.tt.spring.ZxcContext;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        ZxcContext zxcContext=new ZxcContext(AppConfig.class);
        System.out.println(zxcContext.getBean("userService"));
        System.out.println(zxcContext.getBean("userService"));
        UserService service = (UserService)zxcContext.getBean("userService");
        service.test();
        System.out.println(service.getBeanName());
        System.out.println(zxcContext.getBean("orderService"));

    }
}
