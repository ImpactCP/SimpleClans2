/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.*;

/**
 * Represents a JSONUtil
 */
public class JSONUtil {

    private JSONUtil()
    {
    }

    @SuppressWarnings("unchecked")
    public static String collectionToJSON(Collection collection)
    {
        JSONArray array = new JSONArray();

        array.addAll(collection);

        return array.toJSONString();
    }

    public static List<String> JSONToStringList(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        List<String> list = new ArrayList<String>();

        for (Object obj : parser) {
            list.add(obj.toString());
        }

        return list;
    }

    public static Set<String> JSONToStringSet(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        Set<String> set = new HashSet<String>();

        for (Object obj : parser) {
            set.add(obj.toString());
        }

        return set;
    }

    public static List<Long> JSONToLongList(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        List<Long> list = new ArrayList<Long>();

        for (Object obj : parser) {
            list.add((Long) obj);
        }

        return list;
    }

    public static Set<Long> JSONToLongSet(String json, String key)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        Set<Long> set = new HashSet<Long>();

        for (Object obj : parser) {
            set.add((Long) obj);
        }

        return set;
    }
}
