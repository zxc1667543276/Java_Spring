package src.com.tt.spring;

public class BeanDefinition {
    private Class Type;
    private String Scope;

    public Class getType() {
        return Type;
    }

    public void setType(Class type) {
        Type = type;
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String scope) {
        Scope = scope;
    }
}
