package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;

import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        for (Method method : Address.class.getDeclaredMethods()) {
            String returnType;
            if (method.isAnnotationPresent(Inspect.class)) {
                if ("java.lang.String".equals(method.getReturnType().getName())) {
                    returnType = "String";
                } else {
                    returnType = method.getReturnType().getName();
                }
                System.out.println("Method " + method.getName() + " returns a value of type " + returnType + ".");
            }
        }
        // END
    }
}
