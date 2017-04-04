package io.skysail.restlet.utils

import java.util.ArrayList
import scala.collection.mutable.ListBuffer

object ScalaReflectionUtils {

  val inheritedFieldsCache = scala.collection.mutable.Map[Class[_], List[java.lang.reflect.Field]]()
  
  def getInheritedFields(theType: Class[_]): List[java.lang.reflect.Field] = {
    
    if (inheritedFieldsCache.contains(theType)) {
      return inheritedFieldsCache.get(theType).get
    }
    var result = ListBuffer[java.lang.reflect.Field]();

    var i = theType;
    while (i != null && i != classOf[Object]) {
      while (i != null && i != classOf[Object]) {
        for (field <- i.getDeclaredFields()) {
          if (!field.isSynthetic()) {
            result += field
          }
        }
        i = i.getSuperclass();
      }
    }
    inheritedFieldsCache += theType -> result.toList
    result.toList
  }
  //
  //    public static List<Method> getInheritedMethods(Class<?> type) {
  //        List<Method> result = new ArrayList<Method>();
  //
  //        Class<?> i = type;
  //        while (i != null && i != Object.class) {
  //            while (i != null && i != Object.class) {
  //                for (Method method : i.getDeclaredMethods()) {
  //                    if (!method.isSynthetic()) {
  //                        result.add(method);
  //                    }
  //                }
  //                i = i.getSuperclass();
  //            }
  //        }
  //
  //        return result;
  //    }
  //
  //    public static Class<?> getParameterizedType(Class<?> cls) {
  //        ParameterizedType parameterizedType = getParameterizedType1(cls);
  //        if (parameterizedType == null) {
  //            return Object.class;
  //        }
  //        Type firstActualTypeArgument = parameterizedType.getActualTypeArguments()[0];
  //        if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
  //            return Map.class;
  //        }
  //        return (Class<?>) firstActualTypeArgument;
  //    }
  //
  //    private static ParameterizedType getParameterizedType1(Class<?> cls) {
  //        Type genericSuperclass = cls.getGenericSuperclass();
  //        if (genericSuperclass == null) {
  //            Type[] genericInterfaces = cls.getGenericInterfaces();
  //            //return getParameterizedType1(genericInterfaces[0].getClass());
  //            Optional<Type> pt = Arrays.stream(genericInterfaces).filter(i -> i instanceof ParameterizedType).findFirst();
  //            if (pt.isPresent()) {
  //                return (ParameterizedType)pt.get();
  //            }
  //            return null;
  //        }
  //        if (genericSuperclass instanceof ParameterizedType) {
  //            return (ParameterizedType) genericSuperclass;
  //        }
  //        return getParameterizedType1(cls.getSuperclass());
  //    }
  //
  //    public static Type getParameterizedType(Field field) {
  //        Type type = field.getGenericType();
  //        if (type instanceof ParameterizedType) {
  //            ParameterizedType pType = (ParameterizedType)type;
  //            return pType.getActualTypeArguments()[0];
  //        } 
  //        return field.getType();        
  //    }

}