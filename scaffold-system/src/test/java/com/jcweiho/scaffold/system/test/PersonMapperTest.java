package com.jcweiho.scaffold.system.test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonMapperTest {
     /*
     //下面是例子，为了保证项目的可观性，只能观看代码片段，以下片段均可通过测试，对应的Person实体类已经删除了
    @Autowired
    private PersonService personService;

    @Autowired
    private PersonConvert personConvert;

    @Test
    void personTest1() {
        //插入数据库，数据库中Person表的"性别"是存 SexEnum 枚举类的key
        Person p = new Person(1L, "测试", 20, "123", "97@qq.com", SexEnum.MAN);
        personService.save(p);
    }

    @Test
    void personTest2() throws JsonProcessingException {
        //查询Person，查出来的结果是 Person(id=1, name=测试, age=20, password=123, email=97@qq.com, sex=MAN, testsss=null)
        Person p = personService.getOne(new LambdaQueryWrapper<Person>().eq(Person::getId, 1L));
        System.err.println(p.toString());
        ObjectMapper mapper = new ObjectMapper();
        //序列化的结果是:序列化:{"id":1,"name":"测试","age":20,"password":"123","email":"97@qq.com","sex":"男"}
        System.err.println("序列化:" + mapper.writeValueAsString(p));
        //下面只是测试将结果集转化成DTO后再转会实体类进行插入，看看数据库中的sex字段会不会受到影响
        //符合预期(不受影响)
        PersonDto dto = personConvert.toPojo(p);
        System.out.println(dto.toString());
        Person person = personConvert.toEntity(dto);
        personService.save(person);
    }


    @Test
    void personTest3() throws JsonProcessingException {
        //下面是测试PersonDTO中带了另外一个DTO的情况，要确保属性名一致才能跑通，所以建议写MapStruct的Mapper时候直接属性名一致就可以避免
        Person p = new Person(1L, "测试", 20, "123", "97@qq.com", SexEnum.MAN);
        Testsss testsss = new Testsss("测试", "ceshi");
        p.setTestsss(testsss);
        PersonDto dto = personConvert.toPojo(p);
        System.out.println(dto.toString());
        System.out.println(new ObjectMapper().writeValueAsString(dto));
    }

    @Test
    void personTest4() {
        //测试目的同上
        Person p = new Person(1L, "测试", 20, "123", "97@qq.com", SexEnum.MAN);
        Testsss testsss = new Testsss("测试", "ceshi");
        p.setTestsss(testsss);
        System.out.println(personConvert.toPojo(p));
    }
    */
}