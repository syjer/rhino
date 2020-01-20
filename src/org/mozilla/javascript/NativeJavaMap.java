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

    private Map<?, ?> map;
    private Class<?> cls;

    public NativeJavaMap(Scriptable scope, Object map) {
        super(scope, map, map.getClass());
        Class<?> cl = map.getClass();
        if (!Map.class.isAssignableFrom(cl)) {
            throw new RuntimeException("Map expected");
        }
        this.map = (Map<?, ?>) map;
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
        return null; //FIXME implement
    }

    @Override
    public Object get(int index, Scriptable start) {
        return null; //FIXME implement
    }

    @Override
    public Object get(Symbol key, Scriptable start) {
        return Scriptable.NOT_FOUND;
    }

    @Override
    public void delete(Symbol key) {
    }

    public static NativeJavaMap wrap(Scriptable scope, Object map) {
        return new NativeJavaMap(scope, map);
    }
}
