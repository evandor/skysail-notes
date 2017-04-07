package io.skysail.restlet.utils

import java.util.ArrayList
import scala.collection.mutable.ListBuffer
import java.lang.reflect.ParameterizedType

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

  //  def getParameterizedType(getClass: Class[?0]) = {
  //    ???
  //  }
  //
  //  
  //  
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
  def getParameterizedType(cls: Class[_]): Class[_] = {
    val parameterizedType = getParameterizedType1(cls);
    if (parameterizedType == null) {
      return classOf[Any]
    }
    val firstActualTypeArgument = parameterizedType.getActualTypeArguments()(0)
    if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
      return classOf[Map[_, _]];
    }
    return firstActualTypeArgument.asInstanceOf[Class[_]]
  }

  private def getParameterizedType1(cls: Class[_]): ParameterizedType = {
    val genericSuperclass = cls.getGenericSuperclass()
    if (genericSuperclass == null) {
      val genericInterfaces = cls.getGenericInterfaces();
      val pt = genericInterfaces.filter(i => i.isInstanceOf[ParameterizedType]).headOption

      //val pt = java.util.Arrays.stream(genericInterfaces).filter(i => i.isInstanceOf[ParameterizedType]).findFirst();
      if (pt.isDefined) {
        return pt.get.asInstanceOf[ParameterizedType];
      }
      return null;
    }
    if (genericSuperclass.isInstanceOf[ParameterizedType]) {
      return genericSuperclass.asInstanceOf[ParameterizedType]
    }
    return getParameterizedType1(cls.getSuperclass());
  }
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