/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript;

import java.util.Map;

/**
 * This class reflects Java Maps into the JavaScript environment.
 */
public class NativeJavaMap extends NativeJavaObject implements SymbolScriptable {

    private Map<Object, Object> map;
    private Class<?> cls;

    public NativeJavaMap(Scriptable scope, Object map) {
        super(scope, map, map.getClass());
        Class<?> cl = map.getClass();
        if (!Map.class.isAssignableFrom(cl)) {
            throw new RuntimeException("Map expected");
        }
        this.map = (Map<Object, Object>) map;
        this.cls = cl;
    }

    @Override
    public String getClassName() {
        return "JavaMap";
    }

    @Override
    public Object unwrap() {
        return map;
    }

    @Override
    public boolean has(String name, Scriptable start) {
        return map.containsKey(name);
    }

    @Override
    public boolean has(int index, Scriptable start) {
        return map.containsKey(index);
    }

    @Override
    public boolean has(Symbol key, Scriptable start) {
        return false;
    }

    @Override
    public Object get(String id, Scriptable start) {
        return getInternal(id, start);
    }

    @Override
    public Object get(int index, Scriptable start) {
        return getInternal(index, start);
    }

    private Object getInternal(Object key, Scriptable start) {
        if (map.containsKey(key)) {
            Context cx = Context.getContext();
            Object obj = map.get(key);
            return cx.getWrapFactory().wrap(cx, this, obj, obj.getClass());
        }
        Object result = super.get(key.toString(), start);
        if (result == NOT_FOUND && !ScriptableObject.hasProperty(getPrototype(), key.toString())) {
            throw Context.reportRuntimeError2("msg.java.member.not.found", map.getClass().getName(), key.toString());
        }
        return result;
    }

    @Override
    public Object get(Symbol key, Scriptable start) {
        return Scriptable.NOT_FOUND;
    }

    @Override
    public void put(String id, Scriptable start, Object value) {
        map.put(id, Context.jsToJava(value, Object.class));
    }

    @Override
    public void put(int index, Scriptable start, Object value) {
        map.put(index, Context.jsToJava(value, Object.class));
    }

    @Override
    public void delete(Symbol key) {
    }

    public static NativeJavaMap wrap(Scriptable scope, Object map) {
        return new NativeJavaMap(scope, map);
    }
}
