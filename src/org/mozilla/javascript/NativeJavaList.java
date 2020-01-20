/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript;

import java.util.List;

/**
 * This class reflects Java Lists into the JavaScript environment.
 *
 * Based on https://github.com/mozilla/rhino/pull/561 by @makusuko
 *
 * @author Markus Sunela
 */
public class NativeJavaList extends NativeJavaObject implements SymbolScriptable {

    private List<Object> list;
    private Class<?> cls;

    public NativeJavaList(Scriptable scope, Object list) {
        super(scope, list, list.getClass());
        Class<?> cl = list.getClass();
        if (!List.class.isAssignableFrom(cl)) {
            throw new RuntimeException("List expected");
        }
        this.list = (List<Object>) list;
        this.cls = cl;
    }

    @Override
    public String getClassName() {
        return "JavaList";
    }

    @Override
    public Object unwrap() {
        return list;
    }

    @Override
    public boolean has(String id, Scriptable start) {
        return id.equals("length") || super.has(id, start);
    }

    @Override
    public boolean has(int index, Scriptable start) {
        return 0 <= index && index < list.size();
    }

    @Override
    public boolean has(Symbol key, Scriptable start) {
        return SymbolKey.IS_CONCAT_SPREADABLE.equals(key);
    }

    @Override
    public Object get(String id, Scriptable start) {
        if (id.equals("length")) {
            return Integer.valueOf(list.size());
        }
        Object result = super.get(id, start);
        if (result == NOT_FOUND && !ScriptableObject.hasProperty(getPrototype(), id)) {
            throw Context.reportRuntimeError2("msg.java.member.not.found", list.getClass().getName(), id);
        }
        return result;
    }

    @Override
    public Object get(int index, Scriptable start) {
        if (0 <= index && index < list.size()) {
            Context cx = Context.getContext();
            Object obj = list.get(index);
            return cx.getWrapFactory().wrap(cx, this, obj, obj.getClass());
        }
        return Undefined.instance;
    }

    @Override
    public Object get(Symbol key, Scriptable start) {
        if (SymbolKey.IS_CONCAT_SPREADABLE.equals(key)) {
            return true;
        }
        return Scriptable.NOT_FOUND;
    }

    @Override
    public void put(String id, Scriptable start, Object value) {
        // Ignore assignments to "length"--it's readonly.
        if (!id.equals("length")) {
            throw Context.reportRuntimeError1("msg.java.array.member.not.found", id);
        }
    }

    @Override
    public void put(int index, Scriptable start, Object value) {
        if (0 <= index && index < list.size()) {
            list.remove(index);
            list.add(index, Context.jsToJava(value, Object.class));
        } else {
            throw Context.reportRuntimeError2(
                    "msg.java.array.index.out.of.bounds", String.valueOf(index),
                    String.valueOf(list.size() - 1));
        }
    }

    @Override
    public void delete(Symbol key) {
        // All symbols are read-only
    }

    public boolean jsFunction_includes(Object obj) {
        return list.contains(obj);
    }

    @Override
    public Object getDefaultValue(Class<?> hint) {
        if (hint == null || hint == ScriptRuntime.StringClass) {
            return list.toString();
        }
        if (hint == ScriptRuntime.BooleanClass) {
            return Boolean.TRUE;
        }
        if (hint == ScriptRuntime.NumberClass) {
            return ScriptRuntime.NaNobj;
        }
        return this;
    }

    @Override
    public Object[] getIds() {
        Object[] result = new Object[list.size()];
        int i = list.size();
        while (--i >= 0) {
            result[i] = Integer.valueOf(i);
        }
        return result;
    }

    @Override
    public boolean hasInstance(Scriptable value) {
        if (!(value instanceof Wrapper)) {
            return false;
        }
        Object instance = ((Wrapper)value).unwrap();
        return cls.isInstance(instance);
    }

    @Override
    public Scriptable getPrototype() {
        if (prototype == null) {
            prototype = ScriptableObject.getArrayPrototype(this.getParentScope());
        }
        return prototype;
    }

    public static NativeJavaList wrap(Scriptable scope, Object array) {
        return new NativeJavaList(scope, array);
    }
}
