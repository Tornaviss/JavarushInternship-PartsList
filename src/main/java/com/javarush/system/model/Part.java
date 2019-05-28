package com.javarush.system.model;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "parts")
public class Part {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private int count;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EssentialType type;

    @Column(name = "essential")
    private boolean essential;


    public Part(String name, int count, EssentialType type) {

    }

    public Part(String name, EssentialType type) {
        this.name = name;
        this.count = 1;
        this.type = type;
        this.essential = true;
    }

    public Part(String name) {
        this.name = name;
        this.count = 1;
    }

    public Part() {
    }

    public void setEssential(boolean essential) {
        this.essential = essential;
    }

    public void setType(EssentialType type) {
        this.type = type;
        essential = true;
    }

    public EssentialType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEssential() {
        return essential;
    }



    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(name, part.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, id);
    }

    public enum EssentialType {
        CASE("Корпус"), MOTHERBOARD("Материнская плата"), SSD("SSD накопитель"), CPU("Центральный процессор"), RAM("Оперативная память")
        , PS("Блок питания"), GRAPHIC_CARD("Видеокарта");

        private String name;
        EssentialType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "EssentialType{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
