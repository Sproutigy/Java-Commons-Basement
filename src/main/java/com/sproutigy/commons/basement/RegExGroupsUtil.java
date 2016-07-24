package com.sproutigy.commons.basement;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegExGroupsUtil {
    private static Method namedGroupsMethod = null;
    private static Pattern GROUPS_PATTERN = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>");

    static {
        try {
            namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
            namedGroupsMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
    }

    private RegExGroupsUtil() {
    }

    public static Pattern compile(String pattern) {
        return Pattern.compile(pattern);
    }

    public static Set<String> getGroupNames(Matcher matcher) {
        return getGroupNames(matcher.pattern());
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getGroupNames(Pattern pattern) {
        //try to access private method using generics
        if (namedGroupsMethod != null) {
            try {
                Map<String, Integer> namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(pattern);
                if (namedGroups != null) {
                    return namedGroups.keySet();
                }
            } catch (Exception ignore) {
            }
        }

        //match regex
        Set<String> namedGroups = new LinkedHashSet<>();

        Matcher m = GROUPS_PATTERN.matcher(pattern.pattern());

        while (m.find()) {
            namedGroups.add(m.group(1));
        }

        return namedGroups;
    }

    public static Map<String, String> getNamedGroups(Matcher matcher) {
        return getNamedGroups(matcher, getGroupNames(matcher));
    }

    public static Map<String, String> getNamedGroups(Matcher matcher, Collection<String> groupsNames) {
        if (groupsNames.size() == 0) return Collections.emptyMap();

        Map<String, String> map = new LinkedHashMap<>();
        for (String g : groupsNames) {
            map.put(g, matcher.group(g));
        }
        return map;
    }

    public static String rewrite(Matcher matcher, String target) {
        String ret = target;
        ret = ret.replace("$0", matcher.group());

        if (matcher.groupCount() == 0)
            return ret;

        for (int i = 1; i <= matcher.groupCount(); i++) {
            String v = matcher.group(i);
            if (v != null) {
                ret = ret.replace("$" + i, v);
            }
        }

        for (String group : getGroupNames(matcher)) {
            String v = matcher.group(group);
            if (v != null) {
                ret = ret.replace("?<" + group + ">", v);
            }
        }

        return ret;
    }
}
