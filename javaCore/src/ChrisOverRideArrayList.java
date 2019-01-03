import java.util.*;


/**
 * @author ：ChrisY
 * @date ：Created in 2019-01-02 11:34
 * @description：模仿ArrayList的实现方式
 *
 * 思路
 * 构造方法可以设置初始化的大小
 * 如果可以确定数组的大小之后可以提升性能，因为这样可以减少Copy数组的次数
 * 核心方法，在jdk1.7之前用的是System.arrayCopy();后续的版本用的是Arrys.copyof();
 * 知识点：
 * transient,不会被序列化，也就是对象说不会持久化操作
 * Collection,所有集合类接口,<? extends Object> 这样的泛型，在Collection集合中可以存放任意的Object子类类型的数据
 *
 * 多看下javaDoc的源码吧
 */

public class ChrisOverRideArrayList {
    private transient Object[] element;
    private final static int DEFAULT_SIZE= 10;
    private int size;

    public ChrisOverRideArrayList()
    {

    }

    public ChrisOverRideArrayList(int setSize)
    {

    }

    public ChrisOverRideArrayList(Collection<? extends Object>e)
    {

    }
}
