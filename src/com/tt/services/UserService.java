package src.com.tt.services;

import src.com.tt.spring.Autowired;
import src.com.tt.spring.BeanWare;
import src.com.tt.spring.Component;
import src.com.tt.spring.Scope;

@Component("userService")
@Scope("pyototype")
public class UserService  implements BeanWare {
    private String beanName;

    @Autowired
    private OrderService orderService;
    public void test(){
        System.out.println(orderService);
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }

}
