package com.jcweiho.scaffold.common.mapstruct;

import java.util.List;

/**
 * <h1>MapStruct基础对象转换类</h1>
 * <h2 style="color:red">注意事项：</h2>
 * <ol>
 *     <li>
 *        <p>
 *            若使用MapStruct，在生成了Service实现类后修改了实体类，
 *            则<b style="color:red">一定要</b>执行maven命令 mvn clean 后重新编译才可以。
 *        </p>
 *     </li>
 *     <li>
 *         <p>
 *             若一个实体DTO里面包含了另一个实体的DTO，想要自动转化，
 *             推荐定义属性名一致，不用重写MapStructConvert中的方法。
 *             (该类里面的DTO也需要写一个Convert，同时在主类的Convert
 *             的@Mapper注解上的uses属性中添加该Convert类 uses = {XXXConvert.class})
 *         </p>
 *     </li>
 *     <li>
 *         <p>
 *             若选择重写，则需要在对应的MapStructConvert的子类中选择想要重
 *             写的方法，在上面利用注解@Mapping，进行target(转化目标属性名)
 *             和source(源属性名)的转换。
 *         </p>
 *     </li>
 * </ol>
 * <h2 style="color:red">使用方法(Spring环境下)</h2>
 * <ul>
 *     <li>
 *          <p>
 *              写一个XXXConvert.java(推荐Convert结尾，避免与Mybatis的Mapper混淆)继承该类,
 *              然后加上注解：@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
 *          </p>
 *     </li>
 * </ul>
 * <h2 style="color:red">MapStruct与Mybatis-Plus枚举类集成的测试结果：</h2>
 * <ul>
 *     <li>
 *         <p>
 *                  MapStruct在在使用了Mybatis-Plus的枚举类处理上有轻微影响，
 *              若原对象中使用了枚举类，转换目标类对应字段采用了String，则会取
 *              枚举类的属性名作为复制结果对象的属性值，原来Mybatis-Plus和Jackson
 *              的在数据库操作的枚举类处理不受影响(存数据库是存枚举类的key和返回
 *              给前端是返回枚举类的value)。
 *         </p>
 *     </li>
 * </ul>
 *
 * <h2 style="color:red">建议：</h2>
 * <ol>
 *     <li>
 *         <p>
 *                  在DTO或者Entity中，进行互相转化时，有枚举类类型的建议直接使用枚
 *              举类类型(Mybatis-Plus和Jackson会自动在合适的时候进行转化处理)，
 *              避免返回前端没有成功序列化(可能也会抛异常)
 *         </p>
 *     </li>
 *     <li>
 *         <p>
 *                  在定义DTO时候，属性名尽量与Entity实体类一致(可以避免好多异常和麻烦,而且
 *              直接继承该类即可使用)，如果业务需要不一致则需要,在MapStructConvert的对应子类
 *              下的@Mapping注解中进行重写方法
 *         </p>
 *     </li>
 * </ol>
 *
 * @param <P> POJO
 * @param <E> Entity(DO)
 * @author Weiho
 */
public interface MapStructConvert<E, P> {
    /**
     * POJO 转 Entity
     *
     * @param pojo DTO对象
     * @return Entity对象
     */
    E toEntity(P pojo);

    /**
     * Entity 转 POJO
     *
     * @param entity 实体类
     * @return DTO对象
     */
    P toPojo(E entity);

    /**
     * POJO集合 转 Entity集合
     *
     * @param pojoList DTO集合
     * @return Entity集合
     */
    List<E> toEntity(List<P> pojoList);

    /**
     * Entity集合 转 POJO集合
     *
     * @param entityList Entity集合
     * @return DTO集合
     */
    List<P> toPojo(List<E> entityList);
}
