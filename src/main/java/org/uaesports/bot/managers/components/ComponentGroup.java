package org.uaesports.bot.managers.components;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.uaesports.bot.managers.components.annotations.Component;
import org.uaesports.bot.managers.components.annotations.ComponentParam;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for types that handle component interactions.
 */
public class ComponentGroup {
    
    private ComponentHandler instance;
    private Map<String, ComponentMethod> methods = new HashMap<>();
    private Map<Type, Method> typeConversions = new HashMap<>();
    
    public ComponentGroup(ComponentHandler handler) {
        this.instance = handler;
        readData();
    }
    
    public static ComponentGroup read(ComponentHandler instance) {
        return new ComponentGroup(instance);
    }
    
    /**
     * Get the methods that handle component interactions.
     */
    public Map<String, ComponentMethod> getMethods() {
        return methods;
    }
    
    protected void readData() {
        var type = instance.getClass();
        Arrays.stream(type.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(ComponentParam.class))
              .forEach(this::readConversion);
        Arrays.stream(type.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(Component.class))
              .forEach(this::readMethod);
    }
    
    /**
     * Read a method that is annotated with {@link ComponentParam}.
     *
     * @throws IllegalStateException If the conversion method is invalid.
     */
    private void readConversion(Method method) {
        Type output = method.getGenericReturnType();
        if (output == Void.class) {
            throw new IllegalStateException("Conversion method cannot return void.");
        }
        if (typeConversions.containsKey(output)) {
            throw new IllegalArgumentException("Duplicate type converter for type: " + output.getTypeName());
        }
        var params = method.getGenericParameterTypes();
        if (params.length < 1 || params.length > 2) {
            throw new IllegalStateException("Conversion method must take one parameter.");
        }
        if (params[0] != MessageComponentInteraction.class) {
            throw new IllegalStateException("First parameter of type conversion must be MessageComponentInteraction.");
        }
        if (params.length > 1 && params[1] != String.class) {
            throw new IllegalStateException("Optional second parameter of type conversion must be String.");
        }
        typeConversions.put(output, method);
    }
    
    /**
     * Add a method annotated with {@link Component}.
     *
     * @throws IllegalStateException If the handler method is invalid.
     */
    private void readMethod(Method method) {
        Component component = method.getAnnotation(Component.class);
        if (methods.containsKey(component.value())) {
            throw new IllegalStateException("Duplicate component handler for id: " + component.value());
        }
        var params = method.getParameterTypes();
        if (params.length < 1 || params[0] != MessageComponentInteraction.class) {
            throw new IllegalStateException("First parameter of component handler method must be MessageComponentInteraction.");
        }
        var generic = method.getGenericParameterTypes();
        var conversions = Arrays.stream(generic)
                                .skip(1)
                                .map(this::getTypeConverter)
                                .toArray(Method[]::new);
        methods.put(component.value(), new ComponentMethod(instance, method, conversions));
    }
    
    /**
     * Get the type converter for the custom parameter.
     */
    private Method getTypeConverter(Type type) {
        var conversion = typeConversions.getOrDefault(type, null);
        if (conversion == null) {
            throw new IllegalArgumentException("No type converter defined for type: " + type.getTypeName() + ". " +
                                                       "Add a method tagged @ComponentParam that returns this type with parameters " +
                                                       "(MessageComponentInteraction) or (MessageComponentInteraction, String)");
        }
        return conversion;
    }
    
}
