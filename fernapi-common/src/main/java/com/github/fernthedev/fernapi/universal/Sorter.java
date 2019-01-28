package com.github.fernthedev.fernapi.universal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class Sorter {

    public static List<DateObject> sortNewest(HashMap<Date,Object> oldList) {

        List<DateObject> dateObjects = new ArrayList<>();

        for(Date date : oldList.keySet()) {
            dateObjects.add(new DateObject(date,oldList.get(date)));
        }

        return sortNewest(dateObjects);
    }

    public static List<DateObject> sortNewest(List<DateObject> oldList) {
        Collections.sort(oldList, Comparator.comparing(DateObject::getDate));
        return oldList;
    }

    public static List<DateObject> sortOldest(HashMap<Date,Object> oldList) {

        List<DateObject> dateObjects = new ArrayList<>();

        for(Date date : oldList.keySet()) {
            dateObjects.add(new DateObject(date,oldList.get(date)));
        }

        return sortOldest(dateObjects);
    }

    public static List<DateObject> sortOldest(List<DateObject> oldList) {
        Collections.sort(oldList, Comparator.comparing(DateObject::getDate).reversed());
        return oldList;
    }

    public static List<String> sortAz(List<String> list) {
        Collections.sort(list);

        return list;
    }

    public static List<String> sortZa(List<String> list) {
        Collections.reverse(sortZa(list));
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class DateObject {
        private Date date;
        private Object object;

    }

}
