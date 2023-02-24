package src.com.tt.spring;

import com.sun.istack.internal.Nullable;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZxcContext {







    Map<String,BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<>();
    private Class configClass;
    Map<String,Object> singleons=new ConcurrentHashMap<>();

    public ZxcContext( Class configClass) throws ClassNotFoundException {
        this.configClass=configClass;
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path=componentScan.value().replace(".","/");
            ClassLoader loader = ZxcContext.class.getClassLoader();
            URL resource = loader.getResource(path);
            File file = new File(resource.getPath());
            System.out.println(file.getPath());
            if(file.isDirectory()){
                File[] files=file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String path1 = files[i].getAbsolutePath();
                    System.out.println(path1);
                    if(path1.endsWith(".class")){
                        String classname
                                =path1.substring(path1.indexOf("src"),path1.indexOf(".class"));
                        System.out.println(classname);
                        //for windows
                        //classname=classname.replace("\\",".");
                        //for mac
                        classname=classname.replace("/",".");
                        System.out.println(classname);
                        Class<?> a =loader.loadClass(classname);
                        if(a.isAnnotationPresent(Component.class)){
                            Component annotation = a.getAnnotation(Component.class);
                            BeanDefinition beanDefinition=new BeanDefinition();
                            beanDefinition.setType(a);
                            if(a.isAnnotationPresent(Scope.class)){
                                Scope annotation1 = a.getAnnotation(Scope.class);
                                beanDefinition.setScope(annotation1.value());
                            }else{
                                beanDefinition.setScope("singleon");
                            }
                            String beanName =annotation.value();
                            if(beanName.equals(""))
                                beanName= Introspector.decapitalize(a.getSimpleName());
                            beanDefinitionMap.put(beanName,beanDefinition);
                        }
                    }
                }
            }

        }
        for(String beanName:beanDefinitionMap.keySet()){
            BeanDefinition definition=beanDefinitionMap.get(beanName);
            if(definition.getScope().equals("singleon")){
                Object o =createBean(beanName,definition);
                singleons.put(beanName,o);
            }
        }
    }
    public Object getBean(String beanName){
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
        if(beanDefinition==null)
            throw new NullPointerException("Not exist");
        if(beanDefinition.getScope().equals("singleon")){
            Object bean=singleons.get(beanName);
            if(bean==null){
               bean= createBean(beanName,beanDefinition);
               singleons.put(beanName,bean);
            }
            return bean;
        }else{
            return createBean(beanName,beanDefinition);
        }
    }
    private  Object createBean(String beanName,BeanDefinition definition){

        try {
            Class claze = definition.getType();
            Object o = claze.getConstructor().newInstance();
            for (Field f : claze.getDeclaredFields())
                if(f.isAnnotationPresent(Autowired.class)){
                    f.setAccessible(true);
                    f.set(o,getBean(f.getName()));
                }
            if(o instanceof  BeanWare)
                ((BeanWare) o).setBeanName(beanName);
            return o;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
