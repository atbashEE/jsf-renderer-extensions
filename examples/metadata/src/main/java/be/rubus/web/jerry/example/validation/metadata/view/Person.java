package be.rubus.web.jerry.example.validation.metadata.view;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Model
public class Person {

    @NotNull
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void save() {
        System.out.println("name "+name);
        System.out.println("age "+age);

    }
}
