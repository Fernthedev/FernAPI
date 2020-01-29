package com.github.fernthedev.fernapi.universal.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class ListUtil {

    public static boolean containsString(List<String> strings, String string) {
        return strings.parallelStream().anyMatch(s -> s.equals(string));
    }
    
    public static List<DateObject> sortNewest(Map<Date,Object> oldList) {

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

    public static List<DateObject> sortOldest(Map<Date,Object> oldList) {

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

    public List<Object[]> objectToPageArray(List<Object> objects,int perPage) {
        int index = 0;

        List<Object[]> pageList = new ArrayList<>();

        List<Object> curPage = new ArrayList<>();

        for (Object object : objects) {
            index++;

            if (index >= perPage + 1) {
                pageList.add(curPage.toArray());
                curPage.clear();
            }

            curPage.add(object);


        }

        return pageList;
    }

    @Getter
    @AllArgsConstructor
    public static class DateObject {
        private Date date;
        private Object object;

    }

}
