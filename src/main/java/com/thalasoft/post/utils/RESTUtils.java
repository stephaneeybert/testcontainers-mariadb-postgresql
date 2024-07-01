package com.thalasoft.post.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public final class RESTUtils {

    public static final String RESOURCE_PROPERTY_ID = "id";
    public static final String HOST = "localhost";
    public static final String SLASH = "/";
    public static final String API = "api";
    public static final String POSTS = "posts";
    public static final String NOT = "not";
    public static final String COUNT = "count";

    public static final String REL_FIRST = "first";
    public static final String REL_PREV = "prev";
    public static final String REL_NEXT = "next";
    public static final String REL_LAST = "last";

    public static final String PAGEABLE_SORT_SUFFIX = ".dir";

    private RESTUtils() {
        throw new AssertionError();
    }

    public static final Sort stripColumnsFromSorting(Sort sort, Set<String> nonSortableColumns) {
        return Sort.by(sort.stream().filter(order -> {
            return !nonSortableColumns.contains(order.getProperty());
        }).collect(Collectors.toList()));
    }
}